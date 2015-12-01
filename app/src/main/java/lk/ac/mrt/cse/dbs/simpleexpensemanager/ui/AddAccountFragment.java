package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;

/**
 *
 */
public class AddAccountFragment extends Fragment {
    public static AddAccountFragment newInstance() {
        return new AddAccountFragment();
    }

    public AddAccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_account, container, false);
    }
}
