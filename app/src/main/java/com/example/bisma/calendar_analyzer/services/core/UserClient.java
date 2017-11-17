package com.example.bisma.calendar_analyzer.services.core;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;



public interface UserClient {

    @FormUrlEncoded
    @POST("AndroidService/index.php")
    Call<String> login(@Field("facultyID") String userId, @Field("password") String data,
                       @Field("userType") int userType);
    @Multipart
    @POST("AndroidService/saveReport.php")
    Call<String> saveReport(@Part MultipartBody.Part filePart, @Part("userId") String des,
                            @Part("dateTime")String dateTime);


    @GET("AndroidService/getAllReports.php")
    Call<String> getAllReports();
}