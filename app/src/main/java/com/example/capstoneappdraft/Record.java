package com.example.capstoneappdraft;
import com.google.firebase.database.IgnoreExtraProperties;

public class Record {
    public String titleRecord;
    public String dateRecord;
    public String timeRecord;
    public String description;

    public Record() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Record(String titleRecord, String dateRecord, String timeRecord, String description) {
        this.titleRecord = titleRecord;
        this.dateRecord = dateRecord;
        this.timeRecord = timeRecord;
        this.description = description;
    }

    public String getTitleRecord() {
        return titleRecord;
    }

    public void setTitleRecord(String titleRecord) {
        this.titleRecord = titleRecord;
    }

    public String getDateRecord() {
        return dateRecord;
    }

    public void setDateRecord(String dateRecord) {
        this.dateRecord = dateRecord;
    }

    public String getTimeRecord() {
        return timeRecord;
    }

    public void setTimeRecord(String timeRecord) {
        this.timeRecord = timeRecord;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
