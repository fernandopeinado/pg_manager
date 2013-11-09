package br.com.cas10.pgman.web

import groovy.transform.CompileStatic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import br.com.cas10.pgman.service.SystemService

@CompileStatic
@Controller
@RequestMapping("/system")
class SystemController {
	
	@Autowired
	private SystemService system;
	
	@RequestMapping(method = RequestMethod.GET)
	public String open(Model model) {
		model.addAttribute("basicInfo", system.basicInfo)
		model.addAttribute("processorInfo", system.processorInfo)
		model.addAttribute("memoryInfo", system.memoryInfo)
		model.addAttribute("networkInfo", system.networkInfo)
		model.addAttribute("storageInfo", system.storageInfo)
		model.addAttribute("sysctl", system.sysctl)
		return "system"
	}
	
}
