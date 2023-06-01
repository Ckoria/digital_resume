package com.onthemove.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.onthemove.R;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.commons.utils.AppDialog;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.commons.utils.Constants;
import com.onthemove.commons.utils.FragmentVehicalSelectDailog;
import com.onthemove.database.TaskListDatabaseManager;
import com.onthemove.databinding.ActivityMain2Binding;
import com.onthemove.fragments.MyTaskFragment;
import com.onthemove.fragments.NewTaskFragment;
import com.onthemove.interfaces.AuthContract;
import com.onthemove.modelClasses.OnDutyModel;
import com.onthemove.services.ApiCallStatusServices;
import com.onthemove.services.FirebaseLocationService;
import com.onthemove.services.LocationServices;
import com.onthemove.viewModel.AuthViewModel;

public class MainActivity2 extends BaseActivity implements View.OnClickListener, AuthContract.AuthView {

    private static final String TAG = "MainActivity2";

    private static ActivityMain2Binding binding;
    private Context context;
    String[] tabText;
    int[] tabIconSelected;
    FragmentVehicalSelectDailog vehicalSelectDailog;
    boolean isBackEnable;
    private AuthViewModel authViewModel;
    private TaskListDatabaseManager manager;
    MyTaskFragment myTaskFragment;
    TaskDetailActivity detailActivity;
    private static boolean sync = false;
    public boolean checkChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2);
        context = MainActivity2.this;
        manager = new TaskListDatabaseManager(this);
        vehicalSelectDailog = FragmentVehicalSelectDailog.newInstance();
        init();
        listner();
    }

    private void init() {

        detailActivity = new TaskDetailActivity();
        checkLocationRequirement();

        /*if (appPref.getBoolean(Constants.switchChecked) == true) {
            binding.switchDuty.setChecked(true);
        }
        else
        {
            binding.switchDuty.setChecked(false);
        }*/
        if (manager.getTaskStatus().size() > 0) {
            binding.btnSync.setBackgroundResource(R.drawable.syncredbg_btn);
            sync = true;
        }
        else {
            if (manager.getUploadedImages().size() > 0) {
                binding.btnSync.setBackgroundResource(R.drawable.syncredbg_btn);
                sync = true;
            } else if (manager.getRefuelingList().size() > 0) {
                binding.btnSync.setBackgroundResource(R.drawable.syncredbg_btn);
                sync = true;
            }else if(manager.getUploadedIFailReason().size() > 0)
            {
                binding.btnSync.setBackgroundResource(R.drawable.syncredbg_btn);
                sync = true;
            }
            else {
                binding.btnSync.setBackgroundResource(R.drawable.syncbg_btn);
                sync = false;
            }
        }

        binding.switchDuty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    if(!vehicalSelectDailog.isAdded())
                    {
                        if (checkChange == false)
                        {
                            vehicalSelectDailog.show(getSupportFragmentManager(), "datePicker");
                            vehicalSelectDailog.setCancelable(false);
                        }
                        else
                        {
                            AppLog.e(TAG,"dailog already show");
                        }
                    }
                    //gotoActivity(VehicleHealthActivity.class, null, false);
                } else {
                    AppPref.getInstance(context).set(Constants.switchChecked,false);
                    checkChange = false;
                    authViewModel.onDuty();
                }
            }
        });

        if (appPref.getString(Constants.ROLE).equalsIgnoreCase("both")) {

            binding.includeNav.linTaskDetails.setVisibility(View.VISIBLE);
            binding.includeNav.viewTaskDetails.setVisibility(View.VISIBLE);
            binding.includeNav.linMyTaskDetails.setVisibility(View.VISIBLE);
            binding.includeNav.viewMyTaskDetails.setVisibility(View.VISIBLE);
        } else {

            binding.includeNav.linTaskDetails.setVisibility(View.GONE);
            binding.includeNav.viewTaskDetails.setVisibility(View.GONE);
            binding.includeNav.linMyTaskDetails.setVisibility(View.GONE);
            binding.includeNav.viewMyTaskDetails.setVisibility(View.GONE);
        }

        binding.includeNav.tvUserName.setText(appPref.getString(Constants.USER_NAME));

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.setAuthView(this);

        setTabLayout();
        select(0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnline()) {
            ApiCallStatusServices.start(this);
        }


    }

    private void listner() {

        binding.linMenu.setOnClickListener(this);
        binding.includeNav.linNewTask.setOnClickListener(this);
        binding.includeNav.linMyTask.setOnClickListener(this);
        binding.includeNav.linTaskDetails.setOnClickListener(this);
        binding.includeNav.linMyTaskDetails.setOnClickListener(this);
        binding.includeNav.linMyTask.setOnClickListener(this);
        binding.includeNav.linLogout.setOnClickListener(this);
        binding.includeNav.linVehicleRefueling.setOnClickListener(this);
     //   binding.includeNav.linVehicleHealth.setOnClickListener(this);
        binding.linMenu.setOnClickListener(this);
        binding.btnSync.setOnClickListener(this);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLocationSatisfy() {
        super.onLocationSatisfy();

    //    Log.e(TAG,"FirebaseLocationService STart"+isMyServiceRunning(FirebaseLocationService.class));

        if (!isMyServiceRunning(FirebaseLocationService.class))
        {
            if (isGpsOn())
            {
                FirebaseLocationService.start(MainActivity2.this);
                Log.e(TAG,"FirebaseLocationService STart");
            }
        }
       // LocationServices.start(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.linMenu:
                binding.drawer.openDrawer(GravityCompat.START);
                break;

            case R.id.linNewTask:
                binding.drawer.closeDrawer(GravityCompat.START);
                changeFragment(NewTaskFragment.newInstance(null), false, false);
                break;

            case R.id.linMyTask:
                binding.drawer.closeDrawer(GravityCompat.START);
                gotoActivity(MyTaskDetails.class, null, false);
                break;

            case R.id.linTaskDetails:
                binding.drawer.closeDrawer(GravityCompat.START);
                gotoActivity(MainActivity.class, null, false);
                break;

            case R.id.linMyTaskDetails:
                binding.drawer.closeDrawer(GravityCompat.START);
                gotoActivity(OrderDetailsActivity.class, null, false);
                break;

            case R.id.linVehicleRefueling:
                binding.drawer.closeDrawer(GravityCompat.START);
                gotoActivity(AddVehicleRefuelingActivity.class, null, false);
                break;

        /*    case R.id.linVehicleHealth:
                binding.drawer.closeDrawer(GravityCompat.START);
                gotoActivity(VehicleHealthActivity.class, null, false);
                break;*/

            case R.id.linLogout:
                AppDialog.showConfirmDialog(context, "Are you sure you want to logout?", new AppDialog.AppDialogListener() {
                    @Override
                    public void okClick(DialogInterface dialog) {

                        authViewModel.getLogout();
                        authViewModel.logoutStatus.observe(MainActivity2.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean status) {

                                if (status) {

                                    logout();
                                    gotoActivity(LoginActivity.class, null, false);
                                    finish();
                                }
                            }
                        });
                    }
                });
                break;

            case R.id.btnSync: {
                if (isOnline()) {
                    if (!sync) {
                        ApiCallStatusServices.start(this);
                        sync = true;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Required Internet", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public static void syncDone() {
        sync = false;
        binding.btnSync.setBackgroundResource(R.drawable.syncbg_btn);
    }

    public void setToolbarTitle(String title, boolean isBackEnable) {
        this.isBackEnable = isBackEnable;
        if (binding.tvTitle != null) {
            binding.tvTitle.setText(title);
        }
        if (isBackEnable) {

        }
    }

    private void setTabLayout() {

        tabText = getResources().getStringArray(R.array.tabText);
        tabIconSelected = new int[tabText.length];

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.newTask)));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.myTask)));


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        createTab();
    }

    public void selectTabPosition(int position) {
        binding.tabLayout.getTabAt(position).select();
    }

    private void select(int pos) {
        switch (pos) {
            case 0: {
                changeFragment(NewTaskFragment.newInstance(null), false, false);
                break;
            }
            case 1: {
                changeFragment(MyTaskFragment.newInstance(null), false, false);
                break;
            }
        }
    }

    private void createTab() {
        for (int i = 0; i < tabText.length; i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            TextView tv = v.findViewById(R.id.tabText);

            tv.setText(tabText[i]);

//                if (i == 0) {
//                    tv.setTextColor(getResources().getColor(R.color.white));
//                } else {
//                    img.setImageResource(tabIcon[i]);
//                    img.setColorFilter(ContextCompat.getColor(this, R.color.unselectTab), android.graphics.PorterDuff.Mode.SRC_IN);
//                    tv.setTextColor(getResources().getColor(R.color.unselectTab));
//                }
            binding.tabLayout.getTabAt(i).setCustomView(v);
        }
    }
    public void updateTab(int tabPos) {
        select(tabPos);
        for (int i = 0; i < tabText.length; i++) {
            View v = binding.tabLayout.getTabAt(i).getCustomView();
            TextView tv = v.findViewById(R.id.tabText);
            tv.setText(tabText[i]);
        }
    }


    @Override
    public void OnDutyRes(OnDutyModel.OnDutyData onDutyData) {

        Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

      /*  if (onDutyData != null) {
          //  appPref.set(AppPref.VEHICLE_NO, onDutyData.getFkVehicleId());
            binding.switchDuty.setChecked(true);
            appPref.set(Constants.switchChecked,true);
        } else {
            binding.switchDuty.setChecked(false);
            appPref.set(Constants.switchChecked,false);
        }*/
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void otpSent() {

    }

    @Override
    public void otpVerified() {

    }

    @Override
    public void submitReport(String message) {

    }

    @Override
    public void approvedAccount(String account_status) {

    }

    @Override
    public void OnSuccess() {

    }
}
