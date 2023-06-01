package com.onthemove.modelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PartModel implements Serializable {


    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Serializable
    {
        @SerializedName("task_products")
        @Expose
        private ArrayList<TaskProductData> productData;

        public ArrayList<TaskProductData> getProductData() {
            return productData;
        }

        public void setProductData(ArrayList<TaskProductData> productData) {
            this.productData = productData;
        }
    }
    public static class TaskProductData implements Serializable
    {
        @SerializedName("product_id")
        @Expose
        private int product_id;

        @SerializedName("product_name")
        @Expose
        private String product_name;

        @SerializedName("upc_code")
        @Expose
        private String upc_code;

        @SerializedName("qty")
        @Expose
        private String qty;


        public String used_qty;

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getUpc_code() {
            return upc_code;
        }

        public String getUsed_qty() {
            return used_qty;
        }

        public void setUsed_qty(String used_qty) {
            this.used_qty = used_qty;
        }

        public void setUpc_code(String upc_code) {
            this.upc_code = upc_code;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }
    }
    public static class TaskProductDataUsed
    {

        private int product_id;

        private String product_name;

        private String upc_code;

        private String qty;


        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getUpc_code() {
            return upc_code;
        }

        public void setUpc_code(String upc_code) {
            this.upc_code = upc_code;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }
    }
}
