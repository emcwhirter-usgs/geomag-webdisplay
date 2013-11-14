package webdisplay

import org.springframework.beans.factory.InitializingBean

class ConnectionStatusService implements InitializingBean
{
	static transactional = false

	Boolean connected;

	void afterPropertiesSet() {
		connected = false;
	}

	def getState() {
		return connected;
	}

	def setState(Boolean state) {
		synchronized(this) {
			connected = state;
		}
	}
}
