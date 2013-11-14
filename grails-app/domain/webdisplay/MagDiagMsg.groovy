package webdisplay

/*
	Diagnostics message (temperatures and voltages)
	Earthworm message type (in earthworm.d file):
	
	TYPE_MAGDIAG  152
	
	format:
	
	YYYYMMDDhhmmss.sss OBS id1 val1 id2 val2 id3 val3 id4 val4 id5 val5 id6 val6
	
	OBS = 3-char observatory code
	id1 val1, id2 val2, etc. are pairs of id codes and floating-point values where:
	
	id = 1, val = Electronics temperature
	id = 2, val = Fluxgate temperature
	id = 3, val = Proton temperature
	id = 4, val = Outside temperature
	id = 5, val = Battery voltage
	id = 6, val = Auxilliary voltage
*/

class MagDiagMsg {

    String station
    String ts
    Float  electronics_temp
    Float  fluxgate_temp
    Float  total_field_temp
    Float  outside_temp
    Float  battery_voltage
    Float  auxilliary_voltage
    Date   timestamp
	
    
    long epoch
    int  milliseconds
    long one_min
    long five_min
    long ten_min
    long fifteen_min
    long thirty_min

    
    static constraints = {
    }
}
