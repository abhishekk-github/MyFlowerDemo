package com.example.myflower;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class DBHelper extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "BookMark.db";
  public static final String TABLE_NAME = "flowers";
  public static final String COLUMN_ID = "id";
  public static final String COLUMN_VALUE = "value";
  private HashMap hp;

  public DBHelper(Context context) {
    super(context, DATABASE_NAME, null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    // TODO Auto-generated method stub
    db.execSQL(
        "create table "+ TABLE_NAME +
            "(id integer primary key, value interger)"
    );
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }

  public boolean insertBookmark (Integer id, Boolean flag) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_ID, id);
    contentValues.put(COLUMN_VALUE, flag == true?"1":"0");
    db.insert(TABLE_NAME, null, contentValues);
    return true;
  }

  public Cursor getData(int id) {
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
    return res;
  }

  public int numberOfRows(){
    SQLiteDatabase db = this.getReadableDatabase();
    int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    return numRows;
  }

  public boolean updateBookmark (Integer id, Boolean flag) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_ID, id);
    contentValues.put(COLUMN_VALUE, flag == true?"1":"0");
    db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
    return true;
  }

  public Integer deleteBookmark (Integer id) {
    SQLiteDatabase db = this.getWritableDatabase();
    return db.delete(TABLE_NAME,
        "id = ? ",
        new String[] { Integer.toString(id) });
  }

  public ArrayList<String> getAllID() {
    ArrayList<String> array_list = new ArrayList<String>();

    //hp = new HashMap();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
    res.moveToFirst();

    while(res.isAfterLast() == false){
      array_list.add(res.getString(res.getColumnIndex(COLUMN_ID)));
      res.moveToNext();
    }
    return array_list;
  }
}
