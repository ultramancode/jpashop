package com.example.jpashop.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderItem;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.order.query.OrderFlatDto;
import com.example.jpashop.repository.order.query.OrderItemQueryDto;
import com.example.jpashop.repository.order.query.OrderQueryDto;
import com.example.jpashop.repository.order.query.OrderQueryRepository;
import com.example.jpashop.repository.order.simpleQuery.OrderSimpleQueryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문 컨트롤러", description = "주문 API 문서")
public class OrderApiController {

  private final OrderRepository orderRepository;
  private final OrderSimpleQueryRepository orderSimpleQueryRepository;
  private final OrderQueryRepository orderQueryRepository;
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
//  @GetMapping("/api/v3/orders")
//  public List<OrderDto> orderV3() {
//    List<Order> orders = orderRepository.findAllByWithItem();
//    List<OrderDto> orderDtos = orders.stream()
//        .map(o -> new OrderDto(o))
//        .collect(Collectors.toList());
//
//    for (Order order : orders) {
//      System.out.println("order ref=" + order + " id=" + order.getId());
//    }
//    return orderDtos;
//  }
  // 지연로딩 + 배치사이즈 (제일 나은 선택인듯)
  // OSIV 끌꺼면 서비스단에서 처리해줘야겠지
  @GetMapping("/api/v3.1/orders")
  @Operation(summary = "주문 조회", description = "주문 화면 출력")
  @Parameters(value = {@Parameter(name = "offset", description = "오프셋!"),
      @Parameter(name = "limit", description = "리밋!")})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = OrderDto.class))),
      @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = Exception.class)))
  })
  public List<OrderDto> orderV3Page(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "100") int limit
  ) {
    List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
    List<OrderDto> orderDtos = orders.stream()
        .map(o -> new OrderDto(o))
        .collect(toList());

    return orderDtos;
  }

// v4,5,6은 바로 DTO로 조회

//  @GetMapping("/api/v4/orders")
//  private List<OrderQueryDto> ordersV4() {
//
//    return orderQueryRepository.findOrderQueryDtos();
//
//  }
//
//  @GetMapping("api/v5/orders")
//  private List<OrderQueryDto> orderV5() {
//    return orderQueryRepository.findAllByDtoOptimization();
//  }
//
//  @GetMapping("api/v6/orders")
//  private List<OrderQueryDto> orderV6() {
//      List<OrderFlatDto> flats = orderQueryRepository.findAllByDtoFlat();
//      // 뻥튀기 된 row 줄이기 위해 직접 중복 줄이는..
//      // 오더 쿼리 디티오 양식에 맞추기 위해
//      // 오더 쿼리 디티오랑 오더 아이템 쿼리 디티오 발라내야..
//      return flats.stream()
//          .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
//                  o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
//              mapping(o -> new OrderItemQueryDto(o.getOrderId(),
//                  o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
//          )).entrySet().stream()
//          // 최종적으로 오더 쿼리 디이토 만들고
//          .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
//              e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
//              e.getKey().getAddress(), e.getValue()))
//          .collect(toList());
//    }
//  }


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
  @Schema(description = "주문 관련 dto")
  class OrderDto {

    private Long orderId;
    @Schema(description = "이름")
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    @Schema(description = "주소")
    private Address address;

    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {

      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getDelivery().getAddress();
      // 이거 없이 하면 orderItems가 null로 뜸 why? 엔티티니까! 강제 초기화 필요
//      order.getOrderItems().stream().forEach(o -> o.getItem().getName());
      orderItems = order.getOrderItems().stream()
          .map(o -> new OrderItemDto(o))
          .collect(toList());

    }

  }

  @Getter
  class OrderItemDto {

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