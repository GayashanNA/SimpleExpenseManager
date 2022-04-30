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
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * TransactionDAO interface can be used to access the log of transactions requested by the user.
 */
@Dao
public interface TransactionDAO {

    /***
     * Log the transaction requested by the user.
     *
     * @param date        - date of the transaction
     * @param accountNo   - account number involved
     * @param expenseType - type of the expense
     * @param amount      - amount involved
     */
    @Insert
    void logTransaction(Transaction transaction);

    /***
     * Return all the transactions logged.
     *
     * @return - a list of all the transactions
     */
    @Query("select * from `transaction`")
    List<Transaction> getAllTransactionLogs();

    /***
     * Return a limited amount of transactions logged.
     *
     * @param limit - number of transactions to be returned
     * @return - a list of requested number of transactions
     */
    @Query("select * from `transaction` limit :limit")
    List<Transaction> getPaginatedTransactionLogs(int limit);
}
