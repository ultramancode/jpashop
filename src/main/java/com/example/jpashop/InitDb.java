package com.example.jpashop;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Delivery;
import com.example.jpashop.domain.Member;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderItem;
import com.example.jpashop.domain.item.Book;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 총 주문 2개 *userA *JPA1 BOOK *JPA2 BOOK *userB *SPRING1 BOOK *SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class InitDb {

  private final InitService initService;

  //애플리케이션 로딩 시점에 호출
  @PostConstruct
  public void init() {
    initService.dbInit1();
    initService.dbInit2();
  }

  @Component
  @Transactional
  @RequiredArgsConstructor
  static class InitService {

    private final EntityManager em;

    public void dbInit1() {
      Member member = createMember("userA", "서울", "길음", "123-124");
      em.persist(member);

      Book book1 = createBook("JPA 연습중", 9000, 100);
      em.persist(book1);

      Book book2 = createBook("스프링 연습중", 15000, 50);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 2);

      Delivery delivery = createDelivery(member);
      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);

    }

    private static Delivery createDelivery(Member member) {
      Delivery delivery = new Delivery();
      //연습용이니 간단히
      delivery.setAddress(member.getAddress());
      return delivery;
    }


    public void dbInit2() {
      Member member = createMember("userB", "용인", "기흥", "463-124");
      em.persist(member);

      Book book1 = createBook("스프링부트", 9500, 170);
      em.persist(book1);

      Book book2 = createBook("sql", 17000, 500);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 3);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 4);

      Delivery delivery = createDelivery(member);
      Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);

    }

    private Book createBook(String name, int price, int stockQuantity) {
      return Book.builder()
          .name(name)
          .price(price)
          .stockQuantity(stockQuantity)
          .build();
    }

    private Member createMember(String name, String city, String street, String zipcode) {
      return new Member.Builder()
          .name(name)
          .address(new Address(city, street, zipcode))
//          .orders(new ArrayList<>()) //이렇게 수동으로 초기화해주면 실수할 수 있어서 빌더패턴 직접 정의해서 거기서 초기화(@Builder는 초기화 안해줌, 생성자선언만)
          .build();
    }
  }

}