package com.example.jpashop.controller;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Member;
import com.example.jpashop.service.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/members/new")
  public String createForm(Model model) {
    // 컨트롤러에서 뷰로 넘길 때 이 데이터를 실어서 넘긴다.
    model.addAttribute("memberForm", new MemberForm());
    //멤버폼이라는 빈 껍데기 객체를 가져가는 이유는 뭔가 validation같은 걸 해줘서 빈껍데기라도 일단 들고감
    return "members/createMemberForm";
  }

  @PostMapping("/members/new")
  //멤버폼을 파라미터로, @valid로 form에 있는 @notEmpty 검증!
  //BindingResult => 오류 발견해서 result에 담겨서 실행이 됨 이걸 이용해서 에러가 있으면 다른거 리턴하게 한다든가 하는 방식으로 이용
  public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {

    //스프링이 바인딩리절트를 밑에 return에 써둔 createMemberForm까지 끌고가서 화면에 뿌릴 수 있다.
    if (bindingResult.hasErrors()) {
      return "members/createMemberForm";
    }
    Address address = new Address(memberForm.getCity(), memberForm.getStreet(),
        memberForm.getZipcode());

    Member member = new Member.Builder()
        .name(memberForm.getName())
        .address(address)
        .build();
    memberService.join(member);
    //저장 후 리다이렉트 해버리기
    return "redirect:/";
  }

  @GetMapping("/members")
  public String list(Model model) {
    //학습용이라 dto 안만들고 일단 엔티티 넘김 (추후 dto로)
    List<Member> members = memberService.findMembers();
    //멤버스 가져 와서 모델에 담아서 화면에 넘긴다.
    model.addAttribute("members", members);
    return "members/memberList";
  }
}


