package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String ACCOUNT_TABLE = "ACCOUNT_TABLE";
    public static final String ACCOUNT_NO = "ACCOUNT_NO";
    public static final String BANK_NAME = "BANK_NAME";
    public static final String ACC_HOLDER_NAME = "ACC_HOLDER_NAME";
    public static final String BALANCE = "BALANCE";
    public static final String TRANSACTION_TABLE = "TRANSACTION_TABLE";
    public static final String TRANS_ID = "TRANS_ID";
    public static final String TRANS_DATE = "TRANS_DATE";
    public static final String ACC_NO = "ACC_NO";
    public static final String EXPENSE_TYPE = "EXPENSE_TYPE";
    public static final String AMOUNT = "AMOUNT";


    private static SQLiteHelper instance;

    public static SQLiteHelper getInstance(Context context){
        if (instance == null){
            instance = new SQLiteHelper(context);
        }
        return instance;
    }

    public SQLiteHelper(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String accountTableStatement = "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE + " (" + ACCOUNT_NO + " TEXT PRIMARY KEY," +
                BANK_NAME + " TEXT, " + ACC_HOLDER_NAME + " TEXT, " + BALANCE + " DOUBLE)";

        String transactionTableStatement = "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE + " (" + TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRANS_DATE + " DATE, " +
                ACC_NO + " TEXT, " + EXPENSE_TYPE + " TEXT, " + AMOUNT + " DOUBLE, FOREIGN KEY (ACC_NO) REFERENCES ACCOUNT_TABLE(ACCOUNT_NO) )";

        db.execSQL(accountTableStatement);
        db.execSQL(transactionTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS ACCOUNT_TABLE;");
        db.execSQL("DROP TABLE IF EXISTS TRANSACTION_TABLE;");
    }
}
