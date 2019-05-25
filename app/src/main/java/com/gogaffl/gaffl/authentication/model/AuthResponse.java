package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthResponse {

    /**
     * success : true
     * Info : {"token":"i7rETqzgT_d3WSpg9GiY"}
     */

    private String success;
    private InfoBean info;

    public String getSuccess() {
        return success;
    }

    public InfoBean getInfo() {
        return info;
    }

    public static class InfoBean {

        @SerializedName("authentication_token")
        private String token;
        private String error;
        private List<String> email;
        private List<String> password;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<String> getEmail() {
            return email;
        }

        public void setEmail(List<String> email) {
            this.email = email;
        }

        public List<String> getPassword() {
            return password;
        }

        public void setPassword(List<String> password) {
            this.password = password;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
