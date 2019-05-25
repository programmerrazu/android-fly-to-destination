package com.gogaffl.gaffl.profile.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.gogaffl.gaffl.R;
import com.gogaffl.gaffl.profile.model.Interest;
import com.gogaffl.gaffl.profile.model.InterestModel;

import java.util.ArrayList;

class InterestAdapter extends BaseAdapter {

    ArrayList<Interest> names;
    Context context;
    LayoutInflater inflter;
    String value;
    ArrayList<Integer> interestList = null;
    int count = 0;


    public InterestAdapter(ArrayList<Interest> names, Context context) {
        this.names = names;
        this.context = context;
        this.inflter = (LayoutInflater.from(context));
        this.interestList = new ArrayList<>();
        listPreselected();
    }

    private void listPreselected() {
        ArrayList<Interest> interests = InterestModel.getInterests();

        for (Interest name : names) {
            for (Interest interest : interests) {
                if (interest.getName().equals(name.getName())) {
                    interestList.add(name.getId());
                    break;
                }
            }
        }
        setInterests();
    }

    @Override
    public int getCount() {
        return names.size();
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
    public View getView(int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.interest_row_item, null);
        final CheckedTextView simpleCheckedTextView = (CheckedTextView) view.findViewById(R.id.simpleCheckedTextView);
        simpleCheckedTextView.setText(names.get(position).getName());

        ArrayList<Interest> interests = InterestModel.getInterests();

        for (int i = 0; i < interests.size(); i++) {
            if (interests.get(i).getName().equals(names.get(position).getName())) {
                simpleCheckedTextView.setCheckMarkDrawable(R.drawable.ic_check);
                simpleCheckedTextView.setChecked(true);
//                int a = names.get(position).getId();
//                interestList.add(a);
            }
        }

// perform on Click Event Listener on CheckedTextView
        simpleCheckedTextView.setOnClickListener(v -> {

            int a = names.get(position).getId();

            if (simpleCheckedTextView.isChecked()) {
// set cheek mark drawable and set checked property to false
                System.out.println("this is after counting old ones :" + count);
                value = (String) simpleCheckedTextView.getText() + " un-Checked";
                simpleCheckedTextView.setCheckMarkDrawable(null);
                interestList.remove(interestList.indexOf(a));
                //map.remove((String)simpleCheckedTextView.getText());
                simpleCheckedTextView.setChecked(false);
            } else {
// set cheek mark drawable and set checked property to true
                System.out.println("this is after counting new ones :" + count);
                value = (String) simpleCheckedTextView.getText() + " Checked";
                simpleCheckedTextView.setCheckMarkDrawable(R.drawable.ic_check);
                simpleCheckedTextView.setChecked(true);
                interestList.add(a);
            }

            setInterests();
        });


        return view;
    }

    private void setInterests() {
        SparseArray<Integer> sparsList = new SparseArray<>();

        for (int i = 0; i < interestList.size(); i++) {
            sparsList.append(i, interestList.get(i));
        }

        //Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
        System.out.println("map size is " + interestList.size());
        InterestModel.setCounter(sparsList.size());
        InterestModel.setInterest(sparsList);
    }
}