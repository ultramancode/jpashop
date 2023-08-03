package com.example.jpashop.repository;

import com.example.jpashop.domain.item.Item;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

  private final EntityManager em;

  public void save(Item item) {
    if (item.getId() == null) {
      em.persist(item);
    } else {
      // 학습용 사용.. 실무에서는 사용 X, 위험.. merge는 detached상태 entity를 다시 영속화 하는데 사용.. 업데이트 느낌
      // 실무에서는 merge는 null 위험 있으니 수정 시 merge 쓰지말고, 더티체킹 이용하자
      em.merge(item);
    }
  }

  public Item findOne(Long id) {
    return em.find(Item.class, id);
  }

  public List<Item> findAll() {
    return em.createQuery("select i from Item i", Item.class)
        .getResultList();
  }


}
