package com.onthemove.commons.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.github.dhaval2404.imagepicker.util.ImageUtil;
import com.iceteck.silicompressorr.SiliCompressor;
import com.onthemove.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;



public class FragmentSelectImageDialog extends DialogFragment implements View.OnClickListener {

    private static final int IMAGE_CAPTURE = 1;
    private static final int IMAGE_GALLERY = 2;
    private static final String TAG = "SelectImageDialog";
    private static final int IMAGE_VIDEO = 3;

    LinearLayout llGallery, llCamera, llVideo;
    Context context;
    private File mediaFile;
    private Uri uri;
    ImageSelectListener listener;
    private boolean isVideoEnable;

    public static FragmentSelectImageDialog newInstance() {
        return new FragmentSelectImageDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_dialog, container, false);
        llCamera = view.findViewById(R.id.llGallery);
        llCamera.setOnClickListener(this);
        llGallery = view.findViewById(R.id.llCamera);
        llGallery.setOnClickListener(this);
        llVideo = view.findViewById(R.id.llVideo);
        llVideo.setOnClickListener(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isVideoEnable) {
            llVideo.setVisibility(View.VISIBLE);
        } else {
            llVideo.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCamera:
                cameraIntent();
                break;
            case R.id.llGallery:
                galleryIntent();
                break;
            case R.id.llVideo:
                videoIntent();
                break;
        }
    }

    public boolean hasPermission(Context context, String[] permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }

    private void cameraIntent() {
        if (hasPermission(context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mediaFile = getOutputMediaFile();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", mediaFile);
            } else {
                uri = Uri.fromFile(mediaFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, IMAGE_CAPTURE);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void videoIntent() {
        if (hasPermission(context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("video/*");
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Video"), IMAGE_VIDEO);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }

    }

    private void galleryIntent() {

        if (hasPermission(context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
           // galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), IMAGE_GALLERY);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (hasPermission(context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}))
                    cameraIntent();
                break;
            case 2:
                if (hasPermission(context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}))
                    galleryIntent();
                break;
            case 3:
                if (hasPermission(context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}))
                    videoIntent();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

     //   String imagePath;
        if (requestCode == IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
         //   imagePath = mediaFile.getAbsolutePath();

            if (uri != null) {

                if (!TextUtils.isEmpty(uri.toString())) {
                    if (listener != null)
                        onImageSelected(uri);
                } else {
                    Toast.makeText(context, "Can't get image", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        }
        if (requestCode == IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            ClipData clipData = data.getClipData();

            if (clipData == null) {
                Log.e(TAG, "uri from gallery -> " + uri);

               // imagePath = RealPathUtil.getRealPath(context, uri);
                Log.e(TAG, "imagePath -> " + uri);
                if (!TextUtils.isEmpty(uri.toString())) {

                    if (listener != null)
                        onImageSelected(uri);
                } else {
                    Toast.makeText(context, "Can't get image", Toast.LENGTH_SHORT).show();
                }
            } else {
                ArrayList<Uri> multipleImages = new ArrayList<>();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    final Uri uri1 = item.getUri();

                    multipleImages.add(uri1);
                }
                if (listener != null)
                    onImageSelected(multipleImages);
            }
        }

        if (requestCode == IMAGE_VIDEO && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            String videoPath = RealPathUtil.getRealPath(context, selectedImageUri);
            Log.e(TAG, "videoPath -> " + videoPath);
            if (!TextUtils.isEmpty(videoPath)) {
                if (listener != null)
                    listener.onVideoSelected(videoPath);
            } else {
                Toast.makeText(context, "Can't get Video", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public String convertMediaUriToPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    public String getPath(Uri uri)
    {
        String[] projection = {OpenableColumns.DISPLAY_NAME };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =  cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private static File getFile(){
        File mainFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "onTheMove");

        if (!mainFolder.exists()) {
            mainFolder.mkdir();
        }
        return mainFolder;
    }
    private void onImageSelected(Uri imagePath) {
        AppLog.e(TAG,"ON IMAGE SELECTED "+imagePath);
        Uri uri;
        try {
            String filePath = null;

           /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                 filePath = SiliCompressor.with(context).compress(imagePath.toString(), new File(context.getCacheDir(),getPath(imagePath)));
            }else
            {

            }*/

            Bitmap bitmap = null;
            try {
                InputStream is =  context.getContentResolver().openInputStream(imagePath);
                bitmap = ImageUtils.scaleDown(BitmapFactory.decodeStream(is), 1200, false);
                imagePath = ImageUtils.resizeImage(context, bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                AppLog.e(TAG , "Error Occurred : "+e.getLocalizedMessage());
            }

         /*   filePath = SiliCompressor.with(context).compress(imagePath.toString(), context.getCacheDir());

            File file = new File(filePath);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            } else {
                uri = Uri.fromFile(file);
            }*/

           // uri = Uri.parse(uri);


            listener.onImageSelected(imagePath);
        } catch (Exception e) {
            Log.e(TAG,"Exception"+e);
            e.printStackTrace();
        }

    }

    private void onImageSelected(ArrayList<Uri> multipleImages) {

        Uri uri;
        for (int i = 0; i < multipleImages.size(); i++) {
            try {
                AppLog.e(TAG,"ON IMAGE SELECTED LIST"+multipleImages.get(i));

                String filePath = null;


                Bitmap bitmap = null;
                try {
                    InputStream is =  context.getContentResolver().openInputStream(multipleImages.get(i));
                    bitmap = ImageUtils.scaleDown(BitmapFactory.decodeStream(is), 1200, false);
                    multipleImages.set(i,ImageUtils.resizeImage(context, bitmap));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    AppLog.e(TAG , "Error Occurred : "+e.getLocalizedMessage());
                }

                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    filePath = SiliCompressor.with(context).compress(multipleImages.get(i).toString(), new File(context.getCacheDir(),getPath(multipleImages.get(i))));
                }else
                {

                }*//*

                filePath = SiliCompressor.with(context).compress(multipleImages.get(i).toString(), new File(context.getCacheDir(),getPath(multipleImages.get(i))));

                File file = new File(filePath);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                } else {
                    uri = Uri.fromFile(file);
                }

             //   uri = Uri.parse(filePath);

                multipleImages.set(i, uri);*/

                listener.onImageSelected(multipleImages);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setListener(ImageSelectListener listener) {
        this.listener = listener;
    }

    public void setIsVideoEnabled(boolean isEnable) {
        isVideoEnable = isEnable;
    }

    public interface ImageSelectListener {
        public void onImageSelected(Uri path);

        public void onVideoSelected(String path);

        public void onImageSelected(ArrayList<Uri> listImages);
    }

    public File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = context.getCacheDir();

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        return new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + "_" + new Random().nextInt() + ".jpg");


    }
}
