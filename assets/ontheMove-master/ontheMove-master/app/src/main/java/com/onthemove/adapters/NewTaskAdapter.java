package com.onthemove.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.onthemove.R;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.commons.utils.Constants;
import com.onthemove.commons.utils.DateTimeHelper;
import com.onthemove.database.TaskListDatabaseManager;
import com.onthemove.databinding.ItemsNewTaskBinding;
import com.onthemove.responseClasses.NewTaskModel;

import java.util.ArrayList;

public class NewTaskAdapter extends RecyclerView.Adapter<NewTaskAdapter.ViewHolder> {

    private static final String TAG = "NewTaskAdapter";
    private Context context;
    private OnAcceptClickListener listener;
    private ArrayList<NewTaskModel.NewTaskData> newTaskData;

    public NewTaskAdapter(Context context, OnAcceptClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewTaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_new_task, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewTaskAdapter.ViewHolder holder, int position) {


        if (!AppPref.getInstance(context).getBoolean(Constants.switchChecked))
        {
            holder.binding.tvAccept.setVisibility(View.GONE);
            holder.binding.tvDecline.setVisibility(View.GONE);
        }

        NewTaskModel.NewTaskData data = newTaskData.get(position);

        holder.binding.tvDate.setText(DateTimeHelper.convertFormat(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss", "dd MMMM, yyyy hh:mm a"));

        holder.binding.tvCustomerName.setText(data.getCustomerName());

        holder.binding.tvAddress.setText(data.getPickupAddress());

        holder.binding.tvHouseNumber.setText(data.getHouseNumber());

        holder.binding.tvComplexName.setText(data.getComplexName());

        Log.e(TAG,"lat"+ data.getLat());
        Log.e(TAG,"lng"+ data.getLng());

        holder.binding.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.OnAcceptClick(data);
                }
            }
        });

        holder.binding.icCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+data.getMobileNumber()));
                context.startActivity(callIntent);
            }
        });
        holder.binding.tvDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.OnDeclineClick(data);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.OnViewDetailClick(data);
                }
            }
        });
    }



    public interface OnAcceptClickListener{
        void OnAcceptClick(NewTaskModel.NewTaskData data);
        void OnDeclineClick(NewTaskModel.NewTaskData data);
        void OnViewDetailClick(NewTaskModel.NewTaskData data);
    }

    public void addList(ArrayList<NewTaskModel.NewTaskData> newTaskData) {
        this.newTaskData = newTaskData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return newTaskData != null ? newTaskData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemsNewTaskBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
