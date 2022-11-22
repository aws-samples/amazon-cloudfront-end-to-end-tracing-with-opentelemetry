package com.aws.peach.infrastructure.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderData {
    private String orderNo;
    private String delId;
    private List<Item> items;
    private String custId;
    private String custNm;
    private String status;
    private Instant crtAt;
    private Address shipTo;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Item {
        private long id;
        private String prdId;
        private String prdNm;
        private int prdPrice;
        private int qty;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Address {
        private String city;
        private String tel;
        private String name;
        private String addr1;
        private String addr2;
    }
}
