package com.gogaffl.gaffl.notification.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.notification.model.Feed;
import com.gogaffl.gaffl.notification.service.MyWebService;
import com.gogaffl.gaffl.tools.AppConstants;
import com.gogaffl.gaffl.tools.PrefManager;
import com.gogaffl.gaffl.tools.Tools;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Feed> dataSet;
    Context mContext;
    int total_types;
    PrefManager manager;

    public static class NotifyAcceptJoinRequestHolder extends RecyclerView.ViewHolder {


        TextView tvAcceptTitle, tvAcceptTime, tvChatNowEvent;
        ImageView imgPerson;
        CardView itemContainer;


        public NotifyAcceptJoinRequestHolder(View itemView) {
            super(itemView);

            this.tvAcceptTitle = (TextView) itemView.findViewById(R.id.tvAcceptTitle);
            this.tvAcceptTime = (TextView) itemView.findViewById(R.id.tvAcceptTime);
            this.tvChatNowEvent = (TextView) itemView.findViewById(R.id.tvChatNowEvent);
            this.imgPerson = (ImageView) itemView.findViewById(R.id.imgPerson);
            this.itemContainer = (CardView) itemView.findViewById(R.id.itemContainer);

        }

    }

    public static class NotifyCancelJoinRequestViewHolder extends RecyclerView.ViewHolder {


        TextView tvCancelJoinTitle, tvCancelTime, tvNoteUser, tvNote, tvViewTripsEvent;
        ImageView imgPerson;
        CardView itemContainer;


        public NotifyCancelJoinRequestViewHolder(View itemView) {
            super(itemView);

            this.tvCancelJoinTitle = (TextView) itemView.findViewById(R.id.tvCancelJoinTitle);
            this.tvCancelTime = (TextView) itemView.findViewById(R.id.tvCancelTime);
            this.tvNoteUser = (TextView) itemView.findViewById(R.id.tvNoteUser);
            this.tvNote = (TextView) itemView.findViewById(R.id.tvNote);
            this.tvViewTripsEvent = (TextView) itemView.findViewById(R.id.tvViewTripsEvent);
            this.imgPerson = (ImageView) itemView.findViewById(R.id.imgPerson);
            this.itemContainer = (CardView) itemView.findViewById(R.id.itemContainer);

        }

    }

    public static class NotifyInviteJoinTripViewHolder extends RecyclerView.ViewHolder {


        TextView tvTitleInviteJoin, tvInviteJoinTime, tvInviteJoinDescription, tvJoinEvent, tvCancelEvent;
        ImageView imgPerson;
        CardView itemContainer;

        public NotifyInviteJoinTripViewHolder(View itemView) {
            super(itemView);
            this.tvTitleInviteJoin = (TextView) itemView.findViewById(R.id.tvTitleInviteJoinTrip);
            this.tvInviteJoinTime = (TextView) itemView.findViewById(R.id.tvInviteJoinTime);
            this.tvInviteJoinDescription = (TextView) itemView.findViewById(R.id.tvInviteJoinDescription);
            this.tvJoinEvent = (TextView) itemView.findViewById(R.id.tvJoinEvent);
            this.tvCancelEvent = (TextView) itemView.findViewById(R.id.tvCancelEvent);
            this.itemContainer = (CardView) itemView.findViewById(R.id.itemContainer);
            this.imgPerson = (ImageView) itemView.findViewById(R.id.imgPerson);

        }

    }

    public static class NotifyJoinTripViewHolder extends RecyclerView.ViewHolder {


        TextView tvTitleJoinTrips, tvJoinTripTime;
        ImageView imgPerson;
        CardView itemContainer;

        public NotifyJoinTripViewHolder(View itemView) {
            super(itemView);

            this.tvTitleJoinTrips = (TextView) itemView.findViewById(R.id.tvTitleJoinTrips);
            this.tvJoinTripTime = (TextView) itemView.findViewById(R.id.tvJoinTripTime);
            this.itemContainer = (CardView) itemView.findViewById(R.id.itemContainer);
            this.imgPerson = (ImageView) itemView.findViewById(R.id.imgPerson);

        }

    }

    public static class NotifyTripJoinRequestViewHolder extends RecyclerView.ViewHolder {


        TextView tvTitleNotifyTripJoinRequest, tvNotifyTripJoinRequestTime, tvRating, tvAddress, tvAge, tvInterests, tvEmail, tvGoogle, tvFacebook, tvLinkedin, tvPhone, tvNoteUser, tvNote, tvConnectChat, tvCancelled;
        ImageView imgUser;

        CardView itemContainer;

        public NotifyTripJoinRequestViewHolder(View itemView) {
            super(itemView);

            this.tvTitleNotifyTripJoinRequest = (TextView) itemView.findViewById(R.id.tvTitleNotifyTripJoinRequest);
            this.tvNotifyTripJoinRequestTime = (TextView) itemView.findViewById(R.id.tvNotifyTripJoinRequestTime);
            this.tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            this.tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            this.tvAge = (TextView) itemView.findViewById(R.id.tvAge);
            this.tvInterests = (TextView) itemView.findViewById(R.id.tvInterests);
            this.tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            this.tvGoogle = (TextView) itemView.findViewById(R.id.tvGoogle);
            this.tvFacebook = (TextView) itemView.findViewById(R.id.tvFacebook);
            this.tvLinkedin = (TextView) itemView.findViewById(R.id.tvLinkedin);
            this.tvPhone = (TextView) itemView.findViewById(R.id.tvPhone);
            this.tvNoteUser = (TextView) itemView.findViewById(R.id.tvNoteUser);
            this.tvNote = (TextView) itemView.findViewById(R.id.tvNote);

            this.tvConnectChat = (TextView) itemView.findViewById(R.id.tvConnectChat);
            this.tvCancelled = (TextView) itemView.findViewById(R.id.tvCancelled);
            this.imgUser = (ImageView) itemView.findViewById(R.id.imgPerson);
            this.itemContainer = (CardView) itemView.findViewById(R.id.itemContainer);

        }
    }

    public static class NotifyTripCreatorEditTripViewHolder extends RecyclerView.ViewHolder {


        TextView tvTitleTripCreator, tvTripCreatorTime, tvTripCreatorDescription;
        ImageView imgPerson;
        CardView itemContainer;

        public NotifyTripCreatorEditTripViewHolder(View itemView) {
            super(itemView);

            this.tvTitleTripCreator = (TextView) itemView.findViewById(R.id.tvTitleTripCreator);
            this.tvTripCreatorTime = (TextView) itemView.findViewById(R.id.tvTripCreatorTime);
            this.tvTripCreatorDescription = (TextView) itemView.findViewById(R.id.tvTripCreatorDescription);
            this.imgPerson = (ImageView) itemView.findViewById(R.id.imgPerson);
            this.itemContainer = (CardView) itemView.findViewById(R.id.itemContainer);
        }

    }

    public FeedAdapter(List<Feed> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
        manager = new PrefManager(mContext);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Feed.ACCEPT_THE_JOIN_REQUEST_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_accept_join_request, parent, false);
                return new NotifyAcceptJoinRequestHolder(view);
            case Feed.CANCEL_THE_JOIN_REQUEST_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_cancel_join_request, parent, false);
                return new NotifyCancelJoinRequestViewHolder(view);
            case Feed.INVITES_TO_JOIN_THE_TRIP_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_invite_join_trip, parent, false);
                return new NotifyInviteJoinTripViewHolder(view);
            case Feed.JOIN_THE_TRIP_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_join_trips, parent, false);
                return new NotifyJoinTripViewHolder(view);
            case Feed.TRIP_JOIN_REQUEST_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_trip_join_request, parent, false);
                return new NotifyTripJoinRequestViewHolder(view);
            case Feed.TRIP_CREATOR_EDIT_TRIP_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_tripcreator_edit_trip, parent, false);
                return new NotifyTripCreatorEditTripViewHolder(view);
        }
        return null;


    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).getFeedType()) {
            case "join_request":
                return Feed.TRIP_JOIN_REQUEST_TYPE;
            case "accepted":
                return Feed.ACCEPT_THE_JOIN_REQUEST_TYPE;
            case "rejected":
                return Feed.CANCEL_THE_JOIN_REQUEST_TYPE;
            case "new_member":
                return Feed.JOIN_THE_TRIP_TYPE;
            case "trip_invite":
                return Feed.INVITES_TO_JOIN_THE_TRIP_TYPE;
            case "plan_update":
                return Feed.TRIP_CREATOR_EDIT_TRIP_TYPE;
            default:
                return -1;
        }


    }


    String imageURL;
    String fixedText;
    boolean isChangeColor = true;
    List<String> feedIdStrings;

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Feed object = dataSet.get(listPosition);

        SpannableStringBuilder builder;
        if (object != null) {
            switch (object.getFeedType()) {
                case "join_request":
                    fixedText = " has requested to join your ";

                    builder = Tools.getSpannableStringBuilder(object, fixedText);
                    imageURL = object.getUserPicture();
                    ((NotifyTripJoinRequestViewHolder) holder).tvTitleNotifyTripJoinRequest.setText(builder);
                    ((NotifyTripJoinRequestViewHolder) holder).tvNotifyTripJoinRequestTime.setText(object.getFeedTime());
                    ((NotifyTripJoinRequestViewHolder) holder).tvTitleNotifyTripJoinRequest.setMovementMethod(LinkMovementMethod.getInstance());
                    Picasso.get().load(imageURL).into(((NotifyTripJoinRequestViewHolder) holder).imgUser);
                    if (object.getUserRating() != null) {

                        ((NotifyTripJoinRequestViewHolder) holder).tvRating.setText(object.getUserRating());
                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).tvRating.setVisibility(View.GONE);

                    }

                    if (object.getUserAddress() != null) {
                        ((NotifyTripJoinRequestViewHolder) holder).tvAddress.setText(object.getUserAddress());
                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).tvAddress.setVisibility(View.GONE);
                    }
                    if (object.getUserGender() != null & object.getUserAge()!= null) {

                        ((NotifyTripJoinRequestViewHolder) holder).tvAge.setVisibility(View.VISIBLE);
                        ((NotifyTripJoinRequestViewHolder) holder).tvAge.setText("Age " + object.getUserAge() + ", " + object.getUserGender());
                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).tvAge.setVisibility(View.GONE);
                    }
                    List<String> interests = object.getUserInterests();
                    if (interests.size() > 0) {
                        StringBuilder strBuilder = new StringBuilder();
                        for (String temp : interests
                        ) {

                            strBuilder.append(temp + ",");

                            String withoutLastComma = strBuilder.substring(0, strBuilder.length() - ", ".length());
                            ((NotifyTripJoinRequestViewHolder) holder).tvInterests.setVisibility(View.VISIBLE);
                            ((NotifyTripJoinRequestViewHolder) holder).tvInterests.setText("Interests:" + withoutLastComma);
                        }

                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).tvInterests.setVisibility(View.GONE);

                    }


                    if (object.getIsRead()) {
                        ((NotifyTripJoinRequestViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).itemContainer.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
                    }
                    ((NotifyTripJoinRequestViewHolder) holder).itemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (object.getIsRead() == false) {
                                ((NotifyTripJoinRequestViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                                MyWebService webService =
                                        MyWebService.retrofit.create(MyWebService.class);
                                boolean forceRefresh = false;
                                Call<String> call = webService.feedId(forceRefresh ? "no-cache" : null, AppConstants.EMAIL, AppConstants.TOKEN, String.valueOf(object.getFeedId()));
                                sendRequest(call);
                            }
                            
                        }
                    });
                    ((NotifyTripJoinRequestViewHolder) holder).tvTitleNotifyTripJoinRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((NotifyTripJoinRequestViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));

                        }
                    });
                    if (object.getUserEmailVerified()) {
                        ((NotifyTripJoinRequestViewHolder) holder).tvEmail.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        ((NotifyTripJoinRequestViewHolder) holder).tvEmail.setTextColor(Color.parseColor("#5D9C50"));
                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).tvEmail.setVisibility(View.GONE);
                    }
                    if (object.getUserFacebookVerified()) {
                        ((NotifyTripJoinRequestViewHolder) holder).tvFacebook.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        ((NotifyTripJoinRequestViewHolder) holder).tvFacebook.setTextColor(Color.parseColor("#5D9C50"));
                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).tvFacebook.setVisibility(View.GONE);
                    }
                    if (object.getUserGoogleVerified()) {
                        ((NotifyTripJoinRequestViewHolder) holder).tvGoogle.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        ((NotifyTripJoinRequestViewHolder) holder).tvGoogle.setTextColor(Color.parseColor("#5D9C50"));
                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).tvGoogle.setVisibility(View.GONE);
                    }
                    if (object.getUserLinkedinVerified()) {
                        ((NotifyTripJoinRequestViewHolder) holder).tvLinkedin.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        ((NotifyTripJoinRequestViewHolder) holder).tvLinkedin.setTextColor(Color.parseColor("#5D9C50"));
                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).tvLinkedin.setVisibility(View.GONE);
                    }
                    if (object.getUserPhoneVerified()) {
                        ((NotifyTripJoinRequestViewHolder) holder).tvPhone.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        ((NotifyTripJoinRequestViewHolder) holder).tvPhone.setTextColor(Color.parseColor("#5D9C50"));
                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).tvPhone.setVisibility(View.GONE);
                    }
                    if (object.getRequestNote() != null) {
                        ((NotifyTripJoinRequestViewHolder) holder).tvNoteUser.setText("Note from " + object.getUserName() + ":");
                        ((NotifyTripJoinRequestViewHolder) holder).tvNote.setText(object.getRequestNote());

                    } else {
                        ((NotifyTripJoinRequestViewHolder) holder).tvNoteUser.setVisibility(View.GONE);
                        ((NotifyTripJoinRequestViewHolder) holder).tvNote.setVisibility(View.GONE);
                    }

                    break;
                case "accepted":
                    fixedText = " has accepted your request to join the ";

                    builder = Tools.getSpannableStringBuilder(object, fixedText);
                    ((NotifyAcceptJoinRequestHolder) holder).tvAcceptTitle.setText(builder);
                    ((NotifyAcceptJoinRequestHolder) holder).tvAcceptTitle.setMovementMethod(LinkMovementMethod.getInstance());
                    ((NotifyAcceptJoinRequestHolder) holder).tvAcceptTime.setText(object.getFeedTime());
                    imageURL = object.getUserPicture();
                    Picasso.get().load(imageURL).into(((NotifyAcceptJoinRequestHolder) holder).imgPerson);
                    if (object.getIsRead()) {
                        ((NotifyAcceptJoinRequestHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                    } else {
                        ((NotifyAcceptJoinRequestHolder) holder).itemContainer.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
                    }

                    ((NotifyAcceptJoinRequestHolder) holder).itemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (object.getIsRead() == false) {
                                ((NotifyAcceptJoinRequestHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                                MyWebService webService =
                                        MyWebService.retrofit.create(MyWebService.class);
                                boolean forceRefresh = false;
                                Call<String> call = webService.feedId(forceRefresh ? "no-cache" : null, AppConstants.EMAIL, AppConstants.TOKEN, String.valueOf(object.getFeedId()));
                                sendRequest(call);
                                object.setIsRead(true);
                            }

                        }
                    });
                    ((NotifyAcceptJoinRequestHolder) holder).tvAcceptTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((NotifyAcceptJoinRequestHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        }
                    });
                    break;
                case "rejected":
                    fixedText = " did not accept your request to join the ";

                    builder = Tools.getSpannableStringBuilder(object, fixedText);
                    ((NotifyCancelJoinRequestViewHolder) holder).tvCancelJoinTitle.setText(builder);
                    ((NotifyCancelJoinRequestViewHolder) holder).tvCancelJoinTitle.setMovementMethod(LinkMovementMethod.getInstance());
                    ((NotifyCancelJoinRequestViewHolder) holder).tvCancelTime.setText(object.getFeedTime());
                    imageURL = object.getUserPicture();
                    Picasso.get().load(imageURL).into(((NotifyCancelJoinRequestViewHolder) holder).imgPerson);
                    if (object.getCancelReason() != null) {

                        ((NotifyCancelJoinRequestViewHolder) holder).tvNoteUser.setText("Note from " + object.getUserName() + ":");
                        ((NotifyCancelJoinRequestViewHolder) holder).tvNote.setText(object.getCancelReason());
                    } else {
                        ((NotifyCancelJoinRequestViewHolder) holder).tvNoteUser.setVisibility(View.GONE);
                        ((NotifyCancelJoinRequestViewHolder) holder).tvNote.setVisibility(View.GONE);
                    }
                    if (object.getIsRead()) {
                        ((NotifyCancelJoinRequestViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                    } else {
                        ((NotifyCancelJoinRequestViewHolder) holder).itemContainer.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
                    }

                    ((NotifyCancelJoinRequestViewHolder) holder).itemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (object.getIsRead() == false) {
                                ((NotifyCancelJoinRequestViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                                MyWebService webService =
                                        MyWebService.retrofit.create(MyWebService.class);
                                boolean forceRefresh = false;
                                Call<String> call = webService.feedId(forceRefresh ? "no-cache" : null, AppConstants.EMAIL, AppConstants.TOKEN, String.valueOf(object.getFeedId()));
                                sendRequest(call);
                                object.setIsRead(true);

                            }
                        }
                    });

                    ((NotifyCancelJoinRequestViewHolder) holder).tvCancelJoinTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((NotifyCancelJoinRequestViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        }
                    });
                    break;
                case "new_member":
                    fixedText = " has joined the ";

                    builder = Tools.getSpannableStringBuilder(object, fixedText);
                    ((NotifyJoinTripViewHolder) holder).tvTitleJoinTrips.setText(builder);
                    ((NotifyJoinTripViewHolder) holder).tvJoinTripTime.setText(object.getFeedTime());
                    ((NotifyJoinTripViewHolder) holder).tvTitleJoinTrips.setMovementMethod(LinkMovementMethod.getInstance());
                    imageURL = object.getUserPicture();
                    Picasso.get().load(imageURL).into(((NotifyJoinTripViewHolder) holder).imgPerson);
                    if (object.getIsRead()) {
                        ((NotifyJoinTripViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                    } else {
                        ((NotifyJoinTripViewHolder) holder).itemContainer.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
                    }

                    ((NotifyJoinTripViewHolder) holder).itemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (object.getIsRead() == false) {
                                ((NotifyJoinTripViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                                MyWebService webService =
                                        MyWebService.retrofit.create(MyWebService.class);
                                boolean forceRefresh = false;
                                Call<String> call = webService.feedId(forceRefresh ? "no-cache" : null, AppConstants.EMAIL, AppConstants.TOKEN, String.valueOf(object.getFeedId()));
                                sendRequest(call);
                                object.setIsRead(true);
                            }
                        }
                    });
                    ((NotifyJoinTripViewHolder) holder).tvTitleJoinTrips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((NotifyJoinTripViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        }
                    });
                    break;
                case "trip_invite":
                    fixedText = " has invited you to join ";
                    builder = Tools.getSpannableStringBuilder(object, fixedText);
                    ((NotifyInviteJoinTripViewHolder) holder).tvTitleInviteJoin.setText(builder);
                    ((NotifyInviteJoinTripViewHolder) holder).tvTitleInviteJoin.setMovementMethod(LinkMovementMethod.getInstance());
                    ((NotifyInviteJoinTripViewHolder) holder).tvInviteJoinTime.setText(object.getFeedTime());

                    imageURL = object.getUserPicture();
                    Picasso.get().load(imageURL).into(((NotifyInviteJoinTripViewHolder) holder).imgPerson);

                    SpannableString spStr=Tools.getSpannableItalic(object,mContext);

                    ((NotifyInviteJoinTripViewHolder) holder).tvInviteJoinDescription.setText(spStr);
                 //   ((NotifyInviteJoinTripViewHolder) holder).tvInviteJoinDescription.setText(object.getInviteNote());
                    if (object.getIsRead()) {
                        ((NotifyInviteJoinTripViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                    } else {
                        ((NotifyInviteJoinTripViewHolder) holder).itemContainer.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
                    }

                    ((NotifyInviteJoinTripViewHolder) holder).itemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (object.getIsRead() == false) {
                                ((NotifyInviteJoinTripViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                                MyWebService webService =
                                        MyWebService.retrofit.create(MyWebService.class);
                                boolean forceRefresh = false;
                                Call<String> call = webService.feedId(forceRefresh ? "no-cache" : null, AppConstants.EMAIL, AppConstants.TOKEN, String.valueOf(object.getFeedId()));
                                sendRequest(call);
                                object.setIsRead(true);
                            }
                        }
                    });
                    ((NotifyInviteJoinTripViewHolder) holder).tvTitleInviteJoin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((NotifyInviteJoinTripViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        }
                    });
                    break;
                case "plan_update":
                    fixedText = " has made some changes in the plan ";
                    builder = Tools.getSpannableStringBuilder(object, fixedText);
                    ((NotifyTripCreatorEditTripViewHolder) holder).tvTitleTripCreator.setText(builder);
                    ((NotifyTripCreatorEditTripViewHolder) holder).tvTripCreatorTime.setText(object.getFeedTime());
                    ((NotifyTripCreatorEditTripViewHolder) holder).tvTitleTripCreator.setMovementMethod(LinkMovementMethod.getInstance());
                    imageURL = object.getUserPicture();
                    Picasso.get().load(imageURL).into(((NotifyTripCreatorEditTripViewHolder) holder).imgPerson);
                    ((NotifyTripCreatorEditTripViewHolder) holder).tvTripCreatorDescription.setText("NBM");
                    if (object.getIsRead()) {
                        ((NotifyTripCreatorEditTripViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                    } else {
                        ((NotifyTripCreatorEditTripViewHolder) holder).itemContainer.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
                    }

                    ((NotifyTripCreatorEditTripViewHolder) holder).itemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (object.getIsRead() == false) {
                                ((NotifyTripCreatorEditTripViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                                MyWebService webService =
                                        MyWebService.retrofit.create(MyWebService.class);
                                boolean forceRefresh = false;
                                Call<String> call = webService.feedId(forceRefresh ? "no-cache" : null, AppConstants.EMAIL, AppConstants.TOKEN, String.valueOf(object.getFeedId()));
                                sendRequest(call);
                                object.setIsRead(true);
                            }
                        }
                    });
                    ((NotifyTripCreatorEditTripViewHolder) holder).tvTitleTripCreator.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((NotifyTripCreatorEditTripViewHolder) holder).itemContainer.setCardBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        }
                    });
                    break;
            }
        }

    }

    private void sendRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String message = response.body();
                Log.d("Message: ", message);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}

