package com.example.jpashop.service;

import com.example.jpashop.domain.item.Book;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;

  //서비스 단에서 지금 있는 기능정도로 사용할거면 차라리 컨트롤러에서 바로 레포지토리 것을 불러와도 무방할 것 같음
  @Transactional //서비스 단에 readOnly 해놨으니 세이브 하려면 따로 달고
  public void saveItem(Item item) {
    itemRepository.save(item);
  }

  @Transactional
  public void updateItem(Long itemId, String name, int price, int stockQuantity) {
    Item findItem = itemRepository.findOne(itemId);
    //원래는 change메소드 만들어서 수정해야함~ set으로 하면 추후 어디서 바꾸는지 추적 안되니 쓰면 안됨!
    //지금은 학습용이니..
    findItem.setPrice(price);
    findItem.setName(name);
    findItem.setStockQuantity(stockQuantity);
  }

  public List<Item> findItems() {
    return itemRepository.findAll();
  }

  public Item findOne(Long itemId) {
    return itemRepository.findOne(itemId);
  }

}
