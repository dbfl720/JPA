package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 화면에 돌아가는 api에 근접해서 따로 분리해서 씀
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

//    public List<OrderQueryDto> findOrderQueryDtos(){
//        List<OrderQueryDto> result = findOrders(); // query 1qjs -> N개
//
//        result.forEach(o -> {
//            List<OrderItemQueryDto> orderitems =  findOrderItems(o.getOrderId()); // 컬렉션 부분을 직접 채워주고 있다.
//            o.setOrderItems(orderitems);
//
//        });
//        return result;
//    }


    // query 2번을 가능하게 됨
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();


        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;

    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // map으로 바꾸기 - 편하게 하기 위해서
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds =  result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }


//    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
//        return em.createQuery(
//                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
//                        " from OrderItem oi" +
//                        " join oi.item i" +
//                        " where oi.order.id = :orderId", OrderItemQueryDto.class)
//                .setParameter("orderId", orderId)
//                .getResultList();
//
//
//    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }


    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();

    }
}
