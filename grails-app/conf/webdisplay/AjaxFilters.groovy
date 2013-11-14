package webdisplay

/**
 * Prevent Ajax calls from chart pages from resetting the session timeout
 * so that a user that is logged will actually get his session to timeout.
 *
 * Got the idea from http://stackoverflow.com/questions/4834270/how-do-i-execute-an-authenticated-ajax-request-without-resetting-the-tomcats-se
*/
class AjaxFilters
{
	private static final String TIMEOUT_KEY = 'TIMEOUT_KEY'

	def filters = {
		all(controller:'main', action:'*') {
			before = {
				try {
					def now = System.currentTimeMillis()
					def login_timeout_setting = Setting.findByName('login_timeout')?.settingValue
					def timeout = login_timeout_setting.toInteger() * 1000
//					def lat = session.getLastAccessedTime()
//					println "to = $timeout, now = $now, lat = $lat, last_seen = $last_seen"

					if (request.xhr) { // Ajax
						// do not update session timeout
						def last_seen = session.getAttribute(TIMEOUT_KEY) ?: now
						if (now - last_seen > timeout) {
							println "login timed out"
							session.invalidate()
						}
					}
					else { // not Ajax, so the user is well alive and kicking
						// set last seen time to now, effectively resetting login timeout
						session.setAttribute(TIMEOUT_KEY, now)
//						println "reset session timeout"
					}
				} catch (Exception ex) {
					println "ERROR in AjaxFilters: $ex"
				}
				true
			}
		}
	}
}