package webdisplay

import webdisplay.Instrument1Msg
import webdisplay.Instrument2Msg
import webdisplay.RawFluxGateMsg
import webdisplay.MagDiagMsg
import webdisplay.GPSFlagsMsg
import webdisplay.GOESLastTxMsg
import webdisplay.BinLog
import webdisplay.ConnectionStatus
import webdisplay.AbsolutesStatus

class MagWormDBPreenJob {

     def dataSource
     def concurrent = false
    //def timeout = 5000l // execute job once in 5 seconds
     static name = "databasePreen"
	 
	 
    static triggers = {
		// run ever 12 hours (3600 * 12 * 1000)
        simple name:name, startDelay:900000, repeatInterval: 4320000, repeatCount: -1
     }
        
    def execute() {
		log.info("Running task $name\n")

		try{ 
            
            def data_retention_setting = Setting.findByName('data_retention_policy')
            
            // default to 31 days if no setting found
            def days = data_retention_setting?.settingValue ?: 31 ;
            Date endpoint = new Date() - Integer.parseInt(days)  
            
            log.info("Data Rention Policy is set to: $days days (deleting every thing older than $endpoint)")
        
            Instrument1Msg.executeUpdate("delete Instrument1Msg i where i.timestamp < ?", [endpoint])
            Instrument2Msg.executeUpdate("delete Instrument2Msg i where i.timestamp < ?", [endpoint])
            RawFluxGateMsg.executeUpdate("delete RawFluxGateMsg r where r.timestamp < ? ", [endpoint])
            MagDiagMsg.executeUpdate("delete MagDiagMsg m where m.timestamp < ?", [endpoint])
            GPSFlagsMsg.executeUpdate("delete GPSFlagsMsg g where g.timestamp < ?", [endpoint])
            GOESLastTxMsg.executeUpdate("delete GOESLastTxMsg g where g.timestamp < ?", [endpoint])
           	BinLog.executeUpdate("delete BinLog bl where bl.timestamp < ?", [endpoint])
			ConnectionStatus.executeUpdate("delete ConnectionStatus cs where cs.timestamp < ?", [endpoint])
			AbsolutesStatus.executeUpdate("delete AbsolutesStatus a where a.timestamp < ?", [endpoint])
			 
		}catch(Exception e){
			log.info(e)    
		}//end catch

    } //end task
}//end 
