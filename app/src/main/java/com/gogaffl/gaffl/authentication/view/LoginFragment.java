package com.gogaffl.gaffl.authentication.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.widget.EditText;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.helper.AuthService;
import com.gogaffl.gaffl.authentication.model.AuthResponse;
import com.gogaffl.gaffl.authentication.viewmodel.LoginViewModel;
import com.gogaffl.gaffl.profile.view.ProfileActivity;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.ErrorResponse;
import com.gogaffl.gaffl.tools.MySharedPreferences;
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

import static android.support.constraint.Constraints.TAG;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private MySharedPreferences prefs;
    private EditText emailET;
    private EditText passET;
    private ErrorResponse errorResponse;
    private Intent mIntent;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private String token;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), s);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        Button backButton = view.findViewById(R.id.login_back_button);
        emailET = view.findViewById(R.id.login_email_et);
        passET = view.findViewById(R.id.login_password_et);
        emailET.setText("onirban.gaffl@gmail.com");
        passET.setText("12345678");
        Button forgotPassButton = view.findViewById(R.id.forgot_pass_button);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.login_fab);

        backButton.setOnClickListener(v1 -> {
            changeFragment(R.anim.enter_from_left, new LoginChoiceFragment());
        });

        forgotPassButton.setOnClickListener(v -> {

            changeFragment(R.anim.enter_from_right, new ForgotPassFragment());

        });

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");
        boolean isTokenExist = prefs.getBoolean("isTokenExist", false);
        token = prefs.getString("email_token", "default_token");

        if (prefs.containsKey("username")) {
            String phone = prefs.getString("username", "default_username");
            String pass = prefs.getString("password", "default_password");
            emailET.setText(phone);
            passET.setText(pass);
        }

        floatingActionButton.setOnClickListener(v -> login());

        return view;
    }

    public void login() {
        //showProgress();
        AuthService service = RetrofitInstance.getRetrofitInstance().create(AuthService.class);
        Logger.addLogAdapter(new AndroidLogAdapter());

        final String email = emailET.getText().toString();
        final String pass = passET.getText().toString();

        Map<String, RequestBody> loginMap = new HashMap<>();
        loginMap.put("email", createRequestBody(email));
        loginMap.put("password", createRequestBody(pass));


        Call<AuthResponse> call = service.authUser(loginMap);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                String token = "";
                if (response.isSuccessful()) {
                    Logger.i(response.body().getInfo().getToken());
                    // Do awesome stuff
                    if (response.body() != null) {
                        try {
                            token = response.body().getInfo().getToken();
                            System.out.println("token is : " + email);
                            prefs.putString("email", token);
                            prefs.putString("email_token", token);
                            prefs.putBoolean("isEmailTokenExist", true);

                        } catch (Exception t) {
                            Logger.e(t, "token error");
                        }
                    }
                    prefs.putString("username", email);
                    prefs.putString("password", pass);
                    prefs.commit();
                    mIntent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(mIntent);
                    Toasty.success(getActivity(),
                            "logged in!!", Toasty.LENGTH_SHORT).show();

//                    mIntent = new Intent(getActivity(),
//                            TestActivity.class);//test
//                    startActivity(mIntent);
//                    getActivity().finish();

                } else {
                    Gson gson = new Gson();
                    Toasty.error(getActivity(),
                            "failed", Toasty.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Logger.e(t, TAG, "onFailure: login unsuccessful");
                Toasty.error(getActivity(), "Failed to Connect!", Toasty.LENGTH_SHORT).show();
            }
        });
    }

}
