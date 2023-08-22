package com.example.jpashop.controller;

import com.example.jpashop.domain.item.Book;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping("/items/new")
  public String createForm(Model model) {
    model.addAttribute("form", new BookForm());
    return "items/createItemForm";
  }

  @PostMapping("/items/new")
  public String create(BookForm form) {

    Book book = Book.builder()
        .name(form.getName())
        .price(form.getPrice())
        .stockQuantity(form.getStockQuantity())
        .author(form.getAuthor())
        .isbn(form.getIsbn())
        .build();

    itemService.saveItem(book);
    return "redirect:/";
  }

  @GetMapping("/items")
  public String list(Model model) {
    //학습용이라 dto 안만들고 일단 엔티티 넘김 (추후 dto로)
    List<Item> items = itemService.findItems();
    model.addAttribute("items", items);
    return "items/itemList";
  }

  @GetMapping("items/{itemId}/edit")
  public String updateItemFrom(@PathVariable("itemId") Long itemId, Model model) {
    //학습 용이함 위해 (Book) 캐스팅.....
    Book book = (Book) itemService.findOne(itemId);

    //수정버튼 눌렀을 때 기존 값 입력돼있는 상태로 떠야하니
    BookForm form = new BookForm();
    form.setId(book.getId());
    form.setName(book.getName());
    form.setPrice(book.getPrice());
    form.setStockQuantity(book.getStockQuantity());
    form.setAuthor(book.getAuthor());
    form.setIsbn(book.getIsbn());

    model.addAttribute("form", form);
    return "items/updateItemForm";
  }

  @PostMapping("items/{itemId}/edit")
  public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {

//    Book book = Book.builder()
//        .name(form.getName())
//        .price(form.getPrice())
//        .stockQuantity(form.getStockQuantity())
//        .author(form.getAuthor())
//        .isbn(form.getIsbn())
//        .build();
//book.. 객체는 새로운 객체지만 id가 세팅 돼있는.. 준영속!!
//db가 한번 갔다온 상태로.. 식별자가 데이터베이스에 있는 애니까 ..
//jpa가 식별할 수 있는 id 가지고 있으니
//    book.setId(form.getId());
//
//    itemService.saveItem(book);

//dto로 보내는게 낫지만 학습 목적이니 그냥 바로 날림
    itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
    return "redirect:/items";

  }
}
