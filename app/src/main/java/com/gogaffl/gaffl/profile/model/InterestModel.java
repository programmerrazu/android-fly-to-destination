package com.gogaffl.gaffl.profile.model;

import android.util.SparseArray;

import java.util.ArrayList;

public class InterestModel {

    private static int count;
    private static SparseArray<Integer> interest;
    private static boolean update;

    private static ArrayList<Interest> interests = null;

    public static int getCount() {
        return count;
    }

    public static void setCounter(int count) {
        InterestModel.count = count;
    }


    public static void setCount(int count) {
        InterestModel.count = count;
    }

    public static SparseArray<Integer> getInterest() {
        return interest;
    }

    public static void setInterest(SparseArray<Integer> interest) {
        InterestModel.interest = interest;
    }

    public static ArrayList<Interest> getInterests() {
        return interests;
    }

    public static void setInterests(ArrayList<Interest> interests) {
        InterestModel.interests = interests;
    }

    public static boolean isUpdate() {
        return update;
    }

    public static void setUpdate(boolean update) {
        InterestModel.update = update;
    }
}
