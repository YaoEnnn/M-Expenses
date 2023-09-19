package hongdieuan.m_expense.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hongdieuan.m_expense.MainActivity;
import hongdieuan.m_expense.R;
import hongdieuan.m_expense.ListTrip.Trip;
import hongdieuan.m_expense.ListTrip.TripList;

public class FragmentAdd extends Fragment {

    protected EditText etTripName, etDestination, etNumberParticipant, etDate, etDuration, etDescription;
    protected Switch switchAssesment;
    protected Button btnSubmit;
    protected ImageView datePicker;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        etTripName          = view.findViewById(R.id.editTextTripName);
        etDestination       = view.findViewById(R.id.editTextDestination);
        etNumberParticipant = view.findViewById(R.id.editTextNumberParticipant);
        etDate              = view.findViewById(R.id.editTextDate);
        etDuration          = view.findViewById(R.id.editTextDuration);
        etDescription       = view.findViewById(R.id.editTextDescription);
        switchAssesment     = view.findViewById(R.id.switchAssesment);
        btnSubmit           = view.findViewById(R.id.btn_Submit);
        datePicker          = view.findViewById(R.id.datePicker);


        datePicker.setOnClickListener(view2 -> datePicker());
        btnSubmit.setOnClickListener(view1 -> submitEvent());

        return view;
    }

    //Event for Button with some Validation
    protected void submitEvent(){
        if(etTripName.getText().toString().isEmpty()){
            Snackbar.make(getView(), "Trip's Name is Empty", Snackbar.LENGTH_SHORT).show();
        } else if(etDestination.getText().toString().isEmpty()){
            Snackbar.make(getView(), "Destination is Empty", Snackbar.LENGTH_SHORT).show();
        } else if(etDate.getText().toString().isEmpty()){
            Snackbar.make(getView(), "Start Day is Empty", Snackbar.LENGTH_SHORT).show();
        } else if(etNumberParticipant.getText().toString().isEmpty()){
            Snackbar.make(getView(), "Number of Participants is Empty", Snackbar.LENGTH_SHORT).show();
        } else if(Integer.parseInt(etNumberParticipant.getText().toString()) <= 0){
            Snackbar.make(getView(), "Unvalid Number of Participants", Snackbar.LENGTH_SHORT).show();
        } else if(etDuration.getText().toString().isEmpty()){
            Snackbar.make(getView(), "Duration is Empty", Snackbar.LENGTH_SHORT).show();
        } else if(Integer.parseInt(etDuration.getText().toString()) <= 0) {
            Snackbar.make(getView(), "Unvalid Duration", Snackbar.LENGTH_SHORT).show();
        }
          else {
            diaLogSubmit();
        }
    }

    // Dialog Create for Confirming Info
    protected void diaLogSubmit(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_submit_custom);

        TextView tripName = dialog.findViewById(R.id.tripNameDialog);
        TextView destination = dialog.findViewById(R.id.destinationDialog);
        TextView duration = dialog.findViewById(R.id.durationDialog);
        TextView startDay = dialog.findViewById(R.id.starDayDialog);
        TextView numberPeople = dialog.findViewById(R.id.numberPeopleDialog);
        TextView description = dialog.findViewById(R.id.descriptionDialog);
        Button edit = dialog.findViewById(R.id.btnEdit);
        Button submit = dialog.findViewById(R.id.btnsubmit);

        // set text for Dialog TextView by user entered information
        tripName.setText(etTripName.getText().toString());
        destination.setText(etDestination.getText().toString());
        startDay.setText(etDate.getText().toString());

        if(etDuration.getText().toString() == "1"){
            duration.setText("1 day");
        } else {
            duration.setText(etDuration.getText().toString() + " days");
        }

        if(etNumberParticipant.getText().toString() == "1"){
            numberPeople.setText("1 person");
        } else if(etNumberParticipant.getText().toString().isEmpty()){
            numberPeople.setText("N/A");
        } else {
            numberPeople.setText(etNumberParticipant.getText().toString() + " persons");
        }

        if(etDescription.getText().toString().isEmpty()){
            description.setText("N/A");
        } else {
            description.setText(etDescription.getText().toString());
        }

        //Event for Cancel Button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //Event for Submit Button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check risk first
                boolean risk = switchAssesment.isChecked();
                Trip trip = new Trip(etTripName.getText().toString(), etDestination.getText().toString(),
                        etDate.getText().toString(), etDescription.getText().toString(),
                        Integer.parseInt(etDuration.getText().toString()),
                        Integer.parseInt(etNumberParticipant.getText().toString()), risk);

                //add trip to list
                TripList.list.add(trip);

                //put Data to Database (Do Function InsertData from Main)
                if(risk == true){
                    ((MainActivity)getActivity()).InsertData(etTripName.getText().toString(), etDestination.getText().toString(),
                            etDate.getText().toString(), Integer.parseInt(etDuration.getText().toString()), Integer.parseInt(etNumberParticipant.getText().toString()),
                                    etDescription.getText().toString(), "true");
                } else {
                    ((MainActivity)getActivity()).InsertData(etTripName.getText().toString(), etDestination.getText().toString(),
                            etDate.getText().toString(), Integer.parseInt(etDuration.getText().toString()), Integer.parseInt(etNumberParticipant.getText().toString()),
                            etDescription.getText().toString(), null);
                }

                dialog.dismiss();
                Snackbar.make(getView(), "Trip Inserted Successfully", Snackbar.LENGTH_LONG).show();
                ((MainActivity)getActivity()).replaceFragment(new FragmentList(), R.id.menu_list);
            }
        });

        dialog.show();
    }

    protected void datePicker(){
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog dateDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                etDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, date);
        dateDialog.show();
    }
}