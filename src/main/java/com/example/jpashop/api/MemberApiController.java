package com.example.jpashop.api;

import com.example.jpashop.domain.Member;
import com.example.jpashop.service.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@Controller + @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;

// 이렇게 엔티티 바로 던지면 안됨
//  @PostMapping("/api/v1/members")
//  public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
//    Long id = memberService.join(member);
//    return new CreateMemberResponse(id);
//  }

  @PostMapping("api/v2/members")
  //@RequestBody : json으로 온 body 데이터를 member로 매핑
  public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

    Member member = Member.builder().name(request.getName()).build();
    Long id = memberService.join(member);
    return new CreateMemberResponse(id);
  }

  @PutMapping("api/v2/members/{id}")
  public UpdateMemberResponse updateMemberV2(
      @PathVariable("id") Long id,
      @RequestBody @Valid UpdateMemberRequest request) {

    //멤버를 업데이트메소드에서 반환하지 않는 이유! 커맨드,쿼리 분리!
    //영속 상태가 끊긴 객체가 반환되고, 업데이트(커맨드)하면서 쿼리(id가지고 조회)까지 날아가면 커맨드/쿼리 분리 원칙 위배
    //커맨드와 쿼리를 분리하는게 좋음 => 유지보수성 증대
    //커맨드는 엔티티바꾸거나 하는 변경성 메소드.
    //따라서 업데이트는 업데이트만하든가 반환하더라도 id값 정도만 반환해주자
    memberService.update(id, request.getName());
    Member findMember = memberService.findOne(id);

    return new UpdateMemberResponse(findMember.getId(), findMember.getName());


  }

  //이렇게 엔티티 직접 노출시 변경하면 api 스펙 자체가 바뀌고 장애가 난다.
  //예를 들어 엔티티 반환 시에 멤버에서 name을 username으로 바꾸면 api스펙 자체가 바껴버림
  //협업할 때 빌런
//  @GetMapping("/api/v1/members")
//  public List<Member> membersV1() {
//    return memberService.findMembers();
//  }
@GetMapping("/api/v2/members")
public Result memberV2() {
  List<Member> findMembers = memberService.findMembers();
  List<MemberDto> collect = findMembers.stream()
      .map( m -> new MemberDto(m.getName()))
      .collect(Collectors.toList());

  //Result로 한번 감싸주기
  //오브젝트 타입 반환.. 데이터필드 값은 리스트로
  //안감싸주면 바로 리스트 배열타입으로 가고 유연성이 떨어진다.
  //리스트로 바로 반환하면 실무에서는 유연성이 떨어져서 안됨
  //이렇게 한번 감싸면 추가로 .size()도 넣어준다든가 하는 식으로 유연성있게 가능!
  return new Result(collect.size(), collect);

}

//한번 더 감싸 줘서 유연성 up
@Data
@AllArgsConstructor
static class Result<T> {
    private int count;
    private T data;
}
@Data
@AllArgsConstructor
static class MemberDto {
    private String name;
}



  //학습 목적으로 사용하는 @Data
  @Data
  static class CreateMemberRequest {
    private String name;
  }


  //학습 목적으로 사용하는 @Data
  @Data
  static class CreateMemberResponse {
    private Long id;
    public CreateMemberResponse(Long id) {
      this.id = id;
    }
  }

  @Data
  @AllArgsConstructor
  static class UpdateMemberResponse {
    private Long id;
    private String name;
  }

  @Data
  static class UpdateMemberRequest {
    private String name;
  }
}
