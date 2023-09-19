package hongdieuan.m_expense.ListTrip;

import android.os.Parcel;

import java.io.Serializable;

public class Trip implements Serializable {
    private String tripName;
    private String destination;
    private String date;
    private String description;
    private int duration;
    private int numberPeople;
    private boolean risk;

    public Trip(String tripName, String destination, String date, String description, int duration, int numberPeople, boolean risk) {
        this.tripName = tripName;
        this.destination = destination;
        this.date = date;
        this.description = description;
        this.duration = duration;
        this.numberPeople = numberPeople;
        this.risk = risk;
    }

    protected Trip(Parcel in) {
        tripName = in.readString();
        destination = in.readString();
        date = in.readString();
        description = in.readString();
        duration = in.readInt();
        numberPeople = in.readInt();
        risk = in.readByte() != 0;
    }


    public String getTripName() {
        return tripName;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public int getNumberPeople() {
        return numberPeople;
    }

    public boolean isRisk() {
        return risk;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setNumberPeople(int numberPeople) {
        this.numberPeople = numberPeople;
    }

    public void setRisk(boolean risk) {
        this.risk = risk;
    }


}
