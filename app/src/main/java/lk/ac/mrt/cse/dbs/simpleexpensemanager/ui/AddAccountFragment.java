package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;

/**
 *
 */
public class AddAccountFragment extends Fragment implements View.OnClickListener {
    private ExpenseManager currentExpenseManager;
    private EditText accountNumber;
    private EditText bankName;
    private EditText accountHolderName;
    private EditText initialBalance;
    private Button addAccount;

    public static AddAccountFragment newInstance(ExpenseManager expenseManager) {
        AddAccountFragment addAccountFragment = new AddAccountFragment();
        Bundle args = new Bundle();
        args.putSerializable("expense-manager", expenseManager);
        addAccountFragment.setArguments(args);
        return addAccountFragment;
    }

    public AddAccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_account, container, false);
        accountNumber = (EditText) rootView.findViewById(R.id.account_num);
        bankName = (EditText) rootView.findViewById(R.id.bank_name);
        accountHolderName = (EditText) rootView.findViewById(R.id.account_holder_name);
        initialBalance = (EditText) rootView.findViewById(R.id.initial_balance);
        addAccount = (Button) rootView.findViewById(R.id.add_account);
        addAccount.setOnClickListener(this);

        currentExpenseManager = (ExpenseManager) getArguments().get("expense-manager");
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_account:
                String accountNumStr = accountNumber.getText().toString();
                String bankNameStr = bankName.getText().toString();
                String accountHolderStr = accountHolderName.getText().toString();
                String initialBalanceStr = initialBalance.getText().toString();


                if (accountNumStr.isEmpty()) {
                    accountNumber.setError("Account Number cannot be empty!");
                    break;
                }

                if (bankNameStr.isEmpty()) {
                    bankName.setError("Bank Name cannot be empty!");
                    break;
                }

                if (accountHolderStr.isEmpty()) {
                    accountHolderName.setError("Account Holder's Name cannot be empty!");
                    break;
                }

                if (initialBalanceStr.isEmpty()) {
                    initialBalance.setError("Initial balance cannot be empty!");
                    break;
                }

                if (currentExpenseManager != null) {
                    currentExpenseManager.addAccount(accountNumStr, bankNameStr, accountHolderStr,
                            Double.parseDouble(initialBalanceStr));
                }
                cleanUp();
                break;
        }
    }

    private void cleanUp() {
        accountNumber.getText().clear();
        bankName.getText().clear();
        accountHolderName.getText().clear();
        initialBalance.getText().clear();
    }
}
