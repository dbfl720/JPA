package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

// 화면에 dependece 한건데, 로직은 복잡한 경우 이렇게 따로 빼는게 좋음
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    // 재사용성은 안됨
    // 장점은 성능 최적화가 v3보다는 좋다.
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                "from Order o " +
                                "join o.member m " +
                                "join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();


    }
}
