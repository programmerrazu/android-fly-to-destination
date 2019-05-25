package com.gogaffl.gaffl.authentication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LinkedinUserEmail {

    private List<ElementsBean> elements;

    public List<ElementsBean> getElements() {
        return elements;
    }

    // FIXME generate failure  field _$Handle324
    public class ElementsBean {
        /**
         * handle : urn:li:emailAddress:7643157066
         * handle~ : {"emailAddress":"onirban.gaffl@gmail.com"}
         */

        @SerializedName("handle~")
        @Expose
        private String handle;

        public String getHandle() {
            return handle;
        }

        public void setHandle(String handle) {
            this.handle = handle;
        }

        public class Handle {
            /**
             * emailAddress : onirban.gaffl@gmail.com
             */

            private String emailAddress;

            public String getEmailAddress() {
                return emailAddress;
            }

            public void setEmailAddress(String emailAddress) {
                this.emailAddress = emailAddress;
            }
        }

    }
}
