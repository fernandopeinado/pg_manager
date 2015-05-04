package br.com.cas10.pgman.web;

import br.com.cas10.pgman.domain.TopQueriesSnapshot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.cas10.pgman.service.PGManagerDAO;
import java.util.List;

@Controller
@RequestMapping("/topqueries")
public class TopQueriesController {

  @Autowired
  private PGManagerDAO dao;

  @ResponseBody
  @RequestMapping(method = RequestMethod.GET)
  public List<TopQueriesSnapshot> topQueries(Model model) {
    List<TopQueriesSnapshot> snaps = dao.getSnapshotQueries(12);
    return snaps;
  }

}
