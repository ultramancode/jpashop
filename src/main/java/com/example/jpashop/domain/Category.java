package com.example.jpashop.domain;

import com.example.jpashop.domain.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter //학습을 위해 열어둔 Setter
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long id;

  private String name;

  @ManyToMany //학습용으로 설정한 다대다 관계(원래는 하면 안됨.. 중간테이블 조정 못하니)

  private List<Item> items = new ArrayList<>();
}
