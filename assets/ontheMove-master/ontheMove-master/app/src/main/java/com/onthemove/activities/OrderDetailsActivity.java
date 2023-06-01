package com.onthemove.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.onthemove.R;
import com.onthemove.adapters.OrderDetailsAdapter;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.database.DatabaseManager;
import com.onthemove.databinding.ActivityOrderDetailsBinding;
import com.onthemove.interfaces.OrderContract;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.modelClasses.SubmitOrderModel;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.responseClasses.OrderDetailsResponse;
import com.onthemove.viewModel.OrderViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener, OrderContract.OrderView {

    private static final String TAG = "OrderDetailsActivity";

    private ActivityOrderDetailsBinding binding;
    private Context context;
    private OrderViewModel orderViewModel;
    private OrderDetailsAdapter orderDetailsAdapter;
    private DatabaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        context = OrderDetailsActivity.this;

        inits();
        listners();
    }

    private void inits() {

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.setView(this);
        manager = new DatabaseManager(this);

        orderDetailsAdapter = new OrderDetailsAdapter(context);
        binding.rcvOrderList.setAdapter(orderDetailsAdapter);

        if (isOnline()) {
            orderViewModel.getOrderDetails();
        } else {
            ArrayList<OrderDetailsResponse.DataBean> dataList = new ArrayList<>();
            ArrayList<SubmitOrderModel> taskList = manager.getSubmitData();


            for (int i = 0; i < taskList.size(); i++) {

                OrderDetailsResponse.DataBean data = new OrderDetailsResponse.DataBean();
                data.setOrderDetailsId(Integer.parseInt(taskList.get(i).getId()));
                data.setTicketNumber(taskList.get(i).getTicket_number());
                data.setComment(taskList.get(i).getComment());
                data.setLat(String.valueOf(taskList.get(i).getLat()));
                data.setLng(String.valueOf(taskList.get(i).getLng()));
                data.setAddress(taskList.get(i).getAddress());
                data.setCreatedAt(taskList.get(i).getDateTime());

                List<String> imageList = Arrays.asList(taskList.get(i).getImage().split(","));
                ArrayList<OrderDetailsResponse.DataBean.ImagesBean> imgList = new ArrayList<>();

                for (int j = 0; j < imageList.size(); j++) {
                    OrderDetailsResponse.DataBean.ImagesBean img = new OrderDetailsResponse.DataBean.ImagesBean();
                    img.setImage(imageList.get(j));
                    imgList.add(img);
                }
                data.setImages(imgList);
                dataList.add(data);
            }

            orderDetailsAdapter.addList(dataList);
        }

        subscribe();

    }

    private void listners() {

        binding.linBack.setOnClickListener(this);
    }

    private void subscribe() {

        orderViewModel.orderDetailsLiveDataList.observe(this, new Observer<ArrayList<OrderDetailsResponse.DataBean>>() {
            @Override
            public void onChanged(ArrayList<OrderDetailsResponse.DataBean> dataList) {

                orderDetailsAdapter.addList(dataList);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.linBack:
                onBackPressed();
                break;
        }
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

    @Override
    public void OnSuccess() {

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
