package webdisplay

class Instrument2Msg {

	String station
	Float  f
	Float  f_avg600
	String ts
	Date   timestamp

    long epoch
//  int  milliseconds
    long one_min
    long five_min
    long ten_min
    long fifteen_min
    long thirty_min

    
    static constraints = {
		f_avg600(nullable: true)
    }
    
}
