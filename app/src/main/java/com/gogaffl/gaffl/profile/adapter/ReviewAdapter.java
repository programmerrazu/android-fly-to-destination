package com.gogaffl.gaffl.profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.profile.model.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Review> mReviewList;


    public ReviewAdapter(Context context, ArrayList<Review> reviewList) {
        mContext = context;
        mReviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_sample_layout, viewGroup, false);

        return new ReviewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder myViewHolder, int i) {
        final Review review = mReviewList.get(i);
        myViewHolder.title.setText(review.getRaterName());
        myViewHolder.date.setText(review.getRatingTime());
        myViewHolder.speech.setText(review.getRatingText());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_img)
                .error(R.drawable.ic_signal_wifi_off)
                .priority(Priority.HIGH);


        Glide.with(mContext).load(review.getRaterPictureUrl()).apply(options).
                thumbnail(0.5f).into(myViewHolder.thumbnail);
        myViewHolder.myRatingBar.setRating(review.getRating());

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, speech;
        public ImageView thumbnail;
        public RatingBar myRatingBar;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.reviewer_title_text);
            date = view.findViewById(R.id.review_time_text);
            speech = view.findViewById(R.id.review_speech_text);
            myRatingBar = view.findViewById(R.id.reviewRatingBar);
            thumbnail = view.findViewById(R.id.reviewer_owner_profile_pic);
        }
    }
}
