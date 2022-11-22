package com.aws.peach.infrastructure.aurora;

import com.aws.peach.domain.order.entity.Orders;
import com.aws.peach.domain.order.repository.OrderRepository;
import com.aws.peach.domain.order.vo.OrderNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public interface OrderAuroraRepository extends OrderRepository, JpaRepository<Orders, OrderNumber> {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    default OrderNumber nextOrderNo() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        int randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
        String number = String.format("%s-%d", FORMATTER.format(now), randomNo);
        return new OrderNumber(number);
    }
}
