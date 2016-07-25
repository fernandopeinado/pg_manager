package br.com.cas10.pgman.web;

import br.com.cas10.pgman.domain.TopQueriesSnapshot;
import br.com.cas10.pgman.domain.TopQuery;
import br.com.cas10.pgman.service.PGManagerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/topqueries")
public class TopQueriesController {

  @Autowired
  private PGManagerDAO dao;

  @ResponseBody
  @RequestMapping(value = "/{last}", method = RequestMethod.GET)
  public List<TopQueriesSnapshot> topQueries(@PathVariable("last") int last) {
    List<TopQueriesSnapshot> snaps = dao.getSnapshotQueries(last);
    return snaps;
  }

  @ResponseBody
  @RequestMapping(value = "/period/{init}/{end}", method = RequestMethod.GET)
  public List<TopQueriesSnapshot> topQueries(@PathVariable("init") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX") Date init, @PathVariable("end") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX") Date end) {
    List<TopQueriesSnapshot> snaps = dao.getSnapshotQueries(init, end);
    return snaps;
  }

  @ResponseBody
  @RequestMapping(value = "/consolidate/{init}/{end}", method = RequestMethod.GET)
  public List<TopQuery> consolidateQueries(@PathVariable("init") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX") Date init, @PathVariable("end") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX") Date end) {
    List<TopQuery> tops = dao.getConslidatedQueries(init, end);
    return tops;
  }

}
