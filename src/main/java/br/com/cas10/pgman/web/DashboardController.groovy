package br.com.cas10.pgman.web

import br.com.cas10.pgman.service.PostgresqlService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@CompileStatic
@Controller
@RequestMapping("/dashboard")
class DashboardController {
	
	@Autowired
	private PostgresqlService service

	@RequestMapping(method = RequestMethod.GET)
	public String open(Model model) {
		return "dashboard";
	}

	@RequestMapping(value="/databaseStats", method = RequestMethod.GET)
	public String databaseStats(Model model) {
		List<Map<String, Object>> stats = service.getDatabaseStats();
		model.addAttribute("stats", stats);
		return "databaseStats";
	}
	
	@RequestMapping(value="/cacheStats", method = RequestMethod.GET)
	public String cacheStats(Model model) {
		List<Map<String, Object>> stats = service.getCacheStats();
		model.addAttribute("stats", stats);
		return "cacheStats";
	}

	@RequestMapping(value="/topStatements/{size}", method = RequestMethod.GET)
	public String topStatements(@PathVariable("size") Integer size, Model model) {
		List<Map<String, Object>> topSizes = service.getTopStatements(size);
		model.addAttribute("topStatements", topSizes);
		return "topStatements";
	}
	
	@RequestMapping(value="/resetStatementsStats", method = RequestMethod.GET)
	@ResponseBody
	public String resetStatementsStats(Model model) {
		service.resetStatementsStats();
		return "Ok"
	}
	
}
