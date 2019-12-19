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

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.Query;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import static androidx.room.OnConflictStrategy.IGNORE;

//import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * AccountDAO interface can be used to access the account details, including listing, adding, updating, removing
 * accounts and updating account balance.
 */
@Dao
public interface AccountDAO {

    /***
     * Get a list of account numbers.
     *
     * @return - list of account numbers as String
     */
    @Query("select accountNo from account")
    public List<String> getAccountNumbersList();

    /***
     * Get a list of accounts.
     *
     * @return - list of Account objects.
     */
    @Query("select * from account")
    public List<Account> getAccountsList();

    /***
     * Get the account given the account number.
     *
     * @param accountNo as String
     * @return - the corresponding Account
     * @throws InvalidAccountException - if the account number is invalid
     */
    @Query("select * from account where accountNo = :accountNo")
    public Account getAccount(String accountNo) throws InvalidAccountException;

    /***
     * Add an account to the accounts collection.
     *
     * @param account - the account to be added.
     */
    @Insert(onConflict = IGNORE)
    public void addAccount(Account account);

    /***
     * Remove an account from the accounts collection.
     *
     * @param accountNo - of the account to be removed.
     * @throws InvalidAccountException - if the account number is invalid
     */
    @Delete
    public void removeAccount(String accountNo) throws InvalidAccountException;

    /***
     * Update the balance of the given account. The type of the expense is specified in order to determine which
     * action to be performed.
     * <p/>
     * The implementation has the flexibility to figure out how the updating operation is committed based on the type
     * of the transaction.
     *
     * @param accountNo   - account number of the respective account
     * @param expenseType - the type of the transaction
     * @param amount      - amount involved
     * @throws InvalidAccountException - if the account number is invalid
     */
    @Insert
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException;

}
