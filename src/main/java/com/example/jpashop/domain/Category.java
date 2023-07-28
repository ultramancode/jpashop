package com.example.jpashop.domain;

import com.example.jpashop.domain.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
  @JoinTable(
      name = "category_item",
      joinColumns = @JoinColumn(name = "category_id"),
      inverseJoinColumns = @JoinColumn(name = "item_id")
  )
  private List<Item> items = new ArrayList<>();

  //이건 카테고리 내부에서 새로 만드는 거니까 ToOne이 부모~
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Category parent;
  
  @OneToMany(mappedBy = "parent")
  private List<Category> child = new ArrayList<>();

  /**
   * 연관관계 편의 메소드
   */

  //Category 타입
  public void addParentCategory(Category parent) {
    this.parent = parent;
    parent.getChild().add(parent);
  }


}
