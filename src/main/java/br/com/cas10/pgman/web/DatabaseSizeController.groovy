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
import br.com.cas10.pgman.service.PGManagerDAO;
import br.com.cas10.pgman.service.PostgresqlService;

@Controller
@RequestMapping("/database/size")
class DatabaseSizeController {
	
	@Autowired
	private PostgresqlService service
	
	@Autowired
	private PGManagerDAO dao

	@RequestMapping(method = RequestMethod.GET)
	public String open(Model model) {
		List<Map<String, Object>> topSizes = service.getTopDatabaseSizes(-1);
		Map<String, List<Long>> topSizesHist = dao.selectDbSizes(30);
		model.addAttribute("topSizes", topSizes);
		model.addAttribute("topSizesHist", topSizesHist);
		return "topDatabaseSizes";
	}
	
	@RequestMapping(value="/topRelationSizes/{size}", method = RequestMethod.GET)
	public String topRelationSizes(@PathVariable("size") Integer size, Model model) {
		List<Map<String, Object>> topSizes = service.getTopRelationSizes(size, null, null)
		model.addAttribute("topSizes", topSizes);
		return "topRelationSizes";
	}
	
	@RequestMapping(value="/topRelationSizes/{db}/{size}", method = RequestMethod.GET)
	public String topRelationSizesByDB(@PathVariable("db") String db, @PathVariable("size") Integer size, Model model) {
		List<Map<String, Object>> topSizes = service.getTopRelationSizes(size, db, null)
		model.addAttribute("topSizes", topSizes);
		return "topRelationSizes";
	}

}
