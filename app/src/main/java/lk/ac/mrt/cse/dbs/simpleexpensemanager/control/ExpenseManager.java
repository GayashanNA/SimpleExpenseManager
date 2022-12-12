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

package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * The ExpenseManager acts as the mediator when performing transactions. This is an abstract class with an abstract
 * method to setup the DAO objects depending on the implementation.
 */
public abstract class ExpenseManager implements Serializable {
    private AccountDAO accountsHolder;
    private TransactionDAO transactionsHolder;

    /***
     * Get list of account numbers as String.
     *
     * @return
     */
    public List<String> getAccountNumbersList() {
        return accountsHolder.getAccountNumbersList();
    }

    /***
     * Update the account balance.
     *
     * @param accountNo
     * @param day
     * @param month
     * @param year
     * @param expenseType
     * @param amount
     * @throws InvalidAccountException
     */
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

    /***
     * Get a list of transaction logs.
     *
     * @return
     */
    public List<Transaction> getTransactionLogs() throws ParseException {
        return transactionsHolder.getPaginatedTransactionLogs(10);
    }

    /***
     * Add account to the accounts dao.
     *
     * @param accountNo
     * @param bankName
     * @param accountHolderName
     * @param initialBalance
     */
    public void addAccount(String accountNo, String bankName, String accountHolderName, double initialBalance) {
        Account account = new Account(accountNo, bankName, accountHolderName, initialBalance);
        accountsHolder.addAccount(account);
    }

    /***
     * Get access to the AccountDAO concrete implementation.
     *
     * @return
     */
    public AccountDAO getAccountsDAO() {
        return accountsHolder;
    }

    /***
     * Set the concrete AccountDAO implementation.
     *
     * @param accountDAO
     */
    public void setAccountsDAO(AccountDAO accountDAO) {
        this.accountsHolder = accountDAO;
    }

    /***
     * Get access to the TransactionDAO concrete implementation.
     *
     * @return
     */
    public TransactionDAO getTransactionsDAO() {
        return transactionsHolder;
    }

    /***
     * Set the concrete TransactionDAO implementation.
     *
     * @param transactionDAO
     */
    public void setTransactionsDAO(TransactionDAO transactionDAO) {
        this.transactionsHolder = transactionDAO;
    }

    /***
     * This method should be implemented by the concrete implementation of this class. It will dictate how the DAO
     * objects will be initialized.
     */
    public abstract void setup() throws ExpenseManagerException;
}
