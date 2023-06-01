package com.onthemove.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.onthemove.R;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.databinding.ActivitySigntureBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class SigntureActivity extends AppCompatActivity {
    public static final String TAG = "SigntureActivity";
    ActivitySigntureBinding binding;
    private String taskId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signture);

        if (getIntent() != null) {
            if (getIntent().getStringExtra("taskId") != null) {
                taskId = getIntent().getStringExtra("taskId");
            }
        }

        binding.tvTop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.signatureView.clearCanvas();
            }
        });
        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bitmap = binding.signatureView.getSignatureBitmap();

                if (binding.signatureView.isBitmapEmpty()) {
                    Toast.makeText(getApplicationContext(), "empty", Toast.LENGTH_SHORT).show();
                } else {

                    String path = saveImage(bitmap);

                    AppLog.e(TAG, "path: " + path);

                    Intent data = new Intent();
                    data.putExtra("image", path);
                    data.putExtra("taskId", taskId);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }

    private String saveImage(Bitmap bitmap) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        File mediaStorageDir = new File(getCacheDir(), "signature_" + taskId + "_IMG_" + timeStamp + "_" + new Random().nextInt()+".jpg");

        // Create a media file name


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mediaStorageDir);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return directory.getAbsolutePath();
        return String.valueOf(mediaStorageDir);
    }

}