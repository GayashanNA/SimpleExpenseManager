package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBaseManager extends SQLiteOpenHelper {

    public static final String ACCOUNT_TABLE = "ACCOUNT_TABLE";
    public static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    public static final String BANK_NAME = "BANK_NAME";
    public static final String ACCOUNT_HOLDER_NAME = "ACCOUNT_HOLDER_NAME";
    public static final String BALANCE = "BALANCE";
    public static final String TRANSACTION_TABLE = "TRANSACTION_TABLE";
    public static final String ID = "ID";
    public static final String TYPE = "TYPE";
    public static final String DATE = "DATE";
    public static final String AMOUNT = "AMOUNT";

    public DataBaseManager(@Nullable Context context) {
        super(context, "190482K.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase SqLiteDb) {
        String tableStatementForAddAccount = "CREATE TABLE " +
                ACCOUNT_TABLE + " (" +
                ACCOUNT_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BANK_NAME + " TEXT," +
                ACCOUNT_HOLDER_NAME + " TEXT," +
                BALANCE + " REAL" + ")";

        SqLiteDb.execSQL(tableStatementForAddAccount);

        String tableStatementForTransaction = "CREATE TABLE " +
                TRANSACTION_TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ACCOUNT_NUMBER + " INTEGER," +
                TYPE + " INTEGER," +
                DATE + " TEXT," +
                AMOUNT + " REAL" + ")";

        SqLiteDb.execSQL(tableStatementForTransaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int acc_no, int type) {

    }

}
