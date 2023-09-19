package hongdieuan.m_expense.ListExpense;

public class Expense {
    private String type;
    private String comment;
    private String time;
    private String amount;

    public Expense(String type, String comment, String time, String amount) {
        this.type = type;
        this.comment = comment;
        this.time = time;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }

    public String getAmount() {
        return amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
