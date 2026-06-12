package com.nurul.nutrigo.data.remote;

import com.nurul.nutrigo.data.model.EdamamResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EdamamService {

    @GET("api/food-database/v2/parser")
    Call<EdamamResponse> getNutrition(
            @Query("app_id") String appId,
            @Query("app_key") String appKey,
            @Query("ingr") String query
    );
}
