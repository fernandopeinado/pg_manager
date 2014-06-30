import groovy.json.JsonBuilder

import org.springframework.web.context.ContextLoader

import br.com.cas10.pgman.service.PostgresqlService

/*
 request - the HttpServletRequest
 response - the HttpServletResponse
 application - the ServletContext associated with the servlet
 session - the HttpSession associated with the HttpServletRequest
 out - the PrintWriter associated with the ServletReques
 */

response.contentType = 'application/json'

Integer size = 20;

PostgresqlService service = ContextLoader.currentWebApplicationContext.getBean(PostgresqlService.class)

List<Map<String, Object>> stats = service.getTopStatements(size);
def matrixData = []
matrixData << stats[0].keySet()
stats.each { row -> matrixData << row.values() }

def resp = [
	'tableTopSQL': [
		'title': 'Top Statements',
		'matrixData': matrixData
	]
]
out.println new JsonBuilder(resp).toString()





