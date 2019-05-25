package com.gogaffl.gaffl.profile.model;

import java.util.ArrayList;

public class Interests {

    /**
     * data : {"interests":[{"id":1,"name":"Hiking"},{"id":2,"name":"Mountaineering"},{"id":3,"name":"Climbing"},{"id":4,"name":"Biking"},{"id":5,"name":"Swimming"},{"id":6,"name":"Shooting"},{"id":7,"name":"Shooting"},{"id":8,"name":"Sight Seeing"},{"id":9,"name":"Sight Seeing"},{"id":10,"name":"Football"},{"id":11,"name":"Football"},{"id":12,"name":"Football"},{"id":13,"name":"Cricket"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private ArrayList<InterestsBean> interests;

        public ArrayList<InterestsBean> getInterests() {
            return interests;
        }

        public void setInterests(ArrayList<InterestsBean> interests) {
            this.interests = interests;
        }

        public static class InterestsBean {
            /**
             * id : 1
             * name : Hiking
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
