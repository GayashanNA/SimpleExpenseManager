package lk.ac.mrt.cse.dbs.simpleexpensemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Eranga on 12/6/2015.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static DBHandler handler = null;

    //private static final
    private static final String DATABASE_NAME = "130401J.db";
    private static final int DATABASE_VERSION = 1;


    public DBHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static DBHandler getInstance(Context context)
    {
        if(handler == null)
            handler = new DBHandler(context);
        return handler;
    }

    //private static final

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String accountTable = "CREATE TABLE " +
                Fields.TABLE_ACCOUNT + "("
                + Fields.COLUMN_ACCOUNT_NO + " VARCHAR(20) NOT NULL PRIMARY KEY,"
                + Fields.COLUMN_BANK_NAME + " VARCHAR(100) NULL,"
                + Fields.COLUMN_ACCOUNT_HOLDER_NAME + " VARCHAR(100) NULL,"
                + Fields.COLUMN_BALANCE + " DECIMAL(10,2) NULL )";

        String transactionTable = "CREATE TABLE " +
                Fields.TABLE_TRANSACTION_LOG + "("
                + Fields.COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Fields.COLUMN_ACCOUNT_NO + " VARCHAR(100) NOT NULL,"
                + Fields.COLUMN_TRANSACTION_DATE + " DATE NULL,"
                + Fields.COLUMN_TRANSACTION_AMOUNT + " DECIMAL(10,2) NULL,"
                + Fields.COLUMN_EXPENSE_TYPE + " VARCHAR(100) NULL, " +
                "FOREIGN KEY("+Fields.COLUMN_ACCOUNT_NO+") REFERENCES "+Fields.TABLE_ACCOUNT+"("+Fields.COLUMN_ACCOUNT_NO+"))";

        sqLiteDatabase.execSQL(accountTable);
        sqLiteDatabase.execSQL(transactionTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Fields.TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Fields.TABLE_TRANSACTION_LOG);
        onCreate(sqLiteDatabase);

    }
}
