package com.gogaffl.gaffl.home.repository;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.gogaffl.gaffl.home.model.FindTrips;
import com.gogaffl.gaffl.home.model.SearchPlaces;
import com.gogaffl.gaffl.home.model.Trips;
import com.gogaffl.gaffl.home.view.HomeActivity;
import com.gogaffl.gaffl.profile.model.PhoneResponse;
import com.gogaffl.gaffl.rest.RetrofitInstance;

import java.util.ArrayList;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private MutableLiveData<ArrayList<String>> placesDataMutableLiveData;
    private MutableLiveData<Boolean> dataIsLoaded;
    private Call<PhoneResponse> call;
    private Map<String, RequestBody> params;

    private Application application;
    private HomeServices service;
    private MutableLiveData<ArrayList<Trips>> tripsMutableLiveData;
    private MutableLiveData<Boolean> tripdataIsLoaded;

    //constructor to initialize all the objects
    public HomeRepository(Application application) {
        this.application = application;

        //initializing the data models
        if (placesDataMutableLiveData == null) {
            placesDataMutableLiveData = new MutableLiveData<>();
            dataIsLoaded = new MutableLiveData<>();
        }

        if (tripsMutableLiveData == null) {
            tripsMutableLiveData = new MutableLiveData<>();
            tripdataIsLoaded = new MutableLiveData<>();
        }

        service = RetrofitInstance.getRetrofitInstance().
                create(HomeServices.class);
    }

    public MutableLiveData<ArrayList<String>> getPlacesMutableLiveData(String searchText) {


        HomeServices service = RetrofitInstance.getRetrofitInstance().
                create(HomeServices.class);
        Call<SearchPlaces> getPlacesCall = service.getPlaces(searchText, "john@local.com", "VGbp9W6tWSQHyHZTas7g");

        getPlacesCall.enqueue(new Callback<SearchPlaces>() {
            @Override
            public void onResponse(Call<SearchPlaces> call, Response<SearchPlaces> response) {
                if (response.code() == 200) {
                    //inserting data in the post list object
                    SearchPlaces searchPlaces = response.body();
                    boolean found = response.body().isFound();
                    if (!found) {
                        Toasty.warning(application, "Enter valid location!", Toasty.LENGTH_SHORT).show();
                    }
                    if (searchPlaces.getResults() != null) {
                        ArrayList<String> placesname = searchPlaces.getResults();

                        placesDataMutableLiveData.setValue(placesname);

                    }

                }
            }

            @Override
            public void onFailure(Call<SearchPlaces> call, Throwable t) {
                Toast.makeText(application, "Data loading failed!", Toast.LENGTH_SHORT).show();
            }
        });


        return placesDataMutableLiveData;
    }


    public MutableLiveData<ArrayList<Trips>> getTripsMutableLiveData() {

        HomeServices service = RetrofitInstance.getRetrofitInstance().
                create(HomeServices.class);
        boolean forceRefresh = false;
        Call<FindTrips> getPlacesCall = service.getTrips(forceRefresh ? "no-cache" : null, "john@local.com", "VGbp9W6tWSQHyHZTas7g");

        getPlacesCall.enqueue(new Callback<FindTrips>() {
            @Override
            public void onResponse(Call<FindTrips> call, Response<FindTrips> response) {
                if (response.code() == 200) {
                    FindTrips findTrips = response.body();
                    ArrayList<Trips> tripsArrayList = findTrips.getTrips();
                    tripsMutableLiveData.setValue(tripsArrayList);
                    tripdataIsLoaded.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<FindTrips> call, Throwable t) {
                Toast.makeText(application, "Data loading failed!", Toast.LENGTH_SHORT).show();
            }
        });

        return tripsMutableLiveData;
    }


    public MutableLiveData<ArrayList<Trips>> getTripsOnLocationMutableLiveData(String name) {

        HomeServices service = RetrofitInstance.getRetrofitInstance().
                create(HomeServices.class);
        boolean forceRefresh = false;
        Call<FindTrips> getPlacesCall = service.getTripsSpecificLocation(name, forceRefresh ? "no-cache" : null, "john@local.com", "VGbp9W6tWSQHyHZTas7g");

        getPlacesCall.enqueue(new Callback<FindTrips>() {
            @Override
            public void onResponse(Call<FindTrips> call, Response<FindTrips> response) {
                if (response.code() == 200) {
                    FindTrips findTrips = response.body();
                    ArrayList<Trips> tripsArrayList = findTrips.getTrips();
                    tripsMutableLiveData.setValue(tripsArrayList);
                }
            }

            @Override
            public void onFailure(Call<FindTrips> call, Throwable t) {
                Toast.makeText(application, "Data loading failed!", Toast.LENGTH_SHORT).show();
            }
        });

        return tripsMutableLiveData;
    }


    public MutableLiveData<Boolean> getIsDataLoaded() {
        return dataIsLoaded;
    }

    public MutableLiveData<Boolean> getIsTripDataLoaded() {
        return tripdataIsLoaded;
    }


}
