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
import com.onthemove.databinding.ItemsUploadImgesBinding;
import com.onthemove.modelClasses.UploadImagesModel;

import java.io.File;
import java.util.ArrayList;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder> {

    private static final String TAG = "UploadImageAdapter";

    private Context context;
    //    private final ArrayList<String> uploadImages;
    private final ArrayList<UploadImagesModel> uploadImages;
    UploadImageListener listener;

//    public UploadImageAdapter(Context context, ArrayList<String> uploadImages, UploadImageListener listener) {
//        this.context = context;
//        this.uploadImages = uploadImages;
//        this.listener = listener;
//    }


    public UploadImageAdapter(Context context, ArrayList<UploadImagesModel> uploadImages, UploadImageListener listener) {
        this.context = context;
        this.uploadImages = uploadImages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UploadImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.items_upload_imges, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UploadImageAdapter.ViewHolder holder, int position) {

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

            UploadImagesModel data = uploadImages.get(position);

//            String imagePath = uploadImages.get(position);
            Uri uri = null;

//            if(imagePath.toLowerCase().contains("http://")  || imagePath.contains("https://")){
            if (data.getImgPath().toString().toLowerCase().contains("http://") || data.getImgPath().toString().contains("https://")) {
//                uri= Uri.parse(imagePath);
                uri = data.getImgPath();

                AppLog.e(TAG, "GetTarget");
                //  getTarget(imagePath);
            } else {
                uri = data.getImgPath();
//                uri=Uri.fromFile(new File(imagePath));
              //  uri = Uri.fromFile(new File(data.getImgPath()));
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
                    listener.onNewImage();
                }
            }
        });

        holder.binding.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onDeleteImage(position);
            }
        });

        holder.binding.tvEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditName(uploadImages.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
//        return 3;

//        if(uploadImages.size()<4)
//        {
//            return uploadImages.size()+1;
//        }
//        else
//        {
//            return uploadImages.size();
//        }

        if (uploadImages.size() < 10) {
            return uploadImages.size() + 1;
        } else {
            return uploadImages.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemsUploadImgesBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


    public interface UploadImageListener {
        public void onNewImage();

        public void onEditName(UploadImagesModel data);

        public void onDeleteImage(int position);
    }
}
