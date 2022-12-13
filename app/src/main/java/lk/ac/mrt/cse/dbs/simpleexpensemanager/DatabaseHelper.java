package lk.ac.mrt.cse.dbs.simpleexpensemanager;


import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "ExpenseManager.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createAccTableStatement="CREATE TABLE " + ACCOUNT_TABLE + "(" + ACCOUNT_NO + " text PRIMARY KEY, " + BANK + " text, " + HOLDER_NAME + " text, " + BALANCE + " real);";
        String createTranceTableStatement="CREATE TABLE " + TRANSACTIONS_TABLE + "(" + TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NO + " INTEGER , "+AMOUNT+" real, " + TRANSACTION_TYPE + " text, " + DATE + " text, FOREIGN KEY("+ACCOUNT_NO+") REFERENCES "+ACCOUNT_TABLE+"("+ACCOUNT_NO+") );";


        sqLiteDatabase.execSQL(createAccTableStatement);
        sqLiteDatabase.execSQL(createTranceTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE);
        onCreate(db);
    }

}
