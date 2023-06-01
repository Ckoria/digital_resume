package com.onthemove.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.onthemove.R;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.Utils;
import com.onthemove.databinding.ItemTaskImageUploadBinding;
import com.onthemove.modelClasses.UploadAfterTaskImageModel;

import java.io.File;
import java.util.ArrayList;

public class UploadAfterTaskImageAdapter extends RecyclerView.Adapter<UploadAfterTaskImageAdapter.ViewHolder> {
    private static final String TAG = "UPLOADAFTERTASKIMAGEADAPTER";
    Context context;
    LayoutInflater inflater;
    UploadAfterTaskImageAdapter.UploadTaskImageListener listener;
    ArrayList<UploadAfterTaskImageModel> uploadImages;

    public UploadAfterTaskImageAdapter(Context context, LayoutInflater inflater, ArrayList<UploadAfterTaskImageModel> uploadImages, UploadAfterTaskImageAdapter.UploadTaskImageListener listener) {
        this.context = context;
        this.inflater = inflater;
        this.listener = listener;
        this.uploadImages = uploadImages;
    }

    @Override
    public UploadAfterTaskImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_task_image_upload,parent,false);
        return new UploadAfterTaskImageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadAfterTaskImageAdapter.ViewHolder holder, int position) {
        if (uploadImages.size() == position) {

            holder.binding.imgRemove.setVisibility(View.GONE);
            holder.binding.tvName.setVisibility(View.GONE);
            holder.binding.tvEditName.setVisibility(View.GONE);

            Glide.with(context).load(R.drawable.photo).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(holder.binding.imgUpload);

        } else {
            holder.binding.imgRemove.setVisibility(View.VISIBLE);
            holder.binding.tvName.setVisibility(View.VISIBLE);
            holder.binding.tvEditName.setVisibility(View.VISIBLE);

            UploadAfterTaskImageModel data = uploadImages.get(position);

//            String imagePath = uploadImages.get(position);
            Uri uri;

//            if(imagePath.toLowerCase().contains("http://")  || imagePath.contains("https://")){
            if (data.getImgPath().toString().toLowerCase().contains("http://") || data.getImgPath().toString().contains("https://")) {
//                uri= Uri.parse(imagePath);
                uri = data.getImgPath();

                AppLog.e(TAG, "GetTarget");
                //  getTarget(imagePath);
            } else {
//                uri=Uri.fromFile(new File(imagePath));
                uri = data.getImgPath();
            }

            int size = Utils.dpToPx(context.getResources().getDisplayMetrics(), 80);

            RequestOptions reqOption = new RequestOptions();
            reqOption.override(size, size);
            reqOption.placeholder(R.drawable.ic_launcher_background);

            Glide.with(context).load(uri).apply(reqOption).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(holder.binding.imgUpload);
            holder.binding.tvName.setText(data.getImgName());

        }


        holder.binding.cvImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uploadImages.size() == position) {
                    int value = 1;
                    listener.onNewImage(value);
                }
            }
        });

        holder.binding.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = 1;
                listener.onDeleteImage(position,value);
            }
        });
        holder.binding.tvEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = 1;
                listener.onEditName(uploadImages.get(position));
            }
        });
    }

    public void addImageData(ArrayList<UploadAfterTaskImageModel> uploadImages){
        this.uploadImages = uploadImages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (uploadImages.size() < 10) {
            return uploadImages.size() + 1;
        } else {
            return uploadImages.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ItemTaskImageUploadBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
    public interface UploadTaskImageListener {
        public void onNewImage(int value);

        public void onDeleteImage(int value,int position);

        public void onEditName(UploadAfterTaskImageModel uploadAfterTaskImageModel);
    }
}
