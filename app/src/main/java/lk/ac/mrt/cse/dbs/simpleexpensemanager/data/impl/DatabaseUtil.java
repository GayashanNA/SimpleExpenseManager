package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseUtil extends SQLiteOpenHelper {

    //database name
    public static final String DATABASE  = "expenser";
    private static final int VERSION = 1;

    //table names
    public static final String ACCOUNT_TBL = "Account";
    public static final String TRX_TBL = "Trx";

    // ACCOUNT_TBL columns
    public static final String ACCOUNT_NO = "account_no";
    public static final String BANK_NAME = "bank_name";
    public static final String ACCOUNT_HOLDER = "account_holder";
    public static final String BALANCE = "balance";

    public static final String ID = "id";
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String ACC_NUM = "account_no";
    public static final String Type = "type";

    public DatabaseUtil( Context context) {
        super(context, DATABASE, null, VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + ACCOUNT_TBL + "(" +
                ACCOUNT_NO + " TEXT PRIMARY KEY, " +
                BANK_NAME + " TEXT NOT NULL, " +
                ACCOUNT_HOLDER + " TEXT NOT NULL, " +
                BALANCE + " REAL NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TRX_TBL + "(" +
                ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                AMOUNT + " REAL NOT NULL, " +
                DATE + " DATE NOT NULL, " +
                Type + " TEXT NOT NULL, " +
                ACC_NUM + " TEXT NOT NULL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
