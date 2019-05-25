package com.gogaffl.gaffl.profile.view;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.view.AuthActivity;
import com.gogaffl.gaffl.home.view.HomeActivity;
import com.gogaffl.gaffl.profile.adapter.SectionsPagerAdapter;
import com.gogaffl.gaffl.profile.model.PhoneResponse;
import com.gogaffl.gaffl.profile.model.User;
import com.gogaffl.gaffl.profile.model.UserSendModel;
import com.gogaffl.gaffl.profile.repository.ProfileRepository;
import com.gogaffl.gaffl.profile.repository.ProfileServices;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModel;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModelFactory;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.InternetConnection;
import com.gogaffl.gaffl.tools.Tools;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private AppBarLayout mAppBarLayout;
    private ViewPager view_pager;
    private SectionsPagerAdapter viewPagerAdapter;
    private TabLayout tab_layout;
    private ProfileViewModel viewModel;
    private ImageView verifiedStatus, profilePic;
    private static UserSendModel mUserSendModel;
    private RatingBar ratingBar;
    private static User mUser;
    private boolean validity;
    FloatingActionButton floatingActionButton;
    private MaterialProgressBar mProgressBar;
    private static final int REQUEST_CODE_ORIGIN = 500;
    private View parent_view;
    private TextView profileTitle, profilePoints, title;
    private Button editNameBtn;
    private String userName, headline;
    private FragmentRefreshListener fragmentRefreshListener;
    boolean forceRefresh = false;
    private int intentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        viewModel = ViewModelProviders.of(this, new ProfileViewModelFactory(getApplication(),
                new ProfileRepository(getApplication()))).get(ProfileViewModel.class);
        initToolbar();
        ratingBar = findViewById(R.id.myRatingBar);
        profilePic = findViewById(R.id.profile_pic);
        title = findViewById(R.id.title);
        profileTitle = findViewById(R.id.title_profile);
        profilePoints = findViewById(R.id.points);
        mAppBarLayout = findViewById(R.id.app_bar_layout);
        floatingActionButton = findViewById(R.id.fab_profile_ok);
        mProgressBar = findViewById(R.id.progressBarID);
        parent_view = findViewById(R.id.content);
        editNameBtn = findViewById(R.id.basic_info_edit_button);


        //initNavigationMenu();
        ratingBar.setNumStars(5);

        editNameBtn.setOnClickListener(v -> {
            if (InternetConnection.isConnectedToInternet(this)) {
                showProfileDialog();
            } else {
                showNoInternetDialog();
            }
        });

        profilePic.setOnClickListener(v -> {
            if (InternetConnection.isConnectedToInternet(this)) {
                Intent i = new Intent(ProfileActivity.this, AuthActivity.class);
                i.putExtra("frgToLoad", 124);
                // Now start your activity
                UserSendModel.setUid(mUser.getId());
                UserSendModel.setEmail(mUser.getEmail());
                UserSendModel.setToken("NQDeoKf8Vgn_rLA5TpfG");
                startActivity(i);
                finish();
            } else {
                showNoInternetDialog();
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    floatingActionButton.hide();

                } else {
                    //Expanded
                    floatingActionButton.show();
                }
            }
        });

        getUserData();

        //handle situation for not getting data from server!
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mProgressBar.isShown()) {
                    showNoInternetDialog();
                }
            }
        }, 10000);

        if (intentFragment == 125) {
            floatingActionButton.hide();
        } else {
            floatingActionButton.show();
        }

        floatingActionButton.setOnClickListener(v -> {

            Intent intent = new
                    Intent(ProfileActivity.this,
                    HomeActivity.class);
            startActivity(intent);
            finish();

        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        (this).setFragmentRefreshListener(new ProfileActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {

                if (getFragmentRefreshListener() != null) {
                    initComponent();
                }
            }
        });
    }

    private void initToolbar() {

        intentFragment = UserSendModel.getScreenValue();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_p);
        if (intentFragment == 125) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        actionBar.setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(null);
        Tools.setSystemBarColor(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                //ActivityOptions options = ActivityOptions.makeCustomAnimation(this,R.anim.slide_in_bottom, 0);
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                this.overridePendingTransition(R.anim.slide_in_bottom, 0);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initComponent() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        setupViewPager(view_pager);

        tab_layout.setupWithViewPager(view_pager);

        tab_layout.getTabAt(0).setIcon(R.drawable.ic_person);
        tab_layout.getTabAt(1).setIcon(R.drawable.ic_verified_user);
        tab_layout.getTabAt(2).setIcon(R.drawable.ic_trips_white);
        tab_layout.getTabAt(3).setIcon(R.drawable.ic_star_border_white);

        // set icon color pre-selected
        tab_layout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
        tab_layout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    }
                }, 500);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tab.getIcon().setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
                    }
                }, 500);


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(AboutFragment.newInstance(), "About");    // index 0
        viewPagerAdapter.addFragment(TrustFragment.newInstance(), "Trust");   // index 1
        viewPagerAdapter.addFragment(TripsFragment.newInstance(), "Trips");    // index 2
        viewPagerAdapter.addFragment(ReviewsFragment.newInstance(), "Reviews"); // index 3
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }


    private void showNoInternetDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        (dialog.findViewById(R.id.bt_retry)).setOnClickListener(v -> {
            if (InternetConnection.isConnectedToInternet(this)) {
                dialog.dismiss();
                getUserData();
            } else {
                Toasty.error(this, "No Internet!", Toasty.LENGTH_SHORT).show();
            }
        });
        (dialog.findViewById(R.id.bt_open_settings)).setOnClickListener(v -> {
            // Here I've been added intent to open up data settings
            Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivityForResult(settingsIntent, 9003);
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void getUserData() {

        viewModel.getUserDataFromCloud().observe(this, user ->
        {
            mUser = user;
            title.setText(user.getHeadline());
            profileTitle.setText(user.getName());
            ratingBar.setRating(user.getRating());

            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.user_img)
                    .error(R.drawable.ic_signal_wifi_off)
                    .override(350, 350)
                    .priority(Priority.HIGH);

            Glide.with(ProfileActivity.this).
                    load(user.getPicture()).
                    apply(options).
                    into(profilePic);

            profilePoints.setText(user.getPoints() + " POINTS");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // yourMethod();

                    initComponent();
                    getIsLoaded();
                }
            }, 500);   //2 seconds


        });

        getIsLoaded();
    }


    public void getIsLoaded() {

        viewModel.getDataIsLoaded().observe(this, aBoolean -> {
            if (aBoolean) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        mProgressBar.setVisibility(View.GONE);
                    }
                }, 2000);


                //validity = aBoolean;
            }
        });
    }


    public void showProfileDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_profile_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final EditText etName = (EditText) dialog.findViewById(R.id.et_name);
        etName.setText(profileTitle.getText());
        final EditText etTitle = (EditText) dialog.findViewById(R.id.et_title);
        etTitle.setText(title.getText());
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

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private void postData(UserSendModel userSendModel) {


        Logger.addLogAdapter(new AndroidLogAdapter());

        headline = userSendModel.getTitle();
        userName = userSendModel.getName();
        int uID = mUser.getId();

        HashMap<String, String> paramss = new HashMap<>();

        if (!userName.isEmpty()) paramss.put("name", userName);
        if (!headline.isEmpty()) paramss.put("title", headline);


        ProfileServices services = RetrofitInstance.getRetrofitInstance().
                create(ProfileServices.class);

        Call<PhoneResponse> call = services.stringUpload("onirban.gaffl@gmail.com", "NQDeoKf8Vgn_rLA5TpfG", paramss, uID);

        call.enqueue(new Callback<PhoneResponse>() {
            @Override
            public void onResponse(Call<PhoneResponse> call, Response<PhoneResponse> response) {
                if (response.isSuccessful()) {
                    UserSendModel.setUpdateCache(true);
                    System.out.println("post about success!");
                    getUserData();
                    profileTitle.setText(mUser.getName());
                    title.setText(mUser.getHeadline());
                    view_pager.setAdapter(null);

                    initComponent();

                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                Toasty.error(ProfileActivity.this, "Update failed!", Toasty.LENGTH_SHORT).show();
            }
        });


    }


    public interface FragmentRefreshListener {
        void onRefresh();
    }


}
