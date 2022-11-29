package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.Fields;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.DBHandler;;

/**
 * Created by Eranga on 12/6/2015.
 */
public class PersistentAccountDAO implements AccountDAO {

    private Context context;

    //Constructor
    public PersistentAccountDAO(Context context) {
        this.context = context;
    }

    @Override
    public List<String> getAccountNumbersList() {

        //Open the database connection
        DBHandler handler = DBHandler.getInstance(context);
        if( handler == null){
            System.out.print("Damn");
        }
        SQLiteDatabase db = handler.getReadableDatabase();

        //Query to select all account numbers from the account table
        String query = "SELECT "+ Fields.COLUMN_ACCOUNT_NO+" FROM " + Fields.TABLE_ACCOUNT+" ORDER BY " + Fields.COLUMN_ACCOUNT_NO + " ASC";

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<String> resultSet = new ArrayList<>();

        //Add account numbers to a list
        while (cursor.moveToNext())
        {
            resultSet.add(cursor.getString(cursor.getColumnIndex(Fields.COLUMN_ACCOUNT_NO)));
        }

        cursor.close();

        //Return the list of account numbers
        return resultSet;

    }

    @Override
    public List<Account> getAccountsList() {

        DBHandler handler = DBHandler.getInstance(context);
        SQLiteDatabase db = handler.getReadableDatabase();

        //Query to select all the details about all the accounts in the account table
        String query = "SELECT * FROM " + Fields.TABLE_ACCOUNT+" ORDER BY "+Fields.COLUMN_ACCOUNT_NO+" ASC";

        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Account> resultSet = new ArrayList<>();

        //Add account details to a list
        while (cursor.moveToNext())
        {
            Account account = new Account(cursor.getString(cursor.getColumnIndex(Fields.COLUMN_ACCOUNT_NO)),
                    cursor.getString(cursor.getColumnIndex(Fields.COLUMN_BANK_NAME)),
                    cursor.getString(cursor.getColumnIndex(Fields.COLUMN_ACCOUNT_HOLDER_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(Fields.COLUMN_BALANCE)));

            resultSet.add(account);
        }

        cursor.close();

        //Return list of account objects
        return resultSet;

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        DBHandler handler = DBHandler.getInstance(context);
        SQLiteDatabase db = handler.getReadableDatabase();

        //Query to get details of the account specifiec by the account number
        String query = "SELECT * FROM " + Fields.TABLE_ACCOUNT + " WHERE " + Fields.COLUMN_ACCOUNT_NO + " =  '" + accountNo + "'";

        Cursor cursor = db.rawQuery(query, null);

        Account account = null;

        //add the details to an account object
        if (cursor.moveToFirst()) {
            account = new Account(cursor.getString(cursor.getColumnIndex(Fields.COLUMN_ACCOUNT_NO)),
                    cursor.getString(cursor.getColumnIndex(Fields.COLUMN_BANK_NAME)),
                    cursor.getString(cursor.getColumnIndex(Fields.COLUMN_ACCOUNT_HOLDER_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(Fields.COLUMN_BALANCE)));
        }
        //If account is not found throw an exception
        else {
            throw new InvalidAccountException("You have selected an invalid account number...!");
        }

        cursor.close();

        //Return the account object
        return account;
    }

    @Override
    public void addAccount(Account account) {

        DBHandler handler = DBHandler.getInstance(context);
        SQLiteDatabase db = handler.getWritableDatabase();

        //Save account details to the account table
        ContentValues values = new ContentValues();
        values.put(Fields.COLUMN_ACCOUNT_NO, account.getAccountNo());
        values.put(Fields.COLUMN_BANK_NAME, account.getBankName());
        values.put(Fields.COLUMN_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        values.put(Fields.COLUMN_BALANCE, account.getBalance());

        db.insert(Fields.TABLE_ACCOUNT, null, values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        DBHandler handler = DBHandler.getInstance(context);
        SQLiteDatabase db = handler.getWritableDatabase();

        //Query to delete a particular account from the account table
        String query = "SELECT * FROM " + Fields.TABLE_ACCOUNT + " WHERE " + Fields.COLUMN_ACCOUNT_NO + " =  '" + accountNo + "'";

        Cursor cursor = db.rawQuery(query, null);

        Account account = null;

        //Delete the account if found in the table
        if (cursor.moveToFirst()) {
            account = new Account(cursor.getString(cursor.getColumnIndex(Fields.COLUMN_ACCOUNT_NO)),
                    cursor.getString(cursor.getColumnIndex(Fields.COLUMN_BANK_NAME)),
                    cursor.getString(cursor.getColumnIndex(Fields.COLUMN_ACCOUNT_HOLDER_NAME)),
                    cursor.getFloat(cursor.getColumnIndex(Fields.COLUMN_BALANCE)));
            db.delete(Fields.TABLE_ACCOUNT, Fields.COLUMN_ACCOUNT_NO + " = ?", new String[] { accountNo });
            cursor.close();

        }
        //If account is not found throw an exception
        else {
            throw new InvalidAccountException("No such account found...!");
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        DBHandler handler = DBHandler.getInstance(context);
        SQLiteDatabase db = handler.getWritableDatabase();

        ContentValues values = new ContentValues();

        //Retrieve the account details of the selected account
        Account account = getAccount(accountNo);

        //Update the balance if the account is found in the table
        if (account!=null) {

            double new_amount=0;

            //Deduct the amount is it is an expense
            if (expenseType.equals(ExpenseType.EXPENSE)) {
                new_amount = account.getBalance() - amount;
            }
            //Add the amount if it is an income
            else if (expenseType.equals(ExpenseType.INCOME)) {
                new_amount = account.getBalance() + amount;
            }

            //Query to update balance in the account table
            String strSQL = "UPDATE "+Fields.TABLE_ACCOUNT+" SET "+Fields.COLUMN_BALANCE+" = "+new_amount+" WHERE "+Fields.COLUMN_ACCOUNT_NO+" = '"+ accountNo+"'";

            db.execSQL(strSQL);

        }
        //If account is not found throw an exception
        else {
            throw new InvalidAccountException("No such account found...!");
        }

    }
}
