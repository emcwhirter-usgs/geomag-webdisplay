package webdisplay

/*
	GPS clock flags message
	Earthworm message type (in earthworm.d file):
	
	TYPE_SUPPTIMEPACKET  158
	format:
	
	YYYYMMDDhhmmss.sss OBS f1 f2 f3 f4 f5 f6 f7 f8 f9
	
	OBS = 3-char observatory code followed by 9 bit-flag chars:
	
	f1 = primary packet flags (255 = primary packet missing)
	f2 = secondary packet receiver mode flags (255 = secondary packet missing)
	f3 = secondary packet discipline mode flags (0 if f2 = 255)
	f4 = secondary packet self-survey flags (0 if f2 = 255)
	f5 = secondary packet holdover duration flags (0 if f2 = 255)
	f6 = secondary packet critical alarm flags (0 if f2 = 255)
	f7 = secondary packet minor alarm flags (0 if f2 = 255)
	f8 = secondary packet decode status flags (0 if f2 = 255)
	f9 = secondary packet discipline activity flags (0 if f2 = 255)
*/

class GPSFlagsMsg {

	String ts
	String station
	long  f1
	long  f2
	long  f3
	long  f4
	long  f5
	long  f6
	long  f7
	long  f8
	long  f9
    Date  timestamp
	
    long epoch
//    int  milliseconds
    long one_min
    long five_min
    long ten_min
    long fifteen_min
    long thirty_min

        
	static constraints = {
	}
}
