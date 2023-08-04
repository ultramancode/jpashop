package com.example.jpashop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.jpashop.domain.Member;
import com.example.jpashop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // 스프링부트 띄운 상태로 스프링컨테이너 안에서 테스트 돌리겠다!
@Transactional //롤백용, 만약 키생성 전략을 다른 걸(시퀀스 같은)로 했으면 트랜잭션 롤백되니까 회원가입에서 insert 쿼리 안나감!! 영속성 컨테스트가 플러시 안해버림!! 셀렉트 쿼리만~
class MemberServiceTest {

  @Autowired
  MemberService memberService;
  @Autowired
  MemberRepository memberRepository;
  @Autowired
  EntityManager em;
  @Test
  @DisplayName("회원가입")
//  @Rollback(value = false)
  public void 회원가입() {
    //given
    Member member = new Member.Builder().name("ktw").build();
    //when
    Long savedId = memberService.join(member);

    //then   => 테스트단 전체에 트랜잭션 걸려 있으니 같은 거 나오는 것..! pk같으면 영속성컨텍스트에서 같은 걸로 보고 관리하니

    //만약 아이디 생성 전략 sequence로 했을 때 인서트 쿼리 보고 싶으면 이렇게 flush 따로 해주면 됨
//    em.flush();
    assertThat(member).isEqualTo(memberRepository.findOne(savedId));
  }
  @Test
  @DisplayName("중복회원예외")
  public void 중복_회원_예외() {
    //given
    Member member1 = new Member.Builder().name("ktw").build();
    Member member2 = new Member.Builder().name("ktw").build();

//    Member member1 = new Member();
//    member1.setName("ktw");
//    Member member2 = new Member();
//    member2.setName("ktw");
    //when
    memberService.join(member1);
//    memberService.join(member2);

    //then
    Assertions.assertThrows(IllegalStateException.class, () -> {
      memberService.join(member2);
    });

    }
}