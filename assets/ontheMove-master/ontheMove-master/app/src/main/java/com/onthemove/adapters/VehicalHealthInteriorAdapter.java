package com.onthemove.adapters;

import android.text.Editable;
import android.text.TextUtils;
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

public class VehicalHealthInteriorAdapter extends RecyclerView.Adapter<VehicalHealthInteriorAdapter.ViewHolder> {

    public static final String TAG = "interiorADapter";
    private ArrayList<VehicalHealthModel.VehicleInteriorData> vehicleHealthList;
    private onClickItemListner listner;

    public VehicalHealthInteriorAdapter(onClickItemListner listner) {
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutVehicleHealthBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_vehicle_health, parent, false);
        return new VehicalHealthInteriorAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        VehicalHealthModel.VehicleInteriorData data = vehicleHealthList.get(position);

        AppLog.e(TAG,"imteriorname"+data.getInt_type());

        holder.binding.tvName.setText(data.getInt_type_title());


        int idType = data.getInt_id();

        int finalPosition = position;

        int value1 = 1;
        int value2 = 2;
        int value3 = 3;

        final int[] a = {0};

        if (a[0] == 0)
        {
            listner.onSelectedInteriorValueItem(finalPosition,1,idType);
            listner.OnSelectedRadioButtonIntComment(finalPosition,"");
        }

        holder.binding.rgCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbGood:
                        holder.binding.edtComment.setVisibility(View.GONE);
                        listner.onSelectedInteriorValueItem(finalPosition,1,idType);
                        a[0] =1;
                        break;
                    case R.id.rbLooked:
                        listner.onSelectedInteriorValueItem(finalPosition,2,idType);
                        holder.binding.edtComment.setVisibility(View.VISIBLE);
                        a[0] =1;
                        break;
                    case R.id.rbRepaired:
                        listner.onSelectedInteriorValueItem(finalPosition,3,idType);
                        holder.binding.edtComment.setVisibility(View.VISIBLE);
                        a[0] =1;
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
                listner.OnSelectedRadioButtonIntComment(finalPosition,str);
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

    public interface onClickItemListner{
        void onSelectedInteriorValueItem(int pos,int value,int idType);
        void OnSelectedRadioButtonIntComment(int pos,String value);
    }


    @Override
    public int getItemCount() {
        return vehicleHealthList != null ? vehicleHealthList.size() : 0;
    }


    public void addItem(ArrayList<VehicalHealthModel.VehicleInteriorData> vehicleHealthList) {
        this.vehicleHealthList = vehicleHealthList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LayoutVehicleHealthBinding binding;

        public ViewHolder(@NonNull LayoutVehicleHealthBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }
}
