package webdisplay

class Section {

	String name
    String uiLabel
    String uiDescription
    Date   dateCreated
    Date   lastUpdate
    
    SortedSet settings
    static hasMany = [settings:Setting]

    static constraints = {
    }
}
