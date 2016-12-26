package com.example.tsunman.myactivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tsunman.myactivity.ActivityItem.ActivityItem;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int RC_SIGN_IN = 1;

    private FloatingActionButton createActivityFab;

    // activity list
    private RecyclerView mRecyclerView;
    private ActivityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    // database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mActivitiesDatabaseReference;
    private ChildEventListener mActivitiesEventListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize authentication
        initializeAuth();
    }

    private void initializeAuth() {
        // get the authentication instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        // initialize the authentication state change listener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // user is signed in
                    onSignedIn(user);
                } else {
                    // user is not signed in
                    onSignedOut();

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                showToast(R.string.signed_in);
            } else if (resultCode == RESULT_CANCELED) {
                showToast(R.string.sign_in_cancelled);
            } else if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
                showToast(R.string.sign_in_no_network);
            }
        }
    }

    private void onSignedIn(FirebaseUser user) {
        // initialize the activity list
        initializeList();

        // initialize database
        initializeDatabase(user.getUid());

        // add the activities event listener
        addActivitiesListener();

        // initialize the create activity fab
        initializeFab(user.getUid());
    }

    private void initializeList() {
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_list);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ActivityAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initializeDatabase(String uid) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mActivitiesDatabaseReference = mFirebaseDatabase.getReference().child("activities").child(uid);
    }

    private void addActivitiesListener() {
        if (mActivitiesEventListener == null) {
            mActivitiesEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ActivityItem activity = dataSnapshot.getValue(ActivityItem.class);
                    mAdapter.add(activity);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mActivitiesDatabaseReference.addChildEventListener(mActivitiesEventListener);
        }
    }

    private void initializeFab(final String uid) {
        createActivityFab = (FloatingActionButton) findViewById(R.id.create_activity_fab);
        createActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
    }

    private void onSignedOut() {
        removeActivitiesListener();

        if (mAdapter != null) {
            mAdapter.clear();
        }
    }

    private void removeActivitiesListener() {
        if (mActivitiesEventListener != null) {
            mActivitiesDatabaseReference.removeEventListener(mActivitiesEventListener);

            mActivitiesEventListener = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // add the authentication state change listener
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // remove the authentication state change listener
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);

                return true;
            case R.id.master_detail_menu:
                Intent intent = new Intent(this, ActivityListActivity.class);
                startActivity(intent);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast(int stringRes) {
        Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show();
    }
}
