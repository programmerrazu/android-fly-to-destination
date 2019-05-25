package com.gogaffl.gaffl.profile.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gogaffl.gaffl.profile.repository.ProfileRepository;


public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private ProfileRepository mProfileRepository;

    public ProfileViewModelFactory(Application application, ProfileRepository profileRepository) {
        mApplication = application;
        mProfileRepository = profileRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ProfileViewModel(mApplication, mProfileRepository);
    }
}
