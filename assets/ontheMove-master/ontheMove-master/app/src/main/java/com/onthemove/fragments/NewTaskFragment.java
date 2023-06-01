package com.onthemove.fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.onthemove.R;
import com.onthemove.activities.MainActivity2;
import com.onthemove.activities.TaskDetailActivity;
import com.onthemove.adapters.NewTaskAdapter;
import com.onthemove.commons.baseClasses.BaseFragment;
import com.onthemove.commons.utils.AppDialog;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.commons.utils.Constants;
import com.onthemove.commons.utils.DateTimeHelper;
import com.onthemove.database.TaskListDatabaseManager;
import com.onthemove.databinding.FragmentNewTaskBinding;
import com.onthemove.interfaces.OrderContract;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.responseClasses.TaskStatusChangeModel;
import com.onthemove.viewModel.OrderViewModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */

public class NewTaskFragment extends BaseFragment implements View.OnClickListener, OrderContract.OrderView, NewTaskAdapter.OnAcceptClickListener {

    private static final String TAG = "NewTaskFragment";

    private FragmentNewTaskBinding binding;
    private ViewDataBinding bind;
    private static final int PHONECALL_PERMISSION_CODE = 100;
    private Context context;
    TaskListDatabaseManager manager;
    ArrayList<NewTaskModel.NewTaskData> dataList;
    public NewTaskAdapter newTaskAdapter;
    private OrderViewModel orderViewModel;
    SwitchCompat switchCompat;
    private int totalSize = 0;
    public boolean checkChange;


    public static NewTaskFragment newInstance(Bundle bundle) {
        NewTaskFragment fragment = new NewTaskFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public NewTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false);
        manager = new TaskListDatabaseManager(getActivity());

        switchCompat = getActivity().findViewById(R.id.switchDuty);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        inits();
        listner();
        checkPermission();
    }

    private void newTaskListApi() {

        orderViewModel.newTaskList();
    }

    private void addData(int position, ArrayList<NewTaskModel.NewTaskData> dataList) {

        NewTaskModel.NewTaskData data = dataList.get(position);
        manager.addTask(data.getTaskId(), data.getCustomerName(), data.getHouseNumber(),data.getComplexName(),data.getPickupAddress(), data.getMobileNumber(), data.getPickupDistance(), data.getDescription(), data.getCreatedAt(), data.getLat(), data.getLng(), "assigned","no","no",data.getTicketNumber());

        position++;

        if (dataList.size() != position) {
            AppLog.e(TAG, "data added: " + position);
            addData(position, dataList);
        } else {
            newTaskAdapter.addList(manager.getNewTaskData());
            binding.rcvNewTask.setVisibility(View.VISIBLE);
            binding.tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity2) getActivity()).setToolbarTitle(context.getResources().getString(R.string.taskList), false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isOnline()) {
            if (manager.getNewTaskListCount() > 0) {
                dataList = manager.getNewTaskData();
                newTaskAdapter.addList(dataList);
            } else {
                binding.rcvNewTask.setVisibility(View.GONE);
                binding.tvEmpty.setVisibility(View.VISIBLE);
            }
        } else {
            newTaskListApi();
        }
    }

    private void inits() {

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.setView(this);

        newTaskAdapter = new NewTaskAdapter(context, this);
        binding.rcvNewTask.setAdapter(newTaskAdapter);
    }

    @Override
    public void newTaskList(ArrayList<NewTaskModel.NewTaskData> newTaskData) {

        int totalSize = 0;
        if (newTaskData != null && newTaskData.size() > 0) {

            ArrayList<NewTaskModel.NewTaskData> newData = new ArrayList<>();

            for (int i = 0; i < newTaskData.size(); i++) {
                if (manager.checkRecord(newTaskData.get(i).getTaskId())) {
                    AppLog.e(TAG, "exists: ");
                } else {
                    NewTaskModel.NewTaskData data = new NewTaskModel.NewTaskData();
                    data.setTaskId(newTaskData.get(i).getTaskId());
                    data.setCustomerName(newTaskData.get(i).getCustomerName());
                    data.setMobileNumber(newTaskData.get(i).getMobileNumber());
                    data.setEmail(newTaskData.get(i).getEmail());
                    data.setTicketNumber(newTaskData.get(i).getTicketNumber());
                    data.setLat(newTaskData.get(i).getLat());
                    data.setLng(newTaskData.get(i).getLng());
                    data.setPickupAddress(newTaskData.get(i).getPickupAddress());
                    data.setHouseNumber(newTaskData.get(i).getHouseNumber());
                    data.setComplexName(newTaskData.get(i).getComplexName());
                    data.setPickupDistance(newTaskData.get(i).getPickupDistance());
                    data.setPickupDateTime(newTaskData.get(i).getPickupDateTime());
                    data.setCreatedAt(newTaskData.get(i).getCreatedAt());
                    data.setDescription(newTaskData.get(i).getDescription());
                    data.setTaskStatus(newTaskData.get(i).getTaskStatus());
                    data.setAssignedDatetime(newTaskData.get(i).getAssignedDatetime());
                    newData.add(data);
                    orderViewModel.partData(Integer.parseInt(newTaskData.get(i).getTaskId()));
                }
            }

            if (newData.size() > 0) {
                addData(totalSize, newData);
            }
            else {
                newTaskAdapter.addList(manager.getNewTaskData());
                binding.rcvNewTask.setVisibility(View.VISIBLE);
                binding.tvEmpty.setVisibility(View.GONE);
            }


        }
        else {
            binding.rcvNewTask.setVisibility(View.GONE);
            binding.tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void myTaskList(ArrayList<NewTaskModel.NewTaskData> myTaskData) {

    }

    @Override
    public void completedTaskRes(String message) {

    }

    @Override
    public void addPart(int id, ArrayList<PartModel.TaskProductData> productData) {
        if (!manager.checkPartRecord(String.valueOf(id)))
        {
            for (int i=0;i<productData.size();i++)
            {
                manager.addPart(String.valueOf(id),String.valueOf(productData.get(i).getProduct_id()),productData.get(i).getProduct_name(),productData.get(i).getUpc_code(),productData.get(i).getQty());
            }
        }
        else
        {
            Log.e(TAG,"exist part record"+id);
        }
    }

    @Override
    public void addRefuelingRes(String message) {

    }

    @Override
    public void onTaskStatusChange(String message, String task_id) {
        //newTaskListApi();
     //   ((MainActivity2) getActivity()).selectTabPosition(1);
        onStart();
    }

    @Override
    public void OnAcceptClick(NewTaskModel.NewTaskData data) {

        AppDialog.showConfirmDialog(getActivity(), "Are you sure you want to Accept?", new AppDialog.AppDialogListener() {
            @Override
            public void okClick(DialogInterface dialog) {

                String id = data.getTaskId();
                String address = data.getPickupAddress();
                String name = data.getCustomerName();
                String phoneno = data.getMobileNumber();
                String disctance = data.getPickupDistance();
                String des = data.getDescription();
                String date = data.getCreatedAt();
                String ticketNo = data.getTicketNumber();

                if (isConnected()) {
                    AppLog.e(TAG, "Connection");

                    TaskStatusChangeModel.TaskStatusChangeReq taskStatusChangeReq = new TaskStatusChangeModel.TaskStatusChangeReq();
                    taskStatusChangeReq.setTask_id(data.getTaskId());
                    taskStatusChangeReq.setStatus("accepted");
                    orderViewModel.changeTaskStatus(taskStatusChangeReq);

                    try {
                        manager.updateTask(id, name, address, phoneno, disctance, des, date, "accepted", "", "",ticketNo);

                        String currentDate = DateTimeHelper.convertFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
                        manager.addTaskStatus(id, currentDate, "accepted", "true");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        manager.updateTask(id, name, address, phoneno, disctance, des, date, "accepted", "", "",ticketNo);

                        String currentDate = DateTimeHelper.convertFormat(new Date(), "yyyy-MM-dd HH:mm:ss");
                        manager.addTaskStatus(id, currentDate, "accepted", "false");
                        onStart();
                  //      ((MainActivity2) getActivity()).selectTabPosition(1);
                    } catch (SQLiteException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    private void checkPermission() {

        checkPermission(Manifest.permission.CALL_PHONE, PHONECALL_PERMISSION_CODE);
    }

    private void checkPermission(String permission, int phonecallPermissionCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(), new String[] { permission }, phonecallPermissionCode);
        }
        else
        {
            AppLog.e(TAG,"permission already granted");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == PHONECALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AppLog.e(TAG,"CAll Permission Granted");
            }
            else {
                AppLog.e(TAG,"call Permission Denied");

            }
        }
    }


    @Override
    public void OnDeclineClick(NewTaskModel.NewTaskData data) {

    }

    @Override
    public void OnViewDetailClick(NewTaskModel.NewTaskData data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("taskData", data);
        bundle.putString("from", "new");
        gotoActivity(TaskDetailActivity.class, bundle, false);
    }

    private void listner() {

    }

    @Override
    public void onClick(View view) {

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
        if (message.equalsIgnoreCase("on"))
        {
            ((MainActivity2) getActivity()).checkChange = true;
            AppPref.getInstance(context).set(Constants.switchChecked,true);
            switchCompat.setChecked(true);
        }
        else if (message.equalsIgnoreCase("off"))
        {
            ((MainActivity2) getActivity()).checkChange = false;
            AppPref.getInstance(context).set(Constants.switchChecked,false);
            switchCompat.setChecked(false);
        }

    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            AppLog.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
