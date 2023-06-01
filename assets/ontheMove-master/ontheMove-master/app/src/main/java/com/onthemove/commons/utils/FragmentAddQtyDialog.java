package com.onthemove.commons.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.onthemove.R;
import com.onthemove.adapters.QuntityAdapter;
import com.onthemove.database.TaskListDatabaseManager;
import com.onthemove.modelClasses.PartModel;

import java.util.ArrayList;

public class FragmentAddQtyDialog extends DialogFragment{

    Context context;
    public static final String TAG = "FragmentAddQtyDialog";
    RecyclerView recyclerView;
    AppPref appPref;
    Button btnSubmit;
    TaskListDatabaseManager manager;
    String taskId;
    ArrayList<PartModel.TaskProductData> productData = new ArrayList<>();
    ArrayList<String> productDataUsedQun;
    private OnClick OnClick;


    public static FragmentAddQtyDialog newInstance()
    {
        return new FragmentAddQtyDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgment_add_qty, container, false);
        recyclerView = view.findViewById(R.id.rvAddQuntity);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        manager = new TaskListDatabaseManager(getActivity());
        appPref = AppPref.getInstance(context);
        inits();
    }
    public void addTaskID(String taskID,OnClick OnClick)
    {
        this.taskId = taskID;
        this.OnClick = OnClick;
    }
    private void inits() {

        productData = manager.getProductData(taskId);

        productDataUsedQun = new ArrayList<>(productData.size());

        Log.e(TAG,"size"+productData.size());

        QuntityAdapter quntityAdapter = new QuntityAdapter(productData,context);
        recyclerView.setAdapter(quntityAdapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid())
                {
                    OnClick.addList(productData);
                    Log.e(TAG,"List GET SuccessFully...");
                    appPref.set(AppPref.TASK_ID_PART_DATA,taskId);
                    dismiss();
                }

            }
        });

    }

    private Boolean isValid() {


        Boolean flag = true;

        for (int i=0;i<productData.size();i++)
        {
            Log.e(TAG,"QTY IS "+productData.get(i).getQty());

            if (productData.get(i).getUsed_qty() != null && !productData.get(i).getUsed_qty().isEmpty())
            {
                Log.e(TAG,"USED"+productData.get(i).getUsed_qty());
                Log.e(TAG,"QTY"+productData.get(i).getQty());

                if (Integer.parseInt(productData.get(i).getUsed_qty()) > Integer.parseInt(productData.get(i).getQty()))
                {
                    flag = false;
                    Toast.makeText(context,"Quantity is High :"+productData.get(i).getProduct_name(),Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else
            {
                flag = false;
                Toast.makeText(context,"enter Quantity Of :"+productData.get(i).getProduct_name(),Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (flag)
        {
            return true;
        }

       return false;
    }

    public interface OnClick
    {
        void addList(ArrayList<PartModel.TaskProductData> productData);
    }
}
