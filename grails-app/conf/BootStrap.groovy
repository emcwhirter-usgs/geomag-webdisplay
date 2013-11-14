import webdisplay.Station
import webdisplay.Role
import webdisplay.User
import webdisplay.UserRole
import webdisplay.Setting
import webdisplay.EWParam
import webdisplay.VerticalDivision
import webdisplay.HorizontalDivision
import webdisplay.DataRetentionPolicy
import webdisplay.ResetTimeout
import webdisplay.LoginTimeout

class BootStrap {

    def springSecurityService
//	def quartzScheduler

    def init = { servletContext ->

		def admin  = Role.findByAuthority('ROLE_ADMIN')?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
		def user   = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)

		def system = User.findByUsername('System') ?: new User(username:'System',password:springSecurityService.encodePassword('System'),enabled:true).save(failOnError: true)
	
	    // Ensure the System user has both ROLE_USER and ROLE_ADMIN
	        
		def systemrole1 = UserRole.get(system.id,admin.id) ?: new UserRole(user:system,role:admin).save(failOnError:true)
		def systemrole2 = UserRole.get(system.id,user.id)  ?: new UserRole(user:system,role:user).save(failOnError:true)
				
		def sta0  = Station.findByName('brw') ?: new Station(name:"brw",description:"Barrow").save(failOnError: true)
		def sta1  = Station.findByName('bou') ?: new Station(name:"bou",description:"Boulder").save(failOnError: true)
		def sta2  = Station.findByName('cmo') ?: new Station(name:"cmo",description:"College").save(failOnError: true)
		def sta3  = Station.findByName('ded') ?: new Station(name:"ded",description:"Deadhorse").save(failOnError: true)
		def sta4  = Station.findByName('frd') ?: new Station(name:"frd",description:"Fredericksburg").save(failOnError: true)
		def sta5  = Station.findByName('frn') ?: new Station(name:"frn",description:"Fresno").save(failOnError: true)
		def sta6  = Station.findByName('gua') ?: new Station(name:"gua",description:"Guam").save(failOnError: true)
		def sta7  = Station.findByName('hon') ?: new Station(name:"hon",description:"Honolulu").save(failOnError: true)
		def sta8  = Station.findByName('new') ?: new Station(name:"new",description:"Newport").save(failOnError: true)
		def sta9  = Station.findByName('sjg') ?: new Station(name:"sjg",description:"San Juan").save(failOnError: true)
		def sta10 = Station.findByName('shu') ?: new Station(name:"shu",description:"Shumagin").save(failOnError: true)
		def sta11 = Station.findByName('sit') ?: new Station(name:"sit",description:"Sitka").save(failOnError: true)
		def sta12 = Station.findByName('bsl') ?: new Station(name:"bsl",description:"Stennis").save(failOnError: true)
		def sta13 = Station.findByName('tuc') ?: new Station(name:"tuc",description:"Tucson").save(failOnError: true)
		
		// Application settings
		
		def s0 = Setting.findByName('local_station_id')      ?: new Setting(lastUpdate:new Date(), uiLabel:"Local Station ID",         name:"local_station_id").save(failOnError: true)
		def s1 = Setting.findByName('local_station_desc')    ?: new Setting(lastUpdate:new Date(), uiLabel:"Local Station Description",name:"local_station_desc").save(failOnError: true)
		def s2 = Setting.findByName('co_latitude')           ?: new Setting(lastUpdate:new Date(), uiLabel:"Co-Latitude",              name:"co_latitude" ).save(failOnError: true)
		def s3 = Setting.findByName('east_longitude')        ?: new Setting(lastUpdate:new Date(), uiLabel:"East Longitude",           name:"east_longitude").save(failOnError: true)
		def s4 = Setting.findByName('inclination')           ?: new Setting(lastUpdate:new Date(), uiLabel:"Inclination(min)",         name:"inclination" ).save(failOnError: true)
		def s5 = Setting.findByName('declination')           ?: new Setting(lastUpdate:new Date(), uiLabel:"Declination(min)",         name:"declination" ).save(failOnError: true)
//		def s6 = Setting.findByName('observer_declination')  ?: new Setting(lastUpdate:new Date(), uiLabel:"Observer Declination",     name:"observer_declination").save(failOnError: true)
		def s7 = Setting.findByName('vector_mag_sn')         ?: new Setting(lastUpdate:new Date(), uiLabel:"Vector Mag. S/N",          name:"vector_mag_sn").save(failOnError: true)
		def s8 = Setting.findByName('scalar_mag_sn')         ?: new Setting(lastUpdate:new Date(), uiLabel:"Scalar Mag. S/N",          name:"scalar_mag_sn").save(failOnError: true)
		// def s9 = Setting.findByName('quality_alarm')         ?: new Setting(lastUpdate:new Date(), uiLabel:"Quality Alarm",            name:"quality_alarm").save(failOnError: true)
		def s10 = Setting.findByName('vertical_scale')       ?: new Setting(lastUpdate:new Date(), uiLabel:"Vertical Scale",           name:"vertical_scale",   settingValue:"1").save(failOnError: true)
		def s11 = Setting.findByName('horizontal_scale')     ?: new Setting(lastUpdate:new Date(), uiLabel:"Horizontal Scale",         name:"horizontal_scale", settingValue:"1").save(failOnError: true)
		def s110= Setting.findByName('refresh_rate')         ?: new Setting(lastUpdate:new Date(), uiLabel:"Refresh Rate",             name:"refresh_rate",     settingValue:"5").save(failOnError: true)
		def s12 = Setting.findByName('login_timeout')        ?: new Setting(lastUpdate:new Date(), uiLabel:"Default Login Timeout",    name:"login_timeout",    settingValue:"3600").save(failOnError: true)
		def s13 = Setting.findByName('reset_timeout')        ?: new Setting(lastUpdate:new Date(), uiLabel:"Default Reset Timeout",    name:"reset_timeout",    settingValue:"7200").save(failOnError: true)
		def s14 = Setting.findByName('exporter_host')        ?: new Setting(lastUpdate:new Date(), uiLabel:"Exporter Host IP Address", name:"exporter_host",    settingValue:"127.0.0.1").save(failOnError: true)
		def s15 = Setting.findByName('exporter_port')        ?: new Setting(lastUpdate:new Date(), uiLabel:"Exporter Host TCP Port",   name:"exporter_port",    settingValue:"16004").save(failOnError: true)
		def s16 = Setting.findByName('data_retention_policy')?: new Setting(lastUpdate:new Date(), uiLabel:"Data Retention Policy",    name:"data_retention_policy", settingValue:"31").save(failOnError: true)
		def s17 = Setting.findByName('h_bin_minimum')?: new Setting(lastUpdate:new Date(), uiLabel:"H Bin Minimum", name:"h_bin_minimum", settingValue:"-256").save(failOnError: true)
		def s18 = Setting.findByName('h_bin_maximum')?: new Setting(lastUpdate:new Date(), uiLabel:"H Bin Maximum", name:"h_bin_maximum", settingValue:"256").save(failOnError: true)
		def s19 = Setting.findByName('z_bin_minimum')?: new Setting(lastUpdate:new Date(), uiLabel:"Z Bin Minimum", name:"z_bin_minimum", settingValue:"-256").save(failOnError: true)
		def s20 = Setting.findByName('z_bin_maximum')?: new Setting(lastUpdate:new Date(), uiLabel:"Z Bin Maximum", name:"z_bin_maximum", settingValue:"256").save(failOnError: true)
		def s21 = Setting.findByName('e_bin_minimum')?: new Setting(lastUpdate:new Date(), uiLabel:"E Bin Minimum", name:"e_bin_minimum", settingValue:"-256").save(failOnError: true)
		def s22 = Setting.findByName('e_bin_maximum')?: new Setting(lastUpdate:new Date(), uiLabel:"E Bin Maximum", name:"e_bin_maximum", settingValue:"256").save(failOnError: true)
		def s23 = Setting.findByName('h_z_e_voltage_tol')?: new Setting(lastUpdate:new Date(), uiLabel:"H,E,Z Voltage Tolerance Threshold", name:"h_z_e_voltage_tol", settingValue:"0.03").save(failOnError: true)
		def s24 = Setting.findByName('h_z_e_dead_value')?: new Setting(lastUpdate:new Date(), uiLabel:"H,E,Z Dead Values/Hour Threshold", name:"h_z_e_dead_value", settingValue:"100").save(failOnError: true)
		def s25 = Setting.findByName('f_spike_per_hour')?: new Setting(lastUpdate:new Date(), uiLabel:"F Spike/Hour Threshold", name:"f_spike_per_hour", settingValue:"").save(failOnError: true)
		def s251= Setting.findByName('f_spike_amplitude')?: new Setting(lastUpdate:new Date(), uiLabel:"F Spike Amplitude Threshold", name:"f_spike_amplitude", settingValue:"").save(failOnError: true)
		def s252= Setting.findByName('f_spike_resolution')?: new Setting(lastUpdate:new Date(), uiLabel:"F Spike Resolution (seconds)", name:"f_spike_resolution", settingValue:"30").save(failOnError: true)
		def s26 = Setting.findByName('delta_f_critical')?: new Setting(lastUpdate:new Date(), uiLabel:"&Delta;F Critical Threshold(nT)", name:"delta_f_critical", settingValue:"10000").save(failOnError: true)
		def s27 = Setting.findByName('delta_f_rate_change')?: new Setting(lastUpdate:new Date(), uiLabel:"&Delta;F Rate Change Critical Threshold(nt/Hour)", name:"delta_f_rate_change", settingValue:"").save(failOnError: true)
		def s281= Setting.findByName('electronics_min_critical')?: new Setting(lastUpdate:new Date(), uiLabel:"Electronics Temperatures Critical Minimum Threshold", name:"electronics_min_critical", settingValue:"").save(failOnError: true)
		def s282= Setting.findByName('electronics_min_warning')?: new Setting(lastUpdate:new Date(), uiLabel:"Electronics Temperatures Warning Minimum Threshold", name:"electronics_min_warning", settingValue:"").save(failOnError: true)
		def s283= Setting.findByName('electronics_max_warning')?: new Setting(lastUpdate:new Date(), uiLabel:"Electronics Temperatures Warning Maximum Threshold", name:"electronics_max_warning", settingValue:"").save(failOnError: true)
		def s294= Setting.findByName('electronics_max_critical')?: new Setting(lastUpdate:new Date(), uiLabel:"Electronics Temperatures Critical Maximum Threshold", name:"electronics_max_critical", settingValue:"").save(failOnError: true)
		def s301= Setting.findByName('proton_min_critical')?: new Setting(lastUpdate:new Date(), uiLabel:"Proton Temperatures Critical Minimum Threshold", name:"proton_min_critical", settingValue:"").save(failOnError: true)
		def s302= Setting.findByName('proton_min_warning')?: new Setting(lastUpdate:new Date(), uiLabel:"Proton Temperatures Warning Minimum Threshold", name:"proton_min_warning", settingValue:"").save(failOnError: true)
		def s303= Setting.findByName('proton_max_warning')?: new Setting(lastUpdate:new Date(), uiLabel:"Proton Temperatures Warning Maximum Threshold", name:"proton_max_warning", settingValue:"").save(failOnError: true)
		def s304= Setting.findByName('proton_max_critical')?: new Setting(lastUpdate:new Date(), uiLabel:"Proton Temperatures Critical Maximum Threshold", name:"proton_max_critical", settingValue:"").save(failOnError: true)
		def s321= Setting.findByName('fluxgate_min_critical')?: new Setting(lastUpdate:new Date(), uiLabel:"Fluxgate Temperatures Critical Minimum Threshold", name:"fluxgate_min_critical", settingValue:"").save(failOnError: true)
		def s322= Setting.findByName('fluxgate_min_warning')?: new Setting(lastUpdate:new Date(), uiLabel:"Fluxgate Temperatures Warning Minimum Threshold", name:"fluxgate_min_warning", settingValue:"").save(failOnError: true)
		def s323= Setting.findByName('fluxgate_max_warning')?: new Setting(lastUpdate:new Date(), uiLabel:"Fluxgate Temperatures Warning Maximum Threshold", name:"fluxgate_max_warning", settingValue:"").save(failOnError: true)
		def s324= Setting.findByName('fluxgate_max_critical')?: new Setting(lastUpdate:new Date(), uiLabel:"Fluxgate Temperatures Critical Maximum Threshold", name:"fluxgate_max_critical", settingValue:"").save(failOnError: true)
		def s34 = Setting.findByName('battery_critical')?: new Setting(lastUpdate:new Date(), uiLabel:"Battery Voltage Warning Threshold", name:"battery_critical", settingValue:"22").save(failOnError: true)
		def s35 = Setting.findByName('battery_warning')?: new Setting(lastUpdate:new Date(), uiLabel:"Battery Voltage Critical Threshold", name:"battery_warning", settingValue:"25").save(failOnError: true)
		def s36 = Setting.findByName('default_records_per_page')?: new Setting(lastUpdate:new Date(), uiLabel:"Default Records Per Page", name:"default_records_per_page", settingValue:"20").save(failOnError: true)
        def s37 = Setting.findByName('bin_h_const')?: new Setting(lastUpdate:new Date(), uiLabel:"Bin H Constant(nT/Bin)", name:"bin_h_const", settingValue:"500").save(failOnError: true)
		def s38 = Setting.findByName('bin_e_const')?: new Setting(lastUpdate:new Date(), uiLabel:"Bin E Constant(nT/Bin)", name:"bin_e_const", settingValue:"500").save(failOnError: true)
		def s39 = Setting.findByName('bin_z_const')?: new Setting(lastUpdate:new Date(), uiLabel:"Bin Z Constant(nT/Bin)", name:"bin_z_const", settingValue:"500").save(failOnError: true)
		def s40 = Setting.findByName('voltage_h_const')?: new Setting(lastUpdate:new Date(), uiLabel:"Voltage H Constant(nT/V)", name:"voltage_h_const", settingValue:"100").save(failOnError: true)
		def s41 = Setting.findByName('voltage_e_const')?: new Setting(lastUpdate:new Date(), uiLabel:"Voltage E Constant(nT/V)", name:"voltage_e_const", settingValue:"100").save(failOnError: true)
		def s42 = Setting.findByName('voltage_z_const')?: new Setting(lastUpdate:new Date(), uiLabel:"Voltage Z Constant(nT/V)", name:"voltage_z_const", settingValue:"100").save(failOnError: true)
		def s43 = Setting.findByName('heartbeat_interval')?: new Setting(lastUpdate:new Date(), uiLabel:"Upstream Exporter Heartbeat Interval", name:"heartbeat_interval", settingValue:"15").save(failOnError: true)
		def s44 = Setting.findByName('scalar_data_check')?: new Setting(lastUpdate:new Date(), uiLabel:"Scalar Data Stream Check Window", name:"scalar_data_check", settingValue:"120").save(failOnError: true)
		def s45 = Setting.findByName('vector_data_check')?: new Setting(lastUpdate:new Date(), uiLabel:"VectorData Stream Check Window", name:"vector_data_check", settingValue:"120").save(failOnError: true)
		def s46 = Setting.findByName('hez_rate_change')?: new Setting(lastUpdate:new Date(), uiLabel:"H,E,Z Rate Change Critical Threshold(nt/Hour)", name:"hez_rate_change", settingValue:"20").save(failOnError: true)

		def olds = Setting.findByName('electronics_critical'); if (olds) olds.delete()
		olds = Setting.findByName('electronics_warning'); if (olds) olds.delete()
		olds = Setting.findByName('proton_critical'); if (olds) olds.delete()
		olds = Setting.findByName('proton_warning'); if (olds) olds.delete()
		olds = Setting.findByName('fluxgate_critical'); if (olds) olds.delete()
		olds = Setting.findByName('fluxgate_warning'); if (olds) olds.delete()
		olds = Setting.findByName('observer_declination'); if (olds) olds.delete()

		// EWParams	
		
		def ep0 = EWParam.findByName('INST_WILDCARD') ?: new EWParam(lastUpdate:new Date(), uiLabel:"INST_WILDCARD", name:"INST_WILDCARD", settingValue:0, defaultValue:0).save(failOnError: true)
		def ep1 = EWParam.findByName('INST_USGSMAG') ?: new EWParam(lastUpdate:new Date(), uiLabel:"INST_USGSMAG", name:"INST_USGSMAG", settingValue:59, defaultValue:59).save(failOnError: true)
		def ep2 = EWParam.findByName('INST_UNKNOWN') ?: new EWParam(lastUpdate:new Date(), uiLabel:"INST_UNKNOWN", name:"INST_UNKNOWN", settingValue:255, defaultValue:255).save(failOnError: true)
		def ep3 = EWParam.findByName('MOD_STARTSTOP') ?: new EWParam(lastUpdate:new Date(), uiLabel:"MOD_STARTSTOP", name:"MOD_STARTSTOP", settingValue:18, defaultValue:18).save(failOnError: true)
		def ep4 = EWParam.findByName('MOD_EXPORT_GENERIC') ?: new EWParam(lastUpdate:new Date(), uiLabel:"MOD_EXPORT_GENERIC", name:"MOD_EXPORT_GENERIC", settingValue:29, defaultValue:29).save(failOnError: true)
		def ep5 = EWParam.findByName('MOD_PCDCP') ?: new EWParam(lastUpdate:new Date(), uiLabel:"MOD_PCDCP", name:"MOD_PCDCP", settingValue:50, defaultValue:50).save(failOnError: true)
		def ep6 = EWParam.findByName('TYPE_ERROR') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_ERROR", name:"TYPE_ERROR", settingValue:2, defaultValue:2).save(failOnError: true)
		def ep7 = EWParam.findByName('TYPE_HEARTBEAT') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_HEARTBEAT", name:"TYPE_HEARTBEAT", settingValue:3, defaultValue:3).save(failOnError: true)
		def ep8 = EWParam.findByName('TYPE_TRACEBUF') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_TRACEBUF", name:"TYPE_TRACEBUF", settingValue:20, defaultValue:20).save(failOnError: true)
		def ep9 = EWParam.findByName('TYPE_KILL') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_KILL", name:"TYPE_KILL", settingValue:105, defaultValue:105).save(failOnError: true)
		def ep10 = EWParam.findByName('TYPE_RESTART') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_RESTART", name:"TYPE_RESTART", settingValue:107, defaultValue:107).save(failOnError: true)
		def ep11 = EWParam.findByName('TYPE_REQSTATUS') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_REQSTATUS", name:"TYPE_REQSTATUS", settingValue:108, defaultValue:108).save(failOnError: true)
		def ep12 = EWParam.findByName('TYPE_STATUS') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_STATUS", name:"TYPE_STATUS", settingValue:109, defaultValue:109).save(failOnError: true)
		def ep13 = EWParam.findByName('TYPE_DATAMAGFIELD') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_DATAMAGFIELD", name:"TYPE_DATAMAGFIELD", settingValue:150, defaultValue:150).save(failOnError: true)
		def ep14 = EWParam.findByName('TYPE_DATADIDD') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_DATADIDD", name:"TYPE_DATADIDD", settingValue:151, defaultValue:151).save(failOnError: true)
		def ep15 = EWParam.findByName('TYPE_MAGDIAG') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_MAGDIAG", name:"TYPE_MAGDIAG", settingValue:152, defaultValue:152).save(failOnError: true)
		def ep16 = EWParam.findByName('TYPE_DIAG3V3B') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_DIAG3V3B", name:"TYPE_DIAG3V3B", settingValue:153, defaultValue:153).save(failOnError: true)
		def ep17 = EWParam.findByName('TYPE_MAGRESEND') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_MAGRESEND", name:"TYPE_MAGRESEND", settingValue:154, defaultValue:154).save(failOnError: true)
		def ep18 = EWParam.findByName('TYPE_SECDATAMAGFIELD') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_SECDATAMAGFIELD", name:"TYPE_SECDATAMAGFIELD", settingValue:156, defaultValue:156).save(failOnError: true)
		def ep19 = EWParam.findByName('TYPE_TEXT') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_TEXT", name:"TYPE_TEXT", settingValue:157, defaultValue:157).save(failOnError: true)
		def ep20 = EWParam.findByName('TYPE_SUPPTIMEPACKET') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_SUPPTIMEPACKET", name:"TYPE_SUPPTIMEPACKET", settingValue:158, defaultValue:158).save(failOnError: true)
		def ep21 = EWParam.findByName('TYPE_GOESLASTTX') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_GOESLASTTX", name:"TYPE_GOESLASTTX", settingValue:159, defaultValue:159).save(failOnError: true)
		def ep22 = EWParam.findByName('TYPE_RTIERROR') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_RTIERROR", name:"TYPE_RTIERROR", settingValue:160, defaultValue:160).save(failOnError: true)
		def ep23 = EWParam.findByName('TYPE_COMMAND') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_COMMAND", name:"TYPE_COMMAND", settingValue:161, defaultValue:161).save(failOnError: true)
		def ep24 = EWParam.findByName('TYPE_ACTION') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_ACTION", name:"TYPE_ACTION", settingValue:162, defaultValue:162).save(failOnError: true)
		def ep25 = EWParam.findByName('TYPE_MAGREQPARMS') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_MAGREQPARMS", name:"TYPE_MAGREQPARMS", settingValue:170, defaultValue:170).save(failOnError: true)
		def ep26 = EWParam.findByName('TYPE_MAGPARMS') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_MAGPARMS", name:"TYPE_MAGPARMS", settingValue:171, defaultValue:171).save(failOnError: true)
		def ep27 = EWParam.findByName('TYPE_MAGSETPARMS') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_MAGSETPARMS", name:"TYPE_MAGSETPARMS", settingValue:172, defaultValue:172).save(failOnError: true)
		def ep28 = EWParam.findByName('TYPE_MAGOBSERV') ?: new EWParam(lastUpdate:new Date(), uiLabel:"TYPE_MAGOBSERV", name:"TYPE_MAGOBSERV", settingValue:180, defaultValue:180).save(failOnError: true)
		
        def v0 = VerticalDivision.findByOptionName('5000 nT/Div') ?: new VerticalDivision(optionName:'5000 nT/Div', optionValue:5000).save(failOnError:true)
        def v1 = VerticalDivision.findByOptionName('2000 nT/Div') ?: new VerticalDivision(optionName:'2000 nT/Div', optionValue:2000).save(failOnError:true)
        def v2 = VerticalDivision.findByOptionName('1000 nT/Div') ?: new VerticalDivision(optionName:'1000 nT/Div', optionValue:1000).save(failOnError:true)
        def v3 = VerticalDivision.findByOptionName('500 nT/Div') ?: new VerticalDivision(optionName:'500 nT/Div', optionValue:500).save(failOnError:true)
        def v4 = VerticalDivision.findByOptionName('200 nT/Div') ?: new VerticalDivision(optionName:'200 nT/Div', optionValue:200).save(failOnError:true)
        def v5 = VerticalDivision.findByOptionName('100 nT/Div') ?: new VerticalDivision(optionName:'100 nT/Div', optionValue:100).save(failOnError:true)
        def v6 = VerticalDivision.findByOptionName('50 nT/Div') ?: new VerticalDivision(optionName:'50 nT/Div', optionValue:50).save(failOnError:true)
        def v7 = VerticalDivision.findByOptionName('20 nT/Div') ?: new VerticalDivision(optionName:'20 nT/Div', optionValue:20).save(failOnError:true)
        def v8 = VerticalDivision.findByOptionName('10 nT/Div') ?: new VerticalDivision(optionName:'10 nT/Div', optionValue:10).save(failOnError:true)
        def v9 = VerticalDivision.findByOptionName('5 nT/Div') ?: new VerticalDivision(optionName:'5 nT/Div', optionValue:5).save(failOnError:true)
        def v10 = VerticalDivision.findByOptionName('2 nT/Div') ?: new VerticalDivision(optionName:'2 nT/Div', optionValue:2).save(failOnError:true)
        def v11 = VerticalDivision.findByOptionName('1 nT/Div') ?: new VerticalDivision(optionName:'1 nT/Div', optionValue:1).save(failOnError:true)
		def vx = VerticalDivision.findByOptionName('auto') ?: new VerticalDivision(optionName:'auto', optionValue:0).save(failOnError:true)

		def oldhd = HorizontalDivision.findByOptionName('1 week/168 hours');
		if (oldhd) {
			oldhd.delete()
			HorizontalDivision.findByOptionName('2 days/48 hours')?.delete();
			HorizontalDivision.findByOptionName('1 day/24 hours')?.delete();
		}
		[604800: '1 week',
		 172800: '2 days',
		  86400: '1 day',
		  43200: '12 hours',
		  21600: '6 hours',
		   7200: '2 hours',
		   3600: '1 hour',
		   1800: '30 minutes',
		    600: '10 minutes',
		    300: '5 minutes',
		    120: '2 minutes',
		     60: '1 minute',
		     30: '30 seconds'].each{ k,v ->
			def hd = HorizontalDivision.findByOptionName(v);
			if (hd && hd.optionValue == k) return;
			if (!hd) hd = new HorizontalDivision(optionName:v, optionValue:k)
			else hd.optionValue = k
			hd.save(failOnError:true)
		}
/*		def h0 = HorizontalDivision.findByOptionName('1 week') ?: new HorizontalDivision(optionName:'1 week', optionValue:604800).save(failOnError:true)
        def h1 = HorizontalDivision.findByOptionName('2 days') ?: new HorizontalDivision(optionName:'2 days', optionValue:172800).save(failOnError:true)
        def h2 = HorizontalDivision.findByOptionName('1 day') ?: new HorizontalDivision(optionName:'1 day', optionValue:86400).save(failOnError:true)
        def h3 = HorizontalDivision.findByOptionName('12 hours') ?: new HorizontalDivision(optionName:'12 hours', optionValue:43200).save(failOnError:true)
        def h4 = HorizontalDivision.findByOptionName('6 hours') ?: new HorizontalDivision(optionName:'6 hours', optionValue:21600).save(failOnError:true)
		def h5 = HorizontalDivision.findByOptionName('2 hours') ?: new HorizontalDivision(optionName:'2 hours', optionValue:7200).save(failOnError:true)
		def h6 = HorizontalDivision.findByOptionName('1 hour') ?: new HorizontalDivision(optionName:'1 hour', optionValue:3600).save(failOnError:true)
*/
        def drp0 = DataRetentionPolicy.findByOptionName('365 days') ?: new DataRetentionPolicy(optionName:'365 days', optionValue:365).save(failOnError:true)
        def drp1 = DataRetentionPolicy.findByOptionName('182 days') ?: new DataRetentionPolicy(optionName:'182 days', optionValue:182).save(failOnError:true)
        def drp2 = DataRetentionPolicy.findByOptionName('91 days') ?: new DataRetentionPolicy(optionName:'91 days', optionValue:91).save(failOnError:true)
        def drp3 = DataRetentionPolicy.findByOptionName('62 days') ?: new DataRetentionPolicy(optionName:'62 days', optionValue:62).save(failOnError:true)
        def drp4 = DataRetentionPolicy.findByOptionName('31 days') ?: new DataRetentionPolicy(optionName:'31 days', optionValue:31).save(failOnError:true)
        def drp5 = DataRetentionPolicy.findByOptionName('7 days') ?: new DataRetentionPolicy(optionName:'7 days', optionValue:7).save(failOnError:true)

		def rt1m= ResetTimeout.findByOptionName('30 minutes') ?: new ResetTimeout(optionName:'30 minutes', optionValue:1800).save(failOnError:true)
		def rt0 = ResetTimeout.findByOptionName('1 hour') ?: new ResetTimeout(optionName:'1 hour', optionValue:3600).save(failOnError:true)
        def rt1 = ResetTimeout.findByOptionName('2 hours') ?: new ResetTimeout(optionName:'2 hours', optionValue:7200).save(failOnError:true)
        def rt2 = ResetTimeout.findByOptionName('4 hours') ?: new ResetTimeout(optionName:'4 hours', optionValue:14400).save(failOnError:true)
        def rt3 = ResetTimeout.findByOptionName('6 hours') ?: new ResetTimeout(optionName:'6 hours', optionValue:21600).save(failOnError:true)
        def rt4 = ResetTimeout.findByOptionName('12 hours') ?: new ResetTimeout(optionName:'12 hours', optionValue:43200).save(failOnError:true)
        def rt5 = ResetTimeout.findByOptionName('24 hours') ?: new ResetTimeout(optionName:'24 hours', optionValue:86400).save(failOnError:true)
        def rt6 = ResetTimeout.findByOptionName('48 hours') ?: new ResetTimeout(optionName:'48 hours', optionValue:172800).save(failOnError:true)
                
        def lt0 = LoginTimeout.findByOptionName('15 minutes') ?: new LoginTimeout(optionName:'15 minutes', optionValue:900).save(failOnError:true)
        def lt1 = LoginTimeout.findByOptionName('30 minutes') ?: new LoginTimeout(optionName:'30 minutes', optionValue:1800).save(failOnError:true)
        def lt2 = LoginTimeout.findByOptionName('1 hour') ?: new LoginTimeout(optionName:'1 hour', optionValue:3600).save(failOnError:true)
        def lt3 = LoginTimeout.findByOptionName('2 hours') ?: new LoginTimeout(optionName:'2 hours', optionValue:7200).save(failOnError:true)
        def lt4 = LoginTimeout.findByOptionName('4 hours') ?: new LoginTimeout(optionName:'4 hours', optionValue:14400).save(failOnError:true)
        def lt5 = LoginTimeout.findByOptionName('8 hours') ?: new LoginTimeout(optionName:'8 hours', optionValue:28800).save(failOnError:true)


/*
		println "Quartz jobs"
		String[] jobGroups;
		String[] jobsInGroup;
		int i, j
		String s, ss = "Quartz jobs<br>\n"
		jobGroups = quartzScheduler.getJobGroupNames();
		for (i = 0; i < jobGroups.length; i++) {
			s = "Group: " + jobGroups[i] + " contains the following jobs";
			println s
			ss += s + "<br>\n"
			jobsInGroup = quartzScheduler.getJobNames(jobGroups[i]);
			for (j = 0; j < jobsInGroup.length; j++) {
				s = " ${j+1} " + quartzScheduler.getJobDetail(jobsInGroup[j], jobGroups[i])
				println s
				ss += s + "<br>\n"
			}
		}
*/

		println "webdisplay online"
    }
    def destroy = {
		println "webdisplay destroyed"
    }
}
