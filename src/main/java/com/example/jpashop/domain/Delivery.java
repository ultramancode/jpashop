package com.example.jpashop.domain;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter //학습을 위해 열어둔 Setter
public class Delivery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "deliver_id")
  private Long id;

  // 나는 order테이블의 delivery 필드에 의해서 매핑된거야..!
  // 나는 그냥 읽기 전용 거울일 뿐이다. 여기다 값 넣어도 외래키 값이 변경되지는 않는다.
  // 오더의 delivery가 연관관계 주인이다!
  @OneToOne(mappedBy = "delivery")
  private Order order;

  @Embedded
  private Address address;

  //이넘타입은 @Enumerated + 꼭 String으로!!
  //ordinal로 쓰면 중간에 뭐 끼는 순간 순서 밀리고 장애 난다!!
  @Enumerated(EnumType.STRING)
  private DeliveryStatus status; //READY, COMP


}
