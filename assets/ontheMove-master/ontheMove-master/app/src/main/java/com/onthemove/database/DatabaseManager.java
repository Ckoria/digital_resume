package com.onthemove.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.onthemove.commons.utils.AppLog;
import com.onthemove.modelClasses.SubmitOrderModel;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseManager";

    public static final String DATABASE_NAME = "on_the_move";

    public static final String SUBMIT_ORDER_TABLE = "submit_order_table";
    public static final String ID = "id";
    public static final String TICKET_NUMBER = "ticket_number";
    public static final String COMMENT = "comment";
    public static final String NAME_OF_IMAGE = "name_of_image";
    public static final String IMAGES = "images";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String ADDRESS = "address";
    public static final String SYNC = "sync";
    public static final String DATE_TIME = "date_time";

    public DatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(" CREATE TABLE " + SUBMIT_ORDER_TABLE +
                "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TICKET_NUMBER + " TEXT,"
                + COMMENT + " TEXT,"
                + NAME_OF_IMAGE + " TEXT,"
                + IMAGES + " TEXT,"
                + LAT + " TEXT,"
                + LNG + " TEXT,"
                + ADDRESS + " TEXT,"
                + SYNC + " TEXT,"
                + DATE_TIME + " TEXT"
                + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
    }

    public void insertSubmitData(SubmitOrderModel submitOrderModel) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TICKET_NUMBER, submitOrderModel.getTicket_number());
        contentValues.put(COMMENT, submitOrderModel.getComment());
        contentValues.put(NAME_OF_IMAGE, submitOrderModel.getName_of_the_image());
        contentValues.put(IMAGES, String.valueOf(submitOrderModel.getImage()));
        contentValues.put(LAT, submitOrderModel.getLat());
        contentValues.put(LNG, submitOrderModel.getLng());
        contentValues.put(ADDRESS, submitOrderModel.getAddress());
        contentValues.put(SYNC, submitOrderModel.getSync());
        contentValues.put(DATE_TIME, submitOrderModel.getDateTime());

        sqLiteDatabase.insert(SUBMIT_ORDER_TABLE, null, contentValues);

        AppLog.e(TAG, "INSERT SUBMIT DATA IN DATABASE = " + contentValues);

        sqLiteDatabase.close();

        //checkRecord();
    }

    public ArrayList<SubmitOrderModel> getSubmitData() {

        ArrayList<SubmitOrderModel> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + SUBMIT_ORDER_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {
                SubmitOrderModel submitOrderModel = new SubmitOrderModel();
                submitOrderModel.setId(String.valueOf(Integer.parseInt(cursor.getString(0))));
                submitOrderModel.setTicket_number(cursor.getString(cursor.getColumnIndex(TICKET_NUMBER)));
                submitOrderModel.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                submitOrderModel.setName_of_the_image(cursor.getString(cursor.getColumnIndex(NAME_OF_IMAGE)));
                submitOrderModel.setImage(cursor.getString(cursor.getColumnIndex(IMAGES)));
                submitOrderModel.setLat(Double.valueOf(cursor.getString(cursor.getColumnIndex(LAT))));
                submitOrderModel.setLng(Double.valueOf(cursor.getString(cursor.getColumnIndex(LNG))));
                submitOrderModel.setAddress(cursor.getString(cursor.getColumnIndex(ADDRESS)));
                submitOrderModel.setSync(cursor.getString(cursor.getColumnIndex(SYNC)));
                submitOrderModel.setDateTime(cursor.getString(cursor.getColumnIndex(DATE_TIME)));
                list.add(submitOrderModel);
            }
            while (cursor.moveToNext());
        }

        return list;
    }

    public void deleteData(String id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(SUBMIT_ORDER_TABLE, ID + "=? ", new String[]{String.valueOf(id)});

        AppLog.e(TAG, "DELETE SYNC IN DATABASE = " + id);

        sqLiteDatabase.close();
    }

    public void deletAll() {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(SUBMIT_ORDER_TABLE, null, null);

        AppLog.e(TAG, "DELETE ALL DATA IN DATABASE ");

        sqLiteDatabase.close();
    }

    public boolean checkRecord() {
        boolean flag;
        String quString = "SELECT count(*) FROM " + SUBMIT_ORDER_TABLE;

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
}
