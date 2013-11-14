package webdisplay

import webdisplay.BinLog
import java.text.SimpleDateFormat
import java.util.Date;
import groovy.sql.GroovyResultSetProxy
import groovy.sql.Sql
import java.sql.Connection
import java.sql.Statement
import org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin

class FluxgateBinLogAnalyzerJob {

    def dataSource
    def sessionFactory
    def concurrent = false
    static name = "FluxgateAnalyzerBinLog"
        
    static triggers = { simple name:name, startDelay:1000, repeatInterval: 1 * 10 * 1000l, repeatCount: -1 }
        
    def execute() {
//        log.info("Running task $name")

        try{
	            def hibSession = sessionFactory.getCurrentSession()

			// H, E, Z Bin log
	            def station_setting = Setting.findByName('local_station_id')
	            def station = station_setting.settingValue
				
			if (station != null){
				def seconds = 10
	            def cal = Calendar.instance
	            def start_epoch = ( cal.time.time / 1000 as long ) - ( seconds ) 
	            def start_date = new Date( start_epoch * 1000 )
				def end_epoch = ( cal.time.time / 1000 as long )
				def end_date = new Date( end_epoch * 1000 )
			    //log.info("Analyzing Fluxgate records for the last $seconds seconds: $start_date - $end_date")
				def params = [start_epoch, end_epoch, station ]
				//log.info("params $params")
				def sql = new Sql(dataSource)
				sql.execute 'call bin_change_detector(?, ?, ?)', params
			}else{
				log.info("Station id is empty or null can't analyze bins.")
			}
			//log.info("Flushing and clearing")
            DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP.get().clear()
            hibSession.flush()
            hibSession.clear()

        } catch(Exception e){
            log.info(e)    
        }
		
    } //end task
}//end 
