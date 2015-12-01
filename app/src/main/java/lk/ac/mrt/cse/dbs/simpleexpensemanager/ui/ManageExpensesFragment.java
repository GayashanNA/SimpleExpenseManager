package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;

/**
 *
 */
public class ManageExpensesFragment extends Fragment implements View.OnClickListener {
    private Button submitButton;
    private EditText amount;
    private Spinner accountSelector;
    private RadioGroup expenseTypeGroup;
    private RadioButton expenseType;
    private RadioButton incomeType;
    private DatePicker datePicker;

    public static ManageExpensesFragment newInstance() {
        return new ManageExpensesFragment();
    }

    public ManageExpensesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_expenses, container, false);
        submitButton = (Button) rootView.findViewById(R.id.submit_amount);
        submitButton.setOnClickListener(this);

        amount = (EditText) rootView.findViewById(R.id.amount);
        accountSelector = (Spinner) rootView.findViewById(R.id.account_selector);
        expenseTypeGroup = (RadioGroup) rootView.findViewById(R.id.expense_type_group);
        expenseType = (RadioButton) rootView.findViewById(R.id.expense);
        incomeType = (RadioButton) rootView.findViewById(R.id.income);
        datePicker = (DatePicker) rootView.findViewById(R.id.date_selector);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_amount:
                String selectedAccount = (String) accountSelector.getSelectedItem();
                String amountStr = amount.getText().toString();
                RadioButton checkedType = (RadioButton) getActivity().findViewById(expenseTypeGroup
                        .getCheckedRadioButtonId());
                String type = (String) checkedType.getText();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = sdf.format(calendar.getTime());

                if (amountStr.isEmpty()) {
                    amount.setError("Amount is required.");
                }
                break;
        }
    }
}
