package idv.ron.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/*
存取SQLite資料庫的說明如下:
1. 繼承SQLiteOpenHelper並改寫onCreate(), onUpgrade()
2. 第一次呼叫getWritableDatabase()或getReadableDatabase()時會建立資料庫並自動呼叫onCreate()
3. SQLiteOpenHelper子類別內增加其他存取資料庫的方法，方便之後呼叫

資料新增的說明如下:
1. 呼叫getWritableDatabase()建立、開啟資料庫並回傳資料庫物件
2. 建立ContentValues物件儲存欲新增的資料
3. 呼叫ContentValues.put()新增資料到指定欄位內
4. 呼叫SQLiteDatabase.insert()新增資料，新增成功會回傳該資料列的ID；新增失敗則回傳-1
*/

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "TravelSpots";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Spot";
    private static final String COL_id = "id";
    private static final String COL_name = "name";
    private static final String COL_web = "web";
    private static final String COL_phone = "phone";
    private static final String COL_address = "address";
    private static final String COL_image = "image";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ( " +
                    COL_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_name + " TEXT NOT NULL, " +
                    COL_web + " TEXT, " +
                    COL_phone + " TEXT, " +
                    COL_address + " TEXT, " +
                    COL_image + " BLOB ); ";

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<Spot> getAllSpots() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                COL_id, COL_name, COL_web, COL_phone, COL_address, COL_image
        };
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null,
                null);
        List<Spot> spotList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String web = cursor.getString(2);
            String phone = cursor.getString(3);
            String address = cursor.getString(4);
            byte[] image = cursor.getBlob(5);
            Spot spot = new Spot(id, name, web, phone, address, image);
            spotList.add(spot);
        }
        cursor.close();
        return spotList;
    }

    public Spot findById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {
                COL_name, COL_web, COL_phone, COL_address, COL_image
        };
        String selection = COL_id + " = ?;";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);
        Spot spot = null;
        if (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String web = cursor.getString(1);
            String phone = cursor.getString(2);
            String address = cursor.getString(3);
            byte[] image = cursor.getBlob(4);
            spot = new Spot(id, name, web, phone, address, image);
        }
        cursor.close();
        return spot;
    }

    public long insert(Spot spot) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_name, spot.getName());
        values.put(COL_web, spot.getWeb());
        values.put(COL_phone, spot.getPhone());
        values.put(COL_address, spot.getAddress());
        values.put(COL_image, spot.getImage());
        return db.insert(TABLE_NAME, null, values);
    }

    public int update(Spot spot) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_name, spot.getName());
        values.put(COL_web, spot.getWeb());
        values.put(COL_phone, spot.getPhone());
        values.put(COL_address, spot.getAddress());
        values.put(COL_image, spot.getImage());
        String whereClause = COL_id + " = ?;";
        String[] whereArgs = {Integer.toString(spot.getId())};
        return db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    public int deleteById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COL_id + " = ?;";
        String[] whereArgs = {String.valueOf(id)};
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }
}
