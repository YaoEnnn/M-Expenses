package hongdieuan.m_expense.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hongdieuan.m_expense.Database.ExpenseDBEntry;
import hongdieuan.m_expense.MainActivity;
import hongdieuan.m_expense.R;
import hongdieuan.m_expense.ListExpense.Expense;
import hongdieuan.m_expense.ListExpense.ExpenseAdapter;
import hongdieuan.m_expense.ListTrip.Trip;
import hongdieuan.m_expense.ListTrip.TripList;

public class FragmentDetailTrip extends Fragment {

    TextView tvTripName, tvDestination, tvDate, tvDuration, tvNumberPeople, tvDescription, tvRisk;
    Button btnEditDetail, btnDeleteDetail, btnAddExpense;
    Trip trip;
    int position = -1;
    public List<Expense> listExpense = new ArrayList<Expense>();
    RecyclerView recyclerViewExpense;
    ExpenseAdapter adapter;
    EditText etTime;
    int tripId;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_trip, container, false);

        tvTripName          = view.findViewById(R.id.tvTripName);
        tvDestination       = view.findViewById(R.id.tvDestination);
        tvDate              = view.findViewById(R.id.tvDate);
        tvDuration          = view.findViewById(R.id.tvDuration);
        tvNumberPeople      = view.findViewById(R.id.tvNumberPeople);
        tvDescription       = view.findViewById(R.id.tvDescription);
        tvRisk              = view.findViewById(R.id.tvRisk);
        btnEditDetail       = view.findViewById(R.id.btnEditDetail);
        btnDeleteDetail     = view.findViewById(R.id.btnDeleteDetail);
        recyclerViewExpense = view.findViewById(R.id.recyclerViewExpense);
        btnAddExpense       = view.findViewById(R.id.btnAddExpense);

        //Event for 3 button
        btnEditDetail.setOnClickListener(view1 -> EditDialog());
        btnDeleteDetail.setOnClickListener(view1 -> DeleteTrip());
        btnAddExpense.setOnClickListener(view1 -> AddExpense());

        if(getArguments() != null){
            trip = (Trip) getArguments().getSerializable("object_trip");
            position = TripList.list.indexOf(trip);
        }

        SetTextForDetail(trip);

        //Get Trip Id
        tripId = ((MainActivity)getActivity()).GetIDTrip(tvTripName.getText().toString(), tvDestination.getText().toString(),
                tvDate.getText().toString());

        //Add Expense From DB
        ((MainActivity)getActivity()).AddExpenseFromDB(tripId, listExpense);

        SetUpRecyclerViewExpense();

        return view;
    }

    protected void SetTextForDetail(Trip trip){
            tvTripName.setText(trip.getTripName());
            tvDestination.setText(trip.getDestination());
            tvDate.setText(trip.getDate());

            if(trip.getDescription().isEmpty()){
                tvDescription.setText("N/A");
            } else {
                tvDescription.setText(trip.getDescription());
            }

            if (trip.getDuration() == 1){
                tvDuration.setText("1 day");
            } else {
                tvDuration.setText(trip.getDuration() + " days");
            }

            if (trip.getNumberPeople() == 1){
                tvNumberPeople.setText("1 person");
            } else {
                tvNumberPeople.setText(trip.getNumberPeople() + " persons");
            }

            if (trip.isRisk() == true){
                tvRisk.setText("Yes");
            } else {
                tvRisk.setText("No");
            }
        }

    protected void EditDialog(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_trip_edit);

        EditText tripname       = dialog.findViewById(R.id.tripNameDialogEdit);
        EditText destination    = dialog.findViewById(R.id.destinationDialogEdit);
        EditText date           = dialog.findViewById(R.id.dateDialogEdit);
        EditText duration       = dialog.findViewById(R.id.durationDialogEdit);
        EditText numberpeople   = dialog.findViewById(R.id.numberPeopleDialogEdit);
        EditText description    = dialog.findViewById(R.id.descriptionDialogEdit);
        CheckBox checkbox       = dialog.findViewById(R.id.checkboxDialogEdit);
        Button btnCancel        = dialog.findViewById(R.id.btnCancelDialogEdit);
        Button btnSave          = dialog.findViewById(R.id.btnSaveDialogEdit);

        //setText to EditText in Dialog using old infomation
        tripname.setText(tvTripName.getText().toString());
        destination.setText(tvDestination.getText().toString());
        date.setText(tvDate.getText().toString());
        duration.setText("" + TripList.list.get(position).getDuration());
        numberpeople.setText("" + TripList.list.get(position).getNumberPeople());
        description.setText(tvDescription.getText().toString());

        if(tvRisk.getText().toString() == "Yes"){
            checkbox.setChecked(true);
        } else {
            checkbox.setChecked(false);
        }

        //button cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //button save edit Trip info
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean i;
                //Update Data By Using Function in MainActivity
                if(checkbox.isChecked()){
                    i = true;
                    ((MainActivity)getActivity()).UpdateData(tripname.getText().toString(),
                            destination.getText().toString(),
                            date.getText().toString(),
                            Integer.parseInt(duration.getText().toString()),
                            Integer.parseInt(numberpeople.getText().toString()),
                            description.getText().toString(), "true", String.valueOf(tripId));
                } else {
                    i = false;
                    ((MainActivity)getActivity()).UpdateData(tripname.getText().toString(),
                            destination.getText().toString(),
                            date.getText().toString(),
                            Integer.parseInt(duration.getText().toString()),
                            Integer.parseInt(numberpeople.getText().toString()),
                            description.getText().toString(), null, String.valueOf(tripId));
                }

                //Update TripList
                Trip trip_edited = new Trip(tripname.getText().toString(),
                        destination.getText().toString(),
                        date.getText().toString(),
                        description.getText().toString(),
                        Integer.parseInt(duration.getText().toString()),
                        Integer.parseInt(numberpeople.getText().toString()), i);

                TripList.list.set(position, trip_edited);

                SetTextForDetail(trip_edited);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    protected void DeleteTrip(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_delete_trip_custom);

        Button cancel = dialog.findViewById(R.id.btnCancelDialogDeleteTrip);
        Button delete = dialog.findViewById(R.id.btnDeleteDialogDeleteTrip);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove from list
                TripList.list.remove(position);

                //remove from database
                ((MainActivity)getActivity()).DeleteData(String.valueOf(tripId));

                //remove all expense of this database
                String where = ExpenseDBEntry.COL_FK_ID_TRIP + "= ?";
                String[] args = {"" + tripId};
                ((MainActivity)getActivity()).dbread.delete(ExpenseDBEntry.TABLE_NAME, where, args);

                ((MainActivity)getActivity()).replaceFragment(new FragmentList(), R.id.menu_list);
                dialog.dismiss();
                Snackbar.make(getView(), "Trip Deleted", Snackbar.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }

    protected void SetUpRecyclerViewExpense(){
        adapter = new ExpenseAdapter(listExpense, ((MainActivity)getActivity()));
        recyclerViewExpense.setAdapter(adapter);
        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void AddExpense(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_expense);

        ImageButton datepicker;
        Button cancel       = dialog.findViewById(R.id.btnCancelDialogAddExpense);
        Button insert       = dialog.findViewById(R.id.btnInsertDialogAddExpense);
        etTime              = dialog.findViewById(R.id.etTime);
        EditText etType     = dialog.findViewById(R.id.etType);
        EditText etAmount   = dialog.findViewById(R.id.etAmount);
        EditText etComment  = dialog.findViewById(R.id.etComment);

        datepicker = dialog.findViewById(R.id.datePickerExpense);

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etType.getText().toString().isEmpty()){

                    Toast.makeText(getContext(), "Type Could Not Be Empty!", Toast.LENGTH_SHORT).show();

                } else if(etAmount.getText().toString().isEmpty()){

                    Toast.makeText(getContext(), "Amount Could Not Be Empty!", Toast.LENGTH_SHORT).show();

                } else if(etTime.getText().toString().isEmpty()){

                    Toast.makeText(getContext(), "Time Could Not Be Empty!", Toast.LENGTH_SHORT).show();

                } else if(etComment.getText().toString().isEmpty()){

                    Toast.makeText(getContext(), "Comment Could Not Be Empty!", Toast.LENGTH_SHORT).show();

                } else {

                    //Add Expense to List
                    listExpense.add(new Expense(etType.getText().toString(), etComment.getText().toString(),
                            etTime.getText().toString(),
                            etAmount.getText().toString()));

                    //Add Expense to DataBase (Using function from Main)
                    ((MainActivity)getActivity()).InsertExpenseData(etType.getText().toString(), etAmount.getText().toString(),
                            etTime.getText().toString(), etComment.getText().toString(), tripId);

                    Snackbar.make(getView(), "Expense Inserted Successfully!", Snackbar.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
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
                etTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, date);
        dateDialog.show();
    }
}