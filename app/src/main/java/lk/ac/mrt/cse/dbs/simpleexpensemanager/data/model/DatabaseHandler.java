package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHandler";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SimpleExpenseManager";

    // Table Names
    private static final String TABLE_ACOUNTDETAILS = "acountdetails";
    private static final String TABLE_TRANSACTION = "transacion";


    // Common column names
    //private static final String KEY_ID = "id";
    private static final String KEY_ACCOUNT_NO = "accountNo";

    // NOTES Table - column nmaes
    private static final String KEY_ACOUNTDETAILS = "acountdetails";
    private static final String KEY_BANK = "bankName";
    private static final String KEY_ACCOUNT_HOLDER = "accountHolderName";
    private static final String KEY_INITIAL_BALANCE = "balance";


    // NOTE_TAGS Table - column names
    private static final String KEY_TRANSACTION = "transacion";
    private static final String KEY_TYPE = "type";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE= "date";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_ACOUNTDETAILS = "CREATE TABLE "
            + TABLE_ACOUNTDETAILS + "(" + KEY_ACCOUNT_NO + " TEXT PRIMARY KEY," + KEY_BANK
            + " TEXT," + KEY_ACCOUNT_HOLDER + " TEXT," + KEY_INITIAL_BALANCE
            + " DOUBLE" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION
            + "(" + KEY_ACCOUNT_NO + " TEXT PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_AMOUNT + "INTEGER ,"
            + KEY_DATE + " DATETIME" + ")";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ACOUNTDETAILS);
        db.execSQL(CREATE_TABLE_TRANSACTION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACOUNTDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);


        // create new tables
        onCreate(db);
    }


    // Adding Account  Details
    public void insertAccountDetails(int accountNo, String bankName, String accountHolderName, double balance ){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ACCOUNT_NO, accountNo);
        cValues.put(KEY_BANK, bankName);
        cValues.put(KEY_ACCOUNT_HOLDER, accountHolderName);
        cValues.put(KEY_INITIAL_BALANCE, balance);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_ACOUNTDETAILS,null, cValues);
        db.close();
    }



    //Get account_num
    public ArrayList<Integer> GetAccountNO(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Integer> accountnoList=new ArrayList<>();
        String query = "SELECT accountNo FROM "+ TABLE_ACOUNTDETAILS;
        Cursor cursor=db.rawQuery(query,null);
        while (cursor.moveToNext()){

            accountnoList.add(cursor.getColumnIndex(KEY_ACCOUNT_NO));
        }
        return accountnoList;
    }
    private String getDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault());
        return dateFormat.format((date));
    }


    //Adding Details
    public void insertTransaction(int accountNo, String type, int amount, Date date){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ACCOUNT_NO, accountNo);
        cValues.put(KEY_TYPE, type);
        cValues.put(KEY_AMOUNT, amount);
        cValues.put(KEY_DATE, getDate(date));
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_TRANSACTION,null, cValues);
        db.close();
    }


    // Get User Details based on account_no
    public ArrayList<HashMap<String, String>> GetDetail(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> transacionList = new ArrayList<>();
        String query = "SELECT date, accountNo, type , amount FROM "+ TABLE_TRANSACTION;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> transacion = new HashMap<>();
            transacion.put("date",cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            transacion.put("accountNo",cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NO)));
            transacion.put("type",cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            transacion.put("amount",cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)));
            transacionList.add(transacion);

        }
        return  transacionList;
    }
    // Delete Account
    public void DeleteAccountDetails(int accountNo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACOUNTDETAILS, KEY_ACCOUNT_NO+" = ?",new String[]{String.valueOf(accountNo)});
        db.close();
    }

    // Update  Details
    public double UpdateDetails(double balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_INITIAL_BALANCE, balance);

        double i = db.update(TABLE_TRANSACTION, cVals, KEY_ACCOUNT_NO+" = ?",new String[]{String.valueOf(balance)});
        return  i;
    }
}