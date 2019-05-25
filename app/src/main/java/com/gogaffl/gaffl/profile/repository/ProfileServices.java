package com.gogaffl.gaffl.profile.repository;

import com.gogaffl.gaffl.profile.model.CountryList;
import com.gogaffl.gaffl.profile.model.Example;
import com.gogaffl.gaffl.profile.model.PhoneResponse;
import com.gogaffl.gaffl.profile.model.ProfileResponse;
import com.gogaffl.gaffl.profile.model.StateList;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ProfileServices {

    // Get interest lists

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @GET("/api/v1/interests")
    Call<Example> getInterests(@Header("X-User-Email") String email, @Header("X-User-Token") String token);

    // send interest data to the server

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @POST("/api/v1/interests/add_to_user")
    Call<Void> insertInterestData(@Header("X-User-Email") String email,
                                  @Header("X-User-Token") String token,
                                  @Body HashMap interest);

    //Check if phone number already exist

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @POST("/api/v1/phone_verifications/check_number")
    Call<PhoneResponse> checkPhone(@Header("X-User-Email") String email,
                                   @Header("X-User-Token") String token,
                                   @Body HashMap interest);

    // Send phone data to server

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @POST("/api/v1/phone_verifications")
    Call<PhoneResponse> postPhone(@Header("X-User-Email") String email,
                                  @Header("X-User-Token") String token,
                                  @Body HashMap interest);

    // Get Profile data

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @GET("/api/v1/profile")
    Call<ProfileResponse> getProfile(
            @Header("Cache-Control") String cacheControll,
            @Header("X-User-Email") String email,
            @Header("X-User-Token") String token);
    ////////////////////////////////////////country////////////////////////////////////
    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @GET("/api/v1/countries")
    Call<CountryList> getCountry(@Header("X-User-Email") String email,
                                 @Header("X-User-Token") String token);

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @GET("/api/v1/countries/{id}/states")
    Call<StateList> getState(@Path("id") int countryCode,
                             @Header("X-User-Email") String email,
                             @Header("X-User-Token") String token);

    ///////////////////////////////////////////PUT////////////////////////////////////
    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest"
    })
    @Multipart
    @POST("/api/profile")
    Call<PhoneResponse> fileUpload(
            @Header("authorization") String token,
            @PartMap() Map<String, RequestBody> params,
            @Part MultipartBody.Part file);

    ///////////////////////////////////////////////////////

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest"
    })
    @PUT("/api/v1/users/{id}")
    Call<PhoneResponse> stringUpload(
            @Header("X-User-Email") String email,
            @Header("X-User-Token") String token,
            @Body HashMap params,
            @Path("id") int id);

    ///////////////////////post pic////////////////////////
//    @Headers({
//            "Accept: application/json",
//            "Cache-Control: max-age=640000",
//            "X-Requested-With: XMLHttpRequest"
//    })
//    @Multipart
//    @PUT("/api/v1/users/{id}")
//    Call<PhoneResponse> uploadPic(
//            @Header("X-User-Email") String email,
//            @Header("X-User-Token") String token,
//            @Part MultipartBody.Part file,
//            @Path("id") int id);

    @Headers({
            "Accept: application/json",
    })
    @PUT("/api/v1/users/{id}")
    Call<PhoneResponse> uploadPic(
            @Header("X-User-Email") String email,
            @Header("X-User-Token") String token,
            @Body RequestBody body,
            @Path("id") int id);
}
