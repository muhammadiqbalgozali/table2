package com.example.ilgozali.table_pizzana.api;

import com.example.ilgozali.table_pizzana.model.ModelData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiService {
    @GET("pesanan")
    Call<List<ModelData>> getDataTable();

    @FormUrlEncoded
    @POST("pesanan")
    Call<ModelData> postData(@Field("status") String status);

}
