package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Transactional
public class OrderServiceTest {


    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;



    @Test
    public void 상품주문() throws Exception {
        //given - 이런게 주어졌을 때
        Member member = new Member();
        member.setName("시골 JPA");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);

        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;


        //when - 이렇게 하면
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then - 이렇게 된다.
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
    }




    @Test
    public void 주문취소() throws Exception {
        //given - 이런게 주어졌을 때


        //when - 이렇게 하면


        //then - 이렇게 된다.

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        //given - 이런게 주어졌을 때


        //when - 이렇게 하면


        //then - 이렇게 된다.

    }

}