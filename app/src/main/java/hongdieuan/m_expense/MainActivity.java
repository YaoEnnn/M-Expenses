package hongdieuan.m_expense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hongdieuan.m_expense.Database.ExpenseDBEntry;
import hongdieuan.m_expense.Database.TripDBHelper;
import hongdieuan.m_expense.Database.TripDBEntry;
import hongdieuan.m_expense.Fragment.FragmentAbout;
import hongdieuan.m_expense.Fragment.FragmentAdd;
import hongdieuan.m_expense.Fragment.FragmentHome;
import hongdieuan.m_expense.Fragment.FragmentList;
import hongdieuan.m_expense.ListExpense.Expense;
import hongdieuan.m_expense.ListTrip.Trip;
import hongdieuan.m_expense.ListTrip.TripList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected static final int fragmentHome = 0;
    protected static final int fragmentAdd = 1;
    protected static final int fragmentAbout = 2;
    protected static final int fragmentList = 3;
    protected int currentFragment = fragmentHome;

    protected DrawerLayout mDrawerLayout;
    protected Toolbar mToolbar;
    public NavigationView navigationView;

    public TripDBHelper tripDBHelper = new TripDBHelper(this, "M_Expense", 1);
    public SQLiteDatabase dbread, dbwrite;

    protected FirebaseFirestore fireBase = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbread = tripDBHelper.getReadableDatabase();
        dbwrite = tripDBHelper.getWritableDatabase();

        setUpToolBarAndNAV();

        //set selected item on menu
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //Add Fragment Home Firstly
//        replaceFragment(new FragmentHome(), R.id.menu_home);
        navigationView.getMenu().findItem(R.id.menu_home).setChecked(true);

        AddTripFromDB();
    }

    //Set Up ToolBar
    protected void setUpToolBarAndNAV() {
        mDrawerLayout = findViewById(R.id.mDrawer_Layout);
        mToolbar = findViewById(R.id.mToolbar);

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.open_menu, R.string.close_menu);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Set Up Menu Option Selection
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_home) {

            replaceFragment(new FragmentHome(), id);
            currentFragment = fragmentHome;

        } else if (id == R.id.menu_add) {

            //if(currentFragment != fragmentAdd){
            replaceFragment(new FragmentAdd(), id);
            currentFragment = fragmentAdd;
            navigationView.setCheckedItem(id);
            //}

        } else if (id == R.id.menu_list) {

            replaceFragment(new FragmentList(), id);
            currentFragment = fragmentList;

        } else if (id == R.id.menu_about) {

            if (currentFragment != fragmentAbout) {
                replaceFragment(new FragmentAbout(), id);
                currentFragment = fragmentAbout;
            }

        } else if (id == R.id.menu_reset) {

            ResetDialog();

        } else if (id == R.id.menu_hotline) {

            hotLineDiaglog();

        } else if (id == R.id.menu_cloud) {

            UploadTripToCloud();
            UploadExpenseToCloud();

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void replaceFragment(Fragment fragment, int id) {
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, fragment).commit();
        navigationView.setCheckedItem(id);
    }

    //Custom Dialog for Hotline Event
    public void hotLineDiaglog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_hotline_custom);

        Button btn_call = dialog.findViewById(R.id.btnDeleteDialogDeleteTrip);
        Button btn_cancel = dialog.findViewById(R.id.btnCancelDialogDeleteTrip);

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Snackbar.make(mDrawerLayout, "Calling...", Snackbar.LENGTH_LONG).show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Custom Dialog for Reset
    public void ResetDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_reset_custom);

        Button cancel = dialog.findViewById(R.id.btnCancelDialogReset);
        Button confirm = dialog.findViewById(R.id.btnConfirmDialogReset);
        CheckBox checkbox = dialog.findViewById(R.id.checkBoxResetDialog);

        checkbox.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            //set Enable and Background for Confirm Button
            public void onClick(View view) {
                if(checkbox.isChecked()){
                    confirm.setEnabled(true);
                    confirm.setBackgroundColor(getResources().getColor(R.color.button_enable));
                } else {
                    confirm.setEnabled(false);
                    confirm.setBackgroundColor(getResources().getColor(R.color.button_disable));
                }
            }
        });

        //cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //confirm reset data button
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete all data in Database
                long i = dbread.delete(TripDBEntry.TABLE_NAME, null, null);
                dbread.delete(ExpenseDBEntry.TABLE_NAME, null, null);

                //Remove all trip from list
                TripList.list.clear();

                if (i > 0) {
                    Snackbar.make(mDrawerLayout, "Data Reset Successfully", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(mDrawerLayout, "Data Is Currently Empty", Snackbar.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                replaceFragment(new FragmentHome(), R.id.menu_home);

            }
        });
        dialog.show();
    }

    //Insert Trip Data to Database
    public void InsertData(String tripName, String destination, String date,
                           int duration, int people, String description, String risk) {
        ContentValues values = new ContentValues();
        values.put(TripDBEntry.COL_NAME, tripName);
        values.put(TripDBEntry.COL_DESTINATION, destination);
        values.put(TripDBEntry.COL_DATE, date);
        values.put(TripDBEntry.COL_DESCRIPTION, description);
        values.put(TripDBEntry.COL_RISK, risk);
        values.put(TripDBEntry.COL_DURATION, duration);
        values.put(TripDBEntry.COL_NUMBERPEOPLE, people);

        dbwrite.insert(TripDBEntry.TABLE_NAME, null, values);
    }

    //Function Delete Trip DB
    public void DeleteData(String id) {
        //Delete URL in Database
        String where = TripDBEntry.COL_ID + " = ?";
        String[] whereArgs = {id};
        dbread.delete(TripDBEntry.TABLE_NAME, where, whereArgs);
    }

    //Function Update Trip Database
    public void UpdateData(String newName, String newDestination, String newDate, int duration,
                           int people, String description, String risk, String id){
        ContentValues values = new ContentValues();
        values.put(TripDBEntry.COL_NAME, newName);
        values.put(TripDBEntry.COL_DESTINATION, newDestination);
        values.put(TripDBEntry.COL_DATE, newDate);
        values.put(TripDBEntry.COL_DURATION, duration);
        values.put(TripDBEntry.COL_NUMBERPEOPLE, people);
        values.put(TripDBEntry.COL_DESCRIPTION, description);
        values.put(TripDBEntry.COL_RISK, risk);

        String where = TripDBEntry.COL_ID + " = ?";
        String[] args = {id};

        dbwrite.update(TripDBEntry.TABLE_NAME, values, where, args);
    }

    //Function Add Trip From Database Using Select Query
    public void AddTripFromDB(){
        Cursor cursor = dbread.query(TripDBEntry.TABLE_NAME, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TripDBEntry.COL_NAME));
            String destination = cursor.getString(cursor.getColumnIndexOrThrow(TripDBEntry.COL_DESTINATION));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(TripDBEntry.COL_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TripDBEntry.COL_DATE));
            String risk = cursor.getString(cursor.getColumnIndexOrThrow(TripDBEntry.COL_RISK));
            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(TripDBEntry.COL_DURATION));
            int people = cursor.getInt(cursor.getColumnIndexOrThrow(TripDBEntry.COL_NUMBERPEOPLE));

            if (risk != null) {
                TripList.list.add(new Trip(name, destination, date, description, duration, people, true));
            } else {
                TripList.list.add(new Trip(name, destination, date, description, duration, people, false));
            }
        }
    }

    //Get id Trip in Database
    public int GetIDTrip(String name, String destination, String date) {
        String selection = TripDBEntry.COL_NAME + " = ? AND " + TripDBEntry.COL_DESTINATION + " = ? AND " +
                TripDBEntry.COL_DATE + " = ?";
        String[] args = {name, destination, date};
        int id = 0;

        Cursor cursor = dbread.query(TripDBEntry.TABLE_NAME,null,selection,args,null,
                null, null);
        
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(TripDBEntry.COL_ID));
        }
        return id;
    }

    //Insert Data To Expense Database
    public void InsertExpenseData(String type, String amount, String time, String comment, int fkId){
        ContentValues values = new ContentValues();
        values.put(ExpenseDBEntry.COL_TYPE, type);
        values.put(ExpenseDBEntry.COL_AMOUNT, amount);
        values.put(ExpenseDBEntry.COL_TIME, time);
        values.put(ExpenseDBEntry.COL_COMMENT, comment);
        values.put(ExpenseDBEntry.COL_FK_ID_TRIP, fkId);

        dbwrite.insert(ExpenseDBEntry.TABLE_NAME, null, values);
    }

    //Add Expense to List from DB
    public void AddExpenseFromDB(int tripId, List<Expense> list){
        String selection = ExpenseDBEntry.COL_FK_ID_TRIP + " = ?";
        String[] args = {"" + tripId};

        Cursor cursor = dbread.query(ExpenseDBEntry.TABLE_NAME, null, selection, args,
                null, null, null);
        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBEntry.COL_TYPE));
            String amount = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBEntry.COL_AMOUNT));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBEntry.COL_TIME));
            String comment = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBEntry.COL_COMMENT));

            list.add(new Expense(type, comment, time, amount));
        }
    }

    public void DeleteExpenseDB(String type, String amount, String time, String comment) {
        //Delete URL in Database
        String where = ExpenseDBEntry.COL_TYPE + " = ? AND " + ExpenseDBEntry.COL_AMOUNT + " = ? AND " +
                ExpenseDBEntry.COL_TIME + " = ? AND " + ExpenseDBEntry.COL_COMMENT + " = ?";
        String[] whereArgs = {type, amount, time, comment};
        dbread.delete(ExpenseDBEntry.TABLE_NAME, where, whereArgs);
    }

    public void UploadTripToCloud() {
        Cursor cursor = dbread.query(TripDBEntry.TABLE_NAME, null, null, null,
                null, null, null);

        int id = 0;
        String name = null;
        String destination = null;
        String description = null;
        String risk = null;
        String date = null;
        int duration = 0;
        int people = 0;

        while (cursor.moveToNext()) {
             id = cursor.getInt(cursor.getColumnIndexOrThrow(TripDBEntry.COL_ID));
             name = cursor.getString(cursor.getColumnIndexOrThrow(TripDBEntry.COL_NAME));
             destination = cursor.getString(cursor.getColumnIndexOrThrow(TripDBEntry.COL_DESTINATION));
             description = cursor.getString(cursor.getColumnIndexOrThrow(TripDBEntry.COL_DESCRIPTION));
             risk = cursor.getString(cursor.getColumnIndexOrThrow(TripDBEntry.COL_RISK));
             date = cursor.getString(cursor.getColumnIndexOrThrow(TripDBEntry.COL_DATE));
             duration = cursor.getInt(cursor.getColumnIndexOrThrow(TripDBEntry.COL_DURATION));
             people = cursor.getInt(cursor.getColumnIndexOrThrow(TripDBEntry.COL_NUMBERPEOPLE));

            if(id != 0) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", id);
                data.put("name", name);
                data.put("description", description);
                data.put("risk", risk);
                data.put("destination", destination);
                data.put("date", date);
                data.put("duration", duration);
                data.put("people", people);
                fireBase.collection("trip").add(data);
            }
        }
        if(id == 0){
            Snackbar.make(mDrawerLayout, "No Data Found! Please Add New Trip", Snackbar.LENGTH_SHORT).show();
        } else { Snackbar.make(mDrawerLayout, "All Data BackUp Successfully", Snackbar.LENGTH_SHORT).show(); }
    }

    public void UploadExpenseToCloud(){
        Cursor cursor = dbread.query(ExpenseDBEntry.TABLE_NAME, null, null, null,
                null, null, null);

        int id = 0;
        int trip_id = 0;
        String type = null;
        String amount = null;
        String time = null;
        String comment = null;

        while (cursor.moveToNext()) {
            type = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBEntry.COL_TYPE));
            amount = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBEntry.COL_AMOUNT));
            time = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBEntry.COL_TIME));
            comment = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseDBEntry.COL_COMMENT));
            id = cursor.getInt(cursor.getColumnIndexOrThrow(ExpenseDBEntry.COL_ID));
            trip_id = cursor.getInt(cursor.getColumnIndexOrThrow(ExpenseDBEntry.COL_FK_ID_TRIP));

            if(id != 0){
                Map<String, Object> data = new HashMap<>();
                data.put("expenseType", type);
                data.put("amount", amount);
                data.put("time", time);
                data.put("expenseComment", comment);
                data.put("id", id);
                data.put("trip_id", trip_id);

                fireBase.collection("expense").add(data);

            }
        }
    }
}