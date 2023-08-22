package com.example.jpashop.api;

import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderSearch;
import com.example.jpashop.repository.order.simpleQuery.OrderSimpleQueryRepository;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.order.simpleQuery.OrderSimpleQueryDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xToOne(ManyToOne, OneToOne) Order Order -> Member Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;
  private final OrderSimpleQueryRepository orderSimpleQueryRepository;


  //v1. 하면 안된다는 것을 보여주는 예시 코드. 이렇게 엔티티 직접 외부노출 금지! dto 쓰기!
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

  //v2. 이렇게 하면 n+1 문제 발생함. 페치 조인을 해줘야 한방에 쿼리 긁어옴
  @GetMapping("/api/v2/simple-orders")
  public List<OrderSimpleQueryDto> orderV2() {
    List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

    return orders.stream()
        .map(o -> new OrderSimpleQueryDto(o))
        .collect(Collectors.toList());
  }

  //v3. 페치조인으로 n+1문제 해결, 하지만 select가 너무 많아서 지저분함
  @GetMapping("/api/v3/simple-orders")
  public List<OrderSimpleQueryDto> orderV3() {
    List<Order> orders = orderRepository.findAllWithMemberDelivery();
    return orders.stream()
        //(o -> new SimpleOrderDto(o))와 동일
        .map(OrderSimpleQueryDto::new)
        .collect(Collectors.toList());
  }

  //v4, select절에서 내가 원하는 것만 셀렉트!
  //페치조인..조인하는거까진 동일한데 셀렉트절에서 (db) db를 너무 퍼올림, 그래서 네트웤을 더많이쓰니까 셀렉트해서 줄이는 것!
  // v3와 v4 비슷
  //v3는 가져와서 다른데서 막 사용 가능.. 재사용 성 높다. 반면 v4는 핏하게 다 설정해서 만들었기 때문에 다른데서 사용하기 좀 어렵다..
  //대신 v4는 좀 더 성능상 좋음. 하지만 v3는 엔티티라서 비즈니스로직이라서 변경 용이
  //v4는 못바꿈
  //trade off가 있다. v3와 v4 사이에는!
  //v3는 재사용성+코드 쉬움, v4는 성능상 좀 더 좋음... 근데 요새 네트워크가 좋아서 별 차이 안날듯? 그리고
  // 대부분의 성능은 join from 혹은 이상하게 인덱스가 안타거나 이런데서 나타나는거지
  //셀렉절 몇개로 성능영향은 적음 ..만약 20~30개 이상이고 이러면 고려해보고..

  @GetMapping("/api/v4/simple-orders")
  public List<OrderSimpleQueryDto> ordersV4() {
    return orderSimpleQueryRepository.findOrderDtos();
  }


}
