package webdisplay

class DbService
{
	static transactional = true
	def sessionFactory

	def computeFAverage(station, epoch, avg_range_in_sec) {
		def session = sessionFactory.currentSession
		def epoch_from = epoch - avg_range_in_sec
		def query = """\
select avg(f) from instrument2msg where station = '${station}'
and epoch > ${epoch_from} and epoch <= ${epoch}"""
		def results = session.createSQLQuery(query).list()
		return results[0]
	}
}
