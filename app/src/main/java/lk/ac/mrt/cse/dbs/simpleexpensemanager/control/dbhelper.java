package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class dbhelper extends SQLiteOpenHelper {
    public static final String db_name="200154D.db";
    public static final String table_1="account";
    public static final String col_accountNo="accountNo";
    public static final String col_bankName="bankName";
    public static final String col_accountHolderName="accountHolderName";
    public static final String col_balance="balance";
    public static final String table_2="transactions";
    public static final String col_transaction_ID="transaction_ID";
    public static final String col_date="date";
    public static final String col_expenseType="expenseType";
    public static final String col_amount="amount";
    private SQLiteDatabase db;

    public dbhelper( Context context) {
        super(context, db_name, null,1);
        this.db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+table_1+"("+col_accountNo+" text primary key,"+col_bankName+" text not null,"+col_accountHolderName+" text not null,"+col_balance+" real not null);");
        sqLiteDatabase.execSQL("create table "+table_2+"("+col_transaction_ID+" integer primary key autoincrement,"+col_date+" text not null,"+col_accountNo+" text not null, "+col_expenseType+" text not null,"+col_amount+" real not null);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+table_1);
        sqLiteDatabase.execSQL("drop table if exists "+table_2);
        onCreate(sqLiteDatabase);
    }

    public void addtoaccount(Account acc){
        this.db.execSQL("insert into "+table_1+
                "("+col_accountNo+","
                +col_bankName+","
                +col_accountHolderName+","
                +col_balance+") " +
                "values ("+'"'+acc.getAccountNo()+'"'+","
                +'"'+acc.getBankName()+'"'+","
                +'"'+acc.getAccountHolderName()+'"'+","
                +acc.getBalance()+");"
                );
    }


    public List<String> getaccnolist() {
        List<String> acc_no=new ArrayList<>();
        String query="select "+col_accountNo+" from "+table_1+";";
        Cursor cursor=db.rawQuery(query,null);
        while(cursor.moveToNext()){
            String num=cursor.getString(cursor.getColumnIndex(col_accountNo));
            acc_no.add(num);
        }
        cursor.close();
        return acc_no;
    }

    public List<Account> getacclist() {
        List<Account> acc=new ArrayList<>();
        String query="select * from "+table_1;
        Cursor cursor=db.rawQuery(query,null);
        while(cursor.moveToNext()){
            String num=cursor.getString(cursor.getColumnIndex(col_accountNo));
            String bank=cursor.getString(cursor.getColumnIndex(col_bankName));
            String acc_holder=cursor.getString(cursor.getColumnIndex(col_accountHolderName));
            double balance=cursor.getDouble(cursor.getColumnIndex(col_balance));
            acc.add(new Account(num,bank,acc_holder,balance));
        }
        cursor.close();
        return acc;
    }

    public void removeacc(String accountNo) throws InvalidAccountException {
        String[] para={accountNo};
        String query=col_accountNo+"=?";
        int del_rows=this.db.delete(table_1,query,para);
        if (del_rows==0) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    public void updatebal(String accountNo,double amount,Account acc_to_update) {
//        Account acc_to_update = null;
//        String query="select * from "+table_1;
//        Cursor cursor=db.rawQuery(query,null);
//        while(cursor.moveToNext()){
//            String num=cursor.getString(cursor.getColumnIndex(col_accountNo));
//            if(Objects.equals(num, accountNo)) {
//                String bank = cursor.getString(cursor.getColumnIndex(col_bankName));
//                String acc_holder = cursor.getString(cursor.getColumnIndex(col_accountHolderName));
//                double balance = cursor.getDouble(cursor.getColumnIndex(col_balance));
//                acc_to_update = new Account(num, bank, acc_holder, balance);
//            }
//        }
//        cursor.close();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col_balance,acc_to_update.getBalance()+amount);
        db.update(table_1,contentValues,col_accountNo+"=?",new String[]{accountNo});
    }

    public void logtransaction(Transaction transaction) {
        String query="insert into "+table_2+
                "("+col_date+ ","
                +col_accountNo+","
                +col_expenseType+","
                +col_amount+") values ("+'"'
                +transaction.getDate()+'"'+","
                +'"'+transaction.getAccountNo()+'"'+","
                +'"'+transaction.getExpenseType()+'"'+","
                +transaction.getAmount()+");";
        this.db.execSQL(query);
    }

    public List<Transaction> getalltransactions() throws ParseException {
        List<Transaction> trans=new ArrayList<>();
        String query="select * from "+table_2;
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst()){
        while(cursor.moveToNext()){
            Date date= new Date(cursor.getString(cursor.getColumnIndex(col_date)));
            String acc=cursor.getString(cursor.getColumnIndex(col_accountNo));
            ExpenseType ex_t= ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(col_expenseType)));
            double amount=cursor.getDouble(cursor.getColumnIndex(col_amount));

            trans.add(new Transaction(date,acc,ex_t,amount));

        }
        }
        cursor.close();
        return trans;
    }
}
