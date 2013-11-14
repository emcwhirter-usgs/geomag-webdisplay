package webdisplay

class EWParam {
	
    String name
    String uiLabel
    String uiDescription
    int    uiOrder
    int	   uiSection
    int	   settingValue 
    int    defaultValue
    	
    Date   dateCreated
    Date   lastUpdate
    
    static constraints = {
		uiDescription(nullable:true)
		uiOrder(nullable:true)
		uiSection(nullable:true)
    }
    
    int compareTo(obj){
    	uiOrder.compareTo(obj.uiOrder)
    }
}
