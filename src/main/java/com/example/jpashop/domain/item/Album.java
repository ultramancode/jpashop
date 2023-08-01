package com.example.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter //학습을 위해 열어둔 Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album extends Item{

  public String artist;
  public String etc;
}
