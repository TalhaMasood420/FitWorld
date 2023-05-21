package com.example.i190417_i190468_i190260;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {
    public static String DBNAME="users.db";
    public static int VERSION=1;

    public static String CREATE_CONTACTS_TABLE="CREATE TABLE "+
            MyContract.Users.TABLE_NAME +" ( "+
            MyContract.Users._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            MyContract.Users._EMAIL +" TEXT, "+
            MyContract.Users._PASS +" TEXT, "+
            MyContract.Users._NAME +" TEXT"+
            " );";

    public static String DROPE_CONTACTS_TABLE="DROP TABLE IF EXISTS "+
            MyContract.Users.TABLE_NAME;

    public MyDBHelper(@Nullable Context context) {
        super(context, DBNAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROPE_CONTACTS_TABLE);
        onCreate(sqLiteDatabase);

    }
}
