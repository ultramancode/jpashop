package com.example.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("B") //아이템의 DiscriminatorColumn에 들어갈 명칭
@Getter
@Setter //학습을 위해 열어둔 setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {

  private String author;
  private String isbn;

  @Builder
  public Book(String name, int price, int stockQuantity, String author, String isbn) {
    super(name, price, stockQuantity);
    this.author = author;
    this.isbn = isbn;
  }

}
