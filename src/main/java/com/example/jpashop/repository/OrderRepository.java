package com.example.jpashop.repository;

import com.example.jpashop.domain.Order;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
  private final EntityManager em;
  public void save(Order order) {
    em.persist(order);
  }

  private Order findOne(Long id) {
    return em.find(Order.class, id);
  }
}
