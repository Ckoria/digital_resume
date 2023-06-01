package com.onthemove.commons.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.onthemove.R;
import com.onthemove.activities.VehicleHealthActivity;
import com.onthemove.adapters.FreeVehicalAdapter;
import com.onthemove.database.TaskListDatabaseManager;
import com.onthemove.interfaces.AuthContract;
import com.onthemove.interfaces.OrderContract;
import com.onthemove.interfaces.VehicalDetails;
import com.onthemove.modelClasses.OnDutyModel;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.responseClasses.VehicalHealthModel;
import com.onthemove.responseClasses.VehicalListModel;
import com.onthemove.responseClasses.VehicleStatusChangeModel;
import com.onthemove.viewModel.AuthViewModel;
import com.onthemove.viewModel.OrderViewModel;

import java.util.ArrayList;

public class FragmentVehicalSelectDailog extends DialogFragment implements View.OnClickListener, VehicalDetails.VehicalView, OrderContract.OrderView, FreeVehicalAdapter.onCheckedItemClickListner, AuthContract.AuthView {
    Context context;
    public static final String TAG = "FragmentVehicleSelectDailog";
    RecyclerView recyclerView;
    TextView tvEmpty;
    AppPref appPref;
    Button btnSubmit;
    SwitchCompat switchCompat;
    AuthViewModel authViewModel;
    FreeVehicalAdapter freeVehicalAdapter;
    TaskListDatabaseManager manager;
    ArrayList<VehicalListModel.VehicalListData> dataList;
    private OrderViewModel orderViewModel;
    VehicalListModel.VehicalListData SelectedVehicle;

    public static FragmentVehicalSelectDailog newInstance()
    {
        return new FragmentVehicalSelectDailog();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_vehical, container, false);
        recyclerView = view.findViewById(R.id.rcvvehicallist);
        tvEmpty = view.findViewById(R.id.tvEmptyData);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        switchCompat = getActivity().findViewById(R.id.switchDuty);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        manager = new TaskListDatabaseManager(getActivity());
        appPref = AppPref.getInstance(context);
        inits();
    }

    private void inits() {

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.setVehicalView(this);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.setAuthView(this);

        freeVehicalAdapter = new FreeVehicalAdapter(context,this);
        recyclerView.setAdapter(freeVehicalAdapter);

        btnSubmit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSubmit:
                getVehicleData();
                break;
        }
    }

    private void getVehicleData() {

     //   appPref.set(Constants.switchChecked,true);
        appPref.set(AppPref.VEHICLE_NO,String.valueOf(SelectedVehicle.getI()));
        appPref.set(AppPref.VEHICLE_REGISTER,String.valueOf(SelectedVehicle.getReg_number()));
        VehicleStatusChangeModel.VehicleStatusChangeReq changeReq = new VehicleStatusChangeModel.VehicleStatusChangeReq();
        changeReq.setVehicle_id(SelectedVehicle.getI());
        orderViewModel.changeVehicleStatus(changeReq);
        onDismiss();
        startActivity(new Intent(getActivity(), VehicleHealthActivity.class));
    }
    public void onDismiss() {
        Dialog dialog1 = getDialog();
        dialog1.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isOnline()) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
        } else {
            vehicalListApi();
        }

    }

    private void vehicalListApi() {
        AppLog.e("tag","call");
        orderViewModel.vehicalList();
    }
    @Override
    public boolean isOnline() {

        if (isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void unAuthorized() {

    }

    @Override
    public void responseNull() {

    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void OnSuccess() {

    }

    @Override
    public void vehicalList(ArrayList<VehicalListModel.VehicalListData> vehicalListData) {

        int totalSize = 0;

        if (vehicalListData != null && vehicalListData.size() > 0)
        {
            ArrayList<VehicalListModel.VehicalListData> listData = new ArrayList<>();

            for (int i = 0; i < vehicalListData.size(); i++) {
                VehicalListModel.VehicalListData vehicalNewListData = new VehicalListModel.VehicalListData();

                vehicalNewListData.setI(vehicalListData.get(i).getI());
                vehicalNewListData.setMake(vehicalListData.get(i).getMake());
                vehicalNewListData.setModel(vehicalListData.get(i).getModel());
                vehicalNewListData.setReg_number(vehicalListData.get(i).getReg_number());
                vehicalNewListData.setKm(vehicalListData.get(i).getKm());
                vehicalNewListData.setNxt_service(vehicalListData.get(i).getNxt_service());vehicalNewListData.setLice_exp(vehicalListData.get(i).getLice_exp());

                listData.add(vehicalNewListData);
            }

            if (listData.size() > 0)
            {
                freeVehicalAdapter.addList(listData);
                recyclerView.setVisibility(View.VISIBLE);
                tvEmpty.setVisibility(View.GONE);
            }
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
            switchCompat.setChecked(false);
        }
    }

    @Override
    public void vehicalInteriorList(ArrayList<VehicalHealthModel.VehicleInteriorData> interiorModels) {

    }

    @Override
    public void vehicalExteriorList(ArrayList<VehicalHealthModel.VehicleExteriorData> exteriorData) {

    }

    /*private void addData(int position, ArrayList<VehicalListModel.VehicalListData> dataList) {

        VehicalListModel.VehicalListData data = dataList.get(position);
        manager.addVehicle(data.getI(),data.getMake(),data.getModel(),data.getReg_number(),data.getKm(),data.getNxt_service(),data.getLice_exp());

        position++;

        if (dataList.size() != position) {
            AppLog.e(TAG, "data added: " + position);
            addData(position, dataList);
        } else {
            freeVehicalAdapter.addList(manager.getVehicleListData());
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }*/

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

    @Override
    public void myTaskList(ArrayList<NewTaskModel.NewTaskData> myTaskData) {

    }

    @Override
    public void completedTaskRes(String message) {

    }

    @Override
    public void addPart(int id, ArrayList<PartModel.TaskProductData> productData) {

    }

    @Override
    public void addRefuelingRes(String message) {

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

    @Override
    public void OnSelectedItemClick(VehicalListModel.VehicalListData data) {
        AppLog.e(TAG,"SelectITEM"+data.getI());
        SelectedVehicle = data;
    }

    @Override
    public void OnUnSelectedItemClick(VehicalListModel.VehicalListData data) {

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
    public void OnDutyRes(OnDutyModel.OnDutyData onDutyData) {

    }
}
