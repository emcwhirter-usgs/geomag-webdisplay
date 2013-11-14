package webdisplay

/*
	GOES satellite last transmit status message
	Earthworm message type (in earthworm.d file):
	
	TYPE_GOESLASTTX    159
	
	format:
	
		OBS text
	
	OBS = 3-char observatory code followed by a block of text from the Microcom transmitter (max 150 chars), e.g. text =
	
	TxType: Timed
	TxStatus: OK
	TxTime: 00:07:20.67
	FwdPwr: 10.7 dBW
	RevPwr: -16.1 dBW
	TxVSWR: 1.10:1
	TxVolts: 11.6 VDC
	>
	
*/

class GOESLastTxMsg {

	String station
	String text
    Date   timestamp
	
    long epoch
 //   int  milliseconds
    long one_min
    long five_min
    long ten_min
    long fifteen_min
    long thirty_min

        
	static constraints = {
	}
}
