package com.gogaffl.gaffl.profile.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.Toast;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.profile.model.Example;
import com.gogaffl.gaffl.profile.model.Interest;
import com.gogaffl.gaffl.profile.model.InterestModel;
import com.gogaffl.gaffl.profile.model.UserSendModel;
import com.gogaffl.gaffl.profile.repository.ProfileServices;
import com.gogaffl.gaffl.profile.viewmodel.InterestViewModel;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.MySharedPreferences;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class InterestFragment extends Fragment {

    private InterestViewModel mViewModel;
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    MySharedPreferences prefs;
    ListView listView;
    private InterestAdapter adapter;
    private AboutFragment mAboutFragment;

    public static InterestFragment newInstance() {
        return new InterestFragment();
    }

    public static RequestBody createRequestBody(@NonNull String s) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), s);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InterestViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interest_fragment, container, false);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_interest);
        FloatingActionButton floatingBackButton = view.findViewById(R.id.fab_interest_close);
        // initiate a ListView
        listView = (ListView) view.findViewById(R.id.interest_list);
        // set the adapter to fill the data in ListView

        floatingBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        if (UserSendModel.isInterestEdit()) {
            floatingBackButton.show();
        }


        floatingActionButton.setOnClickListener(v -> {

            int count = InterestModel.getCount();

            if (count > 0) {
                postInterest();
            } else {
                Toasty.error(getContext(), "select atleast one interest!", Toasty.LENGTH_SHORT).show();
            }
        });

        interestsGetMethod();

        return view;
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

    public void interestsGetMethod() {
        //showProgress();
        ProfileServices service = RetrofitInstance.getRetrofitInstance().create(ProfileServices.class);
        Logger.addLogAdapter(new AndroidLogAdapter());

        prefs = MySharedPreferences.getInstance(getActivity(),
                "Gaffl_Prefs");

        final String token = prefs.getString("li_access_token", "default");
        final String user = prefs.getString("li_access_token", "default");


        Call<Example> call = service.getInterests("onirban.gaffl@gmail.com", "NQDeoKf8Vgn_rLA5TpfG");
        //Call<Example> call = service.getInterests(user,token);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {


                if (response.isSuccessful()) {

                    // Do awesome stuff
                    if (response.body() != null) {

                        generateInterestList(getContext(), response.body().getData().getInterests());
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    Gson gson = new Gson();
                    Toasty.error(getActivity(),
                            "failed", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Logger.e(t, TAG, "onFailure: login unsuccessful");
            }
        });
    }


    private void generateInterestList(Context mContext, ArrayList<Interest> proDataList) {

        if (proDataList != null) {
            adapter = new InterestAdapter(proDataList, mContext);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(mContext, "Interest is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void postInterest() {
        //showProgress();

        ProfileServices service = RetrofitInstance.getRetrofitInstance().create(ProfileServices.class);
        Logger.addLogAdapter(new AndroidLogAdapter());


        // creating JSONObject
        org.json.JSONObject jo = new JSONObject();

        List<Integer> myList = new ArrayList<>();
        HashMap<String, List<Integer>> interests = new HashMap<>();

        // putting data to JSONObject
        try {

            for (int i = 0; i < InterestModel.getInterest().size(); i++) {
                int key = InterestModel.getInterest().keyAt(i);
                // get the object by the key.
                int obj = InterestModel.getInterest().get(key);
                myList.add(obj);
            }
            jo.put("interests", myList);
            Logger.json(String.valueOf(jo));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        interests.put("interests", myList);


        Call<Void> call = service.insertInterestData("onirban.gaffl@gmail.com", "NQDeoKf8Vgn_rLA5TpfG", interests);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {

                    UserSendModel.setUpdateCache(true);

                    if (mAboutFragment.TAG.equals("aboutfrag")) {
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        InterestModel.setUpdate(true);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        changeFragment(R.anim.enter_from_right, new PhoneFragment());
                    }

                    Toasty.success(getActivity(),
                            "interest send!!", Toasty.LENGTH_SHORT).show();
                } else {
                    Gson gson = new Gson();
                    Toasty.error(getActivity(),
                            "failed", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Logger.e(t, TAG, "onFailure: login unsuccessful");
            }
        });
    }

}
