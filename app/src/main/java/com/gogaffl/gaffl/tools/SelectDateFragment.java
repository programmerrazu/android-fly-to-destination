package com.gogaffl.gaffl.tools;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.authentication.model.User;

import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private User mUser;
    private static boolean tracker;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

       /* Theme
                THEME_DEVICE_DEFAULT_DARK
                THEME_HOLO_DARK
                THEME_HOLO_LIGHT
                THEME_TRADITIONAL
                */

        DatePickerDialog mDate = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT ,this, yy, mm, dd);
        mDate.getWindow().setLayout(0,200);

        return mDate;
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }

    public static boolean isTracker() {
        return tracker;
    }

    public static void setTracker(boolean tracker) {
        SelectDateFragment.tracker = tracker;
    }

    public void populateSetDate(int year, int month, int day) {
        TextView dob= (TextView)getActivity(). findViewById(R.id.date_TV);
        dob.setText(year+"-"+month+"-"+day);
        SelectDateFragment.setTracker(true);
    }
}