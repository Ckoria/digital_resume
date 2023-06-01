package com.onthemove.interfaces;

import com.onthemove.commons.baseClasses.BaseView;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.responseClasses.NewTaskModel;

import java.util.ArrayList;

public interface OrderContract {

    interface OrderView extends BaseView {
        void orderPlaced(String message);
        void onPaymentSuccess();
        void onCancelOrder(String message);


        void Duty(String message);

        void onTaskStatusChange(String message, String task_id);

        void newTaskList(ArrayList<NewTaskModel.NewTaskData> newTaskData);

        void myTaskList(ArrayList<NewTaskModel.NewTaskData> myTaskData);

        void completedTaskRes(String message);

        void addPart(int id,ArrayList<PartModel.TaskProductData> productData);

        void addRefuelingRes(String message);
//        void applyCouponCode(ApplyCouponCodeModel.ApplyCouponCodeRes applyCouponCodeRes);

//        void createRazorpayOrder(CreateRazorpayOrderModel.CreateRazorPayData createRazorPayData);
//        void onPromoApply(PromoCodeRes promoCodeRes);
    }
    public interface OrderPresenter{

    }
}
