package com.onthemove.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.onthemove.R;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.commons.utils.Constants;

public class SplashActivity extends BaseActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(appPref.isLogin()){

                    Log.e("Splash","ROLE " +appPref.getString(Constants.ROLE));
                    if (appPref.getString(Constants.ROLE).equalsIgnoreCase("serviceboy")){

                        gotoActivity(MainActivity.class,null,false);
                        finish();
                    }
                    else {

                        gotoActivity(MainActivity2.class,null,false);
                        finish();
                    }
                }
                else {
                    gotoActivity(LoginActivity.class,null,false);
                    finish();
                }



            }
        },3000);
    }
}
