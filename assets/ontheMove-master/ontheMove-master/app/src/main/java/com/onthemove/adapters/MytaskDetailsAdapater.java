package com.onthemove.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.onthemove.R;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.DateTimeHelper;
import com.onthemove.databinding.ItemsMyTaskBinding;
import com.onthemove.responseClasses.NewTaskModel;

import java.util.ArrayList;

public class MytaskDetailsAdapater extends RecyclerView.Adapter<MytaskDetailsAdapater.ViewHolder> {
    private static final String TAG = "MyTaskDetailsAdapter";
    private OnTaskDetailViewListener listener;

    private ArrayList<NewTaskModel.NewTaskData> list;
    Context context;

    public MytaskDetailsAdapater(Context context,OnTaskDetailViewListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_my_task, parent, false);
        MytaskDetailsAdapater.ViewHolder viewHolder = new MytaskDetailsAdapater.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewTaskModel.NewTaskData data = list.get(position);
        AppLog.e(TAG,"lat"+data.getLat());
        holder.binding.tvId.setText("#" + data.getTicketNumber());
        holder.binding.tvTime.setText(DateTimeHelper.convertFormat(data.getCreatedAt(), "yyyy-MM-dd HH:mm:ss", "hh:mm a") + " - Task");
        holder.binding.tvStatus.setText(data.getTaskStatus());
        holder.binding.tvDistance.setText(data.getPickupDistance() + " km away from current location");
        holder.binding.tvPerson.setText(data.getCustomerName());
        holder.binding.tvAddress.setText(data.getPickupAddress());

        holder.binding.tvViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener!=null)
                {
                    listener.OnTaskDetailViewClick(data);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void addList(ArrayList<NewTaskModel.NewTaskData> olist) {
        this.list = olist;
        notifyDataSetChanged();
    }
    public interface OnTaskDetailViewListener{
        void OnTaskDetailViewClick(NewTaskModel.NewTaskData data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ItemsMyTaskBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
