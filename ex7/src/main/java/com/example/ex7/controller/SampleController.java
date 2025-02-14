package com.example.ex7.controller;

import com.example.ex7.security.dto.ClubMemberAuthDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

  @GetMapping("/login")
  public void exLogin() {log.info("/login....");}

  @GetMapping("/all")
  public void exAll() {log.info("/all....");}

  @GetMapping("/manager")
  public void exManager(@AuthenticationPrincipal ClubMemberAuthDTO clubMemberAuthDTO) {
    log.info("/manager....");
    log.info("ClubMemberAuthDTO: " + clubMemberAuthDTO);
  }

  @GetMapping("/admin")
  public void exAdmin() {log.info("/admin....");}
}
