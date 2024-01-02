package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

// 순수한 entity를 조회하는 용도
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public Order findAll(OrderSearch orderSearch){
        return em.find(Order.class, orderSearch);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
//language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
//주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
//회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }




    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
//주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
//회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName()
                            + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000

        return query.getResultList();
    }




    // **** fetch join : 한번의 쿼리로 모든 쿼리르 조인 후, select절에 다 넣고, 한번에 다 땡겨오기 // JPA에만 있음
    // 90%는 fetch join으로 성능 문제 해결.
    // 외부 (Order)를 건드리지 않는 상태로 내부를 조인해서 가져오는 방식 (fetch join)
    // 재사용성이 됨
    // entity를 조회했기 때문에 비지니스 로직을 써서 데이터를 변경하는것이 가능
    // **** 쿼리 방식 선택 권장됨 !  - many to one, one to one
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    // fetch join !!!   ** 1 : N 을 패치조인할 경우에 페이징 불가능하다 - 데이터가 뻥튀기 되기 때문에 ..  // 1개만 사용해야됨
    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o " +
                            "join fetch o.member m " +
                            "join fetch o.delivery d " +
                            "join fetch o.orderItems oi " +
                            "join fetch oi.item i", Order.class)
                .setFirstResult(1)  // 페이징 조건
                .setMaxResults(100) // 페이징 조건
                .getResultList();
    }
}


