package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter // setter는 쓰지 않음.
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {  // protected로 생성해서, 손대지 말자 라는 뚯.
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}