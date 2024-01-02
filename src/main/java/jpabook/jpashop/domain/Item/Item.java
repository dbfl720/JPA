package jpabook.jpashop.domain.Item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {      // 구현체를 가지고 있기 때문에 추상클래스로 함.

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;


    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    //== 비지니스 로직 : 데이터를 가지고 있는 쪽에 비지니스 로직을 넣는게 가장 좋다!! 그래야 응집력이 있다  ==//
    // 변경해야 될 일이 있으면  setter로 하는게 아니라, 핵심 비지니스 로직을 가지고 하는게 좋다.
    //** 되도록 setter없이 엔티티 안에서 추적할 수 있는 메서드를 만들어라!!!
    /**
     * stock 증가
     * @param quantity
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     * @param quantity
     */
    public void removeStock(int quantity){
       int restStock = this.stockQuantity - quantity;
       if (restStock < 0) {
           throw new NotEnoughStockException("need more stock");
       }
       this.stockQuantity = restStock;
    }
}
