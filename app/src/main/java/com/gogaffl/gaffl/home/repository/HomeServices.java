package com.gogaffl.gaffl.home.repository;

import com.gogaffl.gaffl.home.model.FindTrips;
import com.gogaffl.gaffl.home.model.SearchPlaces;
import com.gogaffl.gaffl.home.model.TripsDetailsModel;
import com.gogaffl.gaffl.profile.model.PhoneResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HomeServices {

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @GET("/api/v1/places/search_locations?")
    Call<SearchPlaces> getPlaces(@Query("term") String terms,
                                 @Header("X-User-Email") String email,
                                 @Header("X-User-Token") String token);

    ////////////////////////////////////////show trips////////////////////////////////////////
    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @GET("/api/v1/trips")
    Call<FindTrips> getTrips(@Header("Cache-Control") String cacheControl,
                             @Header("X-User-Email") String email,
                             @Header("X-User-Token") String token);


    /////////////////////////////////find trips on location////////////////////////////////

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @GET("/api/v1/trips?")
    Call<FindTrips> getTripsSpecificLocation(
            @Query("location") String terms,
            @Header("Cache-Control") String cacheControl,
            @Header("X-User-Email") String email,
            @Header("X-User-Token") String token);

    /////////////////////////////////find trips details////////////////////////////////

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @GET("/api/v1/trips/{id}")
    Call<TripsDetailsModel> getTripsDetails(
            @Path("id") int terms,
            @Header("Cache-Control") String cacheControl,
            @Header("X-User-Email") String email,
            @Header("X-User-Token") String token);

    /////////////////////////////////////accept of pending/////////////////////////////////

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @GET("/api/v1/join_requests/{id}/accept")
    Call<PhoneResponse> acceptPendingUser(
            @Path("id") int reqID,
            @Header("Cache-Control") String cacheControl,
            @Header("X-User-Email") String email,
            @Header("X-User-Token") String token);

    @Headers({
            "Accept: application/json",
            "X-Requested-With: XMLHttpRequest",
            "Content-Type: application/json"
    })
    @POST("/api/v1/join_requests/{id}/cancel")
    Call<PhoneResponse> denyPendingUser(
            @Body HashMap resonse,
            @Path("id") int reqID,
            @Header("X-User-Email") String email,
            @Header("X-User-Token") String token);
}
