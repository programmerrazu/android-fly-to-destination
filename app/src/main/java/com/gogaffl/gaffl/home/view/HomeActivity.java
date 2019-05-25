package com.gogaffl.gaffl.home.view;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.home.adapter.FindTripsAdapter;
import com.gogaffl.gaffl.home.model.Trips;
import com.gogaffl.gaffl.home.repository.HomeRepository;
import com.gogaffl.gaffl.home.viewmodel.HomeViewModel;
import com.gogaffl.gaffl.home.viewmodel.HomeViewModelFactory;
import com.gogaffl.gaffl.notification.adapter.FeedAdapter;
import com.gogaffl.gaffl.notification.model.Feed;
import com.gogaffl.gaffl.notification.service.MyService;
import com.gogaffl.gaffl.notification.service.MyWebSocketService;
import com.gogaffl.gaffl.notification.view.NotificationFragment;
import com.gogaffl.gaffl.profile.model.UserSendModel;
import com.gogaffl.gaffl.profile.repository.ProfileRepository;
import com.gogaffl.gaffl.profile.view.ProfileActivity;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModel;
import com.gogaffl.gaffl.profile.viewmodel.ProfileViewModelFactory;
import com.gogaffl.gaffl.tools.InternetConnection;
import com.gogaffl.gaffl.tools.MyNotification;
import com.gogaffl.gaffl.tools.Tools;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    boolean isNavigationHide = false;
    boolean isSearchBarHide = false;
    AutoCompleteTextView searchTextView;
    private HomeViewModel mViewModel;
    private static final String BACK_STACK_ROOT_TAG = "trip_listing_fragment";
    RecyclerView recyclerViewFt;
    private ProfileViewModel viewModel;
    private BottomNavigationView navigation;
    private View search_bar;
    private TextView mTextMessage, nameView, emailView;
    private ArrayList<String> placesname;
    private FindTripsFragment mFindTripsFragment;
    private HomeActivity mHomeActivity = this;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private ImageView picView;
    static int y;
    private RecyclerView.LayoutManager layoutManager;
    private static int firstVisibleInListView;
    private Switch mSwitch;

    private FrameLayout redCircle;
    private TextView countTextView;
    //private int alertCount = 0;

    private FeedAdapter adapter;
    private boolean networkOk;
    private List<Feed> mFeedsList;


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            mFeedsList = intent.getParcelableArrayListExtra(MyService.MY_SERVICE_PAYLOAD);
            displayDialog();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
        startService(new Intent(this, MyWebSocketService.class));
        networkOk = InternetConnection.isConnectedToInternet(this);

        BroadcastReceiver receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("msg");
                filterMessage(msg);
                Toast.makeText(context, "message: " + msg, Toast.LENGTH_SHORT).show();

            }
        };

        registerReceiver(receiver, new IntentFilter(MyWebSocketService.BROADCAST_ACTION));


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, FindTripsFragment.newInstance())
                    .commitNow();
        }
        Intent intent = getIntent();
        int notifyID = intent.getIntExtra("notifyID", 0);

        NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mgr.cancel(notifyID);

        mViewModel = ViewModelProviders.of(this, new HomeViewModelFactory(getApplication(),
                new HomeRepository(getApplication()))).get(HomeViewModel.class);

        viewModel = ViewModelProviders.of(this, new ProfileViewModelFactory(getApplication(),
                new ProfileRepository(getApplication()))).get(ProfileViewModel.class);

        searchTextView = (AutoCompleteTextView) findViewById(R.id.search_text);
        search_bar = (View) findViewById(R.id.search_bar);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigation.setSelectedItemId(R.id.navigation_find_trips);
        mSwitch = (Switch) findViewById(R.id.mySwitch);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initToolbar();
        initNavigationMenu();
        initComponent();

        getUser();

//        Fragment fragment = new FindTripsFragment();
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.frame_container, fragment)
//                    .commitNow();
//        }


        searchTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = searchTextView.getText().toString();

                getplaces(searchText);
                System.out.println("Text changed triggered");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = searchTextView.getText().toString();

                System.out.println("Text changed after triggered");
                getTripsOnLocationBased(searchText);

            }
        });

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));


    }

    String feedTime, userName, userPicture, userTitle;
    int newNotificationsCount;

    private void filterMessage(String msg) {

        try {
            JSONObject object = new JSONObject(msg);
            JSONObject messageObj = object.getJSONObject("message");
//            Toast.makeText(mHomeActivity, "MessageObject: " + messageObj, Toast.LENGTH_SHORT).show();

            if (isContain(messageObj, "flag")) {
                String flagType = messageObj.getString("flag");
                if (flagType.equalsIgnoreCase("feed")) {
                    if (isContain(messageObj, "feed_time")) {
                        feedTime = messageObj.getString("feed_time");

                    } else {
                        feedTime = "";
                    }

                    if (isContain(messageObj, "user_name")) {
                        userName = messageObj.getString("user_name");

                    } else {
                        userName = "";
                    }
                    if (isContain(messageObj, "user_picture")) {
                        userPicture = messageObj.getString("user_picture");
                     //   Picasso.get().load(userPicture).into(target);

                    } else {
                        userPicture = "";
                    }

                    if (isContain(messageObj, "trip_title")) {
                        userTitle = messageObj.getString("trip_title");

                    } else {
                        userTitle = "";
                    }
                } else if (flagType.equalsIgnoreCase("notification")) {
                    if (isContain(messageObj, "new_notifications_count")) {
                        newNotificationsCount = messageObj.getInt("new_notifications_count");
                        updateAlertIcon();
                    } else {
                        newNotificationsCount = 0;
                    }


                } else if (flagType.equalsIgnoreCase("message")) {

                }
            }


            //  MyNotification.createNotification(HomeActivity.this, feedTime, userName, userPicture, userTitle);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void getplaces(String results) {
        Logger.i("getplaces");

        mViewModel.getPlacesMutableLiveData(results).observe(this, this::initSearchResult);

    }

    public void initSearchResult(ArrayList<String> placesname) {

        Logger.i("initSearchResult");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, placesname);
        adapter.notifyDataSetChanged();
        searchTextView.setAdapter(adapter);

        searchTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = placesname.get(position);
                Toast.makeText(HomeActivity.this, name, Toast.LENGTH_SHORT).show();
                getTripsOnLocationBased(name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getTripsOnLocationBased(String location) {
        mViewModel.
                getTripsOnLocationMutableLiveData(location)
                .observe(this,
                        tripsArrayList -> generateTripsListing(tripsArrayList));
    }

    @Override
    public void onBackPressed() {
    }

    private void initComponent() {


//        NestedScrollView nested_content = (NestedScrollView) findViewById(R.id.nested_scroll_view);
//        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY < oldScrollY) { // up
//                    animateNavigation(false);
//                    animateSearchBar(false);
//                }
//                if (scrollY > oldScrollY) { // down
//                    animateNavigation(true);
//                    animateSearchBar(true);
//                }
//            }
//        });

        // display image
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_1), R.drawable.image_8);
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_2), R.drawable.image_9);
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_3), R.drawable.image_15);
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_4), R.drawable.image_14);
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_5), R.drawable.image_12);
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_6), R.drawable.image_2);
//        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_7), R.drawable.image_5);


//        ((ImageButton) findViewById(R.id.bt_menu)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }

    private void animateNavigation(final boolean hide) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
        isNavigationHide = hide;
        int moveY = hide ? (2 * navigation.getHeight()) : 0;
        navigation.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    private void animateSearchBar(final boolean hide) {
        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return;
        isSearchBarHide = hide;
        int moveY = hide ? -(2 * search_bar.getHeight()) : 0;
        search_bar.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    private void initToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(null);
        Tools.setSystemBarColor(this);
    }

    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        View headerView = nav_view.getHeaderView(0);
        nameView = headerView.findViewById(R.id.user_name_drawer);
        emailView = headerView.findViewById(R.id.email_drawer);
        picView = headerView.findViewById(R.id.avatar_drawer);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        Intent intent = new Intent(
                                HomeActivity.this,
                                ProfileActivity.class);
                        UserSendModel.setScreenValue(125);
                        startActivity(intent);
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_wishlist:
                        return true;
                    case R.id.nav_settings:
                        return true;
                    case R.id.nav_help:
                        return true;
                    case R.id.nav_about:
                        return true;
                }

                drawer.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.action_notification);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

        redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_inbox:

                //alertCount = (alertCount + 1) % 12; // cycle through 0 - 10
                //  updateAlertIcon();
                String img="https://cdn.pixabay.com/photo/2014/09/17/20/03/profile-449912_960_720.jpg";
                Picasso.get().load(img).into(target);

                return true;
            case R.id.action_notification:
                //  newNotificationsCount = 0;
                //    updateAlertIcon();

                //  startActivity(new Intent(this, NotifyActivity.class));
                if (networkOk) {
                    Intent intents = new Intent(this, MyService.class);
                    startService(intents);
                } else {
//            showSnackbar();
                    Toast.makeText(this, "no internet!", Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void displayDialog() {
        if (mFeedsList != null) {
            NotificationFragment notificationFragment =  NotificationFragment.newInstance(mFeedsList);
            notificationFragment.show(getFragmentManager(), "NOTIFICATION_FRAGMENT");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    private void updateAlertIcon() {
        // if alert count extends into two digits, just show the red circle
       /* if (0 < alertCount && alertCount < 11) {
            countTextView.setText(String.valueOf(alertCount));
        } else {
            countTextView.setText("");
        }

        redCircle.setVisibility((alertCount > 0) ? VISIBLE : GONE);*/
        if (0 < newNotificationsCount) {
            countTextView.setText(String.valueOf(newNotificationsCount));
        } else {
            countTextView.setText("");
        }

        redCircle.setVisibility((newNotificationsCount > 0) ? VISIBLE : GONE);
    }


    public void changeFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_to_right);
        transaction.replace(R.id.frame_container, frag);
        transaction.addToBackStack(BACK_STACK_ROOT_TAG);
        transaction.commit();
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment;
        switch (menuItem.getItemId()) {
            case R.id.navigation_start_trip:
                fragment = new StartTripsFragment();
                changeFragment(fragment);
                return true;
            case R.id.navigation_find_trips:
                mFindTripsFragment = new FindTripsFragment();
                fragment = mFindTripsFragment;
                changeFragment(fragment);
                return true;
            case R.id.navigation_Discover:

                fragment = new DiscoverFragment();
                changeFragment(fragment);
                return true;
            case R.id.navigation_splits:
                //mTextMessage.setText(item.getTitle());
                fragment = new SplitsFragment();
                changeFragment(fragment);
                return true;
        }
        return false;
    }

    public void getUser() {
        viewModel.getUserDataFromCloud().observe(this, user ->
        {
            if (user != null) {
                nameView.setText(user.getName());
                emailView.setText(user.getEmail());


                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.user_img)
                        .error(R.drawable.ic_signal_wifi_off)
                        .override(350, 350)
                        .priority(Priority.HIGH);

                Glide.with(HomeActivity.this).
                        load(user.getPicture()).
                        apply(options).
                        into(picView);
            }
        });
    }

    private void generateTripsListing(ArrayList<Trips> trips) {
        if (trips != null) {

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerViewFt = findViewById(R.id.find_trips_recycler_view);
            recyclerViewFt.setLayoutManager(layoutManager);
            FindTripsAdapter mFindTripsAdapter = new FindTripsAdapter(
                    getApplicationContext(), trips, this);
            recyclerViewFt.setItemAnimator(new DefaultItemAnimator());
            mFindTripsAdapter.notifyDataSetChanged();
            recyclerViewFt.setHasFixedSize(true);
            recyclerViewFt.setAdapter(mFindTripsAdapter);
            recyclerViewFt.setItemViewCacheSize(20);
            recyclerViewFt.setDrawingCacheEnabled(true);
            recyclerViewFt.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//            recyclerViewFt.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    mSwitch =(Switch) findViewById(R.id.mySwitch);
//                    toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (dy > 0) {
//                                mSwitch.setVisibility(View.GONE);
//                                Toasty.success(getApplicationContext(),"scrolling up",Toasty.LENGTH_SHORT).show();
//                            }
//                        }
//                    },100);
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(dy<=0){
//                                // Scrolling down
//                                mSwitch.setVisibility(View.VISIBLE);
//                                Toasty.success(getApplicationContext(),"scrolling down",Toasty.LENGTH_SHORT).show();
//                            }
//                        }
//                    },500);
//
//
//                    y=dy;
//                }
//
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//
//                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                        // Do something
//                    } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                        if(y<=0){
//
//                        }
//
//                    } else {
//                        // Do something
//                    }
//                }
//
//            });

        } else {
            Toast.makeText(getApplicationContext(), "pending user is null", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isContain(JSONObject jsonObject, String key) {

        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }


    private Target target = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            Bitmap circleBitmap = Tools.getCroppedBitmap(bitmap);
            // imgShow.setImageBitmap(circleBitmap);
         //   MyNotification.createNotification(HomeActivity.this, feedTime, userName, circleBitmap, userTitle);
            MyNotification.createNotification(HomeActivity.this, feedTime, userName, circleBitmap, userTitle);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
}
