package hongdieuan.m_expense.Fragment;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import hongdieuan.m_expense.ListTrip.Trip;
import hongdieuan.m_expense.R;
import hongdieuan.m_expense.ListTrip.TripAdapter;
import hongdieuan.m_expense.ListTrip.TripList;

public class FragmentList extends Fragment {

    RecyclerView recyclerviewTrip;
    TripAdapter adapter;
    SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

            recyclerviewTrip = view.findViewById(R.id.recyclerviewTrip);
            searchView       = view.findViewById(R.id.searchView);
            searchView.clearFocus();

            //set adapter for RecyclerView
            adapter = new TripAdapter(TripList.list);
            recyclerviewTrip.setAdapter(adapter);
            recyclerviewTrip.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter.notifyDataSetChanged();

            //SearchView Setting
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterList(newText);
                    return true;
                }

                private void filterList(String text) {
                    List<Trip> filterList = new ArrayList<>();
                    for (Trip trip : TripList.list) {
                        if(trip.getTripName().toLowerCase().contains(text.toLowerCase())){
                            filterList.add(trip);
                        }
                    }

                    if(filterList.isEmpty()){
                        Snackbar.make(getView(), "No Trip Found", Snackbar.LENGTH_SHORT).show();
                    } else {
                        adapter.setFilterList(filterList);
                    }
                }
            });


        return view;
    }
}