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

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class ApplicationTest {

    public static final String TEST_BANK_NAME = "testBankName";
    public static final String TEST_ACCOUNT_NO = "111ABC";
    public static final String TEST_ACCOUNT_HOLDER_NAME = "testAccountHolderName";
    public static final int TEST_INITIAL_BALANCE = 1500;
    private ExpenseManager expenseManager;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        expenseManager = new PersistentExpenseManager(context);
    }

    @Test
    public void testAddAccount() {
        expenseManager.addAccount(TEST_ACCOUNT_NO, TEST_BANK_NAME, TEST_ACCOUNT_HOLDER_NAME, TEST_INITIAL_BALANCE);
        List<String> accNumList = expenseManager.getAccountNumbersList();
        assertTrue(accNumList.contains(TEST_ACCOUNT_NO));
    }

    @Test
    public void testLogTransaction() {
        expenseManager.addAccount(TEST_ACCOUNT_NO, TEST_BANK_NAME, TEST_ACCOUNT_HOLDER_NAME, TEST_INITIAL_BALANCE);
        int beforeSize = expenseManager.getTransactionLogs().size();
        expenseManager.getTransactionsDAO().logTransaction(new Date(), TEST_ACCOUNT_NO, ExpenseType.EXPENSE, 2500);
        int afterSize = expenseManager.getTransactionLogs().size();
        assertTrue(afterSize == beforeSize + 1);
    }
}