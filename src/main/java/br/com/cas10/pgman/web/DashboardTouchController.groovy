package br.com.cas10.pgman.web

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam;

import br.com.cas10.pgman.service.PostgresqlService;

@Controller
@RequestMapping("/dashboardTouch")
class DashboardTouchController {
	
	@Autowired
	private PostgresqlService service

	@RequestMapping(method = RequestMethod.GET)
	public String open(Model model) {
		return "dashboardTouch";
	}

	@RequestMapping(value="/auditStats", method = RequestMethod.GET)
	public String auditStats(Model model) {
		List<Map<String, Object>> stats = service.getAuditStats();
		model.addAttribute("stats", stats);
		return "auditStats";
	}

}
