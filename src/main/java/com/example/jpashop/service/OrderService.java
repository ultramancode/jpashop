package com.example.jpashop.service;

import com.example.jpashop.domain.Delivery;
import com.example.jpashop.domain.Member;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderItem;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

  private final MemberService memberService;
  private final ItemService itemService;
  private final OrderRepository orderRepository;

  /**
   * 주문
   * @param memberId
   * @param itemId
   * @param count
   * @return orderId
   */
  @Transactional
  public Long Order(Long memberId, Long itemId, int count){
    //엔티티 조회
    Member member = memberService.findOne(memberId);
    Item item = itemService.findOne(itemId);

    //배송정보 생성

    Delivery delivery = new Delivery();
    delivery.setAddress(member.getAddress());

    //주문상품 생성
    OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

    //주문 생성
    Order order = Order.createOrder(member, delivery, orderItem);

    //주문 저장
    //원래는 delivery도 repository 있어서 save 해서 넣어 주고 orderItem도 jpa에 넣어준 다음에 order 만들 때 넣어줘야함
    //다른 것들 저장 안해줘도 되는 이유는 바로 cascade 설정해줬기 때문.. order 엔티티 persist하면 엔티티에서 cascade걸린 것들도 persist
    //조심 해서 써야함! 여기선 오더아이템과 딜리버리를 다른데서는 참조 하지 않아서.. 즉 다른데서 안쓰기 때문에 cascade 걸어도 되는 거임!!
    //왜냐하면 지울 떄 위험하고 persist도 복잡해짐
    orderRepository.save(order);

    return order.getId();
  }

  /**
   * 주문 취소
   */
  @Transactional
  public void cancelOrder(Long orderId) {
    Order order = orderRepository.findOne(orderId);
    //밑에 처럼만 써줘도 엔티티 내부 메소드들(오더의 캔슬, 오더내부 오더아이템의 캔슬) 작동하면서 더티체킹해서 샥 바뀌니까 간단. 서비스단에서 처리했으면 다 끄집어내서 하나하나 바꿔줬을듯..?
    order.cancel();
  }

  //검색



}
