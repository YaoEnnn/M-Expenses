package hongdieuan.m_expense.ListTrip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import hongdieuan.m_expense.R;
import hongdieuan.m_expense.Fragment.FragmentDetailTrip;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private List<Trip> list;
    private List<Trip> listOld;

    public TripAdapter(List<Trip> list) {
        this.list = list;
        this.listOld = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_info_line, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = list.get(position);
        holder.tripName.setText(trip.getTripName());
        holder.date.setText(trip.getDate());
        holder.destination.setText(trip.getDestination());

        if(trip.isRisk() == true){
            holder.risk.setText("Yes");
        } else {
            holder.risk.setText("No");
        }

        holder.constraintLayoutTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Fragment fragment = new FragmentDetailTrip();

                //put Trip Object into Bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_trip", trip);

                fragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.fragmentContainerView, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout constraintLayoutTrip;
        TextView tripName, date, risk, destination;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayoutTrip = itemView.findViewById(R.id.constraintLayoutTrip);
            tripName = itemView.findViewById(R.id.tripNameViewHolder);
            date = itemView.findViewById(R.id.dateViewHolder);
            risk = itemView.findViewById(R.id.riskViewHolder);
            destination = itemView.findViewById(R.id.destinationViewHolder);

        }
    }

    public void setFilterList(List<Trip> filterList){
        this.list = filterList;
        notifyDataSetChanged();
    }
}
