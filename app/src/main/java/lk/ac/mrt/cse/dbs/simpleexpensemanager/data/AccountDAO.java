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

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

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
    List<String> getAccountNumbersList();

    /***
     * Get a list of accounts.
     *
     * @return - list of Account objects.
     */
    @Query("select * from account")
    List<Account> getAccountsList();

    /***
     * Get the account given the account number.
     *
     * @param accountNo as String
     * @return - the corresponding Account
     * @throws InvalidAccountException - if the account number is invalid
     */
    @Query("select * from account where accountNo like :accountNo")
    Account getAccount(String accountNo);

    /***
     * Add an account to the accounts collection.
     *
     * @param account - the account to be added.
     */
    @Insert
    void addAccount(Account account);



    @Delete
    void removeAccount(Account account);


    @Query("update account set balance=balance-:amount where accountNo=:accountNo")
    void updateBalanceMinus(String accountNo, double amount);

    @Query("update account set balance=balance+:amount where accountNo=:accountNo")
    void updateBalancePlus(String accountNo, double amount);

}
