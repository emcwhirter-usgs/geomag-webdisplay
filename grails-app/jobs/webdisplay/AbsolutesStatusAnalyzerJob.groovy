package webdisplay

import webdisplay.Instrument1Msg
import webdisplay.Instrument2Msg
import webdisplay.RawFluxGateMsg
import webdisplay.MagDiagMsg
import webdisplay.GPSFlagsMsg
import webdisplay.GOESLastTxMsg
import webdisplay.BinLog

class AbsolutesStatusAnalyzerJob {

     def dataSource
     def concurrent = false
     static name = "AnalyzeAbsolutesStatus"

    //def timeout = 5000l // execute job once in 5 seconds
    
    static triggers = {
        //simple name:name, startDelay:5000, repeatInterval: 60000, repeatCount: -1
     }
        
    def execute() {
		log.info("Running task $name\n")

		try{ 
					def data_retention_setting = Setting.findByName('data_retention_policy')
            
            		// default to 31 days if no setting found
            		def days = data_retention_setting?.settingValue ?: 31 ;
            		Date endpoint = new Date() - Integer.parseInt(days)  
            
            		//log.info("Analyzing values to deterime Absolutes Status.")
					
		}catch(Exception e){
			log.info(e)    
		}//end catch

    } //end task
}//end 
