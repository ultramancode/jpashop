package com.example.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Hello {

  @GetMapping("hello")
  public String hello(Model model) {
    //네임이 데이터라는 키의 값을 asdff로~
    model.addAttribute("data", "adfaff");
    //리턴..화면이름 .. hello.html로! .html 생략해도 알아서 인식함. templates안에~!
    //커멘드 클릭하면 .html로 가짐
    //스프링부트 thymleaf viewName 매핑
    // `resources:templates/ +{ViewName}+ `.html` 이렇게 돼있으니 가능한 것.
    //만약 다른 경로로 바꾸고 싶으면 부트 설정에서 바꾸면 될듯
    return "hello";
  }
}
