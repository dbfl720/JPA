package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor // @RequiredArgsConstructor는 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.
public class OrderSimpleAipController {

        private final OrderRepository orderRepository;
        private final OrderSimpleQueryRepository orderSimpleQueryRepository;


        @GetMapping("/api/v2/simple-orders")
        public List<SimpleOrderDto> ordersV2() {

                // ORDER 2개
                // N + 1 -> 1 + 회원N + 배송N
                List<Order> orders = orderRepository.findAllByString(new OrderSearch());
                List<SimpleOrderDto> result = orders.stream()
                        .map(o -> new SimpleOrderDto(o))
                        .collect(Collectors.toList());

                return result;
        }



//        @GetMapping("/api/v3/simple-orders")
//        public List<SimpleOrderDto> ordersV3() {
//
//                List<Order> orders = orderRepository.findAllWithMemberDelivery();
//                List<SimpleOrderDto> result = orders.stream()
//                        .map(o -> new SimpleOrderDto(o))
//                        .collect(Collectors.toList());;
//
//                return result;
//        }




        @GetMapping("/api/v4/simple-orders")
        public List<OrderSimpleQueryDto> ordersV4() {
                return orderSimpleQueryRepository.findOrderDtos();
        }



        @Data
        static class SimpleOrderDto {
                private Long orderId;
                private String name;
                private LocalDateTime orderDate;
                private OrderStatus orderStatus;
                private Address address;

                public SimpleOrderDto(Order order) {
                        orderId = order.getId();
                        name = order.getMember().getName(); // Lazy 초기화
                        orderDate = order.getOrderDate();
                        orderStatus = order.getStatus();
                        address = order.getDelivery().getAddress(); // Lazy 초기화
                }
        }
}
