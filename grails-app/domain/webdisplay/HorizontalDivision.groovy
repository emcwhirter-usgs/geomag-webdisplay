package webdisplay

class HorizontalDivision {
	
	String optionName
    Long   optionValue 

    static constraints = {
    }
    
    int compareTo(obj){
    	optionValue.compareTo(obj.optionValue)
    }
}
