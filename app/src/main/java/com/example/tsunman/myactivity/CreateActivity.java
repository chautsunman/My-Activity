package com.example.tsunman.myactivity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tsunman.myactivity.ActivityItem.ActivityItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateActivity extends AppCompatActivity {
    public static final String ARG_UID = "uid";

    private Button createButton;
    private TextInputEditText nameEditText;

    // database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mActivitiesDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        if (getIntent().hasExtra(ARG_UID)) {
            String uid = getIntent().getStringExtra(ARG_UID);

            // initialize database
            initializeDatabase(uid);

            // initialize the view members
            initializeViewMembers();

            // set the create button on-click listener
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createActivity();
                }
            });
        }
    }

    private void initializeDatabase(String uid) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mActivitiesDatabaseReference = mFirebaseDatabase.getReference().child("activities").child(uid);
    }

    private void initializeViewMembers() {
        createButton = (Button) findViewById(R.id.create_activity_create);
        nameEditText = (TextInputEditText) findViewById(R.id.create_activity_name);
    }

    private void createActivity() {
        String name = nameEditText.getText().toString();
        long time = System.currentTimeMillis();

        ActivityItem activity = new ActivityItem(name, time);

        // add the new activity to the database
        mActivitiesDatabaseReference.push().setValue(activity);

        // clear the edit text
        nameEditText.setText("");

        // navigate back to the main activity
        finish();
    }
}
