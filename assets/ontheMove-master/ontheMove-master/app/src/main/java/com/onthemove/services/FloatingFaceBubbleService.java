package com.onthemove.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.onthemove.R;
import com.onthemove.activities.TaskDetailActivity;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.responseClasses.NewTaskModel;

public class FloatingFaceBubbleService extends Service {
    private WindowManager windowManager;
    private NewTaskModel.NewTaskData taskData = new NewTaskModel.NewTaskData();
    private ImageView floatingFaceBubble;
    private final IBinder mBinder = new LocalBinder();
    private static String TAG = "BubbleService";
    String task;
    public void onCreate() {
        super.onCreate();
        AppLog.e(TAG , "onCreate called");
        floatingFaceBubble = new ImageView(this);
        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.drawable.app_icon);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //here is all the science of params
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams myParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x = 0;
        myParams.y = 100;
        // add a floatingfacebubble icon in window
        windowManager.addView(floatingFaceBubble, myParams);
        try {

            //for moving the picture on touch and slide
            floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press
                    /*if (System.currentTimeMillis() - touchStartTime > ViewConfiguration.getLongPressTimeout() && initialTouchX == event.getX()) {
                        windowManager.removeView(floatingFaceBubble);
                        stopSelf();
                        return false;
                    }*/
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            AppLog.e(TAG , "action down at "+touchStartTime);
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            AppLog.e(TAG , "action UP at "+(System.currentTimeMillis()- touchStartTime));
                            if (System.currentTimeMillis() - touchStartTime < 150) {

                                Intent i = new Intent(FloatingFaceBubbleService.this,TaskDetailActivity.class);
                                i.putExtra("taskData",taskData);
                                i.putExtra("from",task);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                                onDestroy();
                                windowManager.removeView(floatingFaceBubble);
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppLog.e("BubbleService", "onStartCommand");

        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        AppLog.e("BubbleService", "onStartCommand called");
        taskData = (NewTaskModel.NewTaskData) intent.getExtras().getSerializable("taskData");
        task = intent.getStringExtra("from");
        // TODO Auto-generated method stub
        return mBinder;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        removeBubble();
    }
    public class LocalBinder extends Binder {
        public FloatingFaceBubbleService getService() {
            // Return this instance of LocalService so clients can call public methods
            return FloatingFaceBubbleService.this;
        }
    }
    public void removeBubble() {
        try {
            windowManager.removeView(floatingFaceBubble);
            AppLog.e("BubbleService", "removeView");
        } catch (Exception e) {
            AppLog.e("BubbleService", "error is " + e.toString());
        }
        stopSelf();
    }
}