package hongdieuan.m_expense.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hongdieuan.m_expense.MainActivity;
import hongdieuan.m_expense.R;

public class FragmentHome extends Fragment {

    CardView cardViewAdd, cardViewList, cardViewHotline, cardViewCloud, cardViewAbout, cardViewReset;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        cardViewAdd     = view.findViewById(R.id.cardViewAdd);
        cardViewList    = view.findViewById(R.id.cardViewList);
        cardViewHotline = view.findViewById(R.id.cardViewHotline);
        cardViewCloud   = view.findViewById(R.id.cardViewCloud);
        cardViewAbout   = view.findViewById(R.id.cardViewAbout);
        cardViewReset   = view.findViewById(R.id.cardViewReset);

        //Event On Click For Each CardView

        cardViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new FragmentAdd(), R.id.menu_add);
            }
        });

        cardViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new FragmentList(), R.id.menu_list);
            }
        });

        cardViewAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new FragmentAbout(), R.id.menu_about);
            }
        });

        cardViewHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).hotLineDiaglog();
            }
        });

        cardViewCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).UploadTripToCloud();
                ((MainActivity)getActivity()).UploadExpenseToCloud();
            }
        });

        cardViewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).ResetDialog();
            }
        });
        
        return view;
    }
}