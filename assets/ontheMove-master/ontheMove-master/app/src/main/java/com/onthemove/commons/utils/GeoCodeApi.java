package com.onthemove.commons.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.onthemove.BuildConfig;
import com.onthemove.network.ApiService;
import com.onthemove.network.RetroClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeoCodeApi extends AsyncTask<String,String,String> {
    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
    /*
    private static final String TAG = GeoCodeApi.class.getSimpleName();
    double latitude;
    double longitude;
    Context context;
    GeoCodeListener listener;
    GeoAddress geoAddress;
    public GeoCodeApi(double latitude, double longitude, Context context, GeoCodeListener listener) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            geoAddress = new GeoAddress();
//            geoAddress.address = addresses.get(0).getAddressLine(0);
            String address = (addresses.get(0).getSubLocality()+", "+addresses.get(0).getLocality()+", "+
                                            addresses.get(0).getAdminArea()+", "+addresses.get(0).getPostalCode()+", "+
                                                addresses.get(0).getCountryName());
            geoAddress.address = address;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(geoAddress!=null){
            returnAddress();
        }else {
            ApiService apiService = RetroClient.getApiService();
            apiService.getGeoAddress(latitude+","+longitude, BuildConfig.PLACE_KEY_1).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        AppLog.e(TAG,"onResponse : "+response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    returnAddress();
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    AppLog.e(TAG,"onResponse : "+t.toString());
                    returnAddress();
                }
            });

        }
    }
    private void returnAddress(){
        if(listener!=null){
            listener.onGeoAddressFound(geoAddress);
        }
    }

    public static class GeoAddress{
        String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "GeoAddress{" +
                    "address='" + address + '\'' +
                    '}';
        }
    }
    public interface GeoCodeListener{
        void onGeoAddressFound(GeoAddress geoAddress);
    }

     */
}
