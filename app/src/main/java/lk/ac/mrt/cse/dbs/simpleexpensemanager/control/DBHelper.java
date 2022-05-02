package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.method.NumberKeyListener;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "190597R";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE table ACCOUNT (acc_no VARCHAR(20) PRIMARY KEY, bank VARCHAR(20), owner varchar(20), balance DECIMAL(10,2))"
        );
        db.execSQL(
                "CREATE table Trans (tr_id INTEGER PRIMARY KEY AUTOINCREMENT , acc_no VARCHAR(20) , date VARCHAR(20), amount  DECIMAL(10,2), type VARCHAR(20) )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS ACCOUNT");
        db.execSQL("DROP TABLE IF EXISTS Trans");
        onCreate(db);
    }

    public boolean insertAccount (String accountNo, String bankName, String accountHolderName, double initialBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("acc_no", accountNo);
        contentValues.put("bank", bankName);
        contentValues.put("owner", accountHolderName);
        contentValues.put("balance", initialBalance);
        Cursor cursor = db.rawQuery("SELECT * FROM ACCOUNT WHERE acc_no = ?", new String[]{accountNo});

        if (cursor.getCount() == 0) {
        long result = db.insert("ACCOUNT", null, contentValues);
        if(result==-1){
            return false;
        }
        return true;
        }
        return false;
    }

    public boolean updateBalance(String accountNo,  double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("balance", amount);
        Cursor cursor = db.rawQuery("SELECT * FROM ACCOUNT WHERE acc_no = ?", new String[]{accountNo});

        if (cursor.getCount() > 0) {
            long result = db.update("ACCOUNT", contentValues, "acc_no=?", new String[]{accountNo});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean deleteAccount(String accountNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = db.rawQuery("SELECT * FROM ACCOUNT WHERE acc_no = ?", new String[]{accountNo});
        if (cursor.getCount() > 0) {
            long result = db.delete("ACCOUNT", "acc_no=?", new String[]{accountNo});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Cursor getAccount(String acc_no){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ACCOUNT WHERE acc_no = ?", new String[]{acc_no});
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAccounts(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ACCOUNT", null);
        return cursor;
    }

    public Cursor getTransactions(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Trans", null);
        return cursor;
    }
    public Cursor getLimitedTransactions(int limit){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Trans ORDER BY tr_id DESC LIMIT " + Integer.toString(limit) , null);
        return cursor;
    }
    public Cursor getAccountNum(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT acc_no FROM ACCOUNT", null);
        return cursor;
    }


    public boolean insertTransaction(String date, String accountNo, ExpenseType expenseType, double amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("acc_no", accountNo);
        contentValues.put("date", date);
        contentValues.put("amount", amount );
        contentValues.put("type", expenseType.toString());
        long result = db.insert("Trans",null,contentValues);
        if(result==-1){
            return false;
        }
        return true;
    }




}
