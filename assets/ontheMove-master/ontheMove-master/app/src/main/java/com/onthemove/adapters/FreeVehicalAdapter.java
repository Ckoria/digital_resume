package com.onthemove.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.onthemove.R;
import com.onthemove.databinding.ItemVehicalListBinding;
import com.onthemove.databinding.LayoutVehicleHealthBinding;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.responseClasses.VehicalListModel;

import java.util.ArrayList;

public class FreeVehicalAdapter extends RecyclerView.Adapter<FreeVehicalAdapter.ViewHolder> {

    Context context;
    private int selectedPosition = 0;
    private onCheckedItemClickListner listner;
    private ArrayList<VehicalListModel.VehicalListData> vehicalListData;

    public FreeVehicalAdapter(Context context,onCheckedItemClickListner listner) {
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public FreeVehicalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVehicalListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_vehical_list, parent, false);
        return new FreeVehicalAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeVehicalAdapter.ViewHolder holder, int position) {

        VehicalListModel.VehicalListData listData = vehicalListData.get(position);



        holder.binding.tvVehicalMake.setText(listData.getMake());

        holder.binding.tvVehicalModel.setText(listData.getModel());

        holder.binding.tvVehicalRegNo.setText(listData.getReg_number());

        holder.binding.checkBoxSelectVehical.setOnClickListener(view -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

        if (selectedPosition==position){
            holder.binding.checkBoxSelectVehical.setChecked(true);
            if (listner != null){
                listner.OnSelectedItemClick(listData);
            }
        }
        else {
            holder.binding.checkBoxSelectVehical.setChecked(false);
            if (listner != null){
                listner.OnUnSelectedItemClick(listData);
            }
        }
    }
    public void addList(ArrayList<VehicalListModel.VehicalListData> vehicalListData) {
        this.vehicalListData = vehicalListData;
        notifyDataSetChanged();
    }

    public interface onCheckedItemClickListner{
        void OnSelectedItemClick(VehicalListModel.VehicalListData data);
        void OnUnSelectedItemClick(VehicalListModel.VehicalListData data);
    }
    @Override
    public int getItemCount() {
        return vehicalListData != null ? vehicalListData.size() : 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemVehicalListBinding binding;
        public ViewHolder(@NonNull ItemVehicalListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }



}
