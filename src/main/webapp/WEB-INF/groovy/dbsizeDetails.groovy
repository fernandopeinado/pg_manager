import java.util.Map;

import groovy.json.JsonBuilder

import org.springframework.ui.Model;
import org.springframework.web.context.ContextLoader

import br.com.cas10.pgman.service.PGManagerDAO;
import br.com.cas10.pgman.service.PostgresqlService

/*
 request - the HttpServletRequest
 response - the HttpServletResponse
 application - the ServletContext associated with the servlet
 session - the HttpSession associated with the HttpServletRequest
 out - the PrintWriter associated with the ServletReques
 */

response.contentType = 'application/json'

def resp = [:]

String database = request.getParameter('database')
String oid = request.getParameter('oid')

PostgresqlService service = ContextLoader.currentWebApplicationContext.getBean(PostgresqlService.class)
PGManagerDAO dao = ContextLoader.currentWebApplicationContext.getBean(PGManagerDAO.class)

resp = service.getTableDetails(Long.valueOf(oid), database)

out.println new JsonBuilder(resp).toString()