package com.onthemove.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.onthemove.R;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.databinding.ActivityMyTaskDetailsBinding;
import com.onthemove.fragments.MyTaskCompleteDetailsFragment;
import com.onthemove.fragments.MyTaskDetailsFragment;
import com.onthemove.fragments.MyTaskFragment;
import com.onthemove.fragments.NewTaskFragment;

public class MyTaskDetails extends BaseActivity {

    ActivityMyTaskDetailsBinding binding;
    String[] tabText;
    boolean isBackEnable;
    int[] tabIconSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_task_details);
        setTabLayout();
        inits();
    }
    public void inits()
    {
        binding.linBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setTabLayout() {

        tabText = getResources().getStringArray(R.array.mytasktabText);
        tabIconSelected = new int[tabText.length];

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.mytask)));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.CompleteTask)));

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
        select(0);

    }
    public void setToolbarTitle(String title, boolean isBackEnable) {
        this.isBackEnable = isBackEnable;
        if (binding.tvTitle != null) {
            binding.tvTitle.setText(title);
        }
        if (isBackEnable) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void selectTabPosition(int position) {
        binding.tabLayout.getTabAt(position).select();
    }

    private void select(int pos) {
        switch (pos) {
            case 0: {
                changeFragment(MyTaskDetailsFragment.newInstance(null), false, false);
                break;
            }
            case 1: {
                changeFragment(MyTaskCompleteDetailsFragment.newInstance(null), false, false);
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
}