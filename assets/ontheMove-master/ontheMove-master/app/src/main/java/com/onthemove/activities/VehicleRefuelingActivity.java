package com.onthemove.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.onthemove.R;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.databinding.ActivityVehicleRefuelingBinding;

public class VehicleRefuelingActivity extends BaseActivity {

    private ActivityVehicleRefuelingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_vehicle_refueling);

        init();
    }

    private void init(){


    }
}