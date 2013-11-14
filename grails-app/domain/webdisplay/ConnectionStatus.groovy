package webdisplay

import java.util.Date;

class ConnectionStatus {

	Date    timestamp
	long    epoch
	String  ts
	String  station
	Boolean status
		
	static constraints = {
	}
	
	static mapping = {
		epoch column: 'epoch', index: 'conn_epoch_idx'
		timestamp column: 'timestamp', index: 'conn_timestamp_idx'
	}
}
