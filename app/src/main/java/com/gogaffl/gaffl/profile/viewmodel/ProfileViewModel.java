package com.gogaffl.gaffl.profile.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.gogaffl.gaffl.profile.model.User;
import com.gogaffl.gaffl.profile.repository.ProfileRepository;

public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository mProfileRepository;

    public ProfileViewModel(@NonNull Application application, ProfileRepository profileRepository) {
        super(application);
        mProfileRepository = profileRepository;
    }

    public MutableLiveData<User> getUserDataFromCloud() {

        return mProfileRepository.getUserDataFromCloud();
    }


    public MutableLiveData<Boolean> getDataIsLoaded() {
        return mProfileRepository.getIsDataLoaded();
    }
}
