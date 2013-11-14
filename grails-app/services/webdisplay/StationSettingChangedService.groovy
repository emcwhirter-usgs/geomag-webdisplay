package webdisplay

import org.springframework.beans.factory.InitializingBean

class StationSettingChangedService implements InitializingBean {

    static transactional = true

    Boolean changed;
	
	void afterPropertiesSet() {
		changed = false;
	}
	
	def getChanged(){
		return changed;
	}
	
	def setChanged(Boolean state){  
		synchronized(this){ 
			changed = state;
		}
	}
	
}
