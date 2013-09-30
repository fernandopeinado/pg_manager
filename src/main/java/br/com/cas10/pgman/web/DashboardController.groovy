package br.com.cas10.pgman.web

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.cas10.pgman.service.PostgresqlService;

@Controller
@RequestMapping("/dashboard")
class DashboardController {
	
	@Autowired
	private PostgresqlService service

	@RequestMapping(method = RequestMethod.GET)
	public String open(Model model) {
		return "dashboard";
	}

	@RequestMapping(value="/topRelationSizes/{size}", method = RequestMethod.GET)
	public String topRelationSizes(@PathVariable("size") Integer size, Model model) {
		List<Map<String, Object>> topSizes = service.getTopRelationSizes(size);
		model.addAttribute("topSizes", topSizes);
		return "topRelationSizes";
	}

	@RequestMapping(value="/topDatabaseSizes/{size}", method = RequestMethod.GET)
	public String topDatabaseSizes(@PathVariable("size") Integer size, Model model) {
		List<Map<String, Object>> topSizes = service.getTopDatabaseSizes(size);
		model.addAttribute("topSizes", topSizes);
		return "topDatabaseSizes";
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
