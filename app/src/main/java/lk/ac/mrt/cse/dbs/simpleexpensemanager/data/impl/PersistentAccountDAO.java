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

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class PersistentAccountDAO implements AccountDAO {

    private DBHelper dbHelper;

    public PersistentAccountDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select accountNO from Account",null);
        while (cursor.moveToNext()){
            res.add(cursor.getString(0));
        }
        cursor.close();
        return res;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Account> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from Account",null);
        while (cursor.moveToNext()){
            res.add(new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3)));
        }
        cursor.close();
        return res;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Account where accountNO=?",new String[]{accountNo});

        if (cursor.getCount()>0 && cursor.moveToFirst()){
            return (new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3)));
        }
        cursor.close();
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNO",account.getAccountNo());
        contentValues.put("bank",account.getBankName());
        contentValues.put("accHolder",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());
        long result = db.insert("Account",null,contentValues);

        //handle exception here
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from Account where accountNO=?",new String[]{accountNo});
        if(cursor.getCount()>0){
            db.delete("Account","accountNO=?",new String[]{accountNo});
            return;
        }
        cursor.close();
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from Account where accountNO=?",new String[]{accountNo});
        if(cursor.getCount()>0 && cursor.moveToFirst()){
            double balance = cursor.getDouble(3);
            switch (expenseType) {
                case EXPENSE:
                    balance -=amount;
                    break;
                case INCOME:
                    balance +=amount;
                    break;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("balance",balance);
            db.update("Account",contentValues,"accountNO=?",new String[]{accountNo});
            return;
        }
        cursor.close();

        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);

    }

}
