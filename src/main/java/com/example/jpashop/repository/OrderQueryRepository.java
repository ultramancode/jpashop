package com.example.jpashop.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import javax.swing.text.html.parser.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
//DTO로 바로 조회하여 반환하는 경우 레포지토리가 api스펙(화면)에 의존적이고 entity 순수성이 훼손됨
//따로 쿼리레포지토리 분리
public class OrderQueryRepository {

  private final EntityManager em;

  public List<OrderSimpleQueryDto> findOrderDtos() {
    //jpa는 엔티티나 밸류오브젝트(임베더블 같은..)만 반환 가능, dto에 바로 매핑 안됨, address는 값타입이라 가능
    //따라서 new 오퍼레이션 써줘야함 (지저분해진다는 단점 존재)
    return em.createQuery(
            "select new com.example.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderSimpleQueryDto.class)
        .getResultList();
  }
}
