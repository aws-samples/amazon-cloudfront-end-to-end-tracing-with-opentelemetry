package com.aws.peach.application.order.dto;

import com.aws.peach.domain.order.vo.ShippingInformation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.NONE)
public class ShippingDTO {
    private String city;
    private String telephone;
    private String receiver;
    private String address1;
    private String address2;

    public static ShippingDTO newInstance(ShippingInformation vo) {
        ShippingDTO dto = new ShippingDTO();
        dto.setCity(vo.getCity());
        dto.setTelephone(vo.getTelephoneNumber());
        dto.setReceiver(vo.getReceiver());
        dto.setAddress1(vo.getAddress1());
        dto.setAddress2(vo.getAddress2());
        return dto;
    }
}
