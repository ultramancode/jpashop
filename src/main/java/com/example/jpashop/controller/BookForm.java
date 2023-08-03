package com.example.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter //학습 목적 setter
public class BookForm {

  //4개는 아이템에 있던 공통~
  private Long id;
  private String name;
  private int price;
  private int stockQuantity;

  private String author;
  private String isbn;
}
