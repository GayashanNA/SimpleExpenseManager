package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataBaseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

public class PersistentAccountDAO extends DataBaseManager implements AccountDAO {

/**
 * This is an Persistent implementation of the AccountDAO interface.
 */

    public PersistentAccountDAO(@Nullable Context context) {
        super(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();
        String query = "SELECT ACCOUNT_NUMBER from ACCOUNT_TABLE";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String accountNumber = cursor.getString(0);
                accountNumbersList.add(accountNumber);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();
        String query = "SELECT * from ACCOUNT_TABLE";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String accountNumber = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getDouble(3);
                Account newAccount = new Account(accountNumber,bankName, accountHolderName, balance);
                accountList.add(newAccount);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return  accountList;
    }

    @Override
    public Account getAccount(String accountNumber) throws InvalidAccountException {
        String query = "SELECT * from ACCOUNT_TABLE WHERE ACCOUNT_NUMBER = '"+ accountNumber + "' ;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String accountNo = cursor.getString(0);
            String bankName = cursor.getString(1);
            String accountHolderName = cursor.getString(2);
            double balance = cursor.getDouble(3);
            Account acc = new Account(accountNo,bankName, accountHolderName, balance);
            cursor.close();
            db.close();
            return acc;
        }
        else {
            String msg = "Account number is invalid.";
            cursor.close();
            db.close();
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public boolean addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACCOUNT_NUMBER, account.getAccountNo());
        cv.put(BANK_NAME, account.getBankName());
        cv.put(ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        cv.put(BALANCE, account.getBalance());

        if (db.insert(ACCOUNT_TABLE, null, cv) == -1 )
            return false;
        else
            return true;
    }

    @Override
    public void removeAccount(String accountNumber) throws InvalidAccountException {
        String query = "DELETE BALANCE from ACCOUNT_TABLE where ACCOUNT_NUMBER= '"+ accountNumber+"' ;";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    @Override
    public void updateBalance(String accountNumber, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select BALANCE from ACCOUNT_TABLE where ACCOUNT_NUMBER = '"+ accountNumber +"' ;";
        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();
        double balance = cursor.getDouble(0);
        switch (expenseType) {
            case EXPENSE:
                balance  -= amount;
                break;
            case INCOME:
                balance  += amount;
                break;
        }

        String query_update = "UPDATE ACCOUNT_TABLE SET BALANCE = "+ balance +" WHERE ACCOUNT_NUMBER = '"+accountNumber+"' ;";
        db.execSQL(query_update);
        cursor.close();
        db.close();
    }
}
