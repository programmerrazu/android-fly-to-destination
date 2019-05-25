package com.gogaffl.gaffl.authentication.helper;

import com.gogaffl.gaffl.authentication.model.AuthResponse;
import com.gogaffl.gaffl.authentication.model.LinkedinEmail;
import com.gogaffl.gaffl.authentication.model.LinkedinPic;
import com.gogaffl.gaffl.authentication.model.LinkedinUser;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by M. Onirban on 06-Mar-19.
 * Time: 13: 07
 * Project: GAFFL.
 * Email: onirban27@gmail.com
 */
public interface AuthService {

    // register

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest"
    })
    @Multipart
    @POST("/api/v1/registrations")
    Call<AuthResponse> registerUser(
            @PartMap() Map<String, RequestBody> params,
            @Part MultipartBody.Part file);


    // Log in

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest"
    })
    @Multipart
    @POST("/api/v1/sessions")
    Call<AuthResponse> authUser(@PartMap() Map<String, RequestBody> loginmap);


    // Social Sign up

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest"
    })
    @Multipart
    @POST("/api/v1/social_omniauth_callbacks")
    Call<AuthResponse> socialRegisterUser(@PartMap() Map<String, RequestBody> registerMap);

    // Social Sign in

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest"
    })
    @Multipart
    @POST("/api/v1/social_omniauth_callbacks")
    Call<AuthResponse> socialauthUser(@PartMap() Map<String, RequestBody> loginMap);

    @Headers({
            "Accept: application/json"
    })
    @GET("/v2/me?")
    Call<LinkedinUser> getLinkedinUserProfile(@Query("oauth2_access_token") String token);


    //get pic url linkedin
    @Headers({
            "Accept: application/json"
    })
    @GET("/v2/me?")
    Call<LinkedinPic> getLinkedinUserPic(
            @Header("Authorization") String token,
            @Query("projection") String projection);

    //get email linkedin
    @Headers({
            "Accept: application/json"
    })
    @GET("/v2/emailAddress?")
    Call<LinkedinEmail> getLinkedinUserEmail(
            @Header("Authorization") String token,
            @Query("q") String members,
            @Query("projection") String projection);
}
