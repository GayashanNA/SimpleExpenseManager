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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "190438H";

    public DBHelper(Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table Account(accountNO TEXT primary key,bank TEXT,accHolder TEXT,balance REAL)");
        db.execSQL("create Table Log(logID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,logDate DATE ,accountNO TEXT,type TINYINT,amount REAL, CONSTRAINT fk_accountNO " +
                "FOREIGN KEY(accountNO) REFERENCES Account(accountNO))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists Log");
        db.execSQL("drop Table if exists Account");
        onCreate(db);
    }
}
