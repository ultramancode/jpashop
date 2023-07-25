package com.example.jpashop;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {

  @Autowired MemberRepository memberRepository;

  @Test
  @Transactional //없으면 엔티티매니저를 통한 데이터 변경은 트랜잭션 필요하다고 에러 뜸 혹은 실제 레포나 서비스단에서 트랜잭셔널 달아줘야겠지
// + 테스트에 트랜잭셔널 달면 끝나고 디비 롤백함(@Rollback(false) 달면 안함). 실제 레포에만 트랜잭션 있으면 롤백을 안한다!!
  public void testMember() throws Exception {
    //given
    Member member = new Member();
    member.setUsername("memberA");
    //when
    Long savedId = memberRepository.save(member);
    Member findMember = memberRepository.find(savedId);

    //then
    Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
    Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//하지만 트랜잭셔널 떼면 false 뜸!! 1차캐시에 없으니 셀렉트 쿼리 나가고 불러오니 다른 객체 !!
//트랜잭셔널 달려있으면 인서트 쿼리만 나가고 셀렉트 쿼리는 따로 안나감(영속성 컨텍스트 보관된 거 가져오니)
    Assertions.assertThat(findMember).isEqualTo(member);
  }
}