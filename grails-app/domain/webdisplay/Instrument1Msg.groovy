package webdisplay

class Instrument1Msg {

	String station
	Float  h
	Float  d
	Float  z
	String ts
    Date   timestamp
    
    long epoch
    //int  milliseconds
    long one_min
    long five_min
    long ten_min
    long fifteen_min 
    long thirty_min   

    static constraints = {
        
    }
}
