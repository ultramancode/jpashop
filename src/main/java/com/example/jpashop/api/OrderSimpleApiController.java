package com.example.jpashop.api;

import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderSearch;
import com.example.jpashop.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;


  //하면 안된다는 것을 보여주는 예시 코드. 이렇게 엔티티 직접 외부노출 금지! dto 쓰기!
  @GetMapping("/api/v1/simple-orders")
  public List<Order> orderV1() {
    List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
//    for (Order order :
//        all) {
//      order.getMember().getName();//Lazy 강제 초기화
//      order.getDelivery().getAddress();//Lazy 강제 초기화
//    }
    return all;
  }

}
