package hongdieuan.m_expense.ListExpense;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import hongdieuan.m_expense.MainActivity;
import hongdieuan.m_expense.R;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<Expense> list;
    private MainActivity context;

    public ExpenseAdapter(List<Expense> list, MainActivity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_line, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Expense expense = list.get(position);
        holder.type.setText(expense.getType());
        holder.amount.setText(expense.getAmount());
        holder.comment.setText(expense.getComment());
        holder.time.setText(expense.getTime());

        holder.constraintLayoutExpense.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view1) {
                Dialog dialog = new Dialog(view1.getContext());
                dialog.setContentView(R.layout.dialog_delete_expense_custom);

                Button cancel = dialog.findViewById(R.id.btnCancelDialogDeleteExpense);
                Button delete = dialog.findViewById(R.id.btnDeleteDialogDeleteExpense);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Delete From DB
                        context.DeleteExpenseDB(expense.getType(), expense.getAmount(), expense.getTime(), expense.getComment());

                        // Delete From List
                        list.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();

                        Snackbar.make(view1, "Expense Removed", Snackbar.LENGTH_SHORT).show();

                    }
                });

                dialog.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView type, amount, comment, time;
        ConstraintLayout constraintLayoutExpense;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            type                    = itemView.findViewById(R.id.tvType);
            amount                  = itemView.findViewById(R.id.tvAmount);
            comment                 = itemView.findViewById(R.id.tvComment);
            time                    = itemView.findViewById(R.id.tvTime);
            constraintLayoutExpense = itemView.findViewById(R.id.constraintLayoutExpense);
        }
    }
}