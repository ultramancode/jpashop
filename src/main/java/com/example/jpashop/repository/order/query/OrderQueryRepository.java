package com.example.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

  private final EntityManager em;

  // v4
  public List<OrderQueryDto> findOrderQueryDtos() {
    List<OrderQueryDto> result = findOrders(); // 쿼리 1번

    // 컬렉션 부분은 안가져오고 직접 채움
    result.forEach(
        o -> {
          List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // 쿼리 N번
          o.setOrderItems(orderItems);
        }
    );
    return result;
  }


  // v5
  // orderIds를 in절로 받아서 쿼리 한방으로 해결
  public List<OrderQueryDto> findAllByDtoOptimization() {
    List<OrderQueryDto> result = findOrders();

    List<Long> orderIds = result.stream()
            .map( o -> o.getOrderId())
            .collect(Collectors.toList());

    List<OrderItemQueryDto> orderItems = em.createQuery(
        "select new com.example.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
            " from OrderItem oi" +
            " join oi.item i" +
            " where oi.order.id in :orderIds", OrderItemQueryDto.class)
        .setParameter("orderIds", orderIds)
        .getResultList();

    // 위에 걸 바로 써도 되지만 좀 더 최적화 해주기 위해 맵으로
    // 위에서 쿼리 한번 날리고 메모리에서 값을 매칭해서 세팅하는 것
    Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
        .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));

    result.forEach( o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

    return result;
  }

  // 일 대 다 부분 해결 위해..
  private List<OrderItemQueryDto> findOrderItems(Long orderId) {
    return em.createQuery(
        "select new com.example.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
        " from OrderItem oi" +
        " join oi.item i" +
        " where oi.order.id = :orderId", OrderItemQueryDto.class)
        .setParameter("orderId", orderId)
        .getResultList();
  }
  public List<OrderQueryDto> findOrders() {

    return em.createQuery(
            "select new com.example.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderQueryDto.class)
        .getResultList();
  }

  public List<OrderFlatDto> findAllByDtoFlat() {
    return em.createQuery(
            "select new com.example.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItems oi" +
                " join oi.item i", OrderFlatDto.class)
        .getResultList();
  }

  }

