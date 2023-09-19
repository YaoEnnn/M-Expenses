package hongdieuan.m_expense.Database;

public class ExpenseDBEntry {
    public static final String TABLE_NAME = "Expense";
    public static final String COL_ID = "id";
    public static final String COL_TYPE = "type";
    public static final String COL_TIME = "time";
    public static final String COL_COMMENT = "comment";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_FK_ID_TRIP = "trip_id";

    public static final String CREATE_TABLE_SQLITE = "CREATE TABLE IF NOT EXISTS " +
            "Expense (id INTEGER PRIMARY KEY, type TEXT NOT NULL, time TEXT NOT NULL, " +
            "amount TEXT NOT NULL, comment TEXT NOT NULL, trip_id INTEGER NOT NULL, " +
            "FOREIGN KEY (trip_id) REFERENCES Trip(id))";

    public static final String DELETE_TABLE_SQLITE = "DROP TABLE IF EXISTS Expense";
}
