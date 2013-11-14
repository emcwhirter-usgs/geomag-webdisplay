package webdisplay

import java.text.SimpleDateFormat

class MagWormMessageImporterJob
{
	def sessionFactory
	def connectionSettingChangedService
	def connectionStatusService
	def stationSettingChangedService
	def dbService

	def concurrent = false
	//def timeout = 5000l // execute job once in 5 seconds

	static triggers = {
		simple name:'MagWormMessageImporterJob', startDelay:1000, repeatInterval:15000, repeatCount: -1
	}

	/*****************************************************
	 Earthworm / Magworm packet protocol structure

	 Start Packet      Start text character
	 Installation ID   Integer 0-255 (Represented as 3 chars in the packet)
	 Module ID         Integer 0-255 (Represented as 3 chars in the packet)
	 Message Type      Integer 0-255 (Represented as 3 chars in the packet)
	 Payload           Variable length string
	 End Packet        End text character

	 Oct   Dec   Hex   Char
	 ───────────────────────────────────────
	 000   0     00    NUL '\0'
	 002   2     02    STX (start of text)
	 003   3     03    ETX (end of text)
	 033   27    1B    ESC (escape)
	 ******************************************************/

	static char STX = 0x02
	static char ETX = 0x03
	static char ESC = 0x1B
	static int SEARCHING_FOR_MESSAGE_START =  0
	static int EXPECTING_MESSAGE_START     =  1
	static int ASSEMBLING_MESSAGE          =  2
	static byte[] heartbeat_packet = [STX,' ','5','9',' ','2','8',' ',' ','3','a','l','i','v','e',ETX]

	def hibernateSessionFlush(counter) {
		if (counter == -1 || counter > 99) {
//			log.info("number of saved objects = $counter, flushing hibernate session\n")
			def hibSession = sessionFactory.getCurrentSession()
			hibSession.flush()
			hibSession.clear()
			counter = 0
		} else {
			counter++
		}
		return counter
	}

	def resolutionIntervals(timestamp){ // FIXME: this isn't right... convoluted and costly
		// resolutions
		def qtr_min = 15, half_min = 30, one_min = 60, five_min = 300, ten_min = 600, fifteen_min = 900, thirty_min = 1800
		def resolutions = [:]

		if( timestamp %  qtr_min > 0 ){
			resolutions["qtr_min"] = timestamp - (timestamp % qtr_min)
		} else {
			resolutions["qtr_min"] = timestamp
		}

		if( timestamp %  half_min > 0 ){
			resolutions["half_min"] = timestamp - (timestamp % half_min)
		} else {
			resolutions["half_min"] = timestamp
		}

		if( timestamp % one_min > 0 ){
			resolutions["one_min"] = timestamp - (timestamp % one_min)
		} else {
			resolutions["one_min"] = timestamp
		}

		if( timestamp % five_min > 0 ){
			resolutions["five_min"] = timestamp - (timestamp % five_min)
		} else {
			resolutions["five_min"] = timestamp
		}

		if( timestamp % ten_min > 0 ){
			resolutions["ten_min"] = timestamp - (timestamp % ten_min)
		} else {
			resolutions["ten_min"] = timestamp
		}

		if( timestamp % fifteen_min > 0 ){
			resolutions["fifteen_min"] = timestamp - (timestamp % fifteen_min)
		} else {
			resolutions["fifteen_min"] = timestamp
		}

		if( timestamp % thirty_min > 0 ){
			resolutions["thirty_min"] = timestamp - (timestamp % thirty_min)
		} else {
			resolutions["thirty_min"] = timestamp
		}

		return resolutions
	}

	def execute() {
		if (connectionSettingChangedService.shouldStop()) {
			log.trace "message importer job not set to start"
			return
		}
		log.info "message importer job starts"

		def socket = null
		def beat_it = true

		try {
			// Constants
			def secdata_type             = EWParam.findByName('TYPE_SECDATAMAGFIELD')
			def magdiag_type             = EWParam.findByName('TYPE_MAGDIAG')
			def diag3v3b_type            = EWParam.findByName('TYPE_DIAG3V3B')
			def supptimepacket_type      = EWParam.findByName('TYPE_SUPPTIMEPACKET')
			def goeslasttx_type          = EWParam.findByName('TYPE_GOESLASTTX')
			def datamag_type             = EWParam.findByName('TYPE_DATAMAGFIELD')
			// Settings
			def exporter_host_setting    = Setting.findByName('exporter_host')
			def exporter_port_setting    = Setting.findByName('exporter_port')
			def station_setting          = Setting.findByName('local_station_id')
			def station_id               = station_setting.settingValue
			def hb_interval_setting      = Setting.findByName('heartbeat_interval')
			def hb_interval              = hb_interval_setting.settingValue
//			def f_list = [] // [(period:F),..]
//			def f_firstts = 0;
//			def f_lastts = 0;
			def host
			Integer port

			if (exporter_host_setting.settingValue) {
				host = exporter_host_setting.settingValue
			} else {
				host = "127.0.0.1"
				log.info("No host set! Defaulting to $host")
			}

			if (exporter_port_setting.settingValue) {
				port = exporter_port_setting.settingValue as Integer
			} else {
				port = 16004
				log.info("No port set! Defaulting to $port")
			}
			log.info("Listening to $host:$port, station $station_id")
			connectionSettingChangedService.setChanged(false)
			stationSettingChangedService.setChanged(false)

			socket = new Socket()
			socket.setSoTimeout(15000)
			InetSocketAddress isa = new InetSocketAddress(host,port)
			socket.connect(isa)

			byte   last,current = 0
			int    state        = SEARCHING_FOR_MESSAGE_START
			def    is           = socket.getInputStream()
			byte[] inbuffer     = new byte[1]
			def    bufferlength = 1024 * 64;
			byte[] buffer       = new byte[bufferlength]
			int    counter      = 0
			int    bufferindex  = 0
			int    obj_counter  = 0

			connectionStatusService.setState(true)

			Thread.start({
				log.info("heartbeat thread starts")
				while (beat_it && socket != null) {
					try {
						socket << heartbeat_packet
						log.info("❤")
					} catch (Exception ex) {
						log.info(ex)
					}
					sleep ((hb_interval as int) * 1000)
				}
				log.info("heartbeat thread ends")
			})

			int c
			while (beat_it && (c = is.read()) != -1) {

				inbuffer[0] = (byte) c
				last = current
				current = inbuffer[0]

				if (state == SEARCHING_FOR_MESSAGE_START) {
					if (last != ESC && current == STX) {
						state = ASSEMBLING_MESSAGE
						continue
					}
				}

				if (state == EXPECTING_MESSAGE_START) {
					if (last != ESC && current == STX) {
						state = ASSEMBLING_MESSAGE
						continue
					} else {
						state = SEARCHING_FOR_MESSAGE_START
						continue
					}
				}

				if (state == ASSEMBLING_MESSAGE) {
					if (current == ETX) {
						// need to process server heartbeat
						state = EXPECTING_MESSAGE_START

						def str = new String(buffer)
//println "packet: $str"

						def install_id = str[0..2].toInteger()
						def mod_it     = str[3..5].toInteger()
						def msg_type   = str[6..8].toInteger()

						def msg_name = ''

						if (msg_type == secdata_type.settingValue) {

							if (str[34..39] == "INST 1") {
								log.debug "data ${msg_type} packet: $str"

								def a = str[41..-1].split(" ")

								def station = str[28..30]
								def h_val = Float.parseFloat(a[1])
								def d_val = Float.parseFloat(a[3])
								def z_val = Float.parseFloat(a[5])
								def sdf = new SimpleDateFormat("yyyyMMddHHmmss")
								sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

								if (station == station_id) {
									def dateobj   = sdf.parse(str[9..26])
									def timestamp = dateobj.getTime() / 1000 as long
//										println "XXX $dateobj -- $timestamp"
									def resolutions = resolutionIntervals(timestamp)

									//log.info("I1 epoch: $timestamp  date $dateobj")

									new Instrument1Msg(
										station:station,
										h:h_val,
										d:d_val,
										z:z_val,
										ts:str[9..26],
										timestamp:dateobj,
										epoch:timestamp,
										one_min:resolutions["one_min"],
										five_min:resolutions["five_min"],
										ten_min:resolutions["ten_min"],
										fifteen_min:resolutions["fifteen_min"],
										thirty_min:resolutions["thirty_min"]
									).save(failOnError: true)
									obj_counter = hibernateSessionFlush(obj_counter)
								}

							} else if (str[34..39] == "INST 2") {
								log.trace "data ${msg_type} packet: $str"

								def a = str[41..-1].split(" ")
								def station = str[28..30]
								def f_val = Float.parseFloat(a[1])
								def sdf = new SimpleDateFormat("yyyyMMddHHmmss")
								sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

								def dateobj   = sdf.parse(str[9..26])
								def timestamp = dateobj.getTime() / 1000 as long
								def resolutions = resolutionIntervals(timestamp)

								if (station == station_id) {

									// compute F 10min-average and store in DB

/*										// the hard way
										def t0 = System.currentTimeMillis();
										int i
										// append F data to F-list
										if (timestamp > f_lastts) {
											f_list.add([(timestamp):f_val])
											f_lastts = timestamp
										}
										// remove old F data from F-list (trim beginning of list until timestamp is reached)
										long timestamp_minus600 = timestamp - 600
										if (timestamp_minus600 > f_firstts) {
											def newfirsti = 0;
											for (i = 0; i < f_list.size(); i++) {
												if (f_list[i].key < timestamp_minus600) newfirsti = i
												else break
											}
											f_firstts = f_list[i].key
											for (i = 0; i < newfirsti; i++) {
												f_list.remove(0)
											}
										}
										// compute average
										def f_avg600 = f_val
										def flen = f_list.size()
										if (flen > 0) {
											def sum = 0
											for (i = 0; i < flen; i++) {
												sum += f_list[i]
											}
											f_avg600 = sum / flen
										}
										def t1 = System.currentTimeMillis();
										println "F 10min-avg compute time: ${t1-t0}ms: avg = $f_avg600, llen = $flen"
*/
									// the DB way
///										def t0 = System.currentTimeMillis();
									def f_avg600b = dbService.computeFAverage(station, timestamp, 600)
//										def t1 = System.currentTimeMillis();
//										println "F 10min-avg compute time: ${t1-t0}ms: avg = $f_avg600b db"

									//log.info("I2 epoch: $timestamp  date $dateobj")

									new Instrument2Msg(
										station:station,
										f:f_val,
										f_avg600: f_avg600b,
										ts:str[9..26],
										timestamp:dateobj,
										epoch:timestamp,
										one_min:resolutions["one_min"],
										five_min:resolutions["five_min"],
										ten_min:resolutions["ten_min"],
										fifteen_min:resolutions["fifteen_min"],
										thirty_min:resolutions["thirty_min"]
									).save(failOnError: true)

									obj_counter = hibernateSessionFlush(obj_counter)
								}
							}
							msg_name = secdata_type.name
						} else if (msg_type == magdiag_type.settingValue) {
							log.trace "data ${msg_type} packet: $str"

							//[ 59 6515220110912222400.000 BDT 1 15.79 2 16.30 3 20.70 4 29.64 5 25.85 6 25.85]
							def station = str[28..30]
							def a = str[32..-1].split(" ")
							def sdf = new SimpleDateFormat("yyyyMMddHHmmss")
							sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

							def electronics_temp    = Float.parseFloat(a[1])
							def total_field_temp    = Float.parseFloat(a[3])
							def fluxgate_temp       = Float.parseFloat(a[5])
							def outside_temp        = Float.parseFloat(a[7])
							def battery_voltage     = Float.parseFloat(a[9])
							def auxilliary_voltage  = Float.parseFloat(a[11])

							def dateobj   = sdf.parse(str[9..26])
							def timestamp = dateobj.getTime() / 1000 as long
							def resolutions = resolutionIntervals(timestamp)

							if (station == station_id) {
								new MagDiagMsg(
									station:station,
									ts:str[9..26],
									electronics_temp:electronics_temp,
									fluxgate_temp:fluxgate_temp,
									total_field_temp:total_field_temp,
									outside_temp:outside_temp,
									battery_voltage:battery_voltage,
									auxilliary_voltage:auxilliary_voltage,
									timestamp:dateobj,
									epoch:timestamp,
									one_min:resolutions["one_min"],
									five_min:resolutions["five_min"],
									ten_min:resolutions["ten_min"],
									fifteen_min:resolutions["fifteen_min"],
									thirty_min:resolutions["thirty_min"]
								).save(failOnError: true)
								obj_counter = hibernateSessionFlush(obj_counter)
							}

							msg_name = magdiag_type.name

						} else if (msg_type == diag3v3b_type.settingValue) {
							log.trace "data ${msg_type} packet: $str"

							//[ 59 6515320110912205748.000 BDT 0.412347 -1.595679 0.566362 43 0 96]
							def station = str[28..30]
							def a = str[32..-1].split(" ")
							def h_voltage = Float.parseFloat(a[0])
							def e_voltage = Float.parseFloat(a[1])
							def z_voltage = Float.parseFloat(a[2])
							def bh = Float.parseFloat(a[3])
							def be = Float.parseFloat(a[4])
							def bz = Float.parseFloat(a[5])
							def sdf = new SimpleDateFormat("yyyyMMddHHmmss")
							sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

							def dateobj   = sdf.parse(str[9..26])
							def timestamp = dateobj.getTime() / 1000 as long
							def resolutions = resolutionIntervals(timestamp)

							if (station == station_id) {
								new RawFluxGateMsg(
									station:station,
									ts:str[9..26],
									h:h_voltage,
									e:e_voltage,
									z:z_voltage,
									bh:bh,
									be:be,
									bz:bz,
									timestamp:dateobj,
									epoch:timestamp,
									one_min:resolutions["one_min"],
									five_min:resolutions["five_min"],
									ten_min:resolutions["ten_min"],
									fifteen_min:resolutions["fifteen_min"],
									thirty_min:resolutions["thirty_min"]
								).save(failOnError: true)
								obj_counter = hibernateSessionFlush(obj_counter)
							}
							msg_name = diag3v3b_type.name
						} else if (msg_type == supptimepacket_type.settingValue) {
							log.trace "data ${msg_type} packet: $str"
							msg_name = supptimepacket_type.name
						} else if (msg_type == goeslasttx_type.settingValue) {
							log.trace "data ${msg_type} packet: $str"
							msg_name = goeslasttx_type.name
						} else if (msg_type == datamag_type.settingValue) {
							log.trace "data ${msg_type} packet: $str"
							msg_name = datamag_type.name
						} else {
							log.trace "data (unhandled) packet: $str"
							msg_name = "- unhandled -"
						}

						// log.info("Message[$msg_name]: [$str]")
						bufferindex=0
						counter++
						buffer = new byte[bufferlength]
					} else {
						buffer[bufferindex] = inbuffer[0]
						bufferindex++
					}
				} // if state == ASSEMBLING_MESSAGE

				if (connectionSettingChangedService.shouldStop()) {
					beat_it = false
					log.info("stopping message importer as requested")
				}

				if (connectionSettingChangedService.getChanged()) {
					connectionSettingChangedService.setChanged(false)
					beat_it = false
					log.info("connection settings have changed")
				}

				if (stationSettingChangedService.getChanged()) {
					stationSettingChangedService.setChanged(false)
					station_setting = Setting.findByName('local_station_id')
					station_id      = station_setting.settingValue
					log.info("station id has changed to $station_setting")
				}
			} // while
		} // try
		catch (Exception e) {
			log.info(e)
		}
		finally {
			beat_it = false
			hibernateSessionFlush(-1)
			if (socket) {
				socket.close()
				socket = null
			}
			connectionStatusService.setState(false);
		}
		log.info "message importer job ends"
	}
}
