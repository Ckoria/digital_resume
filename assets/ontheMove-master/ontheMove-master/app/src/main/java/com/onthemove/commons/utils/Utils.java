package com.onthemove.commons.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {
    public static String printKeyHash(Context context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static void setTab(Bundle extras, TabLayout tabLayout) {

        if(extras!=null) {

            if (extras.getBoolean("changeTab")) {
                tabLayout.getTabAt(extras.getInt("setTab")).select();
            }else{
                AppLog.e("Bundle","No CHANGE");
            }

        }else{
            AppLog.e("Bundle","EMPTY");
        }
    }

    public static String standardFormat(String data){

        String date="";

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date=format.format(sdf.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date;
    }

    public static int dpToPx(DisplayMetrics displayMetrics, int dp) {

        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String formatDate(String data){

        String date="";

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date=format.format(sdf.parse(data));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date;
    }

    public static String formatTime(String data){

        String date="";

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat hr  = new SimpleDateFormat("hh:mm a");


        try {
            date=hr.format(sdf.parse(data));

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date;
    }


}


