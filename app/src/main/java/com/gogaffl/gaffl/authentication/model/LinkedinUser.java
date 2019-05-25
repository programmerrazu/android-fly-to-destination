package com.gogaffl.gaffl.authentication.model;

public class LinkedinUser {

    /**
     * lastName : {"localized":{"en_US":"Shahrukh"},"preferredLocale":{"country":"US","language":"en"}}
     * firstName : {"localized":{"en_US":"Salman"},"preferredLocale":{"country":"US","language":"en"}}
     * profilePicture : {"displayImage":"urn:li:digitalmediaAsset:C5103AQFFecckrqiD6w"}
     * id : eqGRbW3JMg
     */

    private LastNameBean lastName;
    private FirstNameBean firstName;
    private ProfilePictureBean profilePicture;
    private String id;

    public LastNameBean getLastName() {
        return lastName;
    }

    public void setLastName(LastNameBean lastName) {
        this.lastName = lastName;
    }

    public FirstNameBean getFirstName() {
        return firstName;
    }

    public void setFirstName(FirstNameBean firstName) {
        this.firstName = firstName;
    }

    public ProfilePictureBean getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePictureBean profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class LastNameBean {
        /**
         * localized : {"en_US":"Shahrukh"}
         * preferredLocale : {"country":"US","language":"en"}
         */

        private LocalizedBean localized;
        private PreferredLocaleBean preferredLocale;

        public LocalizedBean getLocalized() {
            return localized;
        }

        public void setLocalized(LocalizedBean localized) {
            this.localized = localized;
        }

        public PreferredLocaleBean getPreferredLocale() {
            return preferredLocale;
        }

        public void setPreferredLocale(PreferredLocaleBean preferredLocale) {
            this.preferredLocale = preferredLocale;
        }

        public static class LocalizedBean {
            /**
             * en_US : Shahrukh
             */

            private String en_US;

            public String getEn_US() {
                return en_US;
            }

            public void setEn_US(String en_US) {
                this.en_US = en_US;
            }
        }

        public static class PreferredLocaleBean {
            /**
             * country : US
             * language : en
             */

            private String country;
            private String language;

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }
        }
    }

    public static class FirstNameBean {
        /**
         * localized : {"en_US":"Salman"}
         * preferredLocale : {"country":"US","language":"en"}
         */

        private LocalizedBeanX localized;
        private PreferredLocaleBeanX preferredLocale;

        public LocalizedBeanX getLocalized() {
            return localized;
        }

        public void setLocalized(LocalizedBeanX localized) {
            this.localized = localized;
        }

        public PreferredLocaleBeanX getPreferredLocale() {
            return preferredLocale;
        }

        public void setPreferredLocale(PreferredLocaleBeanX preferredLocale) {
            this.preferredLocale = preferredLocale;
        }

        public static class LocalizedBeanX {
            /**
             * en_US : Salman
             */

            private String en_US;

            public String getEn_US() {
                return en_US;
            }

            public void setEn_US(String en_US) {
                this.en_US = en_US;
            }
        }

        public static class PreferredLocaleBeanX {
            /**
             * country : US
             * language : en
             */

            private String country;
            private String language;

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }
        }
    }

    public static class ProfilePictureBean {
        /**
         * displayImage : urn:li:digitalmediaAsset:C5103AQFFecckrqiD6w
         */

        private String displayImage;

        public String getDisplayImage() {
            return displayImage;
        }

        public void setDisplayImage(String displayImage) {
            this.displayImage = displayImage;
        }
    }
}
