package com.onthemove.fragments;


import static android.app.Activity.RESULT_OK;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.onthemove.R;
import com.onthemove.activities.MainActivity2;
import com.onthemove.activities.TaskDetailActivity;
import com.onthemove.adapters.MyTaskAdapter;
import com.onthemove.commons.baseClasses.BaseFragment;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.commons.utils.Constants;
import com.onthemove.commons.utils.SwipeAndDrag;
import com.onthemove.database.TaskListDatabaseManager;
import com.onthemove.databinding.FragmentMyTaskBinding;
import com.onthemove.interfaces.OrderContract;
import com.onthemove.interfaces.StartDragItemListner;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.responseClasses.MyTaskModel;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.services.FloatingFaceBubbleService;
import com.onthemove.viewModel.OrderViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTaskFragment extends BaseFragment implements View.OnClickListener, StartDragItemListner, OrderContract.OrderView, MyTaskAdapter.OnTaskDetailViewListener {

    private static final String TAG = "MyTaskFragment";

    private FragmentMyTaskBinding binding;
    private Context context;
    private MyTaskAdapter myTaskAdapter;
    private ItemTouchHelper itemTouchHelper;
    TaskListDatabaseManager manager;
    ArrayList<NewTaskModel.NewTaskData> dataList;
    LatLng latLng = null;
    private OrderViewModel orderViewModel;

    public static MyTaskFragment newInstance(Bundle bundle) {
        MyTaskFragment fragment = new MyTaskFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public MyTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        inits();
        listner();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_task, container, false);
        manager = new TaskListDatabaseManager(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity2) getActivity()).setToolbarTitle(context.getResources().getString(R.string.myTask), false);
    }

    private void inits() {

        if ((appPref.getString(AppPref.LAT) != null && !appPref.getString(AppPref.LAT).isEmpty()) && (appPref.getString(AppPref.LNG)!= null && !appPref.getString(AppPref.LNG).isEmpty()))
        {
            latLng = new LatLng(Double.parseDouble(appPref.getString(AppPref.LAT)),Double.parseDouble(appPref.getString(AppPref.LNG)));
        }


        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.setView(this);

        myTaskAdapter = new MyTaskAdapter(context, this, this);

        SwipeAndDrag swipeAndDrag = new SwipeAndDrag(myTaskAdapter);
        itemTouchHelper = new ItemTouchHelper(swipeAndDrag);
        itemTouchHelper.attachToRecyclerView(binding.rcvMyTask);
        binding.rcvMyTask.setAdapter(myTaskAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isOnline()) {
            if (manager.getMyTaskListCount() > 0 && latLng != null){
                dataList = manager.getSubmitData(latLng);
                myTaskAdapter.addList(dataList);
            }else {
                binding.rcvMyTask.setVisibility(View.GONE);
                binding.tvEmpty.setVisibility(View.VISIBLE);
            }
        } else {
            myTaskListApi();
        }
    }

    private void myTaskListApi() {
        orderViewModel.myTaskList();
    }

    @Override
    public void myTaskList(ArrayList<NewTaskModel.NewTaskData> myTaskData) {

        int totalSize = 0;

        if (myTaskData != null && myTaskData.size() > 0) {

            ArrayList<NewTaskModel.NewTaskData> myTaskNewData = new ArrayList<>();

            for (int i = 0; i < myTaskData.size(); i++) {

                if (manager.checkRecord(myTaskData.get(i).getTaskId())) {
                    AppLog.e(TAG, "exists: ");
                }
                else {
                    NewTaskModel.NewTaskData data = new NewTaskModel.NewTaskData();
                    data.setTaskId(myTaskData.get(i).getTaskId());
                    data.setCustomerName(myTaskData.get(i).getCustomerName());
                    data.setMobileNumber(myTaskData.get(i).getMobileNumber());
                    data.setEmail(myTaskData.get(i).getEmail());
                    data.setTicketNumber(myTaskData.get(i).getTicketNumber());

                    AppLog.e(TAG,"latlong"+myTaskData.get(i).getLat()+"long"+myTaskData.get(i).getLng());
                    data.setLat(myTaskData.get(i).getLat());
                    data.setLng(myTaskData.get(i).getLng());
                    data.setPickupAddress(myTaskData.get(i).getPickupAddress());
                    data.setHouseNumber(myTaskData.get(i).getHouseNumber());
                    data.setComplexName(myTaskData.get(i).getComplexName());
                    data.setPickupDistance(myTaskData.get(i).getPickupDistance());
                    data.setPickupDateTime(myTaskData.get(i).getPickupDateTime());
                    data.setCreatedAt(myTaskData.get(i).getCreatedAt());
                    data.setDescription(myTaskData.get(i).getDescription());
                    data.setTaskStatus(myTaskData.get(i).getTaskStatus());
                    data.setAssignedDatetime(myTaskData.get(i).getAssignedDatetime());
                    myTaskNewData.add(data);
                }
            }

            if (myTaskNewData.size() > 0) {
                addData(totalSize, myTaskNewData);
            } else {

                if (manager.getMyTaskListCount() > 0 && latLng!= null){
                    myTaskAdapter.addList(manager.getSubmitData(latLng));
                    binding.rcvMyTask.setVisibility(View.VISIBLE);
                    binding.tvEmpty.setVisibility(View.GONE);
                }else {
                    binding.rcvMyTask.setVisibility(View.GONE);
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                }
            }

        } else {
            binding.rcvMyTask.setVisibility(View.GONE);
            binding.tvEmpty.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void completedTaskRes(String message) {

    }

    @Override
    public void addPart(int id,ArrayList<PartModel.TaskProductData> productData) {

    }

    @Override
    public void addRefuelingRes(String message) {

    }

    private void addData(int position, ArrayList<NewTaskModel.NewTaskData> myTaskData) {

        NewTaskModel.NewTaskData data = myTaskData.get(position);

        Log.e(TAG,"lat"+data.getLat());
        manager.addTask(data.getTaskId(), data.getCustomerName(), data.getHouseNumber(), data.getComplexName(), data.getPickupAddress(), data.getMobileNumber(), data.getPickupDistance(), data.getDescription(), data.getCreatedAt(),  data.getLat(), data.getLng(),data.getTaskStatus(),"no","no",data.getTicketNumber());

        position++;

        if (myTaskData.size() != position) {
            AppLog.e(TAG, "data added: " + position);
            addData(position, myTaskData);
        } else {
            if (manager.getMyTaskListCount() > 0 && latLng!= null){

                myTaskAdapter.addList(manager.getSubmitData(latLng));
                binding.rcvMyTask.setVisibility(View.VISIBLE);
                binding.tvEmpty.setVisibility(View.GONE);
            }else {
                binding.rcvMyTask.setVisibility(View.GONE);
                binding.tvEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void OnTaskDetailViewClick(NewTaskModel.NewTaskData data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("taskData", data);
        bundle.putString("from", "myTask");
        gotoActivity(TaskDetailActivity.class, bundle, false);
    }

    private void listner() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void startDrag(RecyclerView.ViewHolder viewHolder) {

        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void orderPlaced(String message) {

    }

    @Override
    public void onPaymentSuccess() {

    }

    @Override
    public void onCancelOrder(String message) {

    }

    @Override
    public void Duty(String message) {

    }

    @Override
    public void onTaskStatusChange(String message, String task_id) {

    }

    @Override
    public void newTaskList(ArrayList<NewTaskModel.NewTaskData> newTaskData) {

    }

}
