package com.onthemove.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.onthemove.R;
import com.onthemove.databinding.ItemAddQuntityBinding;
import com.onthemove.databinding.LayoutVehicleHealthBinding;
import com.onthemove.modelClasses.PartModel;

import java.util.ArrayList;

public class QuntityAdapter extends RecyclerView.Adapter<QuntityAdapter.ViewHolder> {
    ArrayList<PartModel.TaskProductData> productData;
    Context context;

    public QuntityAdapter(ArrayList<PartModel.TaskProductData> productData, Context context) {
        this.productData = productData;
        this.context = context;
    }

    @NonNull
    @Override
    public QuntityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddQuntityBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_add_quntity, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuntityAdapter.ViewHolder holder, int position) {
        PartModel.TaskProductData taskProductData = productData.get(position);
        Log.e("QuntityAdapter","product name"+taskProductData.getProduct_name());
        holder.binding.tvQuantity.setText(taskProductData.getProduct_name() + "( Quantity: " + taskProductData.getQty() +")");

        holder.binding.etQuntity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                taskProductData.setUsed_qty(s.toString());
                Log.e("QuntityAdapter","value"+s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productData != null ? productData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemAddQuntityBinding binding;
        public ViewHolder(@NonNull ItemAddQuntityBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
