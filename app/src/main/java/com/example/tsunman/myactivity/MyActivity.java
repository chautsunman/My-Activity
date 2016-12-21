package com.example.tsunman.myactivity;

public class MyActivity {
    private String activityName;

    public MyActivity() {

    }

    public MyActivity(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
