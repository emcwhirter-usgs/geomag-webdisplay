package webdisplay

import java.util.Date;

class AbsolutesStatus {

	Date    timestamp
	long    epoch
	String  ts
	String  station
	Boolean status
		
	static constraints = {
	}
	
	static mapping = { 
		epoch column: 'epoch', index: 'abs_epoch_idx'
		timestamp column: 'timestamp', index: 'abs_timestamp_idx'
	}
}
