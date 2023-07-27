package com.example.jpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter // 학습용으로 열어둔 세터..!
@Table(name = "orders") //order 디비 예약어니까
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id") //매핑을 뭘로 할 것인가~! + 외래키의 이름을 member_id 로 한다~
  private Member member;

  // 양방향 맺을 필요 없지만, 학습용으로 맺어둠
  // 나는 orderItem테이블의 order 필드에 의해서 매핑된거야..!
  // 나는 그냥 읽기 전용 거울일 뿐이다. 여기다 값 넣어도 외래키 값이 변경되지는 않는다.
  // orderItem의 오더가 연관관계 주인이다!
  @OneToMany(mappedBy = "order") //학습을 위한 양방향 관계 설정
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  private LocalDateTime orderDate; //주문시간

  //이넘타입은 @Enumerated + 꼭 String으로!!
  //ordinal로 쓰면 중간에 뭐 끼는 순간 순서 밀리고 장애 난다!!
  @Enumerated(EnumType.STRING)
  private OrderStatus status; //주문상태 [ORDER,CANCEL]







}
