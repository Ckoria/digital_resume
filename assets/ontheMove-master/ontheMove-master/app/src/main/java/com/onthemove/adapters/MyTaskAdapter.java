package com.onthemove.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.onthemove.R;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.DateTimeHelper;
import com.onthemove.databinding.ItemsMyTaskBinding;
import com.onthemove.interfaces.AdapterItemTouchListner;
import com.onthemove.interfaces.StartDragItemListner;
import com.onthemove.interfaces.ViewHolderItemTouchListner;
import com.onthemove.responseClasses.NewTaskModel;

import java.util.ArrayList;
import java.util.Collections;

public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder> implements AdapterItemTouchListner {

    private static final String TAG = "MyTaskAdapter";

    private Context context;
    private StartDragItemListner startDragItemListner;
    private ArrayList<NewTaskModel.NewTaskData> list;
    private OnTaskDetailViewListener listener;

    public MyTaskAdapter(Context context, StartDragItemListner startDragItemListner, OnTaskDetailViewListener listener) {
        this.context = context;
        this.startDragItemListner = startDragItemListner;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_my_task, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTaskAdapter.ViewHolder holder, int position) {
        NewTaskModel.NewTaskData data = list.get(position);
        AppLog.e(TAG,"lat"+data.getLat());
        holder.binding.tvId.setText("#" + data.getTicketNumber());
        holder.binding.tvTime.setText(DateTimeHelper.convertFormat(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss", "hh:mm a") + " - Task");
        holder.binding.tvStatus.setText(data.getTaskStatus());
        holder.binding.tvDistance.setText(data.getPickupDistance() + " km away from current location");
        holder.binding.tvPerson.setText(data.getCustomerName());
        holder.binding.tvAddress.setText(data.getPickupAddress());

        if (data.getTaskStatus().equalsIgnoreCase("failed"))
        {
           holder.binding.imgCheck.setColorFilter(ContextCompat.getColor(context, R.color.colorRed), android.graphics.PorterDuff.Mode.SRC_IN);
           holder.binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        }

        holder.binding.tvViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnTaskDetailViewClick(data);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface OnTaskDetailViewListener {
        void OnTaskDetailViewClick(NewTaskModel.NewTaskData data);
    }

    public void addList(ArrayList<NewTaskModel.NewTaskData> olist) {

        this.list = olist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ViewHolderItemTouchListner {

        private ItemsMyTaskBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void itemSelected() {

        }

        @Override
        public void itemClear() {

        }
    }

    @Override
    public void itemMove(int oldPosition, int newPosition) {
        Collections.swap(list, oldPosition, newPosition);
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void swipeItemDismiss(int position) {

    }

    public boolean isOnline() {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
//        if (!isAvailable) {
//            AppDialog.showNoNetworkDialog(this);
//        }
        return isAvailable;
    }
}
