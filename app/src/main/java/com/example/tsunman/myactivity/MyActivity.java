package com.example.tsunman.myactivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyActivity {
    private String name;
    private long time;

    public MyActivity() {

    }

    public MyActivity(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public String getTimeString() {
        return new SimpleDateFormat().format(new Date(time));
    }

    public void setTime(long time) {
        this.time = time;
    }
}
