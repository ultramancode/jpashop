package com.example.jpashop.repository.order.query;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
//v6 컨트롤러 스트림할 때 그루핑바이에서 묶어주는 용도..
@EqualsAndHashCode(of = "orderId")
public class OrderQueryDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;
  private List<OrderItemQueryDto> orderItems;

  public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus,
      Address address) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
  }

  public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus,
      Address address, List<OrderItemQueryDto> orderItems) {
    this.orderId = orderId;
    this.name = name;
    this.orderDate = orderDate;
    this.orderStatus = orderStatus;
    this.address = address;
    this.orderItems = orderItems;
  }
}
