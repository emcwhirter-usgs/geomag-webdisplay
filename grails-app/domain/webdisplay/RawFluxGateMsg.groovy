package webdisplay

/*
 	Voltage - Bin message (raw fluxgate voltages and bin numbers)
	Earthworm message type (in earthworm.d file):

	TYPE_DIAG3V3B  153

	format:

	YYYYMMDDhhmmss.sss OBS h.hhh e.eee z.zzz bh be bz
	
	OBS = 3-char observatory code followed by 3 voltages and 3 integer bin numbers:

	h.hhh = H voltage
	e.eee = E voltage
	z.zzz = Z voltage

	bh = H bin
	be = E bin
	bz = Z bin 
	
 */


class RawFluxGateMsg {

		String ts
		String station
		Float h	
		Float e	
		Float z	
		int   bh
		int   be
		int   bz
    	Date  timestamp
	
        long epoch
//      int  milliseconds
        long one_min
        long five_min
        long ten_min
        long fifteen_min
        long thirty_min
         
        
	static constraints = {
	}
}
