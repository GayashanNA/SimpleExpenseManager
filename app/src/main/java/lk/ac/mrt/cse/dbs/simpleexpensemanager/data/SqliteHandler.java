package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SqliteHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="190564N.db";
    public static final String TABLE_ACCOUNTS="accounts";
    public static final String TABLE_TRANSACTIONS="transactions";

    public static final String COLUMN_ID="id";
    public static final String COLUMN_ACCOUNT_NO="account_no";
    public static final String COLUMN_BANK_NAME="bank_name";
    public static final String COLUMN_ACCOUNT_HOLDER_NAME="account_holder_name";
    public static final String COLUMN_BALANCE="balance";
    public static final String COLUMN_DATE="date";
    public static final String COLUMN_TYPE="expense_type";
    public static final String COLUMN_AMOUNT="amount";

    public static SqliteHandler sqliteHandler;

    public SqliteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SqliteHandler getInstanceDB(Context context){
        if (sqliteHandler==null){
            sqliteHandler = new SqliteHandler(context);
        }
        return sqliteHandler;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql1 = "CREATE TABLE " + TABLE_ACCOUNTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ACCOUNT_NO + " TEXT UNIQUE, " +
                COLUMN_BANK_NAME + " TEXT, " +
                COLUMN_ACCOUNT_HOLDER_NAME + " TEXT, " +
                COLUMN_BALANCE + " DOUBLE);";

        String sql2 = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COLUMN_ACCOUNT_NO + " TEXT, " +
                COLUMN_DATE + " DATETIME, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_AMOUNT + " DOUBLE);";

        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql1 = "DROP TABLE IF EXISTS " + TABLE_ACCOUNTS + " ;";
        String sql2 = "DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS + " ;";
        onCreate(sqLiteDatabase);

    }
}
