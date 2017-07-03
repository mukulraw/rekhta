package com.example.mukul.rekhta;

import com.example.mukul.rekhta.POJO.dataBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface AllAPIs {

    @GET("api/AppApi/GetContentById")
    Call<dataBean> getData(
            @Query("lang") String lang,
            @Query("contentId") String cid
    );

}
