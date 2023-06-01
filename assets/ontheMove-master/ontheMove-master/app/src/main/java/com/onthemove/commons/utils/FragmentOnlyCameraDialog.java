package com.onthemove.commons.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.iceteck.silicompressorr.SiliCompressor;
import com.onthemove.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class FragmentOnlyCameraDialog extends DialogFragment implements View.OnClickListener{

    private static final int IMAGE_CAPTURE = 1;

    private static final String TAG = "SelectCameraDialog";

    LinearLayout llCamera;
    Context context;
    private File mediaFile;
    private Uri uri = null;
    FragmentOnlyCameraDialog.ImageSelectListener listener;

    public static FragmentOnlyCameraDialog newInstance()
    {
        return new FragmentOnlyCameraDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_dailog, container, false);
        llCamera = view.findViewById(R.id.llCamera);
        llCamera.setOnClickListener(this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (hasPermission(context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}))
                    cameraIntent();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      //  String imagePath;
        if (requestCode == IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
        //    imagePath = mediaFile.getAbsolutePath();

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


    private void onImageSelected(Uri imagePath) {
        AppLog.e(TAG,"ON IMAGE SELECTED");
        try {


          /*  String filePath = SiliCompressor.with(context).compress(imagePath.toString(), new File(context.getCacheDir(),getPath(imagePath)));

            File file = new File(filePath);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            } else {
                uri = Uri.fromFile(file);
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

            //  uri = Uri.parse(filePath);

            listener.onCameraSelected(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setListener(FragmentOnlyCameraDialog.ImageSelectListener listener) {
        this.listener = listener;
    }
    public interface ImageSelectListener {
        public void onCameraSelected(Uri path);
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
