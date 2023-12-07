package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Delievery {
    @Id @GeneratedValue
    @Column( name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private  Address address;

    @Enumerated(EnumType.STRING) // 꼭 string으로 쓰자.
    private DeliveryStatus status; // ready, comp
}
