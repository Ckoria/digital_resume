package com.onthemove.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.onthemove.R;
import com.onthemove.activities.ViewImageActivity;
import com.onthemove.databinding.ItemsOrderImagesBinding;
import com.onthemove.responseClasses.OrderDetailsResponse;

import java.util.ArrayList;

public class OrderDetailsImageAdapter extends RecyclerView.Adapter<OrderDetailsImageAdapter.ViewHolder> {

    private static final String TAG = "OrderDetailsImageAdapter";

    private Context context;
    private ArrayList<OrderDetailsResponse.DataBean.ImagesBean> imageList = new ArrayList<>();
    private ImageClickListner listner;

    public OrderDetailsImageAdapter(Context context, ArrayList<OrderDetailsResponse.DataBean.ImagesBean> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public OrderDetailsImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_order_images,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsImageAdapter.ViewHolder holder, int position) {

        OrderDetailsResponse.DataBean.ImagesBean data = imageList.get(position);

        Glide.with(context).load(data.getImage()).into(holder.binding.imgs);

        holder.binding.cvImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                listner.onClick(data,position);

                Intent intent = new Intent(context, ViewImageActivity.class);
                intent.putExtra("IMAGE",data.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemsOrderImagesBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void getImage(ImageClickListner imageClickListner){

        this.listner =imageClickListner;
    }

    public interface ImageClickListner {

        void onClick(OrderDetailsResponse.DataBean.ImagesBean data,int pos);

    }
}
