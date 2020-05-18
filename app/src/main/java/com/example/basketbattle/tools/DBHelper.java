package com.example.basketbattle.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CHALLENGERS(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, image TEXT NOT NULL, url TEXT UNIQUE NOT NULL, price TEXT, date TEXT)");
        db.execSQL("CREATE TABLE OUTDATED(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, image TEXT NOT NULL, url TEXT UNIQUE NOT NULL, price TEXT, date TEXT)");
        db.execSQL("CREATE TABLE WINNER(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, image TEXT NOT NULL, url TEXT UNIQUE NOT NULL, price TEXT, date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CHALLENGERS");
        db.execSQL("CREATE TABLE CHALLENGERS(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, image TEXT, url TEXT UNIQUE, price TEXT, date TEXT)");
    }

    @SuppressWarnings("SameParameterValue")
    public void insert_Chal(String title, String image, String url, String price, String date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "INSERT INTO CHALLENGERS VALUES(null,'" + title + "','" + image + "','" + url + "','" + price + "','" + date + "');"
        );
    }

    public void insert_Out(String title, String image, String url, String price, String date) {        //사용 후, 여기로 해당 데이터가 이동됨과 동시에 기존 DB의 값은 삭제되도록...
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "INSERT INTO OUTDATED VALUES(null,'" + title + "','" + image + "','" + url + "','" + price + "','" + date + "');"
        );
    }

    public void insert_Win(String title, String image, String url, String price, String date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "INSERT INTO WINNER VALUES(null,'" + title + "','" + image + "','" + url + "','" + price + "','" + date + "');"
        );
    }

    public void delete_Records(String db_Name, String url) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "DELETE FROM "+db_Name+" WHERE url='" + url + "';"
        );
    }
    public void delete_Records(String db_Name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(
                "DELETE FROM "+db_Name+ ";"
        );
    }


    // DB 디버깅용 메서드
    public String getString(String db_Name) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder result = new StringBuilder();
        try (Cursor cursor = db.rawQuery("SELECT * FROM " + db_Name, null)) {
            while (cursor.moveToNext()) {
                result.append(cursor.getString(0)).append(" : ").append(cursor.getString(1)).append(" : ").append(cursor.getString(2)).append(" : ").append(cursor.getString(3)).append(" : ").append(cursor.getString(4)).append(" : ").append(cursor.getString(5)).append("\n");
            }
        }

        return result.toString();
    }

    public int getIndex(String db_Name) {             // 각각의 DB에 담긴 배열의 열의 길이를 출력하는 매서드.
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + db_Name, null);
        int index = cursor.getCount();

        cursor.close();

        return index;
    }

    public String[][] getArray(String db_Name) {                    // 각각의 DB에 담긴 데이터를 2차원 배열로 출력하는 매서드.
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + db_Name, null);
        String[][] challengers = new String[cursor.getCount()][cursor.getColumnCount()];
        try {
            for (int i = 0; i < challengers.length; i++) {
                for (int j = 1; j < challengers[i].length; j++) {

                    cursor.moveToFirst();
                    for (int k = 0; k < i; k++) {
                        cursor.moveToNext();
                    }
                    challengers[i][j] = cursor.getString(j);
                }
            }
        } finally {
            cursor.close();
        }
        return challengers;
    }
}
