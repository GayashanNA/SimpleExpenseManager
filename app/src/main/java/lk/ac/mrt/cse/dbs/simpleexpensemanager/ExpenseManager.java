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

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 *
 */
public class ExpenseManager implements Serializable {
    private AccountDAO accountsHolder;
    private TransactionDAO transactionsHolder;

    public ExpenseManager() {
        this.transactionsHolder = new InMemoryTransactionDAO();
        this.accountsHolder = new InMemoryAccountDAO();
        Account dummyAcct1 = new Account("12345A", "HSBC", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Sampath Bank", "Obi-Wan Kenobi", 80000.0);
        this.accountsHolder.addAccount(dummyAcct1);
        this.accountsHolder.addAccount(dummyAcct2);
    }

    public List<String> getAccountNumbersList() {
        return accountsHolder.getAccountNumbersList();
    }

    public void updateAccountBalance(String accountNo, int day, int month, int year, ExpenseType expenseType,
                                     String amount) throws InvalidAccountException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date transactionDate = calendar.getTime();

        if (!amount.isEmpty()) {
            double amountVal = Double.parseDouble(amount);
            transactionsHolder.logTransaction(transactionDate, accountNo, expenseType, amountVal);
            accountsHolder.updateBalance(accountNo, expenseType, amountVal);
        }
    }

    public List<Transaction> getTransactionLogs() {
        return transactionsHolder.getPaginatedTransactionLogs(10);
    }

    public void addAccount(String accountNo, String bankName, String accountHolderName, double initialBalance) {
        Account account = new Account(accountNo, bankName, accountHolderName, initialBalance);
        accountsHolder.addAccount(account);
    }
}
