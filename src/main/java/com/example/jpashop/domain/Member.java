package com.example.jpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter // 학습용으로 열어둔 setter..!!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  private String name;
  @Embedded //여기에 임베디드 쓰거나 혹은 어드레스 클래스에 임베더블 둘 중 하나만 해줘도 되긴 하는데 명시적으로 둘 다 쓰는게 나음
  //딱봐도 내장타입이다 아니다 인식할 수 있도록
  private Address address;

  // 양방향 맺을 필요 없지만, 학습용으로 맺어둠
  // 나는 order테이블의 member 필드에 의해서 매핑된거야..!
  // 나는 그냥 읽기 전용 거울일 뿐이다. 여기다 값 넣어도 외래키 값이 변경되지는 않는다.
  // order의 멤버가 연관관계 주인이다!
  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();

}
