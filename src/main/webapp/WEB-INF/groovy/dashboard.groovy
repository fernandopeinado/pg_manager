import groovy.json.JsonBuilder

import org.springframework.web.context.ContextLoader

import br.com.cas10.pgman.agent.CpuAgent
import br.com.cas10.pgman.agent.DatabaseStatsAgent
import br.com.cas10.pgman.agent.MemoryAgent
import br.com.cas10.pgman.service.PGManagerDAO
import br.com.cas10.pgman.service.PostgresqlService

/*
request - the HttpServletRequest
response - the HttpServletResponse
application - the ServletContext associated with the servlet
session - the HttpSession associated with the HttpServletRequest
out - the PrintWriter associated with the ServletReques
*/

response.contentType = 'application/json'

Object getCPUData() {
	CpuAgent agent = ContextLoader.currentWebApplicationContext.getBean(CpuAgent.class)
	def matrixData = [[ 'timestamp', 'User', 'System', 'Wait', 'Steal' ]];
	agent.data?.each { snap ->
		if (snap.deltaObs.user_pc != null) {
			matrixData << [ snap.dateTime, snap.deltaObs.user_pc, snap.deltaObs.system_pc, snap.deltaObs.wait_pc, snap.deltaObs.steal_pc ]
		}
	}
	return matrixData;
}

Object getMemoryData() {
	MemoryAgent agent = ContextLoader.currentWebApplicationContext.getBean(MemoryAgent.class)
	def totalMemory = agent.data[0].observations.total / 1024
	def matrixData = [[ 'timestamp', 'Used', 'Buffer', 'Cache' ]];
	agent.data?.each { snap ->
		if (snap.deltaObs.used != null) {
			matrixData << [snap.dateTime, snap.observations.used / 1024, snap.observations.buffer / 1024, snap.observations.cache / 1024]
		}
	}
	return ['matrixData':matrixData, 'totalMemory':totalMemory]
}

List statRows(cols, snapshots, propName) {
	def rows = []
	rows.add(cols)
	snapshots.each { snap ->
		def row = [snap.dateTime]
		snap.deltaObs.each { delta ->  row << delta.value[propName] }
		rows << row
	}
	return rows
}

Map getDatabaseStatsData() {
	DatabaseStatsAgent agent = ContextLoader.currentWebApplicationContext.getBean(DatabaseStatsAgent.class)
	def ret = [:]
	def cols = ['timestamp']
	agent.data[0]?.observations.each { obs -> 
		cols << obs.key
	}
	ret['graphCommit'] = ['title':'Commits / sec', 'legend':true, 'matrixData':statRows(cols, agent.data, 'commits_ps')] 
	ret['graphRollback'] = ['title':'Rollbacks / sec', 'legend':false, 'matrixData':statRows(cols, agent.data, 'rollback_ps')]
	ret['graphBlksread'] = ['title':'Blocks Read / sec', 'legend':false, 'matrixData':statRows(cols, agent.data, 'blks_read_ps')]
	ret['graphBlkshit'] = ['title':'Blocks Hit / sec', 'legend':false, 'matrixData':statRows(cols, agent.data, 'blks_hit_ps')]
	ret['graphTupreturned'] = ['title':'Tuples Returned / sec', 'legend':false, 'matrixData':statRows(cols, agent.data, 'tup_returned_ps')]
	ret['graphTupfetched'] = ['title':'Tuples Fetched / sec', 'legend':false, 'matrixData':statRows(cols, agent.data, 'tup_fetched_ps')]
	ret['graphTupinserted'] = ['title':'Tuples Inserted / sec', 'legend':false, 'matrixData':statRows(cols, agent.data, 'tup_inserted_ps')]
	ret['graphTupupdated'] = ['title':'Tuples Updated / sec', 'legend':false, 'matrixData':statRows(cols, agent.data, 'tup_updated_ps')]
	ret['graphTupdeleted'] = ['title':'Tuples Deleted / sec', 'legend':false, 'matrixData':statRows(cols, agent.data, 'tup_deleted_ps')]
	return ret
}

Map getDatabaseStatsTableData() {
	PostgresqlService service = ContextLoader.currentWebApplicationContext.getBean(PostgresqlService.class)
	def ret = [:]
	
	List<Map<String, Object>> stats = service.getDatabaseStats()
	def tableStatsMatrixData = []
	tableStatsMatrixData << stats[0].keySet()
	stats.each { row -> tableStatsMatrixData << row.values() }  
	ret['tableStats'] = ['title':'Database Statistics', 'matrixData': tableStatsMatrixData]
	
	List<Map<String, Object>> stats2 = service.getCacheStats()
	def tableCacheMatrixData = []
	tableCacheMatrixData << stats2[0].keySet()
	stats2.each { row -> tableCacheMatrixData << row.values() }
	ret['tableCache'] = ['title':'Cache Statistics', 'matrixData': tableCacheMatrixData]
	return ret
}

Map getDatabaseSizesData() {
	PostgresqlService service = ContextLoader.currentWebApplicationContext.getBean(PostgresqlService.class)
	def ret = [:]

	List<Map<String, Object>> topSizes = service.getTopDatabaseSizes(-1);
	def tableSizesMatrixData = []
	tableSizesMatrixData << ['database', 'size']
	topSizes.each { row -> 
		tableSizesMatrixData << row.values() 
	}
	ret['dbSizes'] = ['title':'Database Sizes', 'matrixData': tableSizesMatrixData]
	return ret
}

def resp = [
	'graphCPU': getCPUData(),
	'graphMemory': getMemoryData()
]
resp.putAll(getDatabaseStatsData())
resp.putAll(getDatabaseStatsTableData())
resp.putAll(getDatabaseSizesData())
out.println new JsonBuilder(resp).toString()





