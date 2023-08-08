package com.example.jpashop.repository;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderStatus;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.cglib.core.Local;

@Data
public class OrderSimpleQueryDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;


//  dto가 엔티티를 파라미터로 받는 거는 괜찮. 중요하지 않은데서 중요한 엔티티에 의존하는 거니까

  public OrderSimpleQueryDto(Order order) {
    orderId = order.getId();
    name = order.getMember().getName(); //lazy 초기화
    orderDate = order.getOrderDate();
    orderStatus = order.getStatus();
    address = order.getDelivery().getAddress(); // lazy 초기화
  }


  //jpql에서 select new~~~~~~~~~~OrderSimpleQueryDto(o) 이렇게 엔티티 바로 못넘김, 엔티티가 식별자로 넘어가기 때문이
  //밑에처럼 일일히 하나씩 써줘야함. 위에처럼 엔티티넣어서못함!!
  public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
    this.orderId = orderId;
    this.name = name; //lazy 초기화
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address; // lazy 초기화
  }
}