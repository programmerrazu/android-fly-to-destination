package com.gogaffl.gaffl.authentication.model;

public class FbUserModel {

    /**
     * id : 102799530896313
     * name : Salman Shahrukh
     * email : onirban.gaffl@gmail.com
     * picture : {"data":{"height":200,"is_silhouette":false,"url":"https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=102799530896313&width=200&ext=1555744754&hash=AeQdKrmLKfr_ws_5","width":200}}
     */

    private String id;
    private String name;
    private String email;
    private PictureBean picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PictureBean getPicture() {
        return picture;
    }

    public void setPicture(PictureBean picture) {
        this.picture = picture;
    }

    public static class PictureBean {
        /**
         * data : {"height":200,"is_silhouette":false,"url":"https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=102799530896313&width=200&ext=1555744754&hash=AeQdKrmLKfr_ws_5","width":200}
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
             * height : 200
             * is_silhouette : false
             * url : https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=102799530896313&width=200&ext=1555744754&hash=AeQdKrmLKfr_ws_5
             * width : 200
             */

            private int height;
            private boolean is_silhouette;
            private String url;
            private int width;

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public boolean isIs_silhouette() {
                return is_silhouette;
            }

            public void setIs_silhouette(boolean is_silhouette) {
                this.is_silhouette = is_silhouette;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }
        }
    }
}
