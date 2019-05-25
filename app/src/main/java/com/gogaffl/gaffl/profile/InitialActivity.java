package com.gogaffl.gaffl.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.profile.view.InterestFragment;
import com.gogaffl.gaffl.profile.view.PhoneFragment;

public class InitialActivity extends AppCompatActivity {

    int intentFragment;



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_profile, InterestFragment.newInstance())
                    .commitNow();
            intentFragment = getIntent().getExtras().getInt("frgToLoad");
        }


        switch (intentFragment) {
            case 123:
                // Load corresponding fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_profile, PhoneFragment.newInstance())
                        .commitNow();
                break;

        }

    }


}
