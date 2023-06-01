package com.onthemove.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onthemove.R;
import com.onthemove.activities.MainActivity2;
import com.onthemove.activities.MyTaskDetails;
import com.onthemove.activities.TaskDetailActivity;
import com.onthemove.adapters.MytaskDetailsAdapater;
import com.onthemove.commons.baseClasses.BaseFragment;
import com.onthemove.database.TaskListDatabaseManager;
import com.onthemove.databinding.FragmentMyTaskCompleteDetailsBinding;
import com.onthemove.responseClasses.NewTaskModel;

import java.util.ArrayList;


public class MyTaskCompleteDetailsFragment extends BaseFragment implements View.OnClickListener, MytaskDetailsAdapater.OnTaskDetailViewListener {

    private Context context;
    FragmentMyTaskCompleteDetailsBinding binding;
    ArrayList<NewTaskModel.NewTaskData> dataList;
    private MytaskDetailsAdapater myTaskAdapter;
    TaskListDatabaseManager manager;

    public static MyTaskCompleteDetailsFragment newInstance(Bundle bundle) {
        MyTaskCompleteDetailsFragment fragment = new MyTaskCompleteDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public MyTaskCompleteDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        inits();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_task_complete_details, container, false);
        manager = new TaskListDatabaseManager(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MyTaskDetails) getActivity()).setToolbarTitle(context.getResources().getString(R.string.CompleteTask), false);
    }
    private void inits() {

        myTaskAdapter = new MytaskDetailsAdapater(context,this);
        myTaskAdapter.addList(manager.getSubmitCompleteData());
        binding.rcvMyTaskCompleteDetails.setAdapter(myTaskAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (manager.getMyTaskCompleteListCount() > 0){
            dataList = manager.getSubmitCompleteData();
            myTaskAdapter.addList(dataList);
        }else {
            binding.rcvMyTaskCompleteDetails.setVisibility(View.GONE);
            binding.tvEmpty.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnTaskDetailViewClick(NewTaskModel.NewTaskData data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("taskData", data);
        bundle.putString("from", "myTask");
        gotoActivity(TaskDetailActivity.class, bundle, false);
    }
}