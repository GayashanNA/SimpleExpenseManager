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

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * This POJO holds the information regarding a single transaction.
 */
@Entity(tableName = "transaction")
public class Transaction {
    public int year, month, date;
    @PrimaryKey@NonNull
    public String trans_no;
    //private Date date;

    private String accountNo;
    private ExpenseType expenseType;
    private double amount;

    public Transaction(String trans_no,int year, int month, int date, String accountNo,
                       ExpenseType expenseType, double amount) {
        this.trans_no=trans_no;
        this.year=year;
        this.month=month;
        this.date=date;
        this.accountNo = accountNo;
        this.expenseType = expenseType;
        this.amount = amount;
    }

    public Date getDate() {
        return new Date(year,  month,  date);
    }

    public void setDate(Date date) {
        this.year = date.getYear();
        month=date.getMonth();
        this.date=date.getDate();
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
