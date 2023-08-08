package com.example.jpashop.api;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderItem;
import com.example.jpashop.domain.OrderSearch;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;
//
//  //엔티티 바로 노출
//  //학습용임. 실무에서는 이렇게 사용 X
//  @GetMapping("/api/v1/orders")
//  public List<Order> orderV1() {
//    List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
//    for (Order order : orders) {
//      order.getMember().getName(); // lazy 강제 초기화
//      order.getDelivery().getAddress();// lazy 강제 초기화
//      List<OrderItem> orderItems = order.getOrderItems();
//      orderItems.stream().forEach(o -> o.getItem().getName()); // lazy 강제 초기화
//    }
//
//    return orders;
//  }
//
//  //이렇게 하면 n+1 문제 발생(orderItem..!!!)! 성능 저하! 이제 v3에서 페치조인 해보겠음
//  @GetMapping("/api/v2/orders")
//  public List<OrderDto> orderV2() {
//    List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
//    List<OrderDto> orderDtos = orders.stream()
//        .map(o -> new OrderDto(o))
//        .collect(Collectors.toList());
//
//    return orderDtos;
//  }

  // 페치조인! 쿼리 한방에!
  @GetMapping("/api/v3/orders")
  public List<OrderDto> orderV3() {
    List<Order> orders = orderRepository.findAllByWithItem();
    List<OrderDto> orderDtos = orders.stream()
        .map(o -> new OrderDto(o))
        .collect(Collectors.toList());

    for (Order order : orders) {
      System.out.println("order ref=" + order + " id=" + order.getId());
    }
    return orderDtos;
  }



  //이렇게 Dto안에 엔티티 담으면 안됨(OrderItem)
  //이렇게 되버리면 외부노출 + orderItem 수정시 orderDto에도 영향이 가버리니 당연히 안됨
  //완전히 의존을 끊어라.
//  @Data
//  static class OrderDto {
//    private Long orderId;
//    private String name;
//    private LocalDateTime orderDate;
//    private OrderStatus orderStatus;
//    private Address address;
//
//    private List<OrderItem> orderItems;
//
//    public OrderDto(Order order) {
//
//      orderId = order.getId();
//      name = order.getMember().getName();
//      orderDate = order.getOrderDate();
//      orderStatus = order.getStatus();
//      address = order.getDelivery().getAddress();
//      // 이거 없이 하면 orderItems가 null로 뜸 why? 엔티티니까! 강제 초기화 필요
//      order.getOrderItems().stream().forEach(o -> o.getItem().getName());
//      orderItems = order.getOrderItems();
//
//    }
//
//  }
  @Data
  static class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {

      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getDelivery().getAddress();
      // 이거 없이 하면 orderItems가 null로 뜸 why? 엔티티니까! 강제 초기화 필요
      order.getOrderItems().stream().forEach(o -> o.getItem().getName());
      orderItems = order.getOrderItems().stream()
          .map(o -> new OrderItemDto(o))
          .collect(Collectors.toList());

    }

  }

  @Getter
  static class OrderItemDto {

    private final String itemName; //상품 명
    private final int orderPrice; //주문 가격
    private final int count; //주문 수량

    public OrderItemDto(OrderItem orderItem) {
      this.itemName = orderItem.getItem().getName();
      this.orderPrice = orderItem.getItem().getPrice();
      this.count = orderItem.getCount();
    }
  }


}
