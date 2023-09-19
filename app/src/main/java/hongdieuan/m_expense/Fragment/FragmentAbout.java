package hongdieuan.m_expense.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import hongdieuan.m_expense.MainActivity;
import hongdieuan.m_expense.R;

public class FragmentAbout extends Fragment {

    Button btn_about_fragment;
    private Context MainActivity;
    EditText editTextName, editTextPhone, editTextEmail;
    TextInputEditText editTextMessage;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about, container, false);

        btn_about_fragment = view.findViewById(R.id.btn_about_fragment);
        editTextName       = view.findViewById(R.id.editTextNameAboutFragment);
        editTextPhone      = view.findViewById(R.id.editTextPhoneAboutFragment);
        editTextEmail      = view.findViewById(R.id.editTextTextEmailAboutFragment);
        editTextMessage    = view.findViewById(R.id.editTextMessageAboutFragment);

        checkEmptyEditText();

        return view;
    }

    //Button Event Check Empty Field
    protected void checkEmptyEditText(){
        btn_about_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextName.getText().toString().isEmpty()){

                    Snackbar.make(getView(), "Name Could Not Be Empty!", Snackbar.LENGTH_LONG).show();

                }   else if(editTextPhone.getText().toString().isEmpty()){

                    Snackbar.make(getView(), "Phone Could Not Be Empty!", Snackbar.LENGTH_LONG).show();

                }   else if(editTextEmail.getText().toString().isEmpty()){

                    Snackbar.make(getView(), "Email Could Not Be Empty!", Snackbar.LENGTH_LONG).show();

                }   else if(editTextMessage.getText().toString().isEmpty()){

                    Snackbar.make(getView(), "Message Could Not Be Empty!", Snackbar.LENGTH_LONG).show();
                } else {

                   getParentFragmentManager().beginTransaction().setReorderingAllowed(true)
                                    .replace(R.id.fragmentContainerView, new FragmentHome()).commit();
                    ((MainActivity)getActivity()).navigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                    Snackbar.make(getView(), "MESSAGE SENT!", Snackbar.LENGTH_LONG).show();


                }
            }
        });
    }
}