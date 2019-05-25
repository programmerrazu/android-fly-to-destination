package com.gogaffl.gaffl.notification.view;


import android.app.DialogFragment;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.notification.adapter.FeedAdapter;
import com.gogaffl.gaffl.notification.model.Feed;
import com.gogaffl.gaffl.tools.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends DialogFragment {

    private TextView tvMarkAllAsRead;
    private RecyclerView rv;
    private FeedAdapter adapter;
    private List<Feed> mFeedsList;
    public static final String NOTIFICATION_KEY = "notification_key";

    public static NotificationFragment newInstance(List<Feed> mFeedsList) {
        
        Bundle args = new Bundle();
        args.putParcelableArrayList(NotificationFragment.NOTIFICATION_KEY, (ArrayList<? extends Parcelable>) mFeedsList);
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_notiy, container);

        rv = (RecyclerView) rootView.findViewById(R.id.rvItems);
        tvMarkAllAsRead = (TextView) rootView.findViewById(R.id.tvMarkAllAsRead);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        this.getDialog().setTitle("Notification Show");
        Bundle arguments = getArguments();
        if (arguments != null) {
            mFeedsList = arguments.getParcelableArrayList(NOTIFICATION_KEY);
            adapter = new FeedAdapter(mFeedsList, MyApplication.getContext());
            rv.setAdapter(adapter);
        }

        tvMarkAllAsRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyApplication.getContext(), "Mark as all read", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * .90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.80);
        // Set the width of the dialog proportional to 75% of the screen width
        // window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        // window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, (int) (size.x * 20));
        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}
