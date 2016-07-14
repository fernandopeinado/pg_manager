package br.com.cas10.pgman.web

import br.com.cas10.pgman.service.PgExtensionDAO
import br.com.cas10.pgman.service.PostgresqlService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@CompileStatic
@Controller
@RequestMapping("/parameters")
class PgParametersController {
	
	@Autowired
	private PostgresqlService pgService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String open(Model model) {
		model.addAttribute("categories", pgService.getSettings(false))
		return "parameters"
	}

	@RequestMapping(value = "/nondefault", method = RequestMethod.GET)
	public String openNonDefault(Model model) {
		model.addAttribute("categories", pgService.getSettings(true))
		return "parameters"
	}

}
