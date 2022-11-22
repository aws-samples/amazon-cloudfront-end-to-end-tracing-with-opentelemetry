package com.aws.peach.application.order.dto;

import com.aws.peach.domain.order.vo.OrderLine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.NONE)
public class OrderLineDTO {
    private static final String PRODUCT_NAME_AND_QUANTITY_FORMAT = " %s(%s)";

    private Long id;
    private String productId;
    private String productName;
    private int productPrice;
    private int quantity;

    public int calculateAmounts() {
        return getProductPrice() * getQuantity();
    }

    public String getProductNameAndQuantity() {
        return String.format(PRODUCT_NAME_AND_QUANTITY_FORMAT, getProductName(), getQuantity());
    }

    public static OrderLineDTO newInstance(OrderLine entity) {
        OrderLineDTO dto = new OrderLineDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getOrderProduct().getProductId());
        dto.setProductName(entity.getOrderProduct().getProductName());
        dto.setProductPrice(entity.getOrderProduct().getPrice());
        dto.setQuantity(entity.getQuantity());
        return dto;
    }
}
