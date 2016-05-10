package com.example.admin.projetointegrado_5semestre;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Yuri on 03/05/2016.
 */
public class DatabaseOpenHelper extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "projetoIntegrado.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
