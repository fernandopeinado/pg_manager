package br.com.cas10.pgman.web

import groovy.transform.CompileStatic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import br.com.cas10.pgman.service.DatabaseService;
import br.com.cas10.pgman.service.PostgresqlService;
import br.com.cas10.pgman.service.SystemService

@CompileStatic
@Controller
@RequestMapping("/report")
class ReportController {
	
	@Autowired
	private PostgresqlService service;
	
	@Autowired
	private DatabaseService databaseService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String open(Model model) {
		Collection<String> databases = databaseService.databases;
		Map<String, List<Map<String, Object>>> missingIndexFK = new HashMap<String, List<Map<String, Object>>>()
		databases.each { String db ->
			missingIndexFK[db] = service.getMissingIndexesForForeignKeys(db)
		}
		model.addAttribute("databases", databases)
		model.addAttribute("missingIndexFK", missingIndexFK);
		return "report"
	}
	
}
