package com.gogaffl.gaffl.authentication.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.tools.MySharedPreferences;
import com.orhanobut.logger.Logger;

public class AuthActivity extends AppCompatActivity {

    private MySharedPreferences prefs;
    private Intent mIntent;
    int intentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AuthFragment.newInstance())
                    //.replace(R.id.container, ImageFragment.newInstance())
                    .commitNow();
        }
        // TODO: call viewmodel logic codes here

        prefs = MySharedPreferences.getInstance(AuthActivity.this,
                "GAFFL_Prefs");
        boolean isTokenExist = prefs.getBoolean("isTokenExist", false);
        String token = prefs.getString("access_token", "default_token");
        Logger.i(token);

//        if (isTokenExist) {
//            mIntent = new Intent(AuthActivity.this, TestActivity.class);
//            startActivity(mIntent);
//            finish();
//        }
//        if (isTokenExist && !isProfileExist) {
//            mIntent = new Intent(getActivity(), ProfileFormActivity.class);
//            startActivity(mIntent);
//            finish();
//        }

        if (getIntent().getExtras() != null) {
            //do here
            intentFragment = getIntent().getExtras().getInt("frgToLoad");

            switch (intentFragment) {
                case 124:
                    // Load corresponding fragment
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ImageFragment.newInstance())
                            .commitNow();
                    break;

            }
        }

    }

    Fragment fragment;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Check if the fragment is an instance of the right fragment
        if (fragment instanceof RegistrationFragment) {
            RegistrationFragment my = (RegistrationFragment) fragment;
            // Pass intent or its data to the fragment's method
            my.onNewIntent(intent);
        }

    }

}
