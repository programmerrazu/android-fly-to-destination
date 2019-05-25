package com.gogaffl.gaffl.home.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gogaffl.gaffl.home.repository.HomeRepository;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private HomeRepository mHomeRepository;

    public HomeViewModelFactory(Application application, HomeRepository homeRepository) {
        mApplication = application;
        mHomeRepository = homeRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeViewModel(mApplication, mHomeRepository);
    }
}
