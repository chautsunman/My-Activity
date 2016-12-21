package com.example.tsunman.myactivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;

    private Button mLogoutButton;

    // activity list
    private RecyclerView mRecyclerView;
    private ActivityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

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
                    onSignedIn();
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

    private void onSignedIn() {
        mLogoutButton = (Button) findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MainActivity.this);
            }
        });

        // initialize the activity list
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_list);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ActivityAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void onSignedOut() {
        showToast(R.string.signed_out);
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

    private void showToast(int stringRes) {
        Toast.makeText(this, stringRes, Toast.LENGTH_SHORT).show();
    }
}
