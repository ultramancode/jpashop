package com.example.jpashop.domain.item;

import com.example.jpashop.domain.Category;
import com.example.jpashop.exception.NotEnoughStockException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
//JPA 상속관계 매핑. 지금은 싱글테이블 전략!
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter //학습을 위해 열어둔 Setter
//아이템 자체 객체 만들 일 없고 얠 상속 받는 애들만 있으니 추상화클래스로~
public abstract class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Long id;

  private String name;
  private int price;
  private int stockQuantity;
  @ManyToMany(mappedBy = "items") //학습용으로 설정한 다대다 관계(원래는 하면 안됨.. 중간테이블 조정 못하니). 중간테이블은 카테고리 클래스에 입력!
  private List<Category> categories = new ArrayList<>();

  //==비즈니스 로직==//
  //도메인주도 설계.. 엔티티 자체가 해결할 수 있는 것은 엔티티 안에서 해결하는게 좋다!!
  //객체 지향적으로 데이터를 가지고 있는 곳에 비즈니스 메서드가 있는게 가장 좋음 => 응집력 up!!
  //필드값들 바깥에서 세터로 바꾸는 그런 짓 하지말고.. 이 안에서 핵심 비즈니스 로직으로 바꾸는 객체 지향적 설계해라
  /**
   * stock 증가
   */
  public void addStock(int quantity) {
    this.stockQuantity += quantity;
  }
  /**
   * stock 감소
   */
  public void removeStock(int quantity){
   int restStock = this.stockQuantity - quantity;
   if(restStock < 0 ){
     throw new NotEnoughStockException("재고가 부족합니다");
   }

   this.stockQuantity = restStock;
  }

}
