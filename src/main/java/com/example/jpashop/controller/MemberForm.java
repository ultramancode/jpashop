package com.example.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter //학습 목적 setter
public class MemberForm {

  @NotEmpty(message = "회원 이름은 필수입니다.")
  private String name;

  private String city;
  private String street;
  private String zipcode;
}
