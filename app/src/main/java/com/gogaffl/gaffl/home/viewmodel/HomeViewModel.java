package com.gogaffl.gaffl.home.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.gogaffl.gaffl.home.model.SearchPlaces;
import com.gogaffl.gaffl.home.model.Trips;
import com.gogaffl.gaffl.home.repository.HomeRepository;

import java.util.ArrayList;

public class HomeViewModel extends AndroidViewModel {

    private HomeRepository mHomeRepository;

    public HomeViewModel(@NonNull Application application, HomeRepository homeRepository) {
        super(application);
        mHomeRepository = homeRepository;
    }


    public MutableLiveData<ArrayList<Trips>> getTripsDataFromCloud() {

        return mHomeRepository.getTripsMutableLiveData();
    }

    public MutableLiveData<ArrayList<Trips>> getTripsOnLocationMutableLiveData(String name) {

        return mHomeRepository.getTripsOnLocationMutableLiveData(name);
    }

    public MutableLiveData<ArrayList<String>> getPlacesMutableLiveData(String searchText) {

        return mHomeRepository.getPlacesMutableLiveData(searchText);
    }


    public MutableLiveData<Boolean> getDataIsLoaded() {
        return mHomeRepository.getIsDataLoaded();
    }

    public MutableLiveData<Boolean> getTripDataIsLoaded() {
        return mHomeRepository.getIsTripDataLoaded();
    }
}
