package com.gogaffl.gaffl.notification.service;

import com.gogaffl.gaffl.notification.model.NotificationModel;
import com.gogaffl.gaffl.tools.AppConstants;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface MyWebService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL_NOTIFICATION)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @GET(AppConstants.NOTIFICATION_FEED)
    Call<NotificationModel> notifyItems(@Header("Cache-Control") String cacheControll,
                                        @Header("X-User-Email") String email,
                                        @Header("X-User-Token") String token);

    @GET(AppConstants.NOTIFICATION_FEED+"{id}/mark_as_read")
    Call<String> feedId(
            @Header("Cache-Control") String cacheControll,
            @Header("X-User-Email") String email,
            @Header("X-User-Token") String token,
            @Path("id") String number
    );



   /* @GET("{number}")
    Call<String> trivia(@Path("number") String number);

    @GET("{month}/{day}/date")
    Call<String> date(@Path("month") String month, @Path("day") String day);

    @GET("{random}/date")
    Call<String> randomDate(@Path("random") String random);

    @GET("{random}/trivia")
    Call<String> randomTrivia(@Path("random") String random);

    @GET("{random}/year")
    Call<String> randomYear(@Path("random") String random);

    @GET("{random}/math")
    Call<String> randomMath(@Path("random") String random);
*/

}
