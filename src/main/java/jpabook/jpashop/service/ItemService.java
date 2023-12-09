package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }


    //*** 트랜잭선 안에서 entity를 조회해야 영속상 상태에서 조회가되고, 여기서 값을 변경해야,더티체킹 (변경감지)가 일어날 수 있다. 트랜잭선
    // 커밋할때 flush가 일어나면서 변경감지가 되면서 db에 업데이트 쿼리가 날라간다.
    //** 좋은 설계 예시
    //** 되도록 setter없이 엔티티 안에서 추적할 수 있는 메서드를 만들어라!!!
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }




    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }


}
