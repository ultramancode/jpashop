package com.example.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MemberRepository {

  //엔티티 매니저 생성, 스프링부트가 이 어노테이션 있으면 엔티티 매니저 주입해줌!!
  //엔티티매니저 설정에서 따로 안만들어도 빌드.그래들에 주입했던 스프링부트 스타터 데이터 jpa에서 다 생성해서 넣어줌

  @PersistenceContext
  private EntityManager em;


  @Transactional
  //조회용으로 아이디정도만 리턴
  public Long save(Member member){
    em.persist(member);
    return member.getId();
  }

  public Member find(Long id){
    return em.find(Member.class, id);
  }

}
