package webdisplay

import org.springframework.beans.factory.InitializingBean;

class ConnectionSettingChangedService implements InitializingBean
{
	static transactional = false

	Boolean changed = false
	Boolean stop = false

	void afterPropertiesSet() {
		changed = false
		stop = false
	}

	def getChanged() {
		return changed
	}
	def setChanged(Boolean state) {
		synchronized(this) {
			changed = state;
		}
	}

	def shouldStop() {
		return stop
	}
	def setStop(Boolean state) {
		synchronized(this) {
			stop = state
		}
	}
}
