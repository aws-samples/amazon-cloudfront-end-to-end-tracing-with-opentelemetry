package com.aws.peach.application.dto;

import com.aws.peach.application.DeliveryQueryService;
import com.aws.peach.application.support.DtoUtil;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DeliverySearchResponse extends DeliveryResponse {
    private List<Search> searchResult;

    public static DeliverySearchResponse of(DeliveryQueryService.SearchResult result) {
        List<Search> parsedResultList = Objects.requireNonNull(result).getMappedResultList(Search::of);
        return new DeliverySearchResponse(parsedResultList);
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Search {
        private String deliveryId;
        private Order order;
        private List<DeliveryProduct> items;
        private String status;
        private String updatedAt;

        public static Search of(com.aws.peach.domain.delivery.Delivery delivery) {
            Order order = Order.of(delivery.getOrder());
            List<DeliveryProduct> items = delivery.getItems().stream()
                    .map(DeliveryProduct::of)
                    .collect(Collectors.toList());
            return Search.builder()
                    .deliveryId(delivery.getIdString())
                    .order(order)
                    .items(items)
                    .status(delivery.getStatus().getType().name())
                    .updatedAt(DtoUtil.formatTimestamp(delivery.getStatus().getTimestamp()))
                    .build();
        }
    }
}
