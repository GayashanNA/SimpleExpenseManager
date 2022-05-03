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



import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType.EXPENSE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType.INCOME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


/**
 * This is an In-Memory implementation of TransactionDAO interface. This is not a persistent storage. All the
 * transaction logs are stored in a LinkedList in memory.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private DBHelper dbHelper;

    public PersistentTransactionDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Account WHERE accountNO=?",new String[]{accountNo});
        if(cursor.getCount()>0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("accountNO",accountNo);

            //format and put date into contentValues
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = formatter.format(date);
            contentValues.put("logDate", formattedDate);

            int expType =0;
            if(expenseType==EXPENSE){
                expType=1;
            }
            contentValues.put("type",expType);
            contentValues.put("amount",amount);
            db.insert("Log",null,contentValues);
            return;
        }
        cursor.close();

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> res = new LinkedList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Log",null);
        while (cursor.moveToNext()){
            ExpenseType expenseType = INCOME;
            if(cursor.getInt(3)==1){
                    expenseType = EXPENSE;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date=new Date();
            try {
                date=  dateFormat.parse(cursor.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.add(new Transaction(date,cursor.getString(2),expenseType,cursor.getDouble(4)));
        }
        cursor.close();
        return res;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> res = new LinkedList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Log ORDER BY logID ASC LIMIT ?",new String[]{String.valueOf(limit)});
        while (cursor.moveToNext()){
            ExpenseType expenseType = INCOME;
            if(cursor.getInt(3)==1){
                expenseType = EXPENSE;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date=new Date();
            try {
                date=  dateFormat.parse(cursor.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.add(new Transaction(date,cursor.getString(2),expenseType,cursor.getDouble(4)));
        }
        cursor.close();
        return res;

    }

}
