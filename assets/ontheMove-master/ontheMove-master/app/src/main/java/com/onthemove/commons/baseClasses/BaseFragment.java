package com.onthemove.commons.baseClasses;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.onthemove.R;
import com.onthemove.activities.LoginActivity;
import com.onthemove.commons.utils.AppPref;

public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    protected Context context;
    protected ProgressDialog dialog;
    protected AppPref appPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appPref = AppPref.getInstance(MyApp.getMyApp());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

    }


    //for....prefrences
    public AppPref getPref() {
        return AppPref.getInstance(context);
    }



    //For....get BaseActivity
    public BaseActivity getBase() {
        return ((BaseActivity) getActivity());
    }



    //For... show toast msg.
    public void showToast(String msg) {
        Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
    }



    //For...check internet is on or off
    public boolean isOnline() {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        /*
        if (!isAvailable) {
            AppDialog.showNoNetworkDialog(context);
        }
         */
        return isAvailable;
    }



    //For...show loading process during api call
    public void showLoading(String msg) {
        try {
            if (dialog != null)
                hideLoading();
            if (dialog == null) {
                dialog = new ProgressDialog(context);
            }
            dialog.setMessage(!TextUtils.isEmpty(msg) ? msg : getResources().getString(R.string.loading));
            if (!dialog.isShowing())
                dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    //For...hide loading dialog after getting response
    public void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }



    //For....if user is unauthorized by token
    public void unAuthorized() {
        showToast(getResources().getString(R.string.unautorized));
        logout();
    }



    //For...if response is null from api.
    public void responseNull() {
        showToast(getResources().getString(R.string.responseNull));
    }



    //For...if api calling is fail
    public void onFailure(String message) {
        showToast(message);
    }




    //For....if set code in onSuccess.
    public void OnSuccess() {

    }




    //For....validation of EditText
    public boolean valid(EditText editText, int errorMsg) {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            showToast(getResources().getString(errorMsg));
            return false;
        }
        return true;
    }



    //For....validation of email
    public boolean validEmail(EditText editText, int errorMsg) {
        if (!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches()) {
            showToast(getResources().getString(errorMsg));
            return false;
        }
        return true;
    }



    //For...change activity
    public void gotoActivity(Class className, Bundle bundle, boolean isClearStack) {
        Intent intent = new Intent(context, className);

        if (bundle != null)
            intent.putExtras(bundle);

        if (isClearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }




    //For...logout
    public void logout(){
        new LogoutTask().execute();
    }



    //For...logout
    @SuppressLint("StaticFieldLeak")
    public class LogoutTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading(getResources().getString(R.string.logout));
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                appPref.clearSession();
                //FirebaseInstanceId.getInstance().deleteInstanceId();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideLoading();
            getBase().gotoActivity(LoginActivity.class,null,true);
            getBase().finish();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoading();
    }






}
