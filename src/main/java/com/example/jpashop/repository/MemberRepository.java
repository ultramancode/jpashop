package com.example.jpashop.repository;

import com.example.jpashop.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository // 컴포넌트 스캔해서 빈으로 등록하겠지, 안에 까보면 @component 붙어 , @SpringBootApplication이 컴포넌트스캔해서 샥하겠지
public class MemberRepository {

  @PersistenceContext //스프링이 엔티티매니저 만들어서 주입해줌
  private EntityManager em;

  public void save(Member member) {
    em.persist(member);
  }

  public Member findOne(Long id) {
    return em.find(Member.class, id);
  }

  //JPQL 사용 (테이블이 아닌 엔티티 객체 대상 쿼리)
  public List<Member> findAll() {
    return em.createQuery("select m from Member m", Member.class)
        .getResultList();
  }

  //:name으로 파라미터 바인딩
  public List<Member> findByName(String name) {
    return em.createQuery("select m from Member m where m.name =:name", Member.class)
        .setParameter("name", name)
        .getResultList();
  }



}
