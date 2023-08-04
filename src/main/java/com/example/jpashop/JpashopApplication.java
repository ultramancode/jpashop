package com.example.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule.Feature;
import org.hibernate.Hibernate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

  public static void main(String[] args) {
    SpringApplication.run(JpashopApplication.class, args);
  }

  //학습용.. 실제로는 엔티티 외부 노출할 일 없으니 안쓸듯
  //부트 3.0 이상은 Jakarta붙힌 걸로!
  @Bean
  Hibernate5JakartaModule hibernate5Module() {
    Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
    //강제 지연 로딩 설정(학습용이니 써보는거지 실제로는 쓰면 안됨. lazy 로딩 애들 전부 다 긁어와서 성능 저하됨)
    hibernate5JakartaModule.configure(Feature.FORCE_LAZY_LOADING, true);
    return hibernate5JakartaModule;
  }
}

