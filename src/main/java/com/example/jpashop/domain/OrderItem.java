package com.example.jpashop.domain;


import com.example.jpashop.domain.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter //학습용으로 열어둔 세터..!
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "item_id")//매핑을 뭘로 할 것인가~! + 외래키의 이름을 item_id 로 한다~
  private Item item;

  @ManyToOne
  @JoinColumn(name = "order_id") //매핑을 뭘로 할 것인가~! + 외래키의 이름을 order_id 로 한다~
  private Order order;

  private int orderPrice; // 주문 당시 가격
  private int count; // 주문 당시 수량



}
