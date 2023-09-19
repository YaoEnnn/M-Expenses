package hongdieuan.m_expense.Database;

public class TripDBEntry {
    public static final String TABLE_NAME = "Trip";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_DESTINATION = "destination";
    public static final String COL_DATE = "date";
    public static final String COL_DURATION = "duration";
    public static final String COL_NUMBERPEOPLE = "people";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_RISK = "risk";

    public static final String CREATE_TABLE_SQLITE = "CREATE TABLE IF NOT EXISTS " +
            "Trip (id INTEGER PRIMARY KEY, name TEXT NOT NULL, destination TEXT NOT NULL, " +
            "date TEXT NOT NULL, duration INTEGER NOT NULL, people INTEGER NOT NULL," +
            " description TEXT, risk TEXT)";

    public static final String DELETE_TABLE_SQLITE = "DROP TABLE IF EXISTS Trip";

}
