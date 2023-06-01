package com.onthemove.responseClasses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsResponse {


    /**
     * status : true
     * message : Data fetched
     * data : [{"order_details_id":3,"ticket_number":"66451","comment":"abc","lat":"23.015497","lng":"72.561075","address":"11, New Sharda Mandir Rd, Laxmi Nivas Society, Paldi, Ahmedabad, Gujarat 380007","images":[{"name_of_the_image":"Desktop","image":"http://65.2.107.98/uploads/agent_order/3/5f8f382ef9b31530b08192941d27d0fa.jpeg","created_at":"2021-05-28 11:12:33","updated_at":""}],"created_at":"2021-05-28 11:12:33","updated_at":""}]
     */

    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ArrayList<DataBean> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * order_details_id : 3
         * ticket_number : 66451
         * comment : abc
         * lat : 23.015497
         * lng : 72.561075
         * address : 11, New Sharda Mandir Rd, Laxmi Nivas Society, Paldi, Ahmedabad, Gujarat 380007
         * images : [{"name_of_the_image":"Desktop","image":"http://65.2.107.98/uploads/agent_order/3/5f8f382ef9b31530b08192941d27d0fa.jpeg","created_at":"2021-05-28 11:12:33","updated_at":""}]
         * created_at : 2021-05-28 11:12:33
         * updated_at :
         */

        @SerializedName("order_details_id")
        private int orderDetailsId;
        @SerializedName("ticket_number")
        private String ticketNumber;
        @SerializedName("comment")
        private String comment;
        @SerializedName("lat")
        private String lat;
        @SerializedName("lng")
        private String lng;
        @SerializedName("address")
        private String address;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("updated_at")
        private String updatedAt;
        @SerializedName("images")
        private ArrayList<ImagesBean> images;

        public int getOrderDetailsId() {
            return orderDetailsId;
        }

        public void setOrderDetailsId(int orderDetailsId) {
            this.orderDetailsId = orderDetailsId;
        }

        public String getTicketNumber() {
            return ticketNumber;
        }

        public void setTicketNumber(String ticketNumber) {
            this.ticketNumber = ticketNumber;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public ArrayList<ImagesBean> getImages() {
            return images;
        }

        public void setImages(ArrayList<ImagesBean> images) {
            this.images = images;
        }

        public static class ImagesBean {
            /**
             * name_of_the_image : Desktop
             * image : http://65.2.107.98/uploads/agent_order/3/5f8f382ef9b31530b08192941d27d0fa.jpeg
             * created_at : 2021-05-28 11:12:33
             * updated_at :
             */

            @SerializedName("name_of_the_image")
            private String nameOfTheImage;
            @SerializedName("image")
            private String image;
            @SerializedName("created_at")
            private String createdAt;
            @SerializedName("updated_at")
            private String updatedAt;

            public String getNameOfTheImage() {
                return nameOfTheImage;
            }

            public void setNameOfTheImage(String nameOfTheImage) {
                this.nameOfTheImage = nameOfTheImage;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }
        }
    }
}
