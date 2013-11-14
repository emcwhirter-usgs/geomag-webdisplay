package webdisplay

class Setting {
	
	String name
    String uiLabel
    String uiDescription
	int    uiOrder
	int	   uiSection
	String settingValue 
    String defaultValue
    	
    Date   dateCreated
    Date   lastUpdate
    
    static constraints = {
		uiDescription(nullable:true)
		uiOrder(nullable:true)
		uiSection(nullable:true)
		settingValue(nullable:true)
		defaultValue(nullable:true)
    }
    
    int compareTo(obj){
    	uiOrder.compareTo(obj.uiOrder)
    }
}
