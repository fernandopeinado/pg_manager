package br.com.cas10.pgman.web

import br.com.cas10.pgman.service.DatabaseService
import br.com.cas10.pgman.service.PgExtensionDAO
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@CompileStatic
@Controller
@RequestMapping("/extension")
class PgExtensionController {
	
	@Autowired
	private PgExtensionDAO dao;

	@Autowired
	private DatabaseService dbService;

	@RequestMapping(method = RequestMethod.GET)
	public String listDbs(Model model) {
		model.addAttribute("databases", dbService.databases)
		return "extensionDb"
	}

	@RequestMapping(value = "/{database}", method = RequestMethod.GET)
	public String open(@PathVariable("database") String database, Model model) {
		model.addAttribute("extensions", dao.getExtensions(database))
        model.addAttribute("database", database)
		return "extension"
	}

	@RequestMapping(value = "/{database}/create/{name}", method = RequestMethod.GET)
	public String create(@PathVariable("database") String database, @PathVariable("name") String name, Model model) {
		dao.createExtension(database, name)
		model.addAttribute("extensions", dao.getExtensions(database))
        model.addAttribute("database", database)
		return "extension"
	}

}
