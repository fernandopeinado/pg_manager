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

PostgresqlService service = ContextLoader.currentWebApplicationContext.getBean(PostgresqlService.class)
PGManagerDAO dao = ContextLoader.currentWebApplicationContext.getBean(PGManagerDAO.class)

List<Map<String, Object>> topSizes = service.getTopDatabaseSizes(-1);
Map<String, List<Long>> topSizesHist = dao.selectDbSizes(30);
List<Map<String, Object>> tableSizes = service.getTopRelationSizes(-1, database, 8192)

resp['database'] = database
resp['size'] = (topSizes.find { row -> row.dbname == database })?.size
resp['history'] = (topSizesHist.find { row -> row.key == database })?.value?.collect { it / (1024 * 1024) }


def relationsMatrixData = []
relationsMatrixData << ['relation', 'size']
tableSizes.each { row ->
	relationsMatrixData << [row.relation, row.size] 
}
resp['relations'] = relationsMatrixData

out.println new JsonBuilder(resp).toString()




