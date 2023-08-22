package com.example.jpashop.domain;


import com.example.jpashop.domain.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter //학습용으로 열어둔 세터..!
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")//매핑을 뭘로 할 것인가~! + 외래키의 이름을 item_id 로 한다~
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id") //매핑을 뭘로 할 것인가~! + 외래키의 이름을 order_id 로 한다~
  @JsonIgnore //학습용

  private Order order;

  private int orderPrice; // 주문 당시 가격
  private int count; // 주문 당시 수량

  /**
   * 생성 메소드 item에 있는 orderPrice 안쓰고 따로 받는 이유는 할인 등으로 값이 바뀔 수도 있으니 따로..
   */

  public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
    OrderItem orderItem = new OrderItem();
    orderItem.setItem(item);
    orderItem.setOrderPrice(orderPrice);
    orderItem.setCount(count);

    item.removeStock(count);
    return orderItem;

  }

  //==비즈니스 로직==//
  public void cancel() {
    getItem().addStock(count);
  }

  public int getTotalPrice() {
    return getOrderPrice() * getCount();
  }
}
