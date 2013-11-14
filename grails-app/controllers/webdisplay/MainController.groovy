package webdisplay

import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import java.text.SimpleDateFormat
import java.util.Date;
import webdisplay.ConnectionStatus
import webdisplay.AbsolutesStatus

class MainController
{
	def sessionFactory
	def springSecurityService
	def connectionSettingChangedService
	def connectionStatusService
	def stationSettingChangedService
	def quartzScheduler

	def beforeInterceptor = {
		if (actionName != 'status_data') println("MainController.$actionName $params")
	}

	def index = { redirect(action:"magnetometers") }

//	@Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
	def stopimporter = {
		connectionSettingChangedService.setStop(true)
		Thread.sleep(10000)
		render "importer stop"
	}

//	@Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
	def startimporter = {
		connectionSettingChangedService.setStop(false)
		render "importer start"
	}

	def mainParams(params)
	{
//		println "enter params $params"
		def cal = Calendar.instance

		// load settings
//		def h_scale_setting = Setting.findByName("horizontal_scale")
		def v_scale_setting = Setting.findByName("vertical_scale")
		def station_setting = Setting.findByName("local_station_id")
		def station_id = station_setting.settingValue

		//  set h_div from client or set default, h_div is in hours
//		def h_div = params?.horizontal_scale as float ?: h_scale_setting.settingValue as int
		int h_div = params.horizontal_scale ? params.horizontal_scale as int : 3600 // FIXME default h_scale

		// check h_div is in bounds
//		if(!(h_div <= 744 && h_div >= 0.0082 )){ h_div = 1 }
		if (h_div > 604800 || h_div < 30) h_div = 3600 // FIXME default h_scale

		// set v_div from client or set default
		def v_div = params?.vertical_scale as int ?: v_scale_setting.settingValue as int

		// check v_div is in bounds
		if(!(v_div <= 5000 && v_div >= 1 )){  v_div = 50 }

		// figure out start date, seed with and default to today/now
		cal.setTime(new Date())
		cal.setTimeZone(TimeZone.getTimeZone('GMT'))
		Date timenow = cal.getTime()
		Date startdate = cal.getTime()

		// if provided by the user then use that
		if (params?.startdate) {
			//SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd")
			def min,hour,secs
			if (params?.hour && params?.minute) {
				hour = params.hour
				min = params.minute
				secs = "00"
			} else {
				hour = cal.get(Calendar.HOUR_OF_DAY)
				min = cal.get(Calendar.MINUTE)
				secs = cal.get(Calendar.SECOND)
			}
			//println "Hour/min/sec $hour:$min:$secs"

			SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
			f.setTimeZone(TimeZone.getTimeZone("GMT"));
			startdate = (Date)f.parse(params.startdate + " $hour:$min:$secs")
			println "startdate provided = $params.startdate ($hour:$min:$secs)"
		}

		def startepoch_utc = (startdate.getTime() / 1000L) as long
//		println "start date = $startdate, epoch = $startepoch_utc"
		
		// calculate timewindow with h_div.  timewindow is in seconds
		def timewindow =  Math.floor(h_div) // * 3600)

		// calculate previous epoch 
		def previousepoch_utc = (startepoch_utc - timewindow) as long

		return [
			startepoch_utc: startepoch_utc,
			previousepoch_utc: previousepoch_utc,
			station_id: station_id,
			timewindow: timewindow,
			h_div: h_div,
			v_div: v_div,
			timenow: timenow,
			start_epoch: previousepoch_utc,
			end_epoch: startepoch_utc
		]
	}

	def magnetometers = {
		def data = [:]

		def settings = Setting.list()
		settings.each { data[it.name] = it.settingValue }

		def h_list = [:]
		HorizontalDivision.findAll().each { h_list[it.optionValue] = it.optionName }
		def v_list = [:]
		VerticalDivision.findAll().each { v_list[it.optionValue] = it.optionName }

		data.reset_timeout = data.reset_timeout ?: 3600
		data.local_station_id = data.local_station_id ?: ''
		data.local_station_desc = data.local_station_desc ?: ''

		data.v_list = v_list
		data.v_value = data.vertical_scale ?: 0 // 0 = auto
		data.h_list = h_list
		data.h_value = data.horizontal_scale ?: 3600

		data.starttime = ''
		data.startdate = ''
		data.hour_list = [:]
		data.second_list = [:]
		data.minute_list = [:]
		(0..23).each {
			def padded = it < 10 ? "0$it" : "$it"
			data.hour_list[padded] = padded
		}
		(0..59).each {
			def padded = it < 10 ? "0$it" : "$it"
			data.minute_list[padded] = padded
			data.second_list[padded] = padded
		}
		data.hour_value = 0
		data.minute_value = 0
		data.second_value = 0

		data.rr_list = [:]
		data.rr_list << [ 1: 'every second']
		data.rr_list << [ 2: '2 seconds']
		data.rr_list << [ 5: '5 seconds']
		data.rr_list << [ 10: '10 seconds']
		data.rr_list << [ 30: '30 seconds']
		data.rr_list << [ 60: '1 minute']
		data.rr_value = data.refresh_rate ?: 5

//		println "DATA $data"
		data
	}

	def temperatures = {
		def data = [:]

		def settings = Setting.list()
		settings.each { data[it.name] = it.settingValue }

		def h_list = [:]
		HorizontalDivision.findAll().each { if (it.optionValue >= 1800) h_list[it.optionValue] = it.optionName }
		def v_list = [:]
		VerticalDivision.findAll().each { v_list[it.optionValue] = it.optionName }

		data.reset_timeout = data.reset_timeout ?: 3600
		data.local_station_id = data.local_station_id ?: ''
		data.local_station_desc = data.local_station_desc ?: ''

		data.v_list = v_list
		data.v_value = data.vertical_scale ?: 50
		data.h_list = h_list
		data.h_value =  data.horizontal_scale ?: 3600

		data.starttime = ''
		data.startdate = ''
		data.hour_list = [:]
		data.second_list = [:]
		data.minute_list = [:]
		(0..23).each {
			def padded = it < 10 ? "0$it" : "$it"
			data.hour_list[padded] = padded
		}
		(0..59).each {
			def padded = it < 10 ? "0$it" : "$it"
			data.minute_list[padded] = padded
			data.second_list[padded] = padded
		}
		data.hour_value = 0
		data.minute_value = 0
		data.second_value = 0

		data.rr_list = [:]
//		data.rr_list << [ 1: 'every second']
//		data.rr_list << [ 2: '2 seconds']
		data.rr_list << [ 5: '5 seconds']
		data.rr_list << [ 10: '10 seconds']
		data.rr_list << [ 30: '30 seconds']
		data.rr_list << [ 60: '1 minute']
		data.rr_value = 60

//		println "DATA $data"
		data
	}

	def fetchInstrumentData(common) {
		def session = sessionFactory.currentSession
		long resolution = 1
		def query
		if (common.h_div <= 3600) {
			resolution = 1
			query = """\
select i1.epoch * 1000 as epoch,
i1.timestamp as timestamp,
i1.h as h,
i1.d as e,
i1.z as z,
i2.f as f,
i3.bh as bh,
i3.be as be,
i3.bz as bz,
i3.h as raw_h,
i3.e as raw_e,
i3.z as raw_z,
i2.f_avg600 as f_avg600
from instrument1msg i1
left join instrument2msg i2 on i1.epoch = i2.epoch
left join raw_flux_gate_msg i3 on i2.epoch = i3.epoch
where i1.epoch >= ${common.previousepoch_utc} and i1.epoch <= ${common.startepoch_utc}
order by epoch;"""
		} else if (common.h_div > 3600 && common.h_div < 168*3600) {
			resolution = 60
			query = """\
select i1.one_min * 1000 as one_min,
i1.timestamp as timestamp,
avg(i1.h) as h,
avg(i1.d) as e,
avg(i1.z) as z,
avg(i2.f) as f,
avg(i3.bh) as bh,
avg(i3.be) as be,
avg(i3.bz) as bz,
avg(i3.h) as raw_h,
avg(i3.e) as raw_e,
avg(i3.z) as raw_z,
i2.f_avg600 as f_avg600
from instrument1msg i1
left join instrument2msg i2 on i1.epoch = i2.epoch
left join raw_flux_gate_msg i3 on i2.epoch = i3.epoch
where i1.epoch >= ${common.previousepoch_utc} and i1.epoch <= ${common.startepoch_utc}
group by one_min
order by one_min;"""
		} else {
			resolution = 300
			query = """\
select  i1.five_min * 1000 as five_min,
i1.timestamp as timestamp,
avg(i1.h) as h,
avg(i1.d) as e,
avg(i1.z) as z,
avg(i2.f) as f,
avg(i3.bh) as bh,
avg(i3.be) as be,
avg(i3.bz) as bz,
avg(i3.h) as raw_h,
avg(i3.e) as raw_e,
avg(i3.z) as raw_z,
i2.f_avg600 as f_avg600
from instrument1msg i1
left join instrument2msg i2 on i1.epoch = i2.epoch
left join raw_flux_gate_msg i3 on i2.epoch = i3.epoch
where i1.epoch >= ${common.previousepoch_utc} and i1.epoch <= ${common.startepoch_utc}
group by five_min
order by five_min;"""
		}

		// query_results = list of:
		// 0  epoch
		// 1  i1.timestamp as timestamp,
		// 2  avg(i1.h) as h,
		// 3  avg(i1.d) as e,
		// 4  avg(i1.z) as z,
		// 5  avg(i2.f) as f,
		// 6  avg(i3.be) as be,
		// 7  avg(i3.bz) as bz,
		// 8  avg(i3.bh) as bh,
		// 9  avg(i3.h) as raw_h,
		// 10 avg(i3.e) as raw_e,
		// 11 avg(i3.z) as raw_z
		def query_results = session.createSQLQuery(query).list()

		// get the latest possible data point, only for the scale and vector status indicators to be happy
		if (resolution > 1) {
			def querylast = """\
select i1.epoch * 1000 as epoch,
i1.timestamp as timestamp,
i1.h as h,
i1.d as e,
i1.z as z,
i2.f as f,
i3.bh as bh,
i3.be as be,
i3.bz as bz,
i3.h as raw_h,
i3.e as raw_e,
i3.z as raw_z,
i2.f_avg600 as f_avg600
from instrument1msg i1
left join instrument2msg i2 on i1.epoch = i2.epoch
left join raw_flux_gate_msg i3 on i2.epoch = i3.epoch
where i1.epoch >= ${common.previousepoch_utc} and i1.epoch <= ${common.startepoch_utc}
order by epoch desc limit 1;"""
			def querylast_results = session.createSQLQuery(querylast).list()
			if (querylast_results) query_results.add(querylast_results[0])
		}

		resolution *= 1000
		long query_begin_epoch = common.previousepoch_utc * 1000 // long  java.lang.Long
		long query_end_epoch = common.startepoch_utc * 1000 // long
		println "query epochs: from $query_begin_epoch to $query_end_epoch" // (type ${query_begin_epoch.getClass().getName()})"

		// build full list of epochs without holes, and data map from query results list
		def is_first = true
		long data_begin_epoch = 0
		long data_end_epoch = 0
		def full_epoch_list = []
		def full_data_map = [:]
		long current_epoch
		def data_points_count = query_results.size()
		def added_points_count = 0
		query_results.each {
			long data_epoch = it[0]
			data_end_epoch = data_epoch
			if (is_first) {
				data_begin_epoch = data_epoch
				current_epoch = data_epoch
				is_first = false
			}
			while (current_epoch < data_epoch) {
				added_points_count++
				full_epoch_list.add(current_epoch)
				full_data_map << [(current_epoch):[h:null, e:null, z:null, f:null, be:null, bz:null, bh:null, raw_h:null, raw_e:null, raw_z:null, f_avg600:null]]
				current_epoch += resolution
			}
			full_epoch_list.add(data_epoch)
			full_data_map << [(data_epoch):[h:it[2],e:it[3],z:it[4],f:it[5],bh:it[6],be:it[7],bz:it[8],raw_h:it[9],raw_e:it[10],raw_z:it[11], f_avg600:it[12]]]
			current_epoch = data_epoch + resolution
		}
		println "data point count = $data_points_count + $added_points_count interstitial null points"
		// add first null point if needed
		if (!query_results.size() || query_begin_epoch < data_begin_epoch) {
			full_epoch_list.add(0, query_begin_epoch)
			full_data_map << [(query_begin_epoch):[h:null, e:null, z:null, f:null, be:null, bz:null, bh:null, raw_h:null, raw_e:null, raw_z:null, f_avg600:null]]
			println "added start point at epoch $query_begin_epoch (data-begin $data_begin_epoch, query-begin $query_begin_epoch)"
		}
		// add final null point if needed
		if (!query_results.size() || query_end_epoch > data_end_epoch + resolution*4) {
			long last_epoch = query_end_epoch - resolution*4
			full_epoch_list.add(last_epoch)
			full_data_map << [(last_epoch):[h:null, e:null, z:null, f:null, be:null, bz:null, bh:null, raw_h:null, raw_e:null, raw_z:null, f_avg600:null]]
			println "added end point at epoch $last_epoch (data-end $data_end_epoch, query-end $query_end_epoch)"
		}

		// compute delta f
		/* Visual basic delta f calculation
		 *
		 ' calculate the delta F
		 Public Function DeltaF(ByVal F As Double, ByVal H As Double, ByVal D As Double, ByVal Z As Double) As Double

			 ' DCS -- 20040820 -- test limit set to 1.1 so that any of the "accepted"
			 ' hole values (99999, 99999.9, 99999.99) will get rejected
			 ' DCS -- 20050628 -- dont compute unless all components available

			 If ((Abs(Abs(F) - cHoleValue) < 1.1) Or (Abs(Abs(H) - cHoleValue) < 1.1) Or
				 (Abs(Abs(D) - cHoleValue) < 1.1) Or (Abs(Abs(Z) - cHoleValue) < 1.1)) Then
				 DeltaF = cHoleValue
			 Else
				 DeltaF = Sqr(H * H + D * D + Z * Z) - F
			 End If
		  End Function
		*/
		def hv = 99999.99
		full_epoch_list.each { key ->
			def data = full_data_map[key]
			def delta_f = null
			try {
				def f = data.f
				def h = data.h
				def e = data.e
				def z = data.z
				if ((f == null || h == null || e == null || z == null)
					|| (Math.abs(Math.abs(f) - hv) < 1.1)
					|| (Math.abs(Math.abs(h) - hv) < 1.1)
					|| (Math.abs(Math.abs(e) - hv) < 1.1)
					|| (Math.abs(Math.abs(z) - hv) < 1.1)) {
					delta_f = null
				} else {
					delta_f = Math.sqrt(h*h + e*e + z*z) - f
				}
			} catch(Exception e) {
				println key
				println data
				println e
			}
			full_data_map[key] << [df:delta_f]
		}

//		println "DATA $full_data_map"
		println "returning ${full_epoch_list.size()} magnet. data points"
		[ epoch_list:full_epoch_list, data_end_epoch:data_end_epoch, data_begin_epoch:data_begin_epoch,
		 data:full_data_map, query:query, resolution:resolution ]
	}

	def fetchTemperatureData(common) {
		def session = sessionFactory.currentSession
		long resolution = 1
		def query
		if (common.h_div <= 168 * 3600) {
			resolution = 60 // yes, temp data as 1min res.
			query = """\
select epoch * 1000, timestamp,
battery_voltage, electronics_temp, fluxgate_temp, outside_temp, total_field_temp
from mag_diag_msg where station = '${common.station_id}'
and epoch >= ${common.previousepoch_utc} and epoch <= ${common.startepoch_utc}
order by epoch;"""
/*
		} else if (common.h_div > 3600 && common.h_div <= 168*3600) {
			resolution = 60
			query = """\
select one_min * 1000, timestamp, avg(battery_voltage) as battery_voltage, avg(electronics_temp) as electronics_temp,
avg(fluxgate_temp) as fluxgate_temp, avg(outside_temp) as outside_temp, avg(total_field_temp) as total_field_temp
from mag_diag_msg where station = '${common.station_id}'
and epoch >= ${common.previousepoch_utc} and epoch < ${common.startepoch_utc}
group by one_min order by epoch;"""
*/
		} else {
			resolution = 300
			query = """\
select five_min * 1000, timestamp,
avg(battery_voltage) as battery_voltage, avg(electronics_temp) as electronics_temp,
avg(fluxgate_temp) as fluxgate_temp, avg(outside_temp) as outside_temp, avg(total_field_temp) as total_field_temp
from mag_diag_msg where station = '${common.station_id}'
and epoch >= ${common.previousepoch_utc} and epoch <= ${common.startepoch_utc}
group by five_min order by five_min;"""
		}
		resolution *= 1000
		def query_results = session.createSQLQuery(query).list()

		long query_begin_epoch = common.previousepoch_utc * 1000 // long  java.lang.Long
		long query_end_epoch = common.startepoch_utc * 1000 // long
		println "query epochs: from $query_begin_epoch to $query_end_epoch"

		// build full list of epochs without holes, and data map from query results list
		def is_first = true
		long data_begin_epoch = query_begin_epoch
		long data_end_epoch = query_end_epoch
		def full_epoch_list = []
		def full_data_map = [:]
		long current_epoch
		def data_points_count = query_results.size()
		def added_points_count = 0
		query_results.each {
			long data_epoch = it[0]
			data_end_epoch = data_epoch
			if (is_first) {
				data_begin_epoch = data_epoch
				current_epoch = data_epoch
				is_first = false
			}
			while (current_epoch < data_epoch) {
				added_points_count++
				full_epoch_list.add(current_epoch)
				full_data_map << [(current_epoch):[el:null, fg:null, tf:null, ou:null, ba: null]]
				current_epoch += resolution
			}
			full_epoch_list.add(data_epoch)
			full_data_map << [(data_epoch):[el:it[3],fg:it[4],tf:it[6],ou:it[5],ba:it[2]]]
			current_epoch = data_epoch + resolution
		}
		println "data point count = $data_points_count + $added_points_count interstitial null points"
		// add first null point if needed
		if (!query_results.size() || query_begin_epoch < data_begin_epoch) {
			full_epoch_list.add(0, query_begin_epoch)
			full_data_map << [(query_begin_epoch):[el:null, fg:null, tf:null, ou:null, ba: null]]
			println "added start point at epoch $query_begin_epoch (data-begin $data_begin_epoch, query-begin $query_begin_epoch)"
		}
		// add final null point if needed
		if (!query_results.size() || query_end_epoch > data_end_epoch + resolution*2) {
			long last_epoch = query_end_epoch - resolution*2
			full_epoch_list.add(last_epoch)
			full_data_map << [(last_epoch):[el:null, fg:null, tf:null, ou:null, ba: null]]
			println "added end point at epoch $last_epoch (data-end $data_end_epoch, query-end $query_end_epoch)"
		}

		//println "DATA $full_data_map"
		println "returning ${full_epoch_list.size()} temp. data points"
		[ epoch_list:full_epoch_list, data:full_data_map, query:query, resolution:resolution ]
	}

	def magnetometer_data = {
		def common = mainParams(params)
		def results = fetchInstrumentData(common)
		def status_results = fetchStatusData(common)
		def datamap = results.data
		def is_data = results.epoch_list.size() > 0
		def end_epoch = is_data ? results.epoch_list[-1] : 0
//		def begin_epoch = is_data ? results.epoch_list[0] : 0
		def onehourago_epoch = is_data ? end_epoch - 1000 * 60 * 60 : 0
		def min_h = 999999.9; def max_h = -999999.9
		def min_e = 999999.9; def max_e = -999999.9
		def min_z = 999999.9; def max_z = -999999.9
		def min_df= 999999.9; def max_df= -999999.9
		def dead_h = 0, dead_e = 0, dead_z = 0
		def fspikes = 0
		def f = [] ,h = [], e = [], z = [], be =[], bh = [], bz = [], re = [], rh = [], rz = [], df = [], f_avg600 = []
		def settings = [:]; Setting.list().each{ settings[it.name] = it.settingValue }
		def check_fspikes = settings.f_spike_per_hour && settings.f_spike_amplitude
		def f_spike_amplitude = settings.f_spike_amplitude ? settings.f_spike_amplitude.toInteger() : 0

		results.epoch_list.each{ epoch ->
			def d = datamap[epoch]
			f.add([epoch, d.f])
			f_avg600.add([epoch, d.f_avg600])
			h.add([epoch, d.h])
			e.add([epoch, d.e])
			z.add([epoch, d.z])
			df.add([epoch, d.df])
			bh.add([epoch, d.bh])
			be.add([epoch, d.be])
			bz.add([epoch, d.bz])
			rh.add([epoch, d.raw_h])
			re.add([epoch, d.raw_e])
			rz.add([epoch, d.raw_z])
			// inspect changes for absolutes status
			if (epoch >= onehourago_epoch) {
				if (d.h == null) dead_h++
				else {
					if (d.h < min_h) min_h = d.h
					if (d.h > max_h) max_h = d.h
				}
				if (d.e == null) dead_e++
				else {
					if (d.e < min_e) min_e = d.e
					if (d.e > max_e) max_e = d.e
				}
				if (d.z == null) dead_z++
				else {
					if (d.z < min_z) min_z = d.z
					if (d.z > max_z) max_z = d.z
				}
				if (check_fspikes && d.f_avg600!= null && d.f!=null && Math.abs(d.f - d.f_avg600)>f_spike_amplitude) fspikes++
				if (d.df!= null && d.df<min_df) min_df= d.df
				if (d.df!= null && d.df>max_df) max_df= d.df
			}
		}

		// compute absolutes status
		def abst = 2 // error level: 0 ok, 1 warn, 2 crit
		def crit = 0 // critical errors count
		def warn = 0 // warning errors count
		def fail = [], wrns = [], info = [], nock = []
		def el_temp_state = 'nominal'
		def fg_temp_state = 'nominal'
		def tf_temp_state = 'nominal'
		while (true) {
//			def cal = Calendar.instance
//			cal.timeZone = TimeZone.getTimeZone("UTC")
//			def sdf = new SimpleDateFormat()
//			cal.time = new Date(last_epoch)
//			info = "${sdf.format(cal.time)}\n"

			if (!results.epoch_list.size()) { info += "(no data)"; break }
			def last_epoch = results.epoch_list[-1]
			if (!last_epoch) { info += "(no data)"; break }
			def d = datamap[last_epoch]

			if (settings.h_z_e_dead_value) { // 100
				def hez_dead_value = settings.h_z_e_dead_value.toInteger()
				if (dead_h > hez_dead_value) { crit++; fail += "dead H values: <em>$dead_h</em> &gt; $hez_dead_value" }
				else { info += "dead H values: <em>$dead_h</em> &lt; $hez_dead_value" }

				if (dead_e > hez_dead_value) { crit++; fail += "dead E values: <em>$dead_e</em> &gt; $hez_dead_value" }
				else { info += "dead E values: <em>$dead_e</em> &lt; $hez_dead_value" }

				if (dead_z > hez_dead_value) { crit++; fail += "dead Z values: <em>$dead_z</em> &gt; $hez_dead_value" }
				else { info += "dead Z values: <em>$dead_z</em> &lt; $hez_dead_value" }
			} else {
				nock += "dead H, E, Z values"
			}

			if (settings.h_bin_minimum || settings.h_bin_maximum) { // -256, 256
				if (d.bh == null) { crit++; fail += "no Bin H data" }
				else {
					def binh = d.bh
					if (settings.h_bin_minimum && binh < settings.h_bin_minimum.toInteger()) { crit++; fail += "Bin H: <em>$binh</em> &lt; minimum $settings.h_bin_minimum" }
					else if (settings.h_bin_maximum && binh > settings.h_bin_maximum.toInteger()) { crit++; fail += "Bin H: <em>$binh</em> &gt; maximum $settings.h_bin_maximum" }
					else info += "Bin H: $settings.h_bin_minimum &lt; <em>$binh</em> &lt; $settings.h_bin_maximum"
			}	} else {
				nock += "Bin H range"
			}

			if (settings.e_bin_minimum || settings.e_bin_maximum) {
				if (d.be == null) { crit++; fail += "no Bin E data" }
				else {
					def bine = d.be
					if (settings.e_bin_minimum && bine < settings.e_bin_minimum.toInteger()) { crit++; fail += "Bin E: <em>$bine</em> &lt; minimum $settings.e_bin_minimum" }
					else if (settings.e_bin_maximum && bine > settings.e_bin_maximum.toInteger()) { crit++; fail += "Bin E: <em>$bine</em> &gt; maximum $settings.e_bin_maximum" }
					else info += "Bin E: $settings.e_bin_minimum &lt; <em>$bine</em> &lt; $settings.e_bin_maximum"
			}	} else {
				nock += "Bin E range"
			}

			if (settings.z_bin_minimum || settings.z_bin_maximum) {
				if (d.bz == null) { crit++; fail += "no Bin Z data" }
				else {
					def binz = d.bz
					if (settings.z_bin_minimum && binz < settings.z_bin_minimum.toInteger()) { crit++; fail += "Bin Z: <em>$binz</em> &lt; minimum $settings.z_bin_minimum" }
					else if (settings.z_bin_maximum && binz > settings.z_bin_maximum.toInteger()) { crit++; fail += "Bin Z: <em>$binz</em> &gt; maximum $settings.z_bin_maximum" }
					else info += "Bin Z: $settings.z_bin_minimum &lt; <em>$binz</em> &lt; $settings.z_bin_maximum"
			}	} else {
				nock += "Bin Z range"
			}

			if (settings.h_z_e_voltage_tol) {
				if (d.raw_h == null || d.raw_e == null || d.raw_z == null) { crit++; fail += "missing some H,E,Z voltage data" }
				else {
					def vh = d.raw_h
					def ve = d.raw_e
					def vz = d.raw_z
					def vh2 = Math.floor(1000*vh)/1000
					def ve2 = Math.floor(1000*ve)/1000
					def vz2 = Math.floor(1000*vz)/1000
					def vmin = vh > ve ? ve : vh; vmin = vmin > vz ? vz : vmin
					def vmax = vh < ve ? ve : vh; vmax = vmax < vz ? vz : vmax
					def h_z_e_voltage_tol = settings.h_z_e_voltage_tol.toBigDecimal()
					if (vmax - vmin < h_z_e_voltage_tol) { crit++; fail += "similar H, E, Z voltages: <em>$vh2</em>, <em>$ve2</em>, <em>$vz2</em>" }
					else { info += "H, E, Z voltages: <em>$vh2</em>, <em>$ve2</em>, <em>$vz2</em>" }
			}	} else {
				nock += "H, E, Z voltages"
			}

			if (settings.delta_f_critical) {
				def delta_f_critical = settings.delta_f_critical.toBigDecimal()
				if (d.df == null) { crit++; fail += "no ΔF data" }
				else {
					def d1 = d.df
					def d2 = Math.floor(100*d1)/100
					if (d1 > delta_f_critical) { crit++; fail += "ΔF: <em>$d2</em> &gt;> maximum $settings.delta_f_critical" }
					else { info += "ΔF: <em>$d2</em> &lt; $settings.delta_f_critical" }
			}	} else {
				nock += "ΔF range"
			}

			if (settings.delta_f_rate_change) {
				def df_rate_change = settings.delta_f_rate_change.toInteger()
				if (min_df < 999999.0 && max_df > -999999.0) {
					def crx = max_df - min_df
					def crx2 = Math.round(crx * 100)/100
					if (crx > df_rate_change) { crit++; fail += "ΔF rate of change: <em>$crx2</em> &gt; $df_rate_change within 1h" }
					else { info += "ΔF rate of change: <em>$crx2</em> &lt; $df_rate_change within 1h" }
				} else {
					info += "ΔF rate of change: (no data)"
			}	} else {
				nock += "ΔF rate of change"
			}

			if (settings.hez_rate_change) {
				def hez_rate_change = settings.hez_rate_change.toInteger()
				if (min_h < 999999.0 && max_h > -999999.0) {
					def crx = max_h - min_h
					def crx2 = Math.round(crx * 100)/100
					if (crx > hez_rate_change) { crit++; fail += "H rate of change: <em>$crx2</em> &gt; $hez_rate_change within 1h" }
					else { info += "H rate of change: <em>$crx2</em> &lt; $hez_rate_change within 1h" }
				} else {
					info += "H rate of change: (no data)"
				}

				if (min_e < 999999.0 && max_e > -999999.0) {
					def crx = max_e - min_e
					def crx2 = Math.round(crx * 100)/100
					if (crx > hez_rate_change) { crit++; fail += "E rate of change: <em>$crx2</em> &gt; $hez_rate_change within 1h" }
					else { info += "E rate of change: <em>$crx2</em> &lt; $hez_rate_change within 1h" }
				} else {
					info += "E rate of change: (no data)"
				}

				if (min_z < 999999.0 && max_z > -999999.0) {
					def crx = max_z - min_z
					def crx2 = Math.round(crx * 100)/100
					if (crx > hez_rate_change) { crit++; fail += "Z rate of change: <em>$crx2</em> &gt; $hez_rate_change within 1h" }
					else { info += "Z rate of change: <em>$crx2</em> &lt; $hez_rate_change within 1h" }
				} else {
					info += "Z rate of change: (no data)"
				}
			} else {
				nock += "H, E, Z rate of change"
			}

			if (check_fspikes) {
				def f_spike_per_hour = settings.f_spike_per_hour.toInteger()
				if (fspikes > f_spike_per_hour) { crit++; fail += "F spikes (change > $f_spike_amplitude): <em>$fspikes</em> &gt; $f_spike_per_hour within 1h" }
				else { info += "F spikes (change > $f_spike_amplitude): <em>$fspikes</em> &lt; $f_spike_per_hour within 1h" }
			} else {
				nock += "F spikes count"
			}

			if (settings.battery_warning || settings.battery_critical) {
				if (!status_results.voltage || status_results.voltage == "(no data)") { warn++; wrns += "no battery voltage data" }
				else {
					def ba = status_results.voltage
					if (settings.battery_critical && ba < settings.battery_critical.toFloat()) { crit++; fail += "battery voltage: <em>$ba</em> &lt; $settings.battery_critical" }
					else if (settings.battery_warning && ba < settings.battery_warning.toFloat()) { warn++; wrns += "battery voltage: <em>$ba</em> &lt; $settings.battery_warning" }
					else info += "battery voltage: <em>$ba</em> &gt; $settings.battery_warning"
			}	} else {
				nock += "battery voltage"
			}

			if (settings.electronics_min_critical || settings.electronics_max_critical || settings.electronics_min_warning || settings.electronics_max_warning) {
				if (!status_results.electronics || status_results.electronics == "(no data)") { warn++; wrns += "no electronics temperature data" }
				else {
					def t = status_results.electronics
					if (settings.electronics_max_critical && t > settings.electronics_max_critical.toBigDecimal()) { crit++; fail += "electronics temperatures: <em>$t</em> &gt; $settings.electronics_max_critical"; el_temp_state = 'critmax' }
					else if (settings.electronics_min_critical && t < settings.electronics_min_critical.toBigDecimal()) { crit++; fail += "electronics temperatures: <em>$t</em> &lt; $settings.electronics_min_critical"; el_temp_state = 'critmin' }
					else if (settings.electronics_max_warning && t > settings.electronics_max_warning.toBigDecimal()) { warn++; wrns += "electronics temperatures: <em>$t</em> &gt; $settings.electronics_max_warning"; el_temp_state = 'warnmax' }
					else if (settings.electronics_min_warning && t < settings.electronics_min_warning.toBigDecimal()) { warn++; wrns += "electronics temperatures: <em>$t</em> &lt; $settings.electronics_min_warning"; el_temp_state = 'warnmin' }
					else info += "electronics temperatures: $settings.electronics_min_warning &lt; <em>$t</em> &lt; $settings.electronics_max_warning"
			}	} else {
				nock += "electronics temperatures"
			}

			if (settings.proton_min_critical || settings.proton_max_critical || settings.proton_min_warning || settings.proton_max_warning) {
				if (!status_results.totalfield || status_results.totalfield == "(no data)") { warn++; wrns += "no proton temperature data" }
				else {
					def t = status_results.totalfield
					if (settings.proton_max_critical && t > settings.proton_max_critical.toBigDecimal()) { crit++; fail += "proton temperatures:<em>$t</em> &gt; $settings.proton_max_critical"; tf_temp_state = 'critmax' }
					else if (settings.proton_min_critical && t < settings.proton_min_critical.toBigDecimal()) { crit++; fail += "proton temperatures: <em>$t</em> &lt; $settings.proton_min_critical"; tf_temp_state = 'critmin' }
					else if (settings.proton_max_warning && t > settings.proton_max_warning.toBigDecimal()) { warn++; wrns += "proton temperatures: <em>$t</em> &gt; $settings.proton_max_warning"; tf_temp_state = 'warnmax' }
					else if (settings.proton_min_warning && t < settings.proton_min_warning.toBigDecimal()) { warn++; wrns += "proton temperatures: <em>$t</em> &lt; $settings.proton_min_warning"; tf_temp_state = 'warnmin' }
					else info += "proton temperatures: $settings.proton_min_warning &lt; <em>$t</em> &lt; $settings.proton_max_warning"
			}	} else {
				nock += "proton temperatures"
			}

			if (settings.fluxgate_min_critical || settings.fluxgate_max_critical || settings.fluxgate_min_warning || settings.fluxgate_max_warning) {
				if (!status_results.fluxgate || status_results.fluxgate == "(no data)") { warn++; wrns += "no fluxgate temperature data" }
				else {
					def t = status_results.fluxgate
					if (settings.fluxgate_max_critical && t > settings.fluxgate_max_critical.toBigDecimal()) { crit++; fail += "fluxgate temperatures: <em>$t</em> &gt; $settings.fluxgate_max_critical"; fg_temp_state = 'critmax' }
					else if (settings.fluxgate_min_critical && t < settings.fluxgate_min_critical.toBigDecimal()) { crit++; fail += "fluxgate temperatures: <em>$t</em> &lt; $settings.fluxgate_min_critical"; fg_temp_state = 'critmin' }
					else if (settings.fluxgate_max_warning && t > settings.fluxgate_max_warning.toBigDecimal()) { warn++; wrns += "fluxgate temperatures: <em>$t</em> &gt; $settings.fluxgate_max_warning"; fg_temp_state = 'warnmax' }
					else if (settings.fluxgate_min_warning && t < settings.fluxgate_min_warning.toBigDecimal()) { warn++; wrns += "fluxgate temperatures: <em>$t</em> &lt; $settings.fluxgate_min_warning"; fg_temp_state = 'warnmin' }
					else info += "fluxgate temperatures: $settings.fluxgate_min_warning &lt; <em>$t</em> &lt; $settings.fluxgate_max_warning"
			}	} else {
				nock += "fluxgate temperatures"
			}

			abst = 0
			if (warn) abst = 1
			if (crit) abst = 2
			break
		}
		def html = ""
		if (fail.size()) {
			html += "<div class=\"asinfo asinfo-crit\"><h4>CRITICAL</h4>"
			fail.each{ html += "<p>$it</p>" }
			html += "</div>"
		}
		if (wrns.size()) {
			html += "<div class=\"asinfo asinfo-warn\"><h4>WARNING</h4>"
			wrns.each{ html += "<p>$it</p>" }
			html += "</div>"
		}
		if (info.size()) {
			html += "<div class=\"asinfo asinfo-info\"><h4>INFO</h4>"
			info.each{ html += "<p>$it</p>" }
			html += "</div>"
		}
		if (nock.size()) {
			html += "<div class=\"asinfo asinfo-nock\"><h4>UNCHECKED</h4>"
			nock.each{ html += "<p>$it</p>" }
			html += "</div>"
		}

		def data = [:]
		data.f  = [label:"F",         data:f,  color:2 ]
		data.h  = [label:"H",         data:h,  color:4 ]
		data.e  = [label:"E",         data:e,  color:6 ]
		data.z  = [label:"Z",         data:z,  color:3 ]
		data.df = [label:"&Delta;F",  data:df, color:5 ]
		data.bh = [label:"Bin H",     data:bh, color:6 ]
		data.be = [label:"Bin E",     data:be, color:7 ]
		data.bz = [label:"Bin Z",     data:bz, color:8 ]
		data.vh = [label:"Voltage H", data:rh, color:9 ]
		data.ve = [label:"Voltage E", data:re, color:10]
		data.vz = [label:"Voltage Z", data:rz, color:11]
		data.end_epoch = common.end_epoch
		data.query = results.query
		data.absolutes_status = abst
		data.absolutes_info = html
		data.el_temp_state = el_temp_state
		data.fg_temp_state = fg_temp_state
		data.tf_temp_state = tf_temp_state

		render data as JSON
	}

	def temperature_data = {
		def common = mainParams(params)
		def results = fetchTemperatureData(common)
		def datamap = results.data
		def results2 = fetchInstrumentData(common)
		def datamap2 = results2.data
		def status_results = fetchStatusData(common)
		def is_data = results.epoch_list.size() > 0
		long end_epoch = is_data ? results.epoch_list[-1] : 0
		def onehourago_epoch = is_data ? end_epoch - 1000 * 60 * 60 : 0
		def min_df= 999999.9; def max_df= -999999.9
		def last_df

		def el = [], fg = [], tf = [], ou = [], ba = [], df = []
		results.epoch_list.each{ epoch ->
			def d = datamap[epoch]
			el.add([epoch, d.el])
			fg.add([epoch, d.fg])
			tf.add([epoch, d.tf])
			ou.add([epoch, d.ou])
			ba.add([epoch, d.ba])

			def ddf = datamap2[epoch]
			if (ddf) { df.add([epoch, ddf.df]) } else { df.add([epoch, null]) }

			last_df = ddf ? ddf.df : null
			if (epoch >= onehourago_epoch) { // inspect changes for absolutes status
				if (last_df!=null && last_df<min_df) min_df=last_df
				if (last_df!=null && last_df>max_df) max_df=last_df
			}
		}
/*
		if (results.epoch_list.size() > 0) {
			long begin_epoch = results.epoch_list[0]
			df.add([begin_epoch, null])
			results2.epoch_list.each{ epoch ->
				if (epoch < begin_epoch) return
				if (epoch > end_epoch) return
				def d = datamap2[epoch]
				df.add([epoch, d.df])

				last_df = d.df
				// inspect changes for absolutes status
				if (epoch >= onehourago_epoch) {
					if (d.df!= null && d.df<min_df) min_df= d.df
					if (d.df!= null && d.df>max_df) max_df= d.df
				}
			}
			df.add([end_epoch, null])
		}
		println "XXXXXXXXXXXXXXXXXXXXXX " +df.size()
*/

		// compute absolutes status
		def abst = 2 // error level: 0 ok, 1 warn, 2 crit
		def crit = 0 // critical errors count
		def warn = 0 // warning errors count
		def fail = [], wrns = [], info = [], nock = []
		def el_temp_state = 'nominal'
		def fg_temp_state = 'nominal'
		def tf_temp_state = 'nominal'
		while (true) {
			if (!results.epoch_list.size()) { info += "(no data)"; break }
			def last_epoch = results.epoch_list[-1]
			if (!last_epoch) { info += "(no data)"; break }
			def d = datamap[last_epoch]
			def settings = [:]; Setting.list().each{ settings[it.name] = it.settingValue }

			if (settings.delta_f_critical) {
				def delta_f_critical = settings.delta_f_critical.toBigDecimal()
				if (last_df == null) { crit++; fail += "no ΔF data" }
				else {
					def d1 = last_df
					def d2 = Math.floor(100*d1)/100
					if (d1 > delta_f_critical) { crit++; fail += "ΔF: <em>$d2</em> &gt; maximum $settings.delta_f_critical" }
					else { info += "ΔF: <em>$d2</em> &lt; $settings.delta_f_critical" }
				}	} else {
				nock += "ΔF range"
			}

			if (settings.delta_f_rate_change) {
				def df_rate_change = settings.delta_f_rate_change.toInteger()
				if (min_df < 999999.0 && max_df > -999999.0) {
					def crx = max_df - min_df
					def crx2 = Math.round(crx * 100)/100
					if (crx > df_rate_change) { crit++; fail += "ΔF rate of change: <em>$crx2</em> &gt; $df_rate_change within 1h" }
					else { info += "ΔF rate of change: <em>$crx2</em> &lt; $df_rate_change within 1h" }
				} else {
					info += "ΔF rate of change: (no data)"
				}	} else {
				nock += "ΔF rate of change"
			}

			if (settings.battery_warning || settings.battery_critical) {
				if (!status_results.voltage || status_results.voltage == "(no data)") { warn++; wrns += "no battery voltage data" }
				else {
					def bav = status_results.voltage
					if (settings.battery_critical && bav < settings.battery_critical.toFloat()) { crit++; fail += "battery voltage: <em>$bav</em> &lt; $settings.battery_critical" }
					else if (settings.battery_warning && bav < settings.battery_warning.toFloat()) { warn++; wrns += "battery voltage: <em>$bav</em> &lt; $settings.battery_warning" }
					else info += "battery voltage: <em>$bav</em> &gt; $settings.battery_warning"
				}	} else {
				nock += "battery voltage"
			}

			if (settings.electronics_min_critical || settings.electronics_max_critical || settings.electronics_min_warning || settings.electronics_max_warning) {
				if (!status_results.electronics || status_results.electronics == "(no data)") { warn++; wrns += "no electronics temperature data" }
				else {
					def t = status_results.electronics
					if (settings.electronics_max_critical && t > settings.electronics_max_critical.toBigDecimal()) { crit++; fail += "electronics temperatures: <em>$t</em> &gt; $settings.electronics_max_critical"; el_temp_state = 'critmax' }
					else if (settings.electronics_min_critical && t < settings.electronics_min_critical.toBigDecimal()) { crit++; fail += "electronics temperatures: <em>$t</em> &lt; $settings.electronics_min_critical"; el_temp_state = 'critmin' }
					else if (settings.electronics_max_warning && t > settings.electronics_max_warning.toBigDecimal()) { warn++; wrns += "electronics temperatures: <em>$t</em> &gt; $settings.electronics_max_warning"; el_temp_state = 'warnmax' }
					else if (settings.electronics_min_warning && t < settings.electronics_min_warning.toBigDecimal()) { warn++; wrns += "electronics temperatures: <em>$t</em> &lt; $settings.electronics_min_warning"; el_temp_state = 'warnmin' }
					else info += "electronics temperatures: $settings.electronics_min_warning &lt; <em>$t</em> &lt; $settings.electronics_max_warning"
				}	} else {
				nock += "electronics temperatures"
			}

			if (settings.proton_min_critical || settings.proton_max_critical || settings.proton_min_warning || settings.proton_max_warning) {
				if (!status_results.totalfield || status_results.totalfield == "(no data)") { warn++; wrns += "no proton temperature data" }
				else {
					def t = status_results.totalfield
					if (settings.proton_max_critical && t > settings.proton_max_critical.toBigDecimal()) { crit++; fail += "proton temperatures:<em>$t</em> &gt; $settings.proton_max_critical"; tf_temp_state = 'critmax' }
					else if (settings.proton_min_critical && t < settings.proton_min_critical.toBigDecimal()) { crit++; fail += "proton temperatures: <em>$t</em> &lt; $settings.proton_min_critical"; tf_temp_state = 'critmin' }
					else if (settings.proton_max_warning && t > settings.proton_max_warning.toBigDecimal()) { warn++; wrns += "proton temperatures: <em>$t</em> &gt; $settings.proton_max_warning"; tf_temp_state = 'warnmax' }
					else if (settings.proton_min_warning && t < settings.proton_min_warning.toBigDecimal()) { warn++; wrns += "proton temperatures: <em>$t</em> &lt; $settings.proton_min_warning"; tf_temp_state = 'warnmin' }
					else info += "proton temperatures: $settings.proton_min_warning &lt; <em>$t</em> &lt; $settings.proton_max_warning"
				}	} else {
				nock += "proton temperatures"
			}

			if (settings.fluxgate_min_critical || settings.fluxgate_max_critical || settings.fluxgate_min_warning || settings.fluxgate_max_warning) {
				if (!status_results.fluxgate || status_results.fluxgate == "(no data)") { warn++; wrns += "no fluxgate temperature data" }
				else {
					def t = status_results.fluxgate
					if (settings.fluxgate_max_critical && t > settings.fluxgate_max_critical.toBigDecimal()) { crit++; fail += "fluxgate temperatures: <em>$t</em> &gt; $settings.fluxgate_max_critical"; fg_temp_state = 'critmax' }
					else if (settings.fluxgate_min_critical && t < settings.fluxgate_min_critical.toBigDecimal()) { crit++; fail += "fluxgate temperatures: <em>$t</em> &lt; $settings.fluxgate_min_critical"; fg_temp_state = 'critmin' }
					else if (settings.fluxgate_max_warning && t > settings.fluxgate_max_warning.toBigDecimal()) { warn++; wrns += "fluxgate temperatures: <em>$t</em> &gt; $settings.fluxgate_max_warning"; fg_temp_state = 'warnmax' }
					else if (settings.fluxgate_min_warning && t < settings.fluxgate_min_warning.toBigDecimal()) { warn++; wrns += "fluxgate temperatures: <em>$t</em> &lt; $settings.fluxgate_min_warning"; fg_temp_state = 'warnmin' }
					else info += "fluxgate temperatures: $settings.fluxgate_min_warning &lt; <em>$t</em> &lt; $settings.fluxgate_max_warning"
				}	} else {
				nock += "fluxgate temperatures"
			}

			abst = 0
			if (warn) abst = 1
			if (crit) abst = 2
			break
		}
		def html = ""
		if (fail.size()) {
			html += "<div class=\"asinfo asinfo-crit\"><h4>CRITICAL</h4>"
			fail.each{ html += "<p>$it</p>" }
			html += "</div>"
		}
		if (wrns.size()) {
			html += "<div class=\"asinfo asinfo-warn\"><h4>WARNING</h4>"
			wrns.each{ html += "<p>$it</p>" }
			html += "</div>"
		}
		if (info.size()) {
			html += "<div class=\"asinfo asinfo-info\"><h4>INFO</h4>"
			info.each{ html += "<p>$it</p>" }
			html += "</div>"
		}
		if (nock.size()) {
			html += "<div class=\"asinfo asinfo-nock\"><h4>UNCHECKED</h4>"
			nock.each{ html += "<p>$it</p>" }
			html += "</div>"
		}

		def data = [:]
		data.el = [label:"Electronics", data:el, color:2]
		data.fg = [label:"Fluxgate",    data:fg, color:4]
		data.tf = [label:"Total Field", data:tf, color:6]
		data.ou = [label:"Outside",     data:ou, color:3]
		data.ba = [label:"Battery",     data:ba, color:0]
		data.df = [label:"&Delta;F",    data:df, color:5]
		data.end_epoch = common.end_epoch
		data.query = results.query
		data.absolutes_status = abst
		data.absolutes_info = html
		data.el_temp_state = el_temp_state
		data.fg_temp_state = fg_temp_state
		data.tf_temp_state = tf_temp_state

		render data as JSON
	}

	def fetchStatusData(common) {
		def settings = [:]; Setting.list().each{ settings[it.name] = it.settingValue }
		def data = [:]
//		def local_station_id = Setting.findByName("local_station_id")
//		def station = local_station_id.settingValue

//		// set it back 15 minutes. what?
//		def cal = Calendar.instance
//		cal.time = new Date(cal.time.time - 900 * 1000)
//		def date = new Date(cal.time.time)
//		data.currenttime = date.time

		def cal = Calendar.instance
		cal.setTimeZone(TimeZone.getTimeZone('GMT'))
		data.currenttime = cal.getTime().getTime()

		// def q0 = "from ConnectionStatus where timestamp > :timestamp and station = :station order by timestamp desc"
		// def statuslist = ConnectionStatus.findAll( q0, [station:station,timestamp:date])
		// def status = statuslist.getAt(0).status

		data.connected = false
		def status = connectionStatusService.getState()
		if (status) data.connected = true

		def m1 = MagDiagMsg.withCriteria {
			and {
				eq('station',common.station_id)
				ge('epoch',common.previousepoch_utc)
				le('epoch',common.startepoch_utc)
			}
			order('epoch', 'asc')
		}

		data.el_temp_state = 'nominal'
		data.fg_temp_state = 'nominal'
		data.tf_temp_state = 'nominal'
		if (m1.size() > 0) {
			data.voltage     = m1[-1].battery_voltage
			data.electronics = Math.round(m1[-1].electronics_temp*10)/10
			data.fluxgate    = Math.round(m1[-1].fluxgate_temp*10)/10
			data.totalfield  = Math.round(m1[-1].total_field_temp*10)/10
			data.outside     = Math.round(m1[-1].outside_temp*10)/10
//			data.currenttime = m1[-1].epoch * 1000 // what?

			if (settings.electronics_min_critical || settings.electronics_max_critical || settings.electronics_min_warning || settings.electronics_max_warning) {
				def t = data.electronics
				if (settings.electronics_max_critical && t > settings.electronics_max_critical.toBigDecimal()) { data.el_temp_state = 'critmax' }
				else if (settings.electronics_min_critical && t < settings.electronics_min_critical.toBigDecimal()) { data.el_temp_state = 'critmin' }
				else if (settings.electronics_max_warning && t > settings.electronics_max_warning.toBigDecimal()) { data.el_temp_state = 'warnmax' }
				else if (settings.electronics_min_warning && t < settings.electronics_min_warning.toBigDecimal()) { data.el_temp_state = 'warnmin' }
			}
			if (settings.proton_min_critical || settings.proton_max_critical || settings.proton_min_warning || settings.proton_max_warning) {
				def t = data.totalfield
				if (settings.proton_max_critical && t > settings.proton_max_critical.toBigDecimal()) { data.tf_temp_state = 'critmax' }
				else if (settings.proton_min_critical && t < settings.proton_min_critical.toBigDecimal()) { data.tf_temp_state = 'critmin' }
				else if (settings.proton_max_warning && t > settings.proton_max_warning.toBigDecimal()) { data.tf_temp_state = 'warnmax' }
				else if (settings.proton_min_warning && t < settings.proton_min_warning.toBigDecimal()) { data.tf_temp_state = 'warnmin' }
			}
			if (settings.fluxgate_min_critical || settings.fluxgate_max_critical || settings.fluxgate_min_warning || settings.fluxgate_max_warning) {
				def t = data.fluxgate
				if (settings.fluxgate_max_critical && t > settings.fluxgate_max_critical.toBigDecimal()) { data.fg_temp_state = 'critmax' }
				else if (settings.fluxgate_min_critical && t < settings.fluxgate_min_critical.toBigDecimal()) { data.fg_temp_state = 'critmin' }
				else if (settings.fluxgate_max_warning && t > settings.fluxgate_max_warning.toBigDecimal()) { data.fg_temp_state = 'warnmax' }
				else if (settings.fluxgate_min_warning && t < settings.fluxgate_min_warning.toBigDecimal()) { data.fg_temp_state = 'warnmin' }
			}
		} else {
			data.voltage     = "(no data)"
			data.electronics = "(no data)"
			data.fluxgate    = "(no data)"
			data.totalfield  = "(no data)"
			data.outside     = "(no data)"
			data.currenttime = "(no data)"
		}
		data.end_epoch = common.end_epoch
		data
	}

	def status_data = {
		def data = fetchStatusData(mainParams(params))
		render data as JSON
	}

    def binlog = {
        def common = mainParams(params)
        
        def data = [:]
        def session = sessionFactory.currentSession
        
        def data_retention_setting = Setting.findByName('data_retention_policy')
        //def drs = data_retention_setting.settingValue ?: 31
        def bin_h_const = Setting.findByName("bin_h_const")
        def bin_e_const = Setting.findByName("bin_e_const")
        def bin_z_const = Setting.findByName("bin_z_const")
        def voltage_h_const = Setting.findByName("voltage_h_const")
        def voltage_e_const = Setting.findByName("voltage_e_const")
        def voltage_z_const = Setting.findByName("voltage_z_const")
        def local_station_id = Setting.findByName("local_station_id")
        
        def cal = Calendar.instance
        
        def to_year, to_month, to_day, to_hour, to_minute, to_second
        if (params?."todate") {
                def parts = params.todate.split("/")
                to_month = parts[0] as int
                to_day = parts[1] as int
                to_year = parts[2] as int
        } else {
                // if not provided set a default and set it forward one day 
                // to avoid futzing with the tdate h:m:s time settings
                def d = new Date( cal.time.time + 86400000)
                cal.time = d
                to_year = cal.get(Calendar.YEAR)
                to_month = cal.get(Calendar.MONTH) + 1
                to_day = cal.get(Calendar.DATE)
        }
        def todate = "$to_month/$to_day/$to_year";
        def from_year, from_month,from_day
        if (params?."fromdate") {
                def parts = params.fromdate.split("/")
                from_month = parts[0] as int
                from_day = parts[1] as int
                from_year = parts[2] as int
        } else {
                //def date = new Date( cal.time.time ) - (drs as int)
                def date = new Date( cal.time.time ) - 7
                cal.setTime(date)
                from_year = cal.get(Calendar.YEAR)
                from_month = cal.get(Calendar.MONTH) + 1
                from_day = cal.get(Calendar.DATE)
        }
        def fromdate = "$from_month/$from_day/$from_year";
        def tdate = "$to_year-$to_month-$to_day 00:00:00"
        def fdate = "$from_year-$from_month-$from_day 00:00:00"
        
        def query0 = """\
select bl.id,bl.after_id,bl.before_id,bl.station,bl.timestamp,
rf_before.id id_before,
rf_before.timestamp as timestamp_before,
(rf_before.epoch * 1000) as epoch_before,
rf_before.bh as bh_before,
rf_before.be as be_before,
rf_before.bz as bz_before,
rf_before.h as h_before,
rf_before.e as e_before,
rf_before.z as z_before,
round((rf_before.bh* ${bin_h_const.settingValue} + rf_before.h  * ${voltage_h_const.settingValue}),2) as h_nT_before,
round((rf_before.be* ${bin_e_const.settingValue} + rf_before.e  * ${voltage_e_const.settingValue}),2) as e_nT_before,
round((rf_before.bz* ${bin_z_const.settingValue} + rf_before.z  * ${voltage_z_const.settingValue}),2) as z_nT_before,
rf_after.id as id_after,
rf_after.timestamp as timestamp_after,
(rf_after.epoch * 1000) as epoch_after,
rf_after.bh as bh_after,
rf_after.be as be_after,
rf_after.bz as bz_after,
rf_after.h as h_after,
rf_after.e as e_after,
rf_after.z as z_after,
round((rf_after.bh* ${bin_h_const.settingValue} + rf_after.h  * ${voltage_h_const.settingValue}),2) as h_nT_after,
round((rf_after.be* ${bin_e_const.settingValue} + rf_after.e  * ${voltage_e_const.settingValue}),2) as e_nT_after,
round((rf_after.bz* ${bin_z_const.settingValue} + rf_after.z  * ${voltage_z_const.settingValue}),2) as z_nT_after
from bin_log bl
left join raw_flux_gate_msg rf_before on before_id = rf_before.id
left join raw_flux_gate_msg rf_after on after_id = rf_after.id
where bl.station = '${local_station_id.settingValue}'
and bl.timestamp >= '${fdate}' and bl.timestamp <= '${tdate}'"""
        
        def rows0 = session.createSQLQuery(query0).list()
        data["rows"] = rows0
        
        //println query0
        //println rows0
        
        /*
        if( params?.download == 'csv'){
            response.setHeader("Content-disposition", "attachment; filename=binlog.csv");
            render(contentType: "text/csv", text: "1,2,3");
        }
        */

		// F log
		// Find all rows where |f(t) - t_avg600(t)|>threshold
		// Log: time, delta, f(t), avg600, threshold
		data.fspikes = []
		def resolution_s = Setting.findByName("f_spike_resolution").settingValue
		def resolution = resolution_s ? resolution_s.toInteger() : 1;
		String f_spike_amplitude_s = Setting.findByName('f_spike_amplitude').settingValue
		if (f_spike_amplitude_s) {
			def station = Setting.findByName("local_station_id").settingValue
			def threshold = f_spike_amplitude_s.toInteger()
			def queryf = """\
select epoch, f, f_avg600
from instrument2msg
where station = '$station'
and timestamp >= '$fdate' and timestamp <= '$tdate'
and (f - f_avg600 > $threshold or f_avg600 - f > $threshold)
order by 'epoch' desc;
"""
//			println "F-spikes query = $queryf"
			def frows = session.createSQLQuery(queryf).list()

			long pepoch = 0
			frows.each{ r ->
				if (r[1] == null || r[2] == null) return
				if (r[0] - pepoch < resolution) { pepoch = r[0]; return } // same spike
				pepoch = r[0]
				def d = Math.round(Math.abs(r[1] - r[2])*100)/100
				data.fspikes.add([pepoch*1000,d,r[1],r[2],threshold])
			}
		}
        
        [data_retention:data_retention_setting.settingValue,data:data,fromdate:fromdate,todate:todate,
			f_spike_resolution:resolution
		]
        //[data:data,fromdate:fromdate,todate:todate]
    }

	@Secured(['IS_AUTHENTICATED_FULLY'])
    def userlog = {
        def default_records_per_page = Setting.findByName('default_records_per_page')
        def records_per_page = default_records_per_page.settingValue ?: 10
        // def record_count = ChangeLog.count()
        def page_number
    
            
        def data_retention_setting = Setting.findByName('data_retention_policy')
        def drs = data_retention_setting.settingValue ?: 31
        
        def cal = Calendar.instance
        
        def to_year, to_month, to_day
        if(params?."todate"){
                def parts = params.todate.split("/")
                to_month = parts[0] as int
                to_day = parts[1] as int
                to_year = parts[2] as int
                
        }else{
                to_year = cal.get(Calendar.YEAR)
                to_month = cal.get(Calendar.MONTH) + 1
                to_day = cal.get(Calendar.DATE)
        }
        def todate = "$to_month/$to_day/$to_year";
        
        
        def from_year, from_month,from_day
        if(params?."fromdate"){
                def parts = params.fromdate.split("/")
                from_month = parts[0] as int
                from_day = parts[1] as int
                from_year = parts[2] as int
        }else{
                def date = new Date( cal.time.time ) - (drs as int)
                cal.setTime(date)
                from_year = cal.get(Calendar.YEAR)
                from_month = cal.get(Calendar.MONTH) + 1
                from_day = cal.get(Calendar.DATE)
        }
        def fromdate = "$from_month/$from_day/$from_year";
                        
        if(params?."next"){
            page_number = params?.page_number
            page_number = ( page_number as int) + 1
        }else if(params?.prev){
            page_number = params?.page_number
            page_number = ( page_number as int) - 1
        }else{
            page_number = params?.page_number ?: 1
        }
        
        if(params?."filter"){
            page_number=1
        }
        
        cal.set(Calendar.YEAR,from_year)
        cal.set(Calendar.MONTH, from_month - 1)
        cal.set(Calendar.DATE, from_day)
        
        Date fdate = new Date(cal.time.time)
        
        cal.set(Calendar.YEAR,to_year)
        cal.set(Calendar.MONTH, to_month - 1)
        cal.set(Calendar.DATE, to_day)
                
        Date tdate = new Date(cal.time.time)
        
        def my_offset = ( (page_number as int) - 1 ) * (records_per_page as int)
        
        def criteria1 = ChangeLog.createCriteria()
        def objs1 = criteria1 {
                                between("timestamp",fdate,tdate)
                                order("timestamp","desc")
                                maxResults(records_per_page as int)
                                firstResult(my_offset)
        }//end criteria1
        
        def criteria2 = ChangeLog.createCriteria()
        def objs2 = criteria2 {
                                    between("timestamp",fdate,tdate)
                                    order("timestamp","desc")
        }//end criteria2

        def record_count = objs2.size()
         
        def number_of_pages = Math.ceil( record_count / (records_per_page as int ) ) as int
        
        [page_number:page_number,number_of_pages:number_of_pages,records:objs1,fromdate:fromdate,todate:todate]

    }
    
    @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
    def deleteuser = {
        def userDetails = springSecurityService.principal
        def admin = User.get(userDetails.id)
        def adminusername = admin.username
        
        def roles = Role.list(sort:"authority",order:"desc")
        
        if( params?.delete && params?.user_id ){
                
            //clean up user roles table as well
            def user = User.get(params.user_id)
            def username = user.username
        
            if( username == 'System'){
                
                def message = "'$adminusername' attempted to delete user '$username'."
                def cl = new ChangeLog(timestamp:new Date(),user:adminusername,message:message,host:request.remoteHost)
                cl.save(failOnError:true)
                flash.message = "User '$username' can not be removed."
                
                redirect(action:"admin",fragment:"tab-1")
                
            }else{
                UserRole.removeAll(user)
                user.delete(flush:true)
                def message = "'$adminusername' deleted user '$username'."
                def cl = new ChangeLog(timestamp:new Date(),user:adminusername,message:message,host:request.remoteHost)
                cl.save(failOnError:true)
                flash.message = "User '$username' removed."
            }
            
            def hibSession = sessionFactory.getCurrentSession()
            hibSession.flush()
            hibSession.clear()
        }
    
        redirect(action:"admin",fragment:"tab-1")
    }

    @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
    def createuser = {
        def userDetails = springSecurityService.principal
        def admin = User.get(userDetails.id)
        def adminusername = admin.username
        def username = params.username
                
        def user = User.findByUsername(username)
        if(!user){
            def roles = Role.list()
            
            if( username != '' && username != null && username ==~ /^\w+$/ ) {
                
                if( params.newpassword != '' || params.newpassword != null ||
                    params.confirmpassword != '' || params.confirmpassword != null){
                     
                    if( params.newpassword == params.confirmpassword){
                    
                        def role_user = Role.findByAuthority('ROLE_USER')
                        def new_user = new User( username:params.username,
                                                 password:springSecurityService.encodePassword(params.confirmpassword),
                                                 enabled:true).save(failOnError: true)
                    
                        // by default the user role is added
                        new UserRole(user:new_user,role:role_user).save(failOnError:true)
                        flash.message = "User '$username' successfully created."
                    
                        // check for other roles checked and add them as well
                        // println params
                        
                        roles.each {
                            if(params?."${it.authority}"){
                                if(params?."${it.authority}" == 'on'){
                                    new UserRole(user:new_user,role:it).save(failOnError:true)
                                }
                            }
                        }
                        
                        def message = "'$adminusername' created user '$username'."
                        def cl = new ChangeLog(timestamp:new Date(),user:adminusername,message:message,host:request.remoteHost)
                        cl.save(failOnError:true)
                        
                        def hibSession = sessionFactory.getCurrentSession()
                        hibSession.flush()
                        hibSession.clear()
                    
                    }else{
                        flash.message = "New Password and Confirm Password do not match."
                    }// end if

                }else{
                    flash.message = "New Password and Confirm Password can't be empty or 'null'."
                }// end if
                        
            }else{
                flash.message = "Username can't be empty or 'null' or contain spaces or non alpha numeric characters."
            }
                    
        }else{
            flash.message = "User '$username' already exist!"
            // redirect(action:"admin",fragment:"tab-2",params:params)
        }
        
        redirect(action:"admin",fragment:"tab-2")
    }
    
    @Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
    def createrole = {
        def userDetails = springSecurityService.principal
        def user = User.get(userDetails.id)
        def username = user.username
        def rolename = params.rolename
        
        if( params.rolename != '' && params.rolename != null ){
            def role = Role.findByAuthority(rolename)
            if(!role){
                def message = "'$username' created role '$rolename'"
                def cl = new ChangeLog(timestamp:new Date(),user:username,message:message,host:request.remoteHost)
                cl.save(failOnError:true)
                    
                new Role(authority:rolename).save(failOnError: true)
                
                def hibSession = sessionFactory.getCurrentSession()
                hibSession.flush()
                hibSession.clear()
                
                flash.message = "Role '$rolename' created."
                
            }else{
                flash.message = "Role '$rolename' already exists."
            }
        }else{
            flash.message = "Role can't be empty or 'null'."
        }
        
        redirect(action:"admin",fragment:"tab-4")
    }
  
	@Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
	def edituser = {
		def userDetails = springSecurityService.principal
		def admin = User.get(userDetails.id)
		def adminusername = admin.username
		def user = User.get(params.user_id)
		def username = user.username
		
		//println user
		
		def usersroleslist = UserRole.findAllByUser(user)
		def roles = Role.list(sort:"authority",order:"asc")
		def usersrolesmap = [:], usersroleschanges = [:]
		def role_user  = Role.findByAuthority('ROLE_USER')
		def role_admin = Role.findByAuthority('ROLE_ADMIN')
		
		//println usersroleslist
		
		usersroleslist.each {
			
			usersrolesmap[it.role.id] = true
			
			// always keep the user role
			if(it.role.id == role_user.id ){
				usersroleschanges[it.role.id] = "retain"
				
			// 'System' always retains admin role
			}else if(it.role.id == role_admin.id && user.username == 'System' ){				
				usersroleschanges[it.role.id] = "retain"
			}else{
				usersroleschanges[it.role.id] = "removed"
			}
		}
		
		//println usersrolesmap
		//println "Prior to processing role changes: $usersroleschanges"
		
		if(request.post && params?.changeroles){
		
			// if there is no user then don't bother
			if(user){
				
				def rolemap = [:]
				
				// check for changes
				roles.each {
					rolemap[it.id] = it
					if(params?."${it.authority}"){
						if(params?."${it.authority}" == 'on'){
							if(usersrolesmap.containsKey(it.id)){
								usersroleschanges[it.id] = "retain"
							}else{
								usersroleschanges[it.id] = "added"
							}//end if else
						}//end if
					}//end if
				} //end roles.each
				
				//println "About to process role changes: $usersroleschanges"
							
				usersroleschanges.each { k, v ->
								if( v == 'added'){
									new UserRole(user:user,role:rolemap[k]).save(failOnError:true)
								}else if( v == 'removed'){
									def ur = UserRole.get(user.id, k)
									ur.delete()
								}
				}
				def message = "'$adminusername' changed roles for user '$username'. Changes are: $usersroleschanges"
				flash.message="Roles changed for user '$username'!"
				def cl = new ChangeLog(timestamp:new Date(),user:adminusername,message:message,host:request.remoteHost)
				cl.save(failOnError:true)

				def hibSession = sessionFactory.getCurrentSession()
				hibSession.flush()
				hibSession.clear()
			}else{
				flash.message = "No user to perform operation on!"
			}
			
			redirect(action:"admin",fragment:"tab-1")
		} // changeroles
		
		
		// edit user change password form handler
		if(request.post && params?.changepassword){
			if( params.confirmpassword == '' || params.confirmpassword == null || params.newpassword == '' || params.newpassword == null){
				flash.message = "New Password and Confirm Password can't be empty or 'null'."
			}else{
				if( params.confirmpassword == params.newpassword){
					user.password = springSecurityService.encodePassword(params.confirmpassword)
					user.save(failOnError: true)
						
					def message = "'$adminusername' changed password for user '$username'."
					def cl = new ChangeLog(timestamp:new Date(),user:adminusername,message:message,host:request.remoteHost)
					cl.save(failOnError:true)
						
					def hibSession = sessionFactory.getCurrentSession()
					hibSession.flush()
					hibSession.clear()
									
					flash.message="Password changed for user '$username'!"
					redirect(action:"admin",fragment:"tab-1")
						
				} else {
						flash.message = "New Password and Confirm Password do not match."
				}//end if
			}
		}
		
		// default view model
		[user:user,usersrolesmap:usersrolesmap,roles:roles]
	}

	@Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
	def deleterole = {
		def userDetails = springSecurityService.principal
		def admin = User.get(userDetails.id)
		def adminusername = admin.username
		
		if(params?.role_id){
			// clean up user roles tables
			def r = Role.get(params.role_id)
			def rolename = r.authority
			
			if( rolename == 'ROLE_ADMIN' || rolename == 'ROLE_USER'){
				def message = "'$adminusername' attempted to delete role '$rolename'."
				def cl = new ChangeLog(timestamp:new Date(),user:adminusername,message:message,host:request.remoteHost)
				cl.save(failOnError:true)
				flash.message = "Role '$rolename' can not be removed."
			} else {
				UserRole.removeAll(r)
				r.delete(fluse:true)
				def message = "'$adminusername' deleted role '$rolename'."
				def cl = new ChangeLog(timestamp:new Date(),user:adminusername,message:message,host:request.remoteHost)
				cl.save(failOnError:true)
				flash.message = "Role '$rolename' removed."
			}
					
			def hibSession = sessionFactory.getCurrentSession()
			hibSession.flush()
			hibSession.clear()
		}
		redirect(action:"admin",fragment:"tab-3")
	}

	@Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
	def admin = {
		def userDetails = springSecurityService.principal
		def admin = User.get(userDetails.id)
		def adminusername = admin.username
		def users = User.list(sort:"username",order:"asc")
		def roles = Role.list(sort:"authority",order:"asc")
		def userrolelist = UserRole.list(sort:"user",order:"asc")

		def usersrolesmap = [:]
		userrolelist.each {  usersrolesmap[it.user.id] = []     }
		userrolelist.each {  usersrolesmap[it.user.id] << it }

		[users:users, roles:roles,usersroles:usersrolesmap ]
	}
	
	@Secured(['ROLE_ADMIN', 'IS_AUTHENTICATED_FULLY'])
	def savesettings = {
		def settings = Setting.list()

		if (request.post) {
			if (params?.save) {
				flash.message = ""

				def userDetails = springSecurityService.principal
				def user = User.get(userDetails.id)
				def username = user.username
				def currentpassword = user.password

				settings.each {
					def value = params[it.name]
//					println "VALUE $it.name = '$value'"
					if (!params.containsKey(it.name)) return
					if (it.settingValue == value) return // no change

					def message = "<b>${it.uiLabel}</b> has changed from '${it.settingValue}' to '${value}'."
					def cl = new ChangeLog(timestamp:new Date(),user:username,message:message,host:request.remoteHost)
					cl.save(failOnError:true)

					it.settingValue = value
					it.lastUpdate = new Date();
					it.save(failOnError:true)

					if (it.name == "exporter_host" || it.name == "exporter_port") {
						connectionSettingChangedService.setChanged(true)
					}
					if (it.name == "local_station_id" ) {
						stationSettingChangedService.setChanged(true)
					}
					flash.message = "Application settings saved. "
				}

				// password update
				def pwd_message = ""
				while (params.oldpassword || params.newpassword || params.confirmpassword) {
					println 'starting password change'
					pwd_message = "Password not changed: "
					if (!params.newpassword) {
						pwd_message += "new password not provided."
						println "new password not provided"
						break
					}
					if (!params.oldpassword) {
						pwd_message += "missing current password."
						println "missing current password"
						break
					}
					def oldpassword = springSecurityService.encodePassword(params.oldpassword)
					if (currentpassword != oldpassword) {
						pwd_message += "incorrect password."
						println "incorrect password"
						break
					}
					if (params.confirmpassword != params.newpassword) {
						pwd_message += "new passwords do not match."
						println "new password not confirmed"
						break
					}
					def newpassword = springSecurityService.encodePassword(params.confirmpassword)
					user.password = newpassword
					user.save(failOnError: true)
					def message = "User '$username' changed their password."
					def cl = new ChangeLog(timestamp:new Date(),user:username,message:message,host:request.remoteHost)
					cl.save(failOnError:true)
					pwd_message = "Password changed."
					println "password changed"
					flash.message += pwd_message
					break
				}
			}
		}

		def hibSession = sessionFactory.getCurrentSession()
		hibSession.flush()
		hibSession.clear()

		setLoginTimeout()

		redirect(action:"settings")
	}

	def settings = {
		def settings = Setting.list()
		def data = [:], h_list = [:], v_list = [:], rt_list = [:], lt_list = [:], drp_list = [:], rpp_list = [:]

		HorizontalDivision.findAll().each { h_list[it.optionValue] = it.optionName }
		VerticalDivision.findAll().each { v_list[it.optionValue] = it.optionName }
		ResetTimeout.findAll().sort{ it.optionValue }.each { rt_list[it.optionValue] = it.optionName }
		LoginTimeout.findAll().each { lt_list[it.optionValue] = it.optionName }
		DataRetentionPolicy.findAll().each { drp_list[it.optionValue] = it.optionName }

		data.rpp_list = [:]
		data.rpp_list[5] = "5 rows"
		data.rpp_list[10] = "10 rows"
		data.rpp_list[20] = "20 rows"
		data.rpp_list[50] = "50 rows"
		data.rpp_list[100] = "100 rows"

		data.rr_list = [:]
		data.rr_list << [5: '5 seconds']
		data.rr_list << [10: '10 seconds']
		data.rr_list << [30: '30 seconds']
		data.rr_list << [60: '1 minute']

		settings.each { data[it.name] = it.settingValue }

		def v_scale_setting = Setting.findByName('vertical_scale')
		def h_scale_setting = Setting.findByName('horizontal_scale')
		def refresh_rate_setting = Setting.findByName('refresh_rate')
		def data_retention_setting = Setting.findByName('data_retention_policy')
		def reset_timeout_setting = Setting.findByName('reset_timeout')
		def login_timeout_setting = Setting.findByName('login_timeout')
		def default_records_per_page = Setting.findByName('default_records_per_page')

		data.v_list = v_list
		data.v_value = v_scale_setting?.settingValue ?: 0 // auto
		data.h_list = h_list
		data.h_value = h_scale_setting?.settingValue ?: 3600
		data.rr_value = refresh_rate_setting?.settingValue ?: 5
		data.rt_list = rt_list
		data.rt_value = reset_timeout_setting?.settingValue ?: 3600
		data.lt_list = lt_list
		data.lt_value = login_timeout_setting?.settingValue ?: 3600
		data.drp_list = drp_list
		data.drp_value = data_retention_setting?.settingValue ?: 31
		data.rpp_value = default_records_per_page?.settingValue ?: 20

//		def userDetails = springSecurityService.principal
//		if (userDetails) {
//			def user = User.read(userDetails.id)
//			data.username = user?.username ?: ''
//		}

		data
	} // end settings
	
//	@Secured(['IS_AUTHENTICATED_FULLY'])
//	def userprefs = { }
		
	@Secured(['IS_AUTHENTICATED_FULLY'])
	def updatepassword = {
		def userDetails = springSecurityService.principal
		def user = User.get(userDetails.id)
		def username = user.username
		def oldpassword = user.password
				
		// if the current password matches allow them to change it to something newer
		if( oldpassword == springSecurityService.encodePassword(params.oldpassword)){
			if( params.confirmpassword == '' || params.confirmpassword == null ){
				flash.message = "New Password and Confirm Password can't be empty or 'null'."
			}else{
				if( params.confirmpassword == params.newpassword){
					user.password = springSecurityService.encodePassword(params.confirmpassword)
					user.save(failOnError: true)
					def message = "User '$username' changed their password."
					def cl = new ChangeLog(timestamp:new Date(),user:username,message:message,host:request.remoteHost)
					cl.save(failOnError:true)
					flash.message = 'Password change was successful.'
				} else {
					flash.message = "New Password and Confirm Password do not match."
				}
			}
		}else{
				flash.message = "Current password was incorrect."
		}
		
		def hibSession = sessionFactory.getCurrentSession()
		hibSession.flush()
		hibSession.clear()
		redirect(action:"userprefs")
	}

	def quartzjobs = {
		String[] jobGroups;
		String[] jobsInGroup;
		int i, j
		String s, ss = "Quartz jobs<br><br>\n"
		jobGroups = quartzScheduler.getJobGroupNames();
		for (i = 0; i < jobGroups.length; i++) {
			s = jobGroups[i] + " group jobs:";
			println s
			ss += s + "<br>\n"
			jobsInGroup = quartzScheduler.getJobNames(jobGroups[i]);
			for (j = 0; j < jobsInGroup.length; j++) {
				s = " ${j+1} " + quartzScheduler.getJobDetail(jobsInGroup[j], jobGroups[i])
				println s
				ss += s + "<br>\n"
			}
		}
		render ss
	}

	// this is not actually used, see grails-app/conf/webdisplay/AjaxFilters.groovy
	private setLoginTimeout() {
		def login_timeout_setting = Setting.findByName('login_timeout')?.settingValue
		try {
			def login_timeout = login_timeout_setting.toInteger()
			if (session.getMaxInactiveInterval() != login_timeout) {
				session.setMaxInactiveInterval(login_timeout)
				println "set login timeout to $login_timeout secs"
			}
		} catch (Exception ex) {
			println "failed to set login timeout, current setting = $login_timeout_setting"
		}
	}
}
