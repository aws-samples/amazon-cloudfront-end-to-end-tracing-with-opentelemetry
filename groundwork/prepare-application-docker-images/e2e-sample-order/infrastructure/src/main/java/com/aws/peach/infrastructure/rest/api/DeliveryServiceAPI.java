package com.aws.peach.infrastructure.rest.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DeliveryServiceAPI {

    @POST("delivery")
    Call<DeliveryResponse> createDeliveryOrder(@Body DeliveryCreateRequest request);

}
