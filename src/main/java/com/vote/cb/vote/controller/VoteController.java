package com.vote.cb.vote.controller;

import com.vote.cb.vote.service.VoteService;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/vote")
public class VoteController {

  @Autowired
  private VoteService voteService;

  @GetMapping("")
  public ModelAndView viewSignPage() throws Exception {

    ModelAndView model = new ModelAndView();
    model.setViewName("vote/voting-sign2");
    return model;
  }


  @GetMapping("/{uid}")
  public ModelAndView viewSignPageByLink(@PathVariable("uid") String uid) throws Exception {

    if (voteService.invalidUid(uid)) {
      throw new Exception("없는 데이터");
    }

    ModelAndView model = new ModelAndView();
    model.addObject("uri", uid);
    model.setViewName("vote/voting-sign");

    return model;
  }

  @GetMapping("/{uid}/voting")
  public ModelAndView viewVotingPage(@PathVariable("uid") String uid, HttpSession session)
      throws Exception {

    ModelAndView model = new ModelAndView();

    String name = (String) session.getAttribute("name");
    String phone = (String) session.getAttribute("phone");

    if (voteService.checkVote(name, phone, uid)) {
      throw new Exception();
    }

    model.addObject("voteInfo", voteService.getVoteList(name, phone, uid));
    model.addObject("uid", uid);
    model.setViewName("vote/vote");

    return model;
  }

  @GetMapping("/success")
  public ModelAndView viewSuccessVotingPage() {

    ModelAndView model = new ModelAndView();
    model.setViewName("vote/voteSuccess.html");
    return model;
  }
}
