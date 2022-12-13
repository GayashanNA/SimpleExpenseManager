package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.*;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.ACCOUNT_NO;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.ACCOUNT_TABLE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.BALANCE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.BANK;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.HOLDER_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class DBAccountDAO extends DatabaseHelper implements AccountDAO {
    Context context;
    public DBAccountDAO(Context contex) {
        super(contex);
        this.context=contex;
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db =this.getReadableDatabase();
        List<String> accnum_list=new ArrayList<>();
        String query="SELECT * FROM "+ACCOUNT_TABLE+";";
        Cursor cursor=db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                String acc_no=cursor.getString(0);
                accnum_list.add(acc_no);

            }while (cursor.moveToNext());
        }
        else {//failier do nothing
        }
        db.close();
        cursor.close();

        return accnum_list;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db =this.getReadableDatabase();
        List<Account> acc_list=new ArrayList<>();
        String query="SELECT * FROM "+ACCOUNT_TABLE+";";
        Cursor cursor=db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                String acc_no=cursor.getString(0);
                String bank= cursor.getString(1);
                String h_name= cursor.getString(2);
                long balance= cursor.getLong(3);
                Account account= new Account(acc_no,bank,h_name,balance);
                acc_list.add(account);

            }while (cursor.moveToNext());
        }
        else {//failier do nothing
        }
        db.close();
        cursor.close();
        return acc_list;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = null;
        SQLiteDatabase db =this.getReadableDatabase();
        String query="SELECT * FROM "+ACCOUNT_TABLE+" WHERE "+ACCOUNT_NO+"= '"+accountNo+"' ;";
        //       String query="SELECT * FROM "+ACCOUNT_TABLE+" WHERE ?==?;";
        Cursor cursor = db.rawQuery(query,new String[]{ACCOUNT_NO,accountNo});

        if (cursor.moveToFirst()){
            String acc_no=cursor.getString(0);
            String bank= cursor.getString(1);
            String h_name= cursor.getString(2);
            double balance= cursor.getDouble(3);

            account= new Account(acc_no,bank,h_name,balance);
        }
        else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Toast.makeText(context, account.getAccountNo    ()+account.getAccountHolderName()+account.getBankName(), Toast.LENGTH_SHORT).show();
        db.close();
        cursor.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT_NO,account.getAccountNo());
        cv.put(BANK,account.getBankName());
        cv.put(HOLDER_NAME,account.getAccountHolderName());
        cv.put(BALANCE,account.getBalance());

        db.insert(ACCOUNT_TABLE,null,cv);

        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db=this.getWritableDatabase();
        String query="DELETE FROM "+ACCOUNT_TABLE+" WHERE "+ACCOUNT_NO+" = "+accountNo+";";

        db.rawQuery(query, null);

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db=this.getWritableDatabase();
        String balanceQuarry="SELECT "+BALANCE+" FROM "+ACCOUNT_TABLE+" WHERE "+ACCOUNT_NO+"= '"+accountNo+"' ;";
        Cursor balanceCurser=db.rawQuery(balanceQuarry,null);
        double currentBalance;
        double newBalance = 0;
        if(balanceCurser.moveToFirst()){
            currentBalance= balanceCurser.getDouble(0);

            switch (expenseType) {
                case EXPENSE:
                    newBalance=currentBalance-amount;
                    break;
                case INCOME:
                    newBalance=currentBalance+amount;
                    break;
            }}else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        String quary="UPDATE "+ACCOUNT_TABLE+" SET "+BALANCE+"= "+newBalance+" WHERE "+ACCOUNT_NO+"= '"+accountNo+"' ;";
        db.rawQuery(quary,null);

        db.close();
        balanceCurser.close();
    }
}
