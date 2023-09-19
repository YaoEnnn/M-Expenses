package hongdieuan.m_expense.Database;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TripDBHelper extends SQLiteOpenHelper {

    public TripDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sql) {
        sql.execSQL(TripDBEntry.CREATE_TABLE_SQLITE);
        sql.execSQL(ExpenseDBEntry.CREATE_TABLE_SQLITE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sql, int i, int i1) {
        sql.execSQL(TripDBEntry.DELETE_TABLE_SQLITE);
        sql.execSQL(ExpenseDBEntry.DELETE_TABLE_SQLITE);
        onCreate(sql);
    }
}
