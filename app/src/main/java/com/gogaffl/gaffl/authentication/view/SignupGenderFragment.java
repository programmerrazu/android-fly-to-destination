package com.gogaffl.gaffl.authentication.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.helper.AuthService;
import com.gogaffl.gaffl.authentication.model.AuthResponse;
import com.gogaffl.gaffl.authentication.model.User;
import com.gogaffl.gaffl.authentication.viewmodel.SignupGenderViewModel;
import com.gogaffl.gaffl.profile.view.ProfileActivity;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.ErrorResponse;
import com.gogaffl.gaffl.tools.MySharedPreferences;
import com.gogaffl.gaffl.tools.SelectDateFragment;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupGenderFragment extends Fragment {

    private SignupGenderViewModel mViewModel;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private MySharedPreferences prefs;
    private Intent mIntent;
    private ErrorResponse errorResponse;
    private Context mContext;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String TAG = "register";


    public static SignupGenderFragment newInstance() {
        return new SignupGenderFragment();
    }

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), s);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignupGenderViewModel.class);
        // TODO: Use the ViewModel
    }

    public void changeFragment(int id, Fragment frag) {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.setCustomAnimations(id, 0);
        transaction.remove(newInstance());
        transaction.replace(R.id.container, frag);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_gender_fragment, container, false);
        Button backButton = view.findViewById(R.id.back_button_signup_gender);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_signup_gender);

        radioSexGroup = view.findViewById(R.id.sex_group);


        floatingActionButton.setOnClickListener(v -> {
            // get selected radio button from radioGroup
            Integer selectedId = radioSexGroup.getCheckedRadioButtonId();
            radioSexButton = view.findViewById(selectedId);

            if (selectedId != -1) {
                String gender = radioSexButton.getText().toString();
                User.setGender(gender);
//                if (InternetConnection.checkConnection(getContext())) registerUser();
//                else Toasty.error(getActivity(),
//                        "Check Internet Connection!", Toasty.LENGTH_SHORT).show();
                if (User.isSocialStatus()) {
                    socialSignup(User.getFirstName(), User.getEmail(),
                            User.getUid(), User.getFileUri(), User.getGender(), User.getAge());
                } else {
                    changeFragment(R.anim.enter_from_right, new ImageFragment());
                }
            } else {
                Toasty.error(getActivity(),
                        "Select your gender!", Toasty.LENGTH_SHORT).show();

            }
        });

        backButton.setOnClickListener(v -> {
            SelectDateFragment.setTracker(false);
            changeFragment(R.anim.enter_from_left, new SignupAgeFragment());
        });

        return view;
    }

    public void socialSignup(String name, String email, String uid, Uri photoUrl, String gender, String age) {
        //showProgress();
        AuthService service = RetrofitInstance.getRetrofitInstance().create(AuthService.class);
        Logger.addLogAdapter(new AndroidLogAdapter());


        RequestBody url = (RequestBody) RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), String.valueOf(photoUrl));

        Map<String, RequestBody> registerMap = new HashMap<>();
        registerMap.put("name", createRequestBody(name));
        registerMap.put("email", createRequestBody(email));
        registerMap.put("gender", createRequestBody(gender));
        registerMap.put("age", createRequestBody(age));
        registerMap.put("id", createRequestBody(uid));
        registerMap.put("provider", createRequestBody(User.getProvider()));
        registerMap.put("picture", url);
        //registerMap.put("date_of_birth", createRequestBody(date));
        //registerMap.put("gender", createRequestBody(gender));


        Call<AuthResponse> call = service.socialRegisterUser(registerMap);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.isSuccessful()) {

                    Toasty.success(getActivity(),
                            "logged in!!", Toasty.LENGTH_SHORT).show();

                    mIntent = new Intent(getActivity(),
                            ProfileActivity.class);//test
                    startActivity(mIntent);
                    getActivity().finish();

                } else {
                    Gson gson = new Gson();
                    Toasty.error(getActivity(),
                            "failed", Toasty.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Logger.e(t, TAG, "onFailure: login unsuccessful");
            }
        });
    }


}
