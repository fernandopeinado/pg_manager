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

import br.com.cas10.pgman.agent.CpuAgent;

@Controller
@RequestMapping("/agent/cpu")
class CpuAgentController {
	
	@Autowired
	private CpuAgent agent

	@RequestMapping(method = RequestMethod.GET)
	public String open(Model model) {
		model.addAttribute("snapshots", agent.data)
		return "agent/cpu";
	}

	@RequestMapping(value="/refresh", method = RequestMethod.GET)
	public String refresh(Model model) {
		model.addAttribute("snapshots", agent.data)
		return "agent/cpuFragment";
	}
}
