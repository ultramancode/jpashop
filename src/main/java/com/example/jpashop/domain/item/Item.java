package com.example.jpashop.domain.item;

import com.example.jpashop.domain.Category;
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



}
