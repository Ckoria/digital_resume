package com.onthemove.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.onthemove.R;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.databinding.LayoutVehicleHealthBinding;
import com.onthemove.responseClasses.VehicalHealthModel;

import java.util.ArrayList;

public class VehicleHealthAdapter extends RecyclerView.Adapter<VehicleHealthAdapter.MyViewHolder> {

    private ArrayList<VehicalHealthModel.VehicleExteriorData> vehicleHealthList;

    private OnRadioGroupItemListner listner;

    public VehicleHealthAdapter(OnRadioGroupItemListner listner) {
        this.listner = listner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutVehicleHealthBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_vehicle_health, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        VehicalHealthModel.VehicleExteriorData data = vehicleHealthList.get(position);

        holder.binding.tvName.setText(data.getExt_type_title());

       // holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
     //   holder.binding.edtComment.setText(mDataset[holder.getAdapterPosition()]);
        AppLog.e("VehicleAdapter","ids"+data.getExt_id());

        int finalPosition = position;

        int idtype = data.getExt_id();

        int value1 = 1;
        int value2 = 2;
        int value3 = 3;

        final int[] a = {0};

        if (a[0] == 0)
        {
            listner.onSelectedExteriorValueItem(finalPosition,1,idtype);
            listner.OnSelectedRadioButtonExtComment(finalPosition,"");
        }

        holder.binding.rgCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbGood:
                        holder.binding.edtComment.setVisibility(View.GONE);
                        listner.onSelectedExteriorValueItem(finalPosition,value1,idtype);
                        a[0] = 1;
                        break;
                    case R.id.rbLooked:
                        holder.binding.edtComment.setVisibility(View.VISIBLE);
                        listner.onSelectedExteriorValueItem(finalPosition,value2,idtype);
                        a[0] = 1;
                        break;

                    case R.id.rbRepaired:
                        holder.binding.edtComment.setVisibility(View.VISIBLE);
                        listner.onSelectedExteriorValueItem(finalPosition,value3,idtype);
                        a[0] = 1;
                        break;
                }
            }
        });
        holder.binding.edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                listner.OnSelectedRadioButtonExtComment(finalPosition,str);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnRadioGroupItemListner{
        void onSelectedExteriorValueItem(int pos,int value,int idtype);
        void OnSelectedRadioButtonExtComment(int pos,String value);
    }

    @Override
    public int getItemCount() {
        return vehicleHealthList != null ? vehicleHealthList.size() : 0;
    }

    public void addItem(ArrayList<VehicalHealthModel.VehicleExteriorData> vehicleHealthList) {
        this.vehicleHealthList = vehicleHealthList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LayoutVehicleHealthBinding binding;


        public MyViewHolder(@NonNull LayoutVehicleHealthBinding itemView) {
            super(itemView.getRoot());

            binding = itemView;

        }
    }

}
