package com.onthemove.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.onthemove.R;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.databinding.ActivityViewImageBinding;

public class ViewImageActivity extends BaseActivity {

    private static final String TAG = "ViewImageActivity";

    private ActivityViewImageBinding binding;
    private Context context;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_image);
        context = ViewImageActivity.this;

        inits();

    }

    private void inits() {

        image = getIntent().getStringExtra("IMAGE");
        Glide.with(context).load(image).into(binding.img);

    }
}
