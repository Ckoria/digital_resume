package com.onthemove.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onthemove.R;
import com.onthemove.activities.ViewImageActivity;
import com.onthemove.databinding.ItemsOrderImagesBinding;
import com.onthemove.databinding.ItemsOrderListBinding;
import com.onthemove.responseClasses.OrderDetailsResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private static final String TAG = "OrderDetailsAdapter";

    private Context context;
    private ArrayList<OrderDetailsResponse.DataBean> list = new ArrayList<>();

    public OrderDetailsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_order_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {

        OrderDetailsResponse.DataBean data = list.get(position);

        holder.binding.tvTicketNumber.setText("#"+data.getTicketNumber());

        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM yyyy hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(data.getCreatedAt());
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.binding.tvTime.setText(str);
        holder.binding.tvDescription.setText(data.getComment());
        holder.binding.tvAddress.setText(data.getAddress());

        OrderDetailsImageAdapter orderDetailsImageAdapter = new OrderDetailsImageAdapter(context,data.getImages());
        holder.binding.rcvImages.setAdapter(orderDetailsImageAdapter);
        holder.binding.rcvImages.setHasFixedSize(true);
        holder.binding.rcvImages.setItemViewCacheSize(20);
        holder.binding.rcvImages.setDrawingCacheEnabled(true);
        holder.binding.rcvImages.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;

    }

    public void addList(ArrayList<OrderDetailsResponse.DataBean> olist) {

        this.list = olist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemsOrderListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
