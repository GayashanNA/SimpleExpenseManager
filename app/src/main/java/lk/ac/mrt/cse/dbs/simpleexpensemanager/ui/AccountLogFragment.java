package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;
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



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.EXPENSE_MANAGER;

import androidx.fragment.app.Fragment;

/**
 *
 */
public class AccountLogFragment extends Fragment {
    private ExpenseManager currentExpenseManager;

    public static AccountLogFragment newInstance(ExpenseManager expenseManager) {
        AccountLogFragment accountLogFragment = new AccountLogFragment(expenseManager);
//        Bundle args = new Bundle();
//        args.putSerializable(EXPENSE_MANAGER, expenseManager);
//        expenseLogsFragment.setArguments(args);
        return accountLogFragment;
    }

    public AccountLogFragment(ExpenseManager expenseManager) {
        this.currentExpenseManager=expenseManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_log, container, false);
        TableLayout logsTableLayout = (TableLayout) rootView.findViewById(R.id.logs_table);
        TableRow tableRowHeader = (TableRow) rootView.findViewById(R.id.logs_table_header);

//        currentExpenseManager = (ExpenseManager) getArguments().get(EXPENSE_MANAGER);
        List<Account> accountList = new ArrayList<>();
        if (currentExpenseManager != null) {
            accountList = currentExpenseManager.getAccountList();
        }
        generateTransactionsTable(rootView, logsTableLayout, accountList);
        return rootView;
    }

    private void generateTransactionsTable(View rootView, TableLayout logsTableLayout,
                                           List<Account> accountList) {
        for (Account account : accountList) {
            TableRow tr = new TableRow(rootView.getContext());
            TextView lDateVal = new TextView(rootView.getContext());

            TextView lAccountNoVal = new TextView(rootView.getContext());
            lAccountNoVal.setText(account.getAccountNo());
            tr.addView(lAccountNoVal);


            TextView lAmountVal = new TextView(rootView.getContext());
            lAmountVal.setText(String.valueOf(account.getBalance()));
            tr.addView(lAmountVal);

            logsTableLayout.addView(tr);
        }
    }
}
