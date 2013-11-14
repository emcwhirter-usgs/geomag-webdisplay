package webdisplay

class BinLog {

    Date   timestamp
    String station
    long   before_id    
    long   after_id    
        
    static constraints = {
    }
	
    static mapping = {
		before_id column: 'before_id', index: 'binlog_before_id_idx'
		after_id column: 'after_id', index: 'binlog_after_id_idx'
		epoch column: 'epoch', index: 'binlog_epoch_idx'
		timestamp column: 'timestamp', index: 'binlog_timestamp_idx'
    }
}
