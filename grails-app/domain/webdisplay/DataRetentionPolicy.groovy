package webdisplay

class DataRetentionPolicy {

    String optionName
    Long   optionValue

    static constraints = {
    }
    
    int compareTo(obj){
        optionValue.compareTo(obj.optionValue)
    }

}
