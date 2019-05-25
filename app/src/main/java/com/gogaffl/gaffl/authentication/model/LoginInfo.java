package com.gogaffl.gaffl.authentication.model;

public class LoginInfo {
    /**
     * data : {
     * "user":{
     * "email":"john@local.com",
     * "authentication_token":"B3UW6j3MGkx6BsUdDx3k"
     * }
     * }
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user : {"email":"john@local.com","authentication_token":"B3UW6j3MGkx6BsUdDx3k"}
         */

        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * email : john@local.com
             * authentication_token : B3UW6j3MGkx6BsUdDx3k
             */

            private String email;
            private String authentication_token;

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getAuthentication_token() {
                return authentication_token;
            }

            public void setAuthentication_token(String authentication_token) {
                this.authentication_token = authentication_token;
            }
        }
    }
}
