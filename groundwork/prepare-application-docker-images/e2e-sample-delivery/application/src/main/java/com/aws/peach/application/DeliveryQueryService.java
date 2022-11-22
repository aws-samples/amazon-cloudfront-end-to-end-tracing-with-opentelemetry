package com.aws.peach.application;

import com.aws.peach.application.dto.DeliveryDetailResponse;
import com.aws.peach.domain.delivery.*;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional(readOnly = true)
public class DeliveryQueryService {

    private final DeliveryRepository repository;

    public DeliveryQueryService(DeliveryRepository repository) {
        this.repository = repository;
    }

    public Optional<DeliveryDetailResponse> getDelivery(DeliveryId deliveryId) {
        Optional<Delivery> delivery = this.repository.findById(deliveryId.getValue());
        return delivery.map(DeliveryDetailResponse::of);
    }

    public Optional<DeliveryDetailResponse> getDelivery(String orderNo) {
        Optional<Delivery> delivery = this.repository.findByOrderNo(orderNo);
        return delivery.map(DeliveryDetailResponse::of);
    }

    public SearchResult search(SearchCondition condition) {
        Iterable<Delivery> deliveries;
        PageRequest pageRequest = PageRequest.of(condition.pageNo, condition.pageSize);
        if (condition.getState().isPresent()) {
            DeliveryStatus.Type status = condition.getState().get().type;
            deliveries = this.repository.findAllByStatus(status, pageRequest);
        } else {
            deliveries = this.repository.findAll(pageRequest);
        }
        return new SearchResult(deliveries);
    }

    public static class SearchCondition {
        private final int pageNo;
        private final int pageSize;
        private final State state;

        public static boolean isValidState(String state) {
            return State.isValidState(state);
        }

        private static State resolveState(String state) {
            return (state == null || state.isEmpty()) ? null : State.of(state);
        }

        public SearchCondition(int pageNo, int pageSize, String state) {
            this(pageNo, pageSize, resolveState(state));
        }

        private SearchCondition(int pageNo, int pageSize, State state) {
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.state = state;
        }

        public int getPageNo() {
            return pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public Optional<State> getState() {
            return Optional.ofNullable(state);
        }

        @Getter(AccessLevel.PRIVATE)
        private enum State {
            PREPARING("preparing", DeliveryStatus.Type.PREPARING),
            SHIPPED("shipped", DeliveryStatus.Type.SHIPPED);

            private final String val;
            private final DeliveryStatus.Type type;

            State(String val, DeliveryStatus.Type type) {
                this.val = val;
                this.type = type;
            }

            private static final Map<String, State> VAL_TO_STATE;
            static {
                VAL_TO_STATE = Arrays.stream(State.values())
                        .collect(Collectors.toMap(State::getVal, Function.identity()));
            }

            private static State of(final String state) {
                return VAL_TO_STATE.get(state.toLowerCase(Locale.ROOT));
            }

            private static boolean isValidState(final String state) {
                if (state == null) {
                    return false;
                }
                return VAL_TO_STATE.containsKey(state.toLowerCase(Locale.ROOT));
            }
        }
    }

    @Getter(AccessLevel.PRIVATE)
    public static class SearchResult {
        private final List<Delivery> result;

        public SearchResult() {
            this(Collections.emptyList());
        }

        public SearchResult(Iterable<Delivery> result) {
            this.result = StreamSupport.stream(result.spliterator(), false)
                    .collect(Collectors.toList());
        }

        public <T> List<T> getMappedResultList(Function<Delivery, ? extends T> mapper) {
            return getResult().stream()
                    .map(mapper)
                    .collect(Collectors.toList());
        }
    }
}
