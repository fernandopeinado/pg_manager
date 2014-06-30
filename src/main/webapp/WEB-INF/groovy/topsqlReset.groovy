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

try {
	PostgresqlService service = ContextLoader.currentWebApplicationContext.getBean(PostgresqlService.class)
	service.resetStatementsStats();
	out.println new JsonBuilder(['message': 'Statements reseted']).toString()
} catch (Exception e) {
	out.println new JsonBuilder(['message': "Error: ${e.message}"]).toString()
}
