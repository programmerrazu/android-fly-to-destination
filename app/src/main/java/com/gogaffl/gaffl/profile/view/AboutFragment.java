package com.gogaffl.gaffl.profile.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.profile.InitialActivity;
import com.gogaffl.gaffl.profile.adapter.ProfileInterestAdapter;
import com.gogaffl.gaffl.profile.model.Country;
import com.gogaffl.gaffl.profile.model.CountryList;
import com.gogaffl.gaffl.profile.model.Interest;
import com.gogaffl.gaffl.profile.model.InterestModel;
import com.gogaffl.gaffl.profile.model.PhoneResponse;
import com.gogaffl.gaffl.profile.model.StateList;
import com.gogaffl.gaffl.profile.model.States;
import com.gogaffl.gaffl.profile.model.User;
import com.gogaffl.gaffl.profile.model.UserSendModel;
import com.gogaffl.gaffl.profile.repository.ProfileRepository;
import com.gogaffl.gaffl.profile.repository.ProfileServices;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModel;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModelFactory;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.InternetConnection;
import com.gogaffl.gaffl.tools.MyGridView;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutFragment extends Fragment {



    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    MyGridView gridView;
    private ProfileViewModel viewModel;
    private String status;
    private ArrayList<Interest> mInterestArrayList = null;
    public static final String TAG = "aboutfrag";
    private static final int REQUEST_CODE_ORIGIN = 500;
    private TextView location, joined, gender, mail, response_status, aboutTv, location_dialog_tv;
    private static InterestModel sInterestModel;
    private Call<PhoneResponse> call;
    private Map<String, RequestBody> params;
    private static User mUser;
    private CountryList mCountryList;
    private StateList mStateList;
    private UserSendModel mUserSendModel;
    private Integer selectedId;
    private String birthdate, sex, userName, headline, about;
    private int stateID;
    private int countryID;
    private ProfileInterestAdapter profileInterestAdapter;
    private Button aboutButton, basicButton, interestButton;


    public AboutFragment() {
    }



    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    public ArrayList<Interest> getInterestArrayList() {
        ArrayList<Interest> interestArrayList = mUser.getInterests();

        return interestArrayList;
    }

    public static RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), file);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), s);
    }

    public void initFrag(User user) {
        if (user != null) {
            mUser = user;
            sInterestModel.setInterests(mUser.getInterests());
            mInterestArrayList = user.getInterests();
            int value = user.getResponseTime();
            location.setText(user.getAddress());
            joined.setText(user.getJoinedIn());
            gender.setText(user.getGender() + ", Age " + user.getAge());
            //gender.setText(user.getGender() + ", Age " + user.getAge());
            mail.setText(user.getEmail());
            if (value < 24) {
                //excelent
                //green
                status = "Excellent";
                response_status.setBackgroundTintList(getResources().getColorStateList(
                        R.color.bs_green));
            } else if (value < 48) {
                //good
                //blue
                status = "Good";
                response_status.setBackgroundTintList(getResources().getColorStateList(
                        R.color.bs_blue));
            } else if (value < 72) {
                //average
                //yellow
                status = "Average";
                response_status.setBackgroundTintList(getResources().getColorStateList(
                        R.color.bs_yellow));

            } else if (value > 72) {
                //poor
                //red
                status = "Poor";
                response_status.setBackgroundTintList(getResources().getColorStateList(
                        R.color.bs_red));

            }
            response_status.setText(status);
            if (user.getAbout().length() < 2) aboutTv.setText("write your bio...");
            else aboutTv.setText(user.getAbout());

            if (mInterestArrayList != null) {
                profileInterestAdapter = new ProfileInterestAdapter(
                        mInterestArrayList, getActivity());
                profileInterestAdapter.notifyDataSetChanged();
                gridView.setAdapter(profileInterestAdapter);

            }

            getCountriesFromCloud();

        }
    }

    public void refreshGrid() {
        if (mInterestArrayList != null) {
            profileInterestAdapter = new ProfileInterestAdapter(
                    mInterestArrayList, getActivity());
            profileInterestAdapter.notifyDataSetChanged();
            gridView.setAdapter(profileInterestAdapter);

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this, new ProfileViewModelFactory(getActivity().getApplication(),
                new ProfileRepository(getActivity().getApplication()))).get(ProfileViewModel.class);
        mCountryList = new CountryList();

        viewModel.getUserDataFromCloud().observe(getViewLifecycleOwner(), this::initFrag);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs_about, container, false);

        location = view.findViewById(R.id.location_view);
        joined = view.findViewById(R.id.joined_view);
        gender = view.findViewById(R.id.gender_age_view);
        mail = view.findViewById(R.id.email_view);
        response_status = view.findViewById(R.id.response_status);
        aboutTv = view.findViewById(R.id.about_view);
        gridView = view.findViewById(R.id.interest_gridview);
        aboutButton = view.findViewById(R.id.about_edit_button);
        basicButton = view.findViewById(R.id.basic_info_edit_button);
        interestButton = view.findViewById(R.id.interest_edit_button);


        aboutButton.setOnClickListener(v -> {
            if (InternetConnection.isConnectedToInternet(getContext())) {
                showAboutDialog();
            } else {
                showNoInternetDialog();
            }
        });
        basicButton.setOnClickListener(v -> {
            if (InternetConnection.isConnectedToInternet(getContext())) {
                showBasicDialog();
            } else {
                showNoInternetDialog();
            }
        });
        interestButton.setOnClickListener(v -> {
            if (InternetConnection.isConnectedToInternet(getContext())) {
                getInterestPage();
            } else {
                showNoInternetDialog();
            }
        });


        return view;
    }

    private void getInterestPage() {
        Intent intent = new Intent(getActivity(), InitialActivity.class);
        intent.putExtra("frgToLoad", 126);
        startActivity(intent);
        getActivity().finish();
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

    public void changeFragment(int id, Fragment frag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(id, 0);
        transaction.remove(newInstance());
        transaction.replace(R.id.container_profile, frag);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void showNoInternetDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        (dialog.findViewById(R.id.bt_retry)).setOnClickListener(v -> {
            if (InternetConnection.isConnectedToInternet(getContext())) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_open_settings)).setOnClickListener(v -> {
            // Here I've been added intent to open up data settings
            Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivityForResult(settingsIntent, 9003);
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
                    System.out.println("post basic info success!");
                    UserSendModel.setUpdateCache(true);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(AboutFragment.this).attach(AboutFragment.this).commit();

                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                Toasty.error(getActivity(), "Basic info setting failed!", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void postData(UserSendModel userSendModel) {

        Logger.addLogAdapter(new AndroidLogAdapter());

        if (userSendModel.getAbout() == null) {
            userName = userSendModel.getName();
            headline = userSendModel.getTitle();
        } else {
            about = userSendModel.getAbout();
        }


        int uID = mUser.getId();

        HashMap<String, String> paramss = new HashMap<>();
        if (!about.isEmpty()) paramss.put("intro", about);

        ProfileServices services = RetrofitInstance.getRetrofitInstance().
                create(ProfileServices.class);
        Call<PhoneResponse> call = services.stringUpload("onirban.gaffl@gmail.com", "NQDeoKf8Vgn_rLA5TpfG", paramss, uID);

        call.enqueue(new Callback<PhoneResponse>() {
            @Override
            public void onResponse(Call<PhoneResponse> call, Response<PhoneResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("post about success!");
                    UserSendModel.setUpdateCache(true);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(AboutFragment.this).attach(AboutFragment.this).commit();
                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                Toasty.error(getActivity(), "Update failed!", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshFrag() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(AboutFragment.newInstance()).attach(AboutFragment.newInstance()).commit();
    }

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

    public void getStatesFromCloud(int code) {
        ProfileServices services = RetrofitInstance.getRetrofitInstance().
                create(ProfileServices.class);
        Call<StateList> getResponseCall = services.getState(code, "john@local.com", "VGbp9W6tWSQHyHZTas7g"      );
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


}