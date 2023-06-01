package com.onthemove.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.modelClasses.SubmitTaskStatusModel;
import com.onthemove.responseClasses.AddFailedReasonModel;
import com.onthemove.responseClasses.AddRefuelingModel;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.responseClasses.UploadedImageModel;
import com.onthemove.responseClasses.VehicalListModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TaskListDatabaseManager extends SQLiteOpenHelper {

    private static final String TAG = "TaskListDatabaseManager";
    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "task_details";
    public static final String TASK_LIST_DETAIL = "task_list_detail";
    public static final String ID = "task_id";
    public static final String NAME = "name";
    public static final String COMPLEX_NAME = "complex_name";
    public static final String HOUSE_NUMBER = "house_number";
    public static final String ADDRESS = "address";
    public static final String PHONENO = "phoneno";
    public static final String DISTANCE = "distance";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String STATUS_UPDATE = "status_update";
    public static final String SIGNATURE_IMG = "signature_img";
    public static final String COMMENT = "comment";
    public static final String TICKET_NUMBER = "TICKET_NUMBER";
    public static final String LAT = "lat";
    public static final String LANG = "lang";

    public static final String TASK_STATUS_TABLE = "taskStatus_table";
    public static final String STATUS_ID = "status_id";
    public static final String STATUS_DATE = "status_date";
    public static final String STATUS = "status";
    public static final String STATUS_SYNC = "status_sync";

    public static final String TASK_STATUS_FAIL = "taskStatusFail_table";
    public static final String FAIL_ID = "Failstatus_id";
    public static final String REASON = "reason";
    public static final String FAIL_IMAGE = "fail_image";
    public static final String FAIL_IMAGE_NAME = "fail_image_name";
    public static final String FAIL_SYNC = "fail_sync";

    public static final String TASK_IMAGE_TABLE = "task_image_table";
    public static final String IMAGE_ID = "image_id";
    public static final String TASK_IMAGE = "task_images";
    public static final String IMAGE_TYPE = "image_type";
    public static final String IMAGE_SYNC = "image_sync";

    public static final String REFUELING_TABLE = "refueling_table";
    public static final String REFUELING_ID = "refueling_id";
    public static final String VEHICLE_ID = "vehicle_id";
    public static final String METER_READING = "meter_reading";
    public static final String RECEIPT_NUMBER = "receipt_number";
    public static final String RECEIPT_IMAGE = "receipt_image";
    public static final String PRICE = "price";
    public static final String LITER = "liter";
    public static final String REFUELING_SYNC = "refueling_sync";

    public static final String VEHICLE_TABLE = "vehicle_table";
    public static final String FREE_VEHICLE_ID = "vehicle_id";
    public static final String FREE_VEHICLE_MAKE = "vehicle_make";
    public static final String FREE_VEHICLE_MODEL = "vehicle_model";
    public static final String FREE_VEHICLE_REG_NUMBER = "vehicle_reg_number";
    public static final String FREE_VEHICLE_KM = "vehicle_km";
    public static final String FREE_VEHICLE_SERVICE = "vehicle_next_service";
    public static final String FREE_VEHICLE_LICEXP = "vehicle_lic_exp";



    public static final String PART_TABLE = "part_table";
    public static final String PART_TASK_ID = "part_task_id";
    public static final String PART_PRODUCT_ID = "part_product_id";
    public static final String PART_PRODUCT_NAME = "part_product_name";
    public static final String PART_UPC_CODE = "part_upc_code";
    public static final String PART_QTY = "part_qty";
    public static final String PART_QTY_USED = "part_qty_used";


    public TaskListDatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = " CREATE TABLE IF NOT EXISTS " + TASK_LIST_DETAIL +
                "("
                + ID + " TEXT,"
                + NAME + " TEXT,"
                + HOUSE_NUMBER + " TEXT,"
                + COMPLEX_NAME + " TEXT,"
                + ADDRESS + " TEXT,"
                + PHONENO + " TEXT,"
                + DISTANCE + " TEXT,"
                + DESCRIPTION + " TEXT,"
                + DATE + " TEXT,"
                + LAT + " TEXT,"
                + LANG + " TEXT,"
                + STATUS_UPDATE + " TEXT,"
                + SIGNATURE_IMG + " TEXT,"
                + COMMENT + " TEXT,"
                + TICKET_NUMBER + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(query);

        String status_query = " CREATE TABLE " + TASK_STATUS_TABLE +
                "("
                + STATUS_ID + " TEXT,"
                + STATUS_DATE + " TEXT,"
                + STATUS + " TEXT,"
                + STATUS_SYNC + " TEXT"
                + ")";
        sqLiteDatabase.execSQL(status_query);

        String image_query = " CREATE TABLE " + TASK_IMAGE_TABLE +
                "("
                + IMAGE_ID + " TEXT,"
                + IMAGE_TYPE + " TEXT,"
                + TASK_IMAGE + " TEXT,"
                + IMAGE_SYNC + " TEXT "
                + ")";
        sqLiteDatabase.execSQL(image_query);


        String statusFail_query = " CREATE TABLE " + TASK_STATUS_FAIL +
                "("
                + FAIL_ID + " TEXT,"
                + REASON + " TEXT,"
                + FAIL_IMAGE + " TEXT,"
                + FAIL_IMAGE_NAME + " TEXT,"
                + FAIL_SYNC + " TEXT"
                + ")";

        sqLiteDatabase.execSQL(statusFail_query);


        String refueling_query = " CREATE TABLE " + REFUELING_TABLE +
                "("
                + REFUELING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VEHICLE_ID + " TEXT,"
                + METER_READING + " TEXT,"
                + RECEIPT_NUMBER + " TEXT,"
                + RECEIPT_IMAGE + " TEXT,"
                + PRICE + " TEXT,"
                + LITER + " TEXT,"
                + REFUELING_SYNC + " TEXT "
                + ")";
        sqLiteDatabase.execSQL(refueling_query);

        String vehicle_query = " CREATE TABLE " + VEHICLE_TABLE +
                "("
                + FREE_VEHICLE_ID + " TEXT,"
                + FREE_VEHICLE_MAKE + " TEXT,"
                + FREE_VEHICLE_MODEL + " TEXT,"
                + FREE_VEHICLE_REG_NUMBER + " TEXT,"
                + FREE_VEHICLE_KM + " TEXT,"
                + FREE_VEHICLE_SERVICE + " TEXT,"
                + FREE_VEHICLE_LICEXP + " TEXT"
                + ")";

        sqLiteDatabase.execSQL(vehicle_query);

        String part_query = "CREATE TABLE " + PART_TABLE +
                "("
                + PART_TASK_ID + " TEXT,"
                + PART_PRODUCT_ID + " TEXT,"
                + PART_PRODUCT_NAME + " TEXT,"
                + PART_UPC_CODE + " TEXT,"
                + PART_QTY + " TEXT,"
                + PART_QTY_USED + " TEXT"
                + ")";

        sqLiteDatabase.execSQL(part_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

        onCreate(sqLiteDatabase);
    }

    public void addTask(String id, String name, String houseno,String complexName, String address, String phoneno, String distance, String des, String date,String lat,String lang, String statusupdate, String signatureImg, String comment, String ticketNo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(NAME, name);
        values.put(HOUSE_NUMBER, houseno);
        values.put(COMPLEX_NAME, complexName);
        values.put(ADDRESS, address);
        values.put(PHONENO, phoneno);
        values.put(DISTANCE, distance);
        values.put(DESCRIPTION, des);
        values.put(DATE, date);
        values.put(LAT,lat);
        values.put(LANG,lang);
        values.put(STATUS_UPDATE, statusupdate);
        values.put(SIGNATURE_IMG, signatureImg);
        values.put(COMMENT, comment);
        values.put(TICKET_NUMBER, ticketNo);
        db.insert(TASK_LIST_DETAIL, null, values);
        db.close();
    }

    public void addVehicle(int id,String make,String model,String reg_number,String km,String service,String lic_exp)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FREE_VEHICLE_ID,id);
        values.put(FREE_VEHICLE_MAKE,make);
        values.put(FREE_VEHICLE_MODEL,model);
        values.put(FREE_VEHICLE_REG_NUMBER,reg_number);
        values.put(FREE_VEHICLE_KM,km);
        values.put(FREE_VEHICLE_SERVICE,service);
        values.put(FREE_VEHICLE_LICEXP,lic_exp);

        db.insert(VEHICLE_TABLE,null,values);
        db.close();
    }


    public void addPart(String task_id,String part_id,String part_name,String part_upc,String qty)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PART_TASK_ID,task_id);
        values.put(PART_PRODUCT_ID,part_id);
        values.put(PART_PRODUCT_NAME,part_name);
        values.put(PART_UPC_CODE,part_upc);
        values.put(PART_QTY,qty);

        db.insert(PART_TABLE,null,values);
        db.close();
    }

    public boolean checkPartRecord(String id) {
        boolean flag;
        String quString = "SELECT count(*) FROM " + PART_TABLE + " WHERE " + PART_TASK_ID + "=" + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(quString, null);

        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        if (icount > 0) {
            flag = true;
        } else {
            flag = false;
        }
        AppLog.e(TAG, String.valueOf(flag));
        return flag;
    }

    public void updateTask(String id, String name, String address, String phoneno, String distance, String des, String date, String statusupdate, String signatureImg, String comment, String ticketNo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(NAME, name);
        values.put(ADDRESS, address);
        values.put(PHONENO, phoneno);
        values.put(DISTANCE, distance);
        values.put(DESCRIPTION, des);
        values.put(DATE, date);
        values.put(STATUS_UPDATE, statusupdate);
        values.put(SIGNATURE_IMG, signatureImg);
        values.put(COMMENT, comment);
        values.put(TICKET_NUMBER, ticketNo);

        db.update(TASK_LIST_DETAIL, values, ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updatePart(String task_id,String part_id,String part_name,String part_upc,String qty,String used_qty) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PART_TASK_ID,task_id);
        values.put(PART_PRODUCT_ID,part_id);
        values.put(PART_PRODUCT_NAME,part_name);
        values.put(PART_UPC_CODE,part_upc);
        values.put(PART_QTY,qty);
        values.put(PART_QTY_USED,used_qty);

        db.update(PART_TABLE, values, PART_TASK_ID + " = ?", new String[]{task_id});
        db.close();
    }

    public boolean checkVehicleRecord(int id) {

        boolean flag;
        String quString = "SELECT count(*) FROM " + VEHICLE_TABLE + " WHERE " + VEHICLE_ID + "=" + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(quString, null);

        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        if (icount > 0) {
            flag = true;
        } else {
            flag = false;
        }
        AppLog.e(TAG, String.valueOf(flag));
        return flag;
    }

    public int getNewTaskListCount() {
        int count = 0;

        String Query = "SELECT * FROM task_list_detail WHERE status_update IN ('assigned')";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

        count = cursor.getCount();
        AppLog.e(TAG, "getNewTaskListCount: " + count);
        return count;
    }

    public int getMyTaskListCount() {

        int count = 0;

        String Query = "SELECT * FROM task_list_detail WHERE status_update IN ('start_drive','arrived','accepted')";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

        count = cursor.getCount();
        AppLog.e(TAG, "getMyTaskListCount: " + count);
        return count;
    }
    public int getMyTaskListCountDetails() {

        int count = 0;

        String Query = "SELECT * FROM task_list_detail WHERE status_update IN ('start_drive','arrived','accepted','failed')";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

        count = cursor.getCount();
        AppLog.e(TAG, "getMyTaskListCount: " + count);
        return count;
    }

    public int getVehicleListCount() {

        int count = 0;

        String Query = "SELECT * FROM vehicle_table";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

        count = cursor.getCount();
        AppLog.e(TAG, "getVehicleListCount: " + count);
        return count;
    }

    public int getMyTaskCompleteListCount() {

        int count = 0;

        String Query = "SELECT * FROM task_list_detail WHERE status_update IN ('completed')";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

        count = cursor.getCount();
        AppLog.e(TAG, "getMyTaskListCount: " + count);
        return count;
    }

    public ArrayList<NewTaskModel.NewTaskData> getMyTaskDetailsData() {

        ArrayList<NewTaskModel.NewTaskData> list = new ArrayList<>();

        String Query = "SELECT * FROM task_list_detail WHERE status_update IN ('start_drive','arrived','accepted','failed') ORDER BY date";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        AppLog.e(TAG, String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {

            do {
                NewTaskModel.NewTaskData submitTaskModel = new NewTaskModel.NewTaskData();
                submitTaskModel.setTaskId(cursor.getString(cursor.getColumnIndex(ID)));
                submitTaskModel.setCustomerName(cursor.getString(cursor.getColumnIndex(NAME)));
                submitTaskModel.setPickupAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                submitTaskModel.setMobileNumber(cursor.getString(cursor.getColumnIndex(PHONENO)));
                submitTaskModel.setPickupDistance(cursor.getString(cursor.getColumnIndex(DISTANCE)));
                submitTaskModel.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                submitTaskModel.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE)));
                submitTaskModel.setLat(cursor.getString(cursor.getColumnIndex(LAT)));
                submitTaskModel.setLng(cursor.getString(cursor.getColumnIndex(LANG)));
                submitTaskModel.setTaskStatus(cursor.getString(cursor.getColumnIndex(STATUS_UPDATE)));
                submitTaskModel.setSignatureImg(cursor.getString(cursor.getColumnIndex(SIGNATURE_IMG)));
                submitTaskModel.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                submitTaskModel.setTicketNumber(cursor.getString(cursor.getColumnIndex(TICKET_NUMBER)));
                list.add(submitTaskModel);
            }
            while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<NewTaskModel.NewTaskData> getSubmitData(LatLng from) {

        LatLng to;
        ArrayList<NewTaskModel.NewTaskData> list = new ArrayList<>();

        String Query = "SELECT * FROM task_list_detail WHERE status_update IN ('start_drive','arrived','accepted')";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        AppLog.e(TAG, String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {

            do {
                NewTaskModel.NewTaskData submitTaskModel = new NewTaskModel.NewTaskData();
                submitTaskModel.setTaskId(cursor.getString(cursor.getColumnIndex(ID)));
                submitTaskModel.setCustomerName(cursor.getString(cursor.getColumnIndex(NAME)));
                submitTaskModel.setPickupAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                submitTaskModel.setMobileNumber(cursor.getString(cursor.getColumnIndex(PHONENO)));
                submitTaskModel.setPickupDistance(cursor.getString(cursor.getColumnIndex(DISTANCE)));

                Double lat = Double.valueOf(String.valueOf(cursor.getString(cursor.getColumnIndex(LAT))));
                Double lng = Double.valueOf(String.valueOf(cursor.getString(cursor.getColumnIndex(LANG))));

                to = new LatLng(lat,lng);

                submitTaskModel.setCalculate_distance(String.valueOf(distance(from,to)));

                submitTaskModel.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                submitTaskModel.setHouseNumber(cursor.getString(cursor.getColumnIndex(HOUSE_NUMBER)));
                submitTaskModel.setComplexName(cursor.getString(cursor.getColumnIndex(COMPLEX_NAME)));
                submitTaskModel.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE)));
                submitTaskModel.setLat(cursor.getString(cursor.getColumnIndex(LAT)));
                submitTaskModel.setLng(cursor.getString(cursor.getColumnIndex(LANG)));
                submitTaskModel.setTaskStatus(cursor.getString(cursor.getColumnIndex(STATUS_UPDATE)));
                submitTaskModel.setSignatureImg(cursor.getString(cursor.getColumnIndex(SIGNATURE_IMG)));
                submitTaskModel.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                submitTaskModel.setTicketNumber(cursor.getString(cursor.getColumnIndex(TICKET_NUMBER)));
                list.add(submitTaskModel);
            }
            while (cursor.moveToNext());
        }


        Collections.sort(list, new Comparator<NewTaskModel.NewTaskData>() {
            @Override
            public int compare(NewTaskModel.NewTaskData o1, NewTaskModel.NewTaskData o2) {
                return o1.getCalculate_distance().compareTo(o2.getCalculate_distance());
            }
        });
        return list;
    }

    public ArrayList<NewTaskModel.NewTaskData> getSubmitCompleteData() {

        ArrayList<NewTaskModel.NewTaskData> list = new ArrayList<>();

        String Query = "SELECT * FROM task_list_detail WHERE status_update IN ('completed') ORDER BY date";
        Log.e(TAG,"CData"+Query);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        AppLog.e(TAG, String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {

            do {
                NewTaskModel.NewTaskData submitTaskModel = new NewTaskModel.NewTaskData();
                submitTaskModel.setTaskId(cursor.getString(cursor.getColumnIndex(ID)));
                submitTaskModel.setCustomerName(cursor.getString(cursor.getColumnIndex(NAME)));
                submitTaskModel.setPickupAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                submitTaskModel.setMobileNumber(cursor.getString(cursor.getColumnIndex(PHONENO)));
                submitTaskModel.setPickupDistance(cursor.getString(cursor.getColumnIndex(DISTANCE)));
                submitTaskModel.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                submitTaskModel.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE)));
                submitTaskModel.setLat(cursor.getString(cursor.getColumnIndex(LAT)));
                submitTaskModel.setLng(cursor.getString(cursor.getColumnIndex(LANG)));
                submitTaskModel.setTaskStatus(cursor.getString(cursor.getColumnIndex(STATUS_UPDATE)));
                submitTaskModel.setSignatureImg(cursor.getString(cursor.getColumnIndex(SIGNATURE_IMG)));
                submitTaskModel.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                submitTaskModel.setTicketNumber(cursor.getString(cursor.getColumnIndex(TICKET_NUMBER)));
                list.add(submitTaskModel);
            }
            while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<NewTaskModel.NewTaskData> getNewTaskData() {

        ArrayList<NewTaskModel.NewTaskData> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM task_list_detail WHERE status_update = 'assigned'";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        AppLog.e(TAG,"getNEWTASK"+String.valueOf(cursor.getCount()));

        if (cursor.moveToFirst()) {

            do {
                NewTaskModel.NewTaskData submitTaskModel = new NewTaskModel.NewTaskData();
                submitTaskModel.setTaskId(cursor.getString(cursor.getColumnIndex(ID)));
                submitTaskModel.setCustomerName(cursor.getString(cursor.getColumnIndex(NAME)));
                submitTaskModel.setHouseNumber(cursor.getString(cursor.getColumnIndex(HOUSE_NUMBER)));
                submitTaskModel.setComplexName(cursor.getString(cursor.getColumnIndex(COMPLEX_NAME)));
                submitTaskModel.setLat(cursor.getString(cursor.getColumnIndex(LAT)));
                submitTaskModel.setLng(cursor.getString(cursor.getColumnIndex(LANG)));
                submitTaskModel.setPickupAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                submitTaskModel.setMobileNumber(cursor.getString(cursor.getColumnIndex(PHONENO)));
                submitTaskModel.setPickupDistance(cursor.getString(cursor.getColumnIndex(DISTANCE)));
                submitTaskModel.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                submitTaskModel.setCreatedAt(cursor.getString(cursor.getColumnIndex(DATE)));
                submitTaskModel.setTaskStatus(cursor.getString(cursor.getColumnIndex(STATUS_UPDATE)));
                submitTaskModel.setSignatureImg(cursor.getString(cursor.getColumnIndex(SIGNATURE_IMG)));
                submitTaskModel.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                submitTaskModel.setTicketNumber(cursor.getString(cursor.getColumnIndex(TICKET_NUMBER)));
                list.add(submitTaskModel);
            }
            while (cursor.moveToNext());
        }

        return list;
    }


    public ArrayList<VehicalListModel.VehicalListData> getVehicleListData() {

        ArrayList<VehicalListModel.VehicalListData> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM vehicle_table";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        AppLog.e(TAG,"getVehicle"+String.valueOf(cursor.getCount()));

        if (cursor.moveToFirst()) {

            do {
                VehicalListModel.VehicalListData vehicalListData = new VehicalListModel.VehicalListData();
                vehicalListData.setI(cursor.getInt(cursor.getColumnIndex(FREE_VEHICLE_ID)));
                vehicalListData.setMake(cursor.getString(cursor.getColumnIndex(FREE_VEHICLE_MAKE)));
                vehicalListData.setModel(cursor.getString(cursor.getColumnIndex(FREE_VEHICLE_MODEL)));
                vehicalListData.setReg_number(cursor.getString(cursor.getColumnIndex(FREE_VEHICLE_REG_NUMBER)));
                vehicalListData.setKm(cursor.getString(cursor.getColumnIndex(FREE_VEHICLE_KM)));
                vehicalListData.setNxt_service(cursor.getString(cursor.getColumnIndex(FREE_VEHICLE_SERVICE)));
                vehicalListData.setLice_exp(cursor.getString(cursor.getColumnIndex(FREE_VEHICLE_LICEXP)));
                list.add(vehicalListData);
            }
            while (cursor.moveToNext());
        }

        return list;
    }

    public void addTaskStatus(String id, String date, String status, String sync) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STATUS_ID, id);
        values.put(STATUS_DATE, date);
        values.put(STATUS, status);
        values.put(STATUS_SYNC, sync);

        AppLog.e(TAG, "addTaskStatus: ");
        db.insert(TASK_STATUS_TABLE, null, values);
        db.close();
    }


    public void addFailTaskStatus(String id,String reason,String image,String imagename,Boolean sync) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FAIL_ID, id);
        values.put(REASON, reason);
        values.put(FAIL_IMAGE,image);
        values.put(FAIL_IMAGE_NAME,imagename);
        values.put(FAIL_SYNC,sync);
        AppLog.e(TAG, "addTaskFailStatus: ");
        db.insert(TASK_STATUS_FAIL, null, values);
        db.close();
    }

    public ArrayList<SubmitTaskStatusModel> getTaskStatus() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM taskStatus_table WHERE status_sync = 'false'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<SubmitTaskStatusModel> ModalArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                SubmitTaskStatusModel submitTaskStatusModel = new SubmitTaskStatusModel();
                submitTaskStatusModel.setId(cursor.getString(cursor.getColumnIndex(STATUS_ID)));
                submitTaskStatusModel.setDate(cursor.getString(cursor.getColumnIndex(STATUS_DATE)));
                submitTaskStatusModel.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                submitTaskStatusModel.setSync(cursor.getString(cursor.getColumnIndex(STATUS_SYNC)));
                ModalArrayList.add(submitTaskStatusModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ModalArrayList;
    }

    public ArrayList<UploadedImageModel> getUploadedImages() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM task_image_table where image_sync = 'false'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<UploadedImageModel> ModalArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                UploadedImageModel uploadedImageModel = new UploadedImageModel();
                uploadedImageModel.setId(cursor.getString(cursor.getColumnIndex(IMAGE_ID)));
                uploadedImageModel.setImage(cursor.getString(cursor.getColumnIndex(TASK_IMAGE)));
                uploadedImageModel.setImage_type(cursor.getString(cursor.getColumnIndex(IMAGE_TYPE)));
                uploadedImageModel.setSync(cursor.getString(cursor.getColumnIndex(IMAGE_SYNC)));
                ModalArrayList.add(uploadedImageModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ModalArrayList;
    }

    public ArrayList<AddFailedReasonModel.AddFailedReq> getUploadedIFailReason() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM taskStatusFail_table where fail_sync = 'false'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<AddFailedReasonModel.AddFailedReq> ModalArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                AddFailedReasonModel.AddFailedReq addFailedReq = new AddFailedReasonModel.AddFailedReq();
                addFailedReq.setFail_task_id(cursor.getString(cursor.getColumnIndex(FAIL_ID)));
                addFailedReq.setFail_reason(cursor.getString(cursor.getColumnIndex(REASON)));
                addFailedReq.setFail_image(Uri.parse(cursor.getString(cursor.getColumnIndex(FAIL_IMAGE))));
                addFailedReq.setFail_image_name(cursor.getString(cursor.getColumnIndex(FAIL_IMAGE_NAME)));
                addFailedReq.setFail_sync(cursor.getString(cursor.getColumnIndex(FAIL_SYNC)));
                ModalArrayList.add(addFailedReq);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ModalArrayList;
    }

    public ArrayList<UploadedImageModel> getUploadedImagesId(String id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM task_image_table where image_id = " + id + " AND image_sync = 'false'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<UploadedImageModel> ModalArrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                UploadedImageModel uploadedImageModel = new UploadedImageModel();
                uploadedImageModel.setId(cursor.getString(cursor.getColumnIndex(IMAGE_ID)));
                uploadedImageModel.setImage(cursor.getString(cursor.getColumnIndex(TASK_IMAGE)));
                uploadedImageModel.setImage_type(cursor.getString(cursor.getColumnIndex(IMAGE_TYPE)));
                uploadedImageModel.setSync(cursor.getString(cursor.getColumnIndex(IMAGE_SYNC)));
                ModalArrayList.add(uploadedImageModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ModalArrayList;
    }

    public String getSignatureImage(String id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT signature_img FROM task_list_detail where task_id = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        String image = "";

        if (cursor.moveToFirst()) {
            image = cursor.getString(cursor.getColumnIndex(SIGNATURE_IMG));
        }
        cursor.close();
        return image;
    }

    public String getComment(String id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT comment FROM task_list_detail where task_id = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        String comment = "";

        if (cursor.moveToFirst()) {
            comment = cursor.getString(cursor.getColumnIndex(COMMENT));
        }
        cursor.close();
        return comment;
    }

    public ArrayList<String> getId() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct(image_id) from task_image_table where image_sync = 'false'", null);

        ArrayList<String> idList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String imgId = cursor.getString(cursor.getColumnIndex(IMAGE_ID));
                idList.add(imgId);
            } while (cursor.moveToNext());
        }

        return idList;
    }

    public void uploadTaskImages(String id, String imageType, String userimage, String sync) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE_ID, id);
        values.put(IMAGE_TYPE, imageType);
        values.put(TASK_IMAGE, userimage);
        values.put(IMAGE_SYNC, sync);
        db.insert(TASK_IMAGE_TABLE, null, values);
        db.close();
    }

    public void updateTaskStatus() {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put(STATUS_SYNC, "true");

        // updating row
        db.update(TASK_STATUS_TABLE, newValues, STATUS_SYNC + " = ?", new String[]{String.valueOf("false")});
        db.close();
    }

    public void updateSyncTaskImage(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put(IMAGE_SYNC, "true");

        db.update(TASK_IMAGE_TABLE, newValues, IMAGE_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean checkRecord(String id) {
        boolean flag;
        String quString = "SELECT count(*) FROM " + TASK_LIST_DETAIL + " WHERE " + ID + "=" + id;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(quString, null);

        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        if (icount > 0) {
            flag = true;
        } else {
            flag = false;
        }
        AppLog.e(TAG, String.valueOf(flag));
        return flag;
    }

    public void addRefueling(AddRefuelingModel.AddRefuelingReq req) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VEHICLE_ID, req.getFk_vehicle_id());
        values.put(METER_READING, req.getMeter_reading());
        values.put(RECEIPT_NUMBER, req.getReceipt_number());
        values.put(RECEIPT_IMAGE, req.getReceipt_image().toString());
        values.put(PRICE, req.getPrice());
        values.put(LITER, req.getLiter());
        values.put(REFUELING_SYNC, "false");

        db.insert(REFUELING_TABLE, null, values);
        db.close();
    }

    public ArrayList<AddRefuelingModel.AddRefuelingReq> getRefuelingList() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM refueling_table where refueling_sync = 'false'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<AddRefuelingModel.AddRefuelingReq> addRefuelingReq = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                AddRefuelingModel.AddRefuelingReq req = new AddRefuelingModel.AddRefuelingReq();
                req.setId(String.valueOf(Integer.parseInt(cursor.getString(0))));
                req.setFk_vehicle_id(cursor.getString(cursor.getColumnIndex(VEHICLE_ID)));
                req.setMeter_reading(cursor.getString(cursor.getColumnIndex(METER_READING)));
                req.setReceipt_number(cursor.getString(cursor.getColumnIndex(RECEIPT_NUMBER)));
                req.setReceipt_image(Uri.parse(cursor.getString(cursor.getColumnIndex(RECEIPT_IMAGE))));
                req.setPrice(cursor.getString(cursor.getColumnIndex(PRICE)));
                req.setLiter(cursor.getString(cursor.getColumnIndex(LITER)));
                req.setSync(cursor.getString(cursor.getColumnIndex(REFUELING_SYNC)));
                addRefuelingReq.add(req);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return addRefuelingReq;
    }


    public ArrayList<PartModel.TaskProductData> getProductData(String id) {

        Log.e(TAG,"task id"+id);

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM part_table where part_task_id = "+id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<PartModel.TaskProductData> productData = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                PartModel.TaskProductData req = new PartModel.TaskProductData();
                req.setProduct_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PART_PRODUCT_ID))));
                req.setProduct_name(cursor.getString(cursor.getColumnIndex(PART_PRODUCT_NAME)));
                req.setUpc_code(cursor.getString(cursor.getColumnIndex(PART_UPC_CODE)));
                req.setQty(cursor.getString(cursor.getColumnIndex(PART_QTY)));
                productData.add(req);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return productData;
    }
    public ArrayList<PartModel.TaskProductDataUsed> getUsedProductData(String id) {

        Log.e(TAG,"task id"+id);

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM part_table where part_task_id = "+id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<PartModel.TaskProductDataUsed> productData = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                PartModel.TaskProductDataUsed req = new PartModel.TaskProductDataUsed();
                req.setProduct_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PART_PRODUCT_ID))));
                req.setProduct_name(cursor.getString(cursor.getColumnIndex(PART_PRODUCT_NAME)));
                req.setUpc_code(cursor.getString(cursor.getColumnIndex(PART_UPC_CODE)));
                req.setQty(cursor.getString(cursor.getColumnIndex(PART_QTY_USED)));
                productData.add(req);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return productData;
    }
    public void updateRefuelingSync(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        newValues.put(REFUELING_SYNC, "true");

        db.update(REFUELING_TABLE, newValues, REFUELING_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public static double distance(LatLng start, LatLng end){
        try {
            Location location1 = new Location("locationA");
            location1.setLatitude(start.latitude);
            location1.setLongitude(start.longitude);

            Location location2 = new Location("locationB");
            location2.setLatitude(end.latitude);
            location2.setLongitude(end.longitude);

            double distance = location1.distanceTo(location2);
            Log.e(TAG,"CALCULATE Distance"+distance);
            return distance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
