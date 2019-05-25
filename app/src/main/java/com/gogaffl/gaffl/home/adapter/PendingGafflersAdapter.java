package com.gogaffl.gaffl.home.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.home.model.PendingRequest;
import com.gogaffl.gaffl.home.model.Trips;
import com.gogaffl.gaffl.home.repository.HomeServices;
import com.gogaffl.gaffl.home.view.TripsDetailsActivity;
import com.gogaffl.gaffl.profile.model.PhoneResponse;
import com.gogaffl.gaffl.rest.RetrofitInstance;
import com.gogaffl.gaffl.tools.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingGafflersAdapter extends RecyclerView.Adapter<PendingGafflersAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<PendingRequest> mPendingRequests;


    public PendingGafflersAdapter(Context context, ArrayList<PendingRequest> pendingRequests) {
        mContext = context;
        mPendingRequests = pendingRequests;
    }

    @NonNull
    @Override
    public PendingGafflersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_request_sample, viewGroup, false);

        return new PendingGafflersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingGafflersAdapter.MyViewHolder myViewHolder, int i) {
        final PendingRequest pendingRequest = mPendingRequests.get(i);
        myViewHolder.title.setText(pendingRequest.getName());
        if (Double.parseDouble(pendingRequest.getRating()) == 0) {
            myViewHolder.mRatingBar.setVisibility(View.GONE);
        } else {
            myViewHolder.mRatingBar.setRating((long) Double.parseDouble(pendingRequest.getRating()));
        }
        Glide.with(mContext).load(pendingRequest.getPictureUrl()).
                diskCacheStrategy(DiskCacheStrategy.ALL).into(myViewHolder.thumbnail);

        myViewHolder.mActionBtn.setOnClickListener(v -> {
            actionPendingUser(mPendingRequests.get(i).getId());
        });

        myViewHolder.mCancelBtn.setOnClickListener(v -> {
            showCancelDialog(mPendingRequests.get(i).getId());
        });

    }

    @Override
    public int getItemCount() {
        return mPendingRequests.size();
    }

    public void actionPendingUser(int id) {

        HomeServices service = RetrofitInstance.getRetrofitInstance().
                create(HomeServices.class);
        boolean forceRefresh = false;

        Call<PhoneResponse> getPlacesCall = service.acceptPendingUser(id, forceRefresh ? "no-cache" : null,
                "onirban.gaffl@gmail.com", "NQDeoKf8Vgn_rLA5TpfG");


        getPlacesCall.enqueue(new Callback<PhoneResponse>() {
            @Override
            public void onResponse(Call<PhoneResponse> call, Response<PhoneResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("Added!");
                    TripsDetailsActivity activity = (TripsDetailsActivity) mContext;
                    activity.getTrips();
                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                System.out.println("Failed!");
            }
        });

    }

    public void showCancelDialog(int id) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_cancel_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final EditText etReason = (EditText) dialog.findViewById(R.id.et_reason);
        final EditText etNote = (EditText) dialog.findViewById(R.id.et_note);
        String reason, note;
        note = etNote.getText().toString();
        reason = etReason.getText().toString();

        HomeServices service = RetrofitInstance.getRetrofitInstance().
                create(HomeServices.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cancel_reason", reason);
        hashMap.put("cancel_note", note);

        Call<PhoneResponse> getPlacesCall = service.denyPendingUser(hashMap, id,
                "onirban.gaffl@gmail.com", "NQDeoKf8Vgn_rLA5TpfG");


        getPlacesCall.enqueue(new Callback<PhoneResponse>() {
            @Override
            public void onResponse(Call<PhoneResponse> call, Response<PhoneResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("Canceled!");
                    TripsDetailsActivity activity = (TripsDetailsActivity) mContext;
                    activity.getTrips();
                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                System.out.println("Failed!");
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public Button mActionBtn, mCancelBtn;
        public ImageButton mCrossBtn;
        public RatingBar mRatingBar;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.user_name);
            mActionBtn = view.findViewById(R.id.connect_and_chat_btn);
            mCancelBtn = view.findViewById(R.id.cancel_button);
            thumbnail = view.findViewById(R.id.user_profile_pic);
            mCrossBtn = view.findViewById(R.id.cross_btn);
            mRatingBar = view.findViewById(R.id.myRatingBar_pending);
        }


    }
}
