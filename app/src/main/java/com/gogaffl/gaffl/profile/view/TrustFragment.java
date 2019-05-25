package com.gogaffl.gaffl.profile.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.helper.AuthService;
import com.gogaffl.gaffl.authentication.model.AuthResponse;
import com.gogaffl.gaffl.authentication.model.FbUserModel;
import com.gogaffl.gaffl.authentication.model.LinkedinEmail;
import com.gogaffl.gaffl.authentication.model.LinkedinPic;
import com.gogaffl.gaffl.authentication.model.LinkedinUser;
import com.gogaffl.gaffl.authentication.view.AuthActivity;
import com.gogaffl.gaffl.authentication.viewmodel.RegistrationViewModel;
import com.gogaffl.gaffl.profile.InitialActivity;
import com.gogaffl.gaffl.profile.adapter.SectionsPagerAdapter;
import com.gogaffl.gaffl.profile.model.Country;
import com.gogaffl.gaffl.profile.model.CountryList;
import com.gogaffl.gaffl.profile.model.PhoneResponse;
import com.gogaffl.gaffl.profile.model.StateList;
import com.gogaffl.gaffl.profile.model.States;
import com.gogaffl.gaffl.profile.model.Status;
import com.gogaffl.gaffl.profile.model.User;
import com.gogaffl.gaffl.profile.model.UserSendModel;
import com.gogaffl.gaffl.profile.repository.ProfileRepository;
import com.gogaffl.gaffl.profile.repository.ProfileServices;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModel;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModelFactory;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.MySharedPreferences;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TrustFragment extends Fragment {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    ImageView pic, basic, profile, interest, phone, social;
    private static final String TAG = "register";
    boolean socialValid, picValid = false;
    private ProfileViewModel viewModel;
    private Status mStatus;
    private static User mUser;
    private static UserSendModel mUserSendModel;
    protected View mView;
    private int countryID;
    private int stateID;
    /****YOUR LINKEDIN APP INFO HERE*********/
    private static final String API_KEY = "81hual36aq601t";
    private static final int RC_SIGN_IN = 9001;
    //linkedin
    /*CONSTANT FOR THE AUTHORIZATION PROCESS*/
    private static final String SECRET_KEY = "mG45M9AsTUuNsKOW";
    private CountryList mCountryList;
    private StateList mStateList;
    private static final String CALLBACK_URL = "oauth-testing:///";
    //This is any string we want to use. This will be used for avoid CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
    private static final String STATE = "E3ZYKC1T6H2yP4z";

    ///////////////////////social///////////////////////////////


    public static TrustFragment newInstance() {
        return new TrustFragment();
    }

    private static final String REDIRECT_URI = "https://www.google.com";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, new ProfileViewModelFactory(getActivity().getApplication(),
                new ProfileRepository(getActivity().getApplication()))).get(ProfileViewModel.class);
        viewModel.getUserDataFromCloud().observe(getViewLifecycleOwner(), user -> {
            mUser = user;
            Status status = user.getStatus();
            initFrag(status);
        });
    }

    //private static final String SCOPES = "r_fullprofile%20r_emailaddress%20r_network";
    private static final String SCOPES = "r_liteprofile";
    private static final String SCOPE_EMAIL = "r_emailaddress ";
    /*********************************************/

    //These are constants used for build the urls
    private static final String AUTHORIZATION_URL =
            "https://www.linkedin.com/oauth/v2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/oauth/v2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE = "code";

    public void getStatesFromCloud(int code) {
        ProfileServices services = RetrofitInstance.getRetrofitInstance().
                create(ProfileServices.class);
        Call<StateList> getResponseCall = services.getState(code, "john@local.com", "VGbp9W6tWSQHyHZTas7g");
        getResponseCall.enqueue(new Callback<StateList>() {
            @Override
            public void onResponse(Call<StateList> call, Response<StateList> response) {
                mStateList = response.body();
            }

            @Override
            public void onFailure(Call<StateList> call, Throwable t) {
                Toast.makeText(getContext(), "Data loading failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static final String CLIENT_ID_PARAM = "client_id";


    ///////////////////////////////Handle SOcial///////////////////////////////
    private static final String SCOPE_PARAM = "scope";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    View vGoogle, uGoogle, vFb, uFb, vLinkedin, uLinkedin, vPhone, uPhone, vEmail, uEmail;
    TextView picView, basicView, profileView, interestView, phoneView, socialView,
            connectFacebook, connectGoogle, connectLinkedin;
    Intent mIntent;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    String profileImageUrl;
    String userid;
    Activity mActivity = getActivity();
    String linkName, linkId, linkPic, linkEmail;
    private String birthdate, about, userName, headline;
    private boolean isBirthdate = false;
    private String sex;
    private SectionsPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private RegistrationViewModel mViewModel;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private MySharedPreferences prefs;
    private GoogleSignInClient mGoogleSignInClient;
    //google
    private String token = null;
    //facebook
    private AccessToken mAccessToken;
    private CallbackManager callbackManager;
    private WebView webView;
    private ProgressDialog pd;

    /**
     * Method that generates the url for get the authorization token from the Service
     *
     * @return Url
     */
    private static String getAuthorizationUrl() {
        String URL = AUTHORIZATION_URL
                + QUESTION_MARK
                + RESPONSE_TYPE_PARAM
                + EQUALS
                + RESPONSE_TYPE_VALUE
                + AMPERSAND
                + CLIENT_ID_PARAM
                + EQUALS
                + API_KEY
                + AMPERSAND
                + SCOPE_PARAM
                + EQUALS
                + SCOPE_EMAIL
                + SCOPES
                + AMPERSAND
                + STATE_PARAM
                + EQUALS
                + STATE
                + AMPERSAND
                + REDIRECT_URI_PARAM
                + EQUALS
                + REDIRECT_URI;
        Log.i("authorization URL", "" + URL);
        return URL;
    }

    private static String getAccessTokenUrl(String authorizationToken) {
        String URL = ACCESS_TOKEN_URL
                + QUESTION_MARK
                + GRANT_TYPE_PARAM
                + EQUALS
                + GRANT_TYPE
                + AMPERSAND
                + RESPONSE_TYPE_VALUE
                + EQUALS
                + authorizationToken
                + AMPERSAND
                + CLIENT_ID_PARAM
                + EQUALS
                + API_KEY
                + AMPERSAND
                + REDIRECT_URI_PARAM
                + EQUALS
                + REDIRECT_URI
                + AMPERSAND
                + SECRET_KEY_PARAM
                + EQUALS
                + SECRET_KEY;
        Log.i("accessToken URL", "" + URL);
        return URL;
    }

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), s);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trust_fragment, container, false);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);



        this.mView = view;
        vEmail = view.findViewById(R.id.verified_view_email);
        uEmail = view.findViewById(R.id.unverified_view_email);
        vGoogle = view.findViewById(R.id.verified_view_google);
        uGoogle = view.findViewById(R.id.unverified_view_google);
        vFb = view.findViewById(R.id.verified_view_facebook);
        uFb = view.findViewById(R.id.unverified_view_facebook);
        vLinkedin = view.findViewById(R.id.verified_view_linkedin);
        uLinkedin = view.findViewById(R.id.unverified_view_linkedin);
        vPhone = view.findViewById(R.id.verified_view_phone);
        uPhone = view.findViewById(R.id.unverified_view_phone);

        connectFacebook = view.findViewById(R.id.connectFacebook);
        connectGoogle = view.findViewById(R.id.connectGoogle);
        connectLinkedin = view.findViewById(R.id.connectLinkedIn);

        pic = view.findViewById(R.id.verified_status_pic);
        basic = view.findViewById(R.id.verified_status_basic_true);
        profile = view.findViewById(R.id.verified_status_profile_true);
        interest = view.findViewById(R.id.verified_status_interest_true);
        phone = view.findViewById(R.id.verified_status_phone_true);
        social = view.findViewById(R.id.verified_status_social_true);

        picView = view.findViewById(R.id.verified_status_pic_false);
        basicView = view.findViewById(R.id.verified_status_basic_false);
        profileView = view.findViewById(R.id.verified_status_profile_false);
        interestView = view.findViewById(R.id.verified_status_interest_false);
        phoneView = view.findViewById(R.id.verified_status_phone_false);
        socialView = view.findViewById(R.id.verified_status_social_false);


        profileView.setOnClickListener(v -> showProfileDialog());
        basicView.setOnClickListener(v -> showBasicDialog());
        phoneView.setOnClickListener(v ->
        {
            UserSendModel.setPhoneEdit(true);
            Intent i = new Intent(getActivity(), InitialActivity.class);
            i.putExtra("frgToLoad", 123);

            // Now start your activity
            startActivity(i);
            getActivity().finish();
        });

        picView.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AuthActivity.class);
            i.putExtra("frgToLoad", 124);
            // Now start your activity
            UserSendModel.setUid(mUser.getId());
            UserSendModel.setEmail(mUser.getEmail());
            UserSendModel.setToken("NQDeoKf8Vgn_rLA5TpfG");
            startActivity(i);
            getActivity().finish();
        });

        interestView.setOnClickListener(v -> {
            if (mUser.getAbout().length() < 2) {
                showAboutDialog();
            } else {
                UserSendModel.setInterestEdit(true);
                getInterestPage();
            }
        });

        socialView.setOnClickListener(v -> showSocialDialog());

        connectGoogle.setOnClickListener(v -> signIn());
        connectFacebook.setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
            //LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code
                            System.out.println("onSuccess");
                            mAccessToken = loginResult.getAccessToken();
                            System.out.println("onSuccess: token is: " + mAccessToken);
                            boolean isLoggedIn = mAccessToken != null && !mAccessToken.isExpired();
                            //prefs.putString("fbtoken",mAccessToken.getToken());
                            getUserProfile(mAccessToken);
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            System.out.println("onCancel");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            Log.v("LoginActivity", exception.getCause().toString());
                        }
                    });
        });

        connectLinkedin.setOnClickListener(v -> signupLinkedin(view));


        return view;
    }

    private void initFrag(Status status) {
        if (status != null) {

            //verified status

            if (!mUser.getEmail().isEmpty()) {
                vEmail.setVisibility(View.VISIBLE);
                uEmail.setVisibility(View.GONE);
            }
            if (status.getGoogle()) {
                vGoogle.setVisibility(View.VISIBLE);
                uGoogle.setVisibility(View.GONE);
                socialValid = true;
            }
            if (status.getFacebook()) {
                vFb.setVisibility(View.VISIBLE);
                uFb.setVisibility(View.GONE);
                socialValid = true;
            }
            if (status.getFacebook()) {
                vLinkedin.setVisibility(View.VISIBLE);
                uLinkedin.setVisibility(View.GONE);
                socialValid = true;
            }
            if (status.getPhoneVerified()) {
                vPhone.setVisibility(View.VISIBLE);
                uPhone.setVisibility(View.GONE);
            }

            //trust points
            if (!mUser.getPicture().isEmpty()) {
                pic.setVisibility(View.VISIBLE);
                picView.setVisibility(View.GONE);
            }

            if (status.getProfileInfo()) {
                profile.setVisibility(View.VISIBLE);
                profileView.setVisibility(View.GONE);
            }
            if (status.getBasicInfo()) {
                basic.setVisibility(View.VISIBLE);
                basicView.setVisibility(View.GONE);
            }
            if (status.getPhoneVerified()) {
                phone.setVisibility(View.VISIBLE);
                phoneView.setVisibility(View.GONE);
            }
            if (status.getInterestAndAbout()) {
                interest.setVisibility(View.VISIBLE);
                interestView.setVisibility(View.GONE);
            }
            if (socialValid) {
                social.setVisibility(View.VISIBLE);
                socialView.setVisibility(View.GONE);
            }
            if (picValid) {
                pic.setVisibility(View.VISIBLE);
                picView.setVisibility(View.GONE);
            }

            getCountriesFromCloud();

        }
    }

    private void showProfileDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_profile_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final EditText etName = (EditText) dialog.findViewById(R.id.et_name);
        etName.setText(mUser.getName());
        final EditText etTitle = (EditText) dialog.findViewById(R.id.et_title);
        etTitle.setText(mUser.getHeadline());
        //etAbout.setText();

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> dialog.dismiss());
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(v -> {

            userName = etName.getText().toString();
            headline = etTitle.getText().toString();
            if (headline.length() > 3 && userName.length() > 0) {
                mUserSendModel = new UserSendModel(userName, headline);
                postData(mUserSendModel);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showSocialDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.social_dialog_sample);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

//        vGoogle = dialog.findViewById(R.id.verified_view_google);
//        uGoogle = dialog.findViewById(R.id.unverified_view_google);
//        vFb = dialog.findViewById(R.id.verified_view_facebook);
//        uFb = dialog.findViewById(R.id.unverified_view_facebook);
//        vLinkedin = dialog.findViewById(R.id.verified_view_linkedin);
//        uLinkedin = dialog.findViewById(R.id.unverified_view_linkedin);
//        vPhone = dialog.findViewById(R.id.verified_view_phone);
//        uPhone = dialog.findViewById(R.id.unverified_view_phone);
        connectFacebook = dialog.findViewById(R.id.connectFacebook);
        connectGoogle = dialog.findViewById(R.id.connectGoogle);
        connectLinkedin = dialog.findViewById(R.id.connectLinkedIn);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> dialog.dismiss());
        (dialog.findViewById(R.id.connectGoogle)).setOnClickListener(v -> {
            signIn();
            dialog.dismiss();
        });
        (dialog.findViewById(R.id.connectFacebook)).setOnClickListener(v -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
            //LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code
                            System.out.println("onSuccess");
                            mAccessToken = loginResult.getAccessToken();
                            System.out.println("onSuccess: token is: " + mAccessToken);
                            boolean isLoggedIn = mAccessToken != null && !mAccessToken.isExpired();
                            //prefs.putString("fbtoken",mAccessToken.getToken());
                            getUserProfile(mAccessToken);
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            System.out.println("onCancel");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            Log.v("LoginActivity", exception.getCause().toString());
                        }
                    });
            dialog.dismiss();
        });
        (dialog.findViewById(R.id.connectLinkedIn)).setOnClickListener(v -> {
            signupLinkedin(mView);
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public void showBasicDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_basic_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextView tvDate = dialog.findViewById(R.id.datetv);
        final RadioGroup radioSexGroup = dialog.findViewById(R.id.sex_group_edit);
        final Spinner countrySpinner = dialog.findViewById(R.id.country_spinner);
        final Spinner stateSpinner = dialog.findViewById(R.id.state_spinner);



        tvDate.setOnClickListener(v -> {
            //Calendar now = Calendar.getInstance();
            final Calendar c = Calendar.getInstance();

            //DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
            //       AlertDialog.THEME_HOLO_DARK,this,year,month,day);
            DatePickerDialog dpd = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_DARK,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int yy,
                                              int mm, int dd) {

                            if (Integer.parseInt(getAge(yy, mm, dd)) >= 18) {
                                birthdate = yy + "-"
                                        + (mm + 1) + "-" + dd;
                                tvDate.setText(birthdate);
                            } else {
                                Toasty.warning(getActivity(), "You'r age is under 18!", Toasty.LENGTH_SHORT).show();
                            }


                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
            dpd.show();


        });



        ArrayList<Country> countryList = mCountryList.getCountries();
        ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, countryList);
        countrySpinner.setAdapter(adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryID = (int) countrySpinner.getSelectedItemId() + 1;
                getStatesFromCloud(countryID);
                String iddd = countrySpinner.getSelectedItem().toString();
                System.out.println(countryID + " country item " + iddd);


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<States> states = mStateList.getStates();
                        ArrayList<String> stateNames = new ArrayList<>();
                        HashMap<Integer, String> stateMap = new HashMap<>();
                        for (States state : states) {
                            stateNames.add(state.getName());
                            stateMap.put(state.getId(), state.getName());
                            Log.i("state Details", state.getId() + "-" + state.getName());
                        }
                        ArrayAdapter<String> statesArrayAdapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_spinner_dropdown_item, stateNames);
                        stateSpinner.setAdapter(statesArrayAdapter);
                        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                stateID = (int) stateSpinner.getSelectedItemId();
                                States states1 = states.get(stateID);
                                stateID = states1.getId();
                                System.out.println(stateID + "this is state real id");
                                String iddd = stateSpinner.getSelectedItem().toString();
                                System.out.println(stateID + " spinner item " + iddd);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }, 2000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                long idd = countrySpinner.getSelectedItemId() + 1;
                String iddd = countrySpinner.getSelectedItem().toString();
                System.out.println(idd + " spinner item " + iddd);
            }
        });


        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> dialog.dismiss());
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(v -> {
            //Put the data in server  radioSexButton.getText().toString()
            //mUserSendModel = new UserSendModel(countryID, stateID, "male", birthdate);
            //postData(mUserSendModel);

            if (radioSexGroup.getCheckedRadioButtonId() != -1) {

                int selectedId = radioSexGroup.getCheckedRadioButtonId();

                final RadioButton radioSexButton = dialog.findViewById(selectedId);
                radioSexButton.setOnCheckedChangeListener((group, checkedId) -> radioSexButton.setGravity(Gravity.CENTER));

                sex = radioSexButton.getText().toString();

                if (birthdate != null) {

                    postBasicInfoData(birthdate, sex, countryID, stateID);
                    dialog.dismiss();
                } else {
                    Toasty.warning(getActivity(), "Select Birthdate!", Toasty.LENGTH_SHORT).show();
                }

            } else {
                Toasty.warning(getActivity(), "Select Gender!", Toasty.LENGTH_SHORT).show();
            }


        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }

    private void showAboutDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final EditText etAbout = (EditText) dialog.findViewById(R.id.et_about);
        etAbout.setText(mUser.getAbout());
        //etAbout.setText();

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> dialog.dismiss());
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(v -> {
            //Put the data in server
            String aboutText = etAbout.getText().toString();
            if (aboutText.length() > 10) {
                mUserSendModel = new UserSendModel(etAbout.getText().toString());
                postData(mUserSendModel);
                dialog.dismiss();
            } else {
                Toasty.warning(getActivity(), "write more!", Toasty.LENGTH_SHORT).show();
            }

        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void postData(UserSendModel userSendModel) {


        Logger.addLogAdapter(new AndroidLogAdapter());

        about = userSendModel.getAbout();
        userName = userSendModel.getName();
        headline = userSendModel.getTitle();
        int uID = mUser.getId();

        HashMap<String, String> paramss = new HashMap<>();

        if (about != null) {
            if (!about.isEmpty()) paramss.put("about", about);
        } else {
            if (!userName.isEmpty()) paramss.put("name", userName);
            if (!headline.isEmpty()) paramss.put("title", headline);
        }


        ProfileServices services = RetrofitInstance.getRetrofitInstance().
                create(ProfileServices.class);
        Call<PhoneResponse> call = services.stringUpload("onirban.gaffl@gmail.com", "NQDeoKf8Vgn_rLA5TpfG", paramss, uID);

        call.enqueue(new Callback<PhoneResponse>() {
            @Override
            public void onResponse(Call<PhoneResponse> call, Response<PhoneResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("post about success!");
                    UserSendModel.setUpdateCache(true);
                    if (userName != null) {
                        TextView tv = getActivity().findViewById(R.id.title_profile);
                        TextView title = getActivity().findViewById(R.id.title);
                        tv.setText(mUserSendModel.getName());
                        title.setText(mUserSendModel.getTitle());
                    } else {
                        getActivity().recreate();
                    }

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(AboutFragment.newInstance()).attach(AboutFragment.newInstance()).commit();

                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                Toasty.error(getActivity(), "Update failed!", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void postBasicInfoData(String birthDate, String gender, int countryID, int stateID) {


        Logger.addLogAdapter(new AndroidLogAdapter());

        HashMap<String, String> paramss = new HashMap<>();

        int uID = mUser.getId();

        if (!birthDate.isEmpty() && birthDate.length() > 5) paramss.put("date_of_birth", birthDate);
        if (!gender.isEmpty()) paramss.put("gender", gender);
        if (countryID > -1) paramss.put("country_id", Integer.toString(countryID));
        if (stateID > -1) paramss.put("state_id", Integer.toString(stateID));


        ProfileServices services = RetrofitInstance.getRetrofitInstance().
                create(ProfileServices.class);
        Call<PhoneResponse> call = services.stringUpload("onirban.gaffl@gmail.com", "NQDeoKf8Vgn_rLA5TpfG", paramss, uID);

        call.enqueue(new Callback<PhoneResponse>() {
            @Override
            public void onResponse(Call<PhoneResponse> call, Response<PhoneResponse> response) {
                if (response.isSuccessful()) {
                    UserSendModel.setUpdateCache(true);
                    System.out.println("post basic info success!");
                    getActivity().recreate();

                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                Toasty.error(getActivity(), "Basic info setting failed!", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    //GOOGLE

    public void getCountriesFromCloud() {
        ProfileServices services = RetrofitInstance.getRetrofitInstance().
                create(ProfileServices.class);
        Call<CountryList> getResponseCall = services.getCountry("john@local.com", "VGbp9W6tWSQHyHZTas7g");
        getResponseCall.enqueue(new Callback<CountryList>() {
            @Override
            public void onResponse(Call<CountryList> call, Response<CountryList> response) {
                mCountryList = response.body();
            }

            @Override
            public void onFailure(Call<CountryList> call, Throwable t) {
                Toast.makeText(getContext(), "Data loading failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getInterestPage() {
        Intent intent = new Intent(getActivity(), InitialActivity.class);
        intent.putExtra("frgToLoad", 126);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.startTracking();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook option to init
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                prefs = MySharedPreferences.getInstance(getActivity(),
                        "Gaffl_Prefs");
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                String fbCurToken = currentAccessToken.getToken();
                if (oldAccessToken.getToken() != null) {
                    String fbOldToken = oldAccessToken.getToken();
                    System.out.println("this is old fb token: " + fbOldToken);
                    prefs.putString("fb_o_token", fbOldToken);
                }


                System.out.println("this is current fb token: " + fbCurToken);
                prefs.putString("fb_c_token", fbCurToken);

                prefs.commit();

            }
        };

        // If the access token is available already assign it.
        mAccessToken = AccessToken.getCurrentAccessToken();
        Logger.i("fb access token is available from before");

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
            if (acct != null) {
                String personName = acct.getDisplayName();
                System.out.println("this is token auth code: " + acct.getIdToken());
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                if (personName == null) personName = "No Name";
                if (personPhoto == null)
                    personPhoto = Uri.parse("https://static.thenounproject.com/png/17241-200.png");

                socialSignup(personName, personEmail, personId, personPhoto, "google");

            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);


        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        // [END on_start_sign_in]
    }

    ////////////////////////////////////////LINKEDIN////////////////////////////////////////////////

    //facebook profile
    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken,
                (object, response) -> {
                    try {
                        //You can fetch user Info like this…
                        //object.getJSONObject(“picture”).
                        userid = AccessToken.getCurrentAccessToken().getUserId();
                        System.out.println("this is user id: " + userid);
                        System.out.println(response.toString());
                        ;
                        FbUserModel fbUserModel = new Gson().fromJson(object.toString(), FbUserModel.class);
                        String urlfb = fbUserModel.getPicture().getData().getUrl();
                        String name = object.getString("name");
                        String email = object.getString("email");
                        String uid = object.getString("id");
                        Uri myUri = Uri.parse(urlfb);
                        socialSignup(name, email, uid, myUri, "facebook");
                        //object.getString(“name”);
                        //object.getString(“email”));
                        //object.getString(“id”));
                        System.out.println(name + "\n" + email + "\n" + uid + "\n" + myUri + "\n");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void signupLinkedin(View view) {

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");
        String tokenOfLi = prefs.getString("li_access_token", "default");

//        if (!tokenOfLi.isEmpty()) {
//
//            prepareLinkedinData();
//            getEmailOfLi();
//
//        } else {


        showLinkedinDialog();


        //}

    }

    private void showLinkedinDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.webview_layout);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        final WebView webView = dialog.findViewById(R.id.webView_li);
        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> dialog.dismiss());
        pd = ProgressDialog.show(getActivity(), "", "Loading..", true);
        //Set a custom web view client
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //This method will be executed each time a page finished loading.
                //The only we do is dismiss the progressDialog, in case we are showing any.
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                //This method will be called when the Auth proccess redirect to our RedirectUri.
                //We will check the url looking for our RedirectUri.
                if (authorizationUrl.startsWith(REDIRECT_URI)) {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(authorizationUrl);
                    //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                    //If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(STATE_PARAM);
                    if (stateToken == null || !stateToken.equals(STATE)) {
                        Log.e("Authorize", "State token doesn't match");
                        return true;
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }
                    Log.i("Authorize", "Auth token received: " + authorizationToken);
                    prefs = MySharedPreferences.getInstance(getActivity(),
                            "Gaffl_Prefs");


                    prefs.putString("li_auth_token", authorizationToken);
                    prefs.commit();

                    //Generate URL for requesting Access Token
                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                    //We make the request in a AsyncTask
                    new TrustFragment.PostRequestAsyncTask().execute(accessTokenUrl);
                } else {
                    //Default behaviour
                    Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                    webView.loadUrl(authorizationUrl);
                }
                return true;
            }
        });

        //Get the authorization Url
        String authUrl = getAuthorizationUrl();
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void onNewIntent(Intent intent) {
        dealWithLinkedinResponse(intent);
    }

    private void dealWithLinkedinResponse(Intent intent) {
        Uri uri = intent.getData();
        System.out.println("URI=" + uri);
        if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
            String oauthVerifier = uri.getQueryParameter("oauth_verifier");
            //authoriseNewUser(oauthVerifier);
        }
    }

    public void prepareLinkedinData() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        int timeOut = 5 * 60;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.linkedin.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        /*Create handle for the RetrofitInstance interface*/
        AuthService service = retrofit.create(AuthService.class);
        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");
        String tokenOfLi = prefs.getString("li_access_token", "default");
        Logger.i("Main token is here: " + tokenOfLi);

        /*Call the method with parameter in the interface to get the employee data*/
        Call<LinkedinUser> call = service.getLinkedinUserProfile(tokenOfLi);

        /*Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<LinkedinUser>() {
            @Override
            public void onResponse(Call<LinkedinUser> call, Response<LinkedinUser> response) {
                if (response.body() != null) {
                    LinkedinUser linkedinUser = response.body();
                    linkName =
                            linkedinUser.getLastName().getLocalized().getEn_US()
                                    + " " +
                                    linkedinUser.getLastName().getLocalized().getEn_US();

                    linkId = linkedinUser.getId();

                    //String photoUrl = linkedinUser.getProfilePicture().getDisplayImage().

                    System.out.println("user ran linkedin: " + linkName);
//                    String jsonObject = new Gson().toJson(response);
//                    Logger.i("Linkedin Response:", jsonObject);

                    getPicOfLi();


                } else {
                    Toast.makeText(getActivity(), "No Response found! " + response.body(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LinkedinUser> call, Throwable t) {
                Log.d("Error linkedin user", t.toString());
                Toast.makeText(getContext(), "error linkedin User!",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getPicOfLi() {


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.linkedin.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        /*Create handle for the RetrofitInstance interface*/
        AuthService service = retrofit.create(AuthService.class);

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");
        String tokenOfLi = "Bearer " + prefs.getString("li_access_token", "default");
        if (tokenOfLi.length() > 5) {
            /*Call the method with parameter in the interface to get the employee data*/
            String projection = "(id,profilePicture(displayImage~:playableStreams))";
            Call<LinkedinPic> call = service.getLinkedinUserPic(tokenOfLi, projection);

            /*Log the URL called*/
            Log.wtf("URL Called", call.request().url() + "");


            call.enqueue(new Callback<LinkedinPic>() {
                @Override
                public void onResponse(Call<LinkedinPic> call, Response<LinkedinPic> response) {
                    if (response.body() != null) {

                        linkPic = response.body().getProfilePicture().
                                getDisplayImageSee().
                                getElements().get(3).
                                getIdentifiers().get(0).
                                getIdentifier().toString();
//
                        System.out.println("ran perfectly\n " + new Gson().toJson(response.body()) + "\n" + linkPic);
//                        //Logger.i("Linkedin Response:", new Gson().toJson(response));
//
//                        if (response.isSuccessful())
//                            Log.e("Success", new Gson().toJson(response.body()));
//                        else
//                            Log.e("unSuccess", new Gson().toJson(response.errorBody()));

                        getEmailOfLi();

                    } else {
                        Toast.makeText(getActivity(), "No Response found! " + response.body(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LinkedinPic> call, Throwable t) {
                    Log.d("Error Linkedin Pic", t.toString());
                    Toast.makeText(getContext(), "Error linkedin Pic!",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void getEmailOfLi() {


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.linkedin.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        /*Create handle for the RetrofitInstance interface*/
        AuthService service = retrofit.create(AuthService.class);

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");
        String tokenOfLi = "Bearer " + prefs.getString("li_access_token", "default");
        if (tokenOfLi.length() > 5) {
            /*Call the method with parameter in the interface to get the employee data*/
            String q = "members";
            String projection = "(elements*(handle~))";
            Call<LinkedinEmail> call = service.getLinkedinUserEmail(tokenOfLi, q, projection);

            /*Log the URL called*/
            Log.wtf("URL Called", call.request().url() + "");


            call.enqueue(new Callback<LinkedinEmail>() {
                @Override
                public void onResponse(Call<LinkedinEmail> call, Response<LinkedinEmail> response) {
                    if (response.body() != null) {

                        linkEmail = response.body().getElements().get(0).getHandleEmail().getEmailAddress();
                        System.out.println("email is" + linkEmail);
//
                        System.out.println("ran perfectly\n " + new Gson().toJson(response.body()) + "\n" + linkEmail);
//                        //Logger.i("Linkedin Response:", new Gson().toJson(response));
//
//                        if (response.isSuccessful())
//                            Log.e("Success", new Gson().toJson(response.body()));
//                        else
//                            Log.e("unSuccess", new Gson().toJson(response.errorBody()));
                        if (linkName != null && linkEmail != null && linkId != null && linkPic != null) {

                            socialSignup(linkName, linkEmail, linkId, Uri.parse(linkPic), "linkedin");

                        }


                    } else {
                        Toast.makeText(getActivity(), "No Response found! " + response.body(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LinkedinEmail> call, Throwable t) {
                    Log.d("Error Linkedin Email", t.toString());
                    Toast.makeText(getContext(), "Error linkedin Email!",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    public void socialSignup(String name, String email, String uid, Uri photoUrl, String provider) {
        //showProgress();
        AuthService service = RetrofitInstance.getRetrofitInstance().create(AuthService.class);
        Logger.addLogAdapter(new AndroidLogAdapter());


        RequestBody url = (RequestBody) RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), String.valueOf(photoUrl));

        Map<String, RequestBody> registerMap = new HashMap<>();
        registerMap.put("name", createRequestBody(name));
        registerMap.put("email", createRequestBody(email));
        registerMap.put("id", createRequestBody(uid));
        registerMap.put("provider", createRequestBody(provider));
        registerMap.put("picture", url);
        //registerMap.put("date_of_birth", createRequestBody(date));
        //registerMap.put("gender", createRequestBody(gender));


        Call<AuthResponse> call = service.socialRegisterUser(registerMap);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.isSuccessful()) {

                    Toasty.success(getActivity(),
                            "Connected!!", Toasty.LENGTH_SHORT).show();
                    UserSendModel.setUpdateCache(true);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(TrustFragment.this).attach(TrustFragment.this).commit();

                } else {
                    Gson gson = new Gson();
                    Toasty.error(getActivity(),
                            "failed to send data to server!", Toasty.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Logger.e(t, TAG, "onFailure: login unsuccessful");
            }
        });
    }

    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(getActivity(), "", "Loading..", true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            if (urls.length > 0) {
                String url = urls[0];

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                try {
                    okhttp3.Response response = okHttpClient.newCall(request).execute();
                    if (response != null && response.isSuccessful()) {
                        //If status is OK 200
                        JSONObject resultJson = new JSONObject(response.body().string());
                        //Extract data from JSON Response
                        int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;

                        String accessToken =
                                resultJson.has("access_token") ? resultJson.getString("access_token") : null;
                        Log.e("Tokenm", "" + accessToken);
                        prefs.putString("li_access_token", accessToken);
                        prefs.commit();

                        if (expiresIn > 0 && accessToken != null) {
                            Log.i("Authorize", "This is the access Token: "
                                    + accessToken
                                    + ". It will expires in "
                                    + expiresIn
                                    + " secs");

                            //Calculate date of expiration
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.SECOND, expiresIn);
                            long expireDate = calendar.getTimeInMillis();

                            ////Store both expires in and access token in shared preferences
                            SharedPreferences preferences =
                                    getActivity().getSharedPreferences("user_info", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putLong("expires", expireDate);
                            editor.putString("accessToken", accessToken);
                            editor.commit();
                            return true;
                        }
                    } else {
                        return false;
                    }
                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (ParseException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (status) {
                //If everything went Ok, change to another activity.
                prepareLinkedinData();
                //Logger.i("this is email"+getEmailOfLi());
                webView.setVisibility(View.INVISIBLE);

//                Intent startProfileActivity = new Intent(MainActivity.this, HomeActivity.class);
//                MainActivity.this.startActivity(startProfileActivity);
            }
        }
    }



}
