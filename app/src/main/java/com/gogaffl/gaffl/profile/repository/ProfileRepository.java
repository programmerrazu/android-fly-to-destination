package com.gogaffl.gaffl.profile.repository;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.widget.Toast;

import com.gogaffl.gaffl.profile.model.CountryList;
import com.gogaffl.gaffl.profile.model.PhoneResponse;
import com.gogaffl.gaffl.profile.model.ProfileResponse;
import com.gogaffl.gaffl.profile.model.User;
import com.gogaffl.gaffl.profile.model.UserSendModel;
import com.gogaffl.gaffl.rest.RetrofitInstance;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private MutableLiveData<User> userDataListMutableLiveData;
    private MutableLiveData<CountryList> mCountryListMutableLiveData;
    private MutableLiveData<Boolean> dataIsLoaded;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private Call<PhoneResponse> call;
    private Map<String, RequestBody> params;

    private Application application;
    private ProfileServices service;
    private boolean forceRefresh = false;

    //constructor to initialize all the objects
    public ProfileRepository(Application application) {
        this.application = application;

        //initializing the data models
        if (userDataListMutableLiveData == null) {
            userDataListMutableLiveData = new MutableLiveData<>();
            dataIsLoaded = new MutableLiveData<>();
        }

        service = RetrofitInstance.getRetrofitInstance().
                create(ProfileServices.class);
    }

    public MutableLiveData<User> getUserDataFromCloud() {

        if (UserSendModel.isUpdateCache()) {
            forceRefresh = true;
        } else {
            forceRefresh = false;
        }

      //  Call<ProfileResponse> postResponseCall = service.getProfile(forceRefresh ? "no-cache" : null, "onirban.gaffl@gmail.com", "NQDeoKf8Vgn_rLA5TpfG");
        Call<ProfileResponse> postResponseCall = service.getProfile(forceRefresh ? "no-cache" : null, "john@local.com", "VGbp9W6tWSQHyHZTas7g");

        postResponseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.code() == 200) {
                    UserSendModel.setUpdateCache(false);
                    //inserting data in the post list object
                    User userData = response.body().getUser();
                    //setting the value to the view model setter
                    userDataListMutableLiveData.setValue(userData);
                    //setting the value to the view model setter if data is loaded
                    dataIsLoaded.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(application, "Data loading failed!", Toast.LENGTH_SHORT).show();
                UserSendModel.setDataLoadFailed(true);
            }
        });
        return userDataListMutableLiveData;
    }


    public MutableLiveData<Boolean> getIsDataLoaded() {
        return dataIsLoaded;
    }


}
