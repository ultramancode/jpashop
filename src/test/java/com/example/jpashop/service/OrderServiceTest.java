package com.example.jpashop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Member;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.domain.item.Book;
import com.example.jpashop.exception.NotEnoughStockException;
import com.example.jpashop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

  @Autowired
  EntityManager em;
  @Autowired
  OrderService orderService;
  @Autowired
  OrderRepository orderRepository;
  @Autowired
  MemberService memberService;


  @Test
  @DisplayName("상품주문")
  public void 상품주문() throws Exception {
    //given
    Address address = createAddress("용인", "34번길", "123-123");

    Member member = createMember("회원1", address);

    Book book = createBook("스프링", 10000, 10);

    int orderCount = 2;
    //when
    Long orderId = orderService.createOrder(member.getId(), book.getId(), orderCount);

    //then
    Order getOrder = orderRepository.findOne(orderId);

    assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
    assertThat(getOrder.getOrderItems().size()).isEqualTo(1);
    assertThat(getOrder.getTotalPrice()).isEqualTo(10000 * orderCount);
    assertThat(book.getStockQuantity()).isEqualTo(8);


  }

  @Test
  @DisplayName("상품주문_재고수량초과_예외")

  public void 상품주문_재고수량초과_예외() throws Exception {
    //given
    Address address = createAddress("용인", "모름길", "1111-111");
    Member member = createMember("웅", address);
    Book book = createBook("스프링", 10000, 10);

    int orderCount = 11;

    //then, when
    assertThrows(NotEnoughStockException.class,
        () -> orderService.createOrder(member.getId(), book.getId(), orderCount));
  }

  @Test
  @DisplayName("주문취소")
  public void 주문취소() throws Exception {
    //given
    Address address = createAddress("용인", "모름길", "1111-111");
    Member member = createMember("웅", address);
    Book book = createBook("스프링", 10000, 10);

    int orderCount = 3;

    Long orderId = orderService.createOrder(member.getId(), book.getId(), orderCount);

    //when
    orderService.cancelOrder(orderId);
    //then
    Order getOrder = orderRepository.findOne(orderId);
    assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);

  }

  private Book createBook(String name, int price, int quantity) {
    Book book = Book.builder()
        .name(name)
        .price(price)
        .stockQuantity(quantity)
        .build();
    em.persist(book);
    return book;
  }

  private Member createMember(String name, Address address) {
    Member member = new Member.Builder()
        .name(name)
        .address(address)
        //빌더패턴쓰면 필드초기화 안돼서 수동으로 초기화..
        //@Builder를 사용하면 해당 필드는 초기화되지 않는다. + 참고로 @noargs에서 force로 강제 기본생성자 생성할 때도 null이 들어가버림
        //@Builder가 아닌 직접 빌더패턴 만들 때 초기화 시켜버렸음 지금은!
        .orders(new ArrayList<>())
        .build();

    em.persist(member);
    return member;
  }

  private static Address createAddress(String city, String street, String zipcode) {
    Address address = Address.builder()
        .city(city)
        .street(street)
        .zipcode(zipcode)
        .build();
    return address;
  }

}