package com.example.jpashop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//이 느낌! Logger log = LoggerFactory.getLogger(getClass())
@Slf4j
public class HomeController {

  @RequestMapping("/")
  public String home() {
    log.info("home controller");
    return "home";
  }

}
