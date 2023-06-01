package com.onthemove.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onthemove.BuildConfig;
import com.onthemove.commons.baseClasses.MyApp;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.AppPref;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.BASE_URL)
                .client(getHttpClient())
                .build();
    }
    public static OkHttpClient getHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization", "Bearer " + AppPref.getInstance(MyApp.getMyApp()).getToken());

                AppLog.e("Authorization", "Bearer " + AppPref.getInstance(MyApp.getMyApp()).getToken());
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).addInterceptor(interceptor).build();
    }
    /**
     * Get API Service
     *
     * @return API Service
     */
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}