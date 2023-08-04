package com.example.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter // 학습용으로 열어둔 세터..!
@Table(name = "orders") //order 디비 예약어니까
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id") //매핑을 뭘로 할 것인가~! + 외래키의 이름을 member_id 로 한다~
  private Member member;

  // 양방향 맺을 필요 없지만, 학습용으로 맺어둠
  // 나는 orderItem테이블의 order 필드에 의해서 매핑된거야..!
  // 나는 그냥 읽기 전용 거울일 뿐이다. 여기다 값 넣어도 외래키 값이 변경되지는 않는다.
  // orderItem의 오더가 연관관계 주인이다!
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //학습을 위한 양방향 관계 설정
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "delivery_id")
  @JsonIgnore //학습용
  private Delivery delivery;

  private LocalDateTime orderDate; //주문시간

  //이넘타입은 @Enumerated + 꼭 String으로!!ㄹㄹ
  //ordinal로 쓰면 중간에 뭐 끼는 순간 순서 밀리고 장애 난다!!
  @Enumerated(EnumType.STRING)
  private OrderStatus status; //주문상태 [ORDER,CANCEL]

  /**
   * 연관관계 편의 메소드
   */
  public void addMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  /**
   * 연관관계 편의 메소드
   */
  public void addDelivery(Delivery delivery) {
    this.delivery = delivery;
    //OneToOne이니 get 후에 할 필요 없이 바로
    delivery.setOrder(this);
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  /**
   * 생성 메소드, 생성 복잡한 경우 .. 여기만 손 보면 해결 되도록
   * ... 가변 인자는 제일 뒤에
   */
  public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
    Order order = new Order();
    order.addMember(member);
    order.addDelivery(delivery);
    for (OrderItem orderItem : orderItems) {
      order.addOrderItem(orderItem);
    }
    order.setStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());
    return order;
  }
//==비즈니스 로직==//

  /**
   * 주문 취소
   */
  public void cancel() {
    if (delivery.getStatus() == DeliveryStatus.COMP) {
      throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
    }

    this.setStatus(OrderStatus.CANCEL);
    for (OrderItem orderItem : orderItems) {
      orderItem.cancel();
    }
  }
//==조회 로직==//

  /**
   * 전체 주문 가격 조회
   */
  public int getTotalPrice() {
//    int totalPrice = 0;
//    for (OrderItem orderItem : orderItems) {
//      totalPrice += orderItem.getTotalPrice();
//    }
//    return totalPrice;
    //위와 동일
    return orderItems.stream()
        .mapToInt(OrderItem::getTotalPrice)
        .sum();
  }
}



