package com.gogaffl.gaffl.profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.profile.model.Interest;

import java.util.ArrayList;

public class ProfileInterestAdapter extends BaseAdapter {

    private final Context mContext;
    LayoutInflater inflter;
    private ArrayList<Interest> mInterests;

    public ProfileInterestAdapter(ArrayList<Interest> interestNames, Context context) {
        mContext = context;
        this.mInterests = interestNames;
    }


    @Override
    public int getCount() {
        return mInterests.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Interest interest = mInterests.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.list_interest_view, null);
        }
        final TextView simpleTextView = convertView.findViewById(R.id.interest_tv);
        simpleTextView.setText(interest.getName());

        return convertView;
    }
}