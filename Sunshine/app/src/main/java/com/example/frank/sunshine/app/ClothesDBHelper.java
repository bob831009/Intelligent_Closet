/**
 * Created by kaku on 5/30/15.
 */
package com.example.frank.sunshine.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClothesDBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Clothes.db";
    public static final String CLOTHES_COLUMN_ID = "id";
    public static final String TABLE_NAME = "Clothes";
    public static final String CATEGORY = "category";
    public static final String WARMTH_INDEX = "warmth";
    public static final String KEY_IMAGE = "image";



    //constructor
    public ClothesDBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS Clothes (id integer primary key autoincrement, category text not null, warmth integer not null,"
                        + KEY_IMAGE + " BLOB )"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Clothes");
        onCreate(db);
    }

    public boolean addClothes (String category,int warmth,byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY, category);
        cv.put(WARMTH_INDEX, warmth);
        cv.put(KEY_IMAGE, image);
        try {
            db.insertOrThrow(TABLE_NAME, null, cv);
        }
        catch (SQLiteException e){
            return false;
        }
        return true;
    }
    public ArrayList<Map<String,Object>> getClothes(int warm, String category){

        ArrayList <Map<String,Object>> array_list = new ArrayList<>();
        Map<String,Object> map;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE warmth = ? and category = ? " , new String[]{String.valueOf(warm), category} );
        Log.e("!!!!!!!!!!!!!!res found" , "" + res.getCount());
        if(res.moveToFirst()) {
            do {
                map = new HashMap<>();
                map.put(CATEGORY, res.getString(res.getColumnIndex(CATEGORY)));
                map.put(WARMTH_INDEX, res.getInt(res.getColumnIndex(WARMTH_INDEX)));
                map.put(KEY_IMAGE, res.getBlob(res.getColumnIndex(KEY_IMAGE)));
                map.put(CLOTHES_COLUMN_ID,res.getInt(res.getColumnIndex(CLOTHES_COLUMN_ID)));
                array_list.add(map);
                Log.e("added image length = ",array_list.get(array_list.size()-1).get("image")+"");
            } while (res.moveToNext());

            res.close();
            return array_list;
        }
        Log.e("getClothes:","failed!!!!!!!!!!!!!!!!!!");
        res.close();
        return array_list;
        // HOW TO USE Cursor Type?
    }


    public boolean editClothes (int id, String category, int warmth,byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY, category);
        cv.put(WARMTH_INDEX, warmth);
        cv.put(KEY_IMAGE,image);
        return db.update(TABLE_NAME,cv,"id = ?", new String[]{Integer.toString(id)}) != 1 ;
    }


    public boolean deleteClothes (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME,null);
        Log.e("!!!before delete","count = "+res.getCount());
        int ret = db.delete(TABLE_NAME,"id = ?",new String[]{Integer.toString(id)});
        res = db.rawQuery("select * from "+TABLE_NAME,null);
        Log.e("!!!after","count = "+res.getCount());
        res.close();
        return ret == 1;
        //Should be 1
    }


    public ArrayList<Map<String,Object>> getAllClothes(){
        ArrayList<Map<String,Object>> array_list = new ArrayList<>();
        Map<String,Object> map;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME +" ORDER BY "+ WARMTH_INDEX,null);
        if(res.getCount() > 0 && res.moveToFirst()) {
            Log.e("return all"+res.getCount(),"!!!!!!!");
            do {
                map = new HashMap<>();
                map.put(CATEGORY, res.getString(res.getColumnIndex(CATEGORY)));
                map.put(WARMTH_INDEX, res.getInt(res.getColumnIndex(WARMTH_INDEX)));
                map.put(KEY_IMAGE, res.getBlob(res.getColumnIndex(KEY_IMAGE)));
                map.put(CLOTHES_COLUMN_ID,res.getInt(res.getColumnIndex(CLOTHES_COLUMN_ID)));
                array_list.add(map);
            } while (res.moveToNext());
            res.close();
            assert array_list.size() == res.getCount();
            return array_list;
        }
        Log.e("!!!!!!","COLD");
        res.close();
        return array_list;

    }

}
