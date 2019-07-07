package com.anandsankritya.sikhoindiaadminapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;

    private ArrayList<String> titles;

    private ArrayAdapter<String> adapter;

    private FirebaseAuth auth;
    private TextView tvUsername, email;

    private EditText etVideoId, etVideoTitle;
    private AutoCompleteTextView autoTextView;

    private Button submit;

    private String username;

    Spinner spin;
    String spin_val;
    String[] category = { "General", "Android", "AI", "IOS", "Web Dev" };

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(MainActivity.this);
        submit = findViewById(R.id.btn);
        etVideoId = findViewById(R.id.et);
        etVideoTitle = findViewById(R.id.etVideoTitle);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        titles = new ArrayList<>();

        if (auth.getCurrentUser() != null) {
            // already signed in
            findViewById(R.id.view).setVisibility(View.VISIBLE);
            FirebaseUser user = auth.getCurrentUser();
            username = user.getEmail().substring(0, user.getEmail().indexOf("@"));


        } else {
            // not signed in
            showSignInOptions();
        }



        updatePlayListTitles();




        autoTextView = findViewById(R.id.autoTextView);
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, titles);
        autoTextView.setThreshold(1); //will start working from first character
        autoTextView.setAdapter(adapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etVideoId.getText().toString().equals("")
                        && !autoTextView.getText().toString().equals("")
                && !etVideoTitle.getText().toString().equals("")){

                    String url = etVideoId.getText().toString().trim();

                    String videoid = "";


                    if(!url.equals("")){
                        videoid = url.substring(url.lastIndexOf("/")+1);
                    }

                    Video video = new Video(username, videoid, spin_val, autoTextView.getText().toString(), etVideoTitle.getText().toString());

                    reference.push().setValue(video);



                    updatePlayListTitles();

                    autoTextView.setText("");
                    spin.setSelection(0);
                    etVideoTitle.setText("");
                    etVideoId.setText("");
                }else{
                    Toast.makeText(MainActivity.this, "Input fields should not be empty.", Toast.LENGTH_SHORT).show();
                }

            }
        });



        spin = findViewById(R.id.spinner);//fetching view's id
        //Register a callback to be invoked when an item in this AdapterView has been selected
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {

                spin_val = category[position];//saving the value selected


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
        //setting array adaptors to spinners
        //ArrayAdapter is a BaseAdapter that is backed by an array of arbitrary objects
        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, category);

        // setting adapteers to spinners
        spin.setAdapter(spin_adapter);

    }

    private void showSignInOptions(){

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setTosAndPrivacyPolicyUrls("https://superapp.example.com/terms-of-service.html",
                                "https://superapp.example.com/privacy-policy.html")
                        .build(),
                RC_SIGN_IN);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {

                if(auth!=null){
                    findViewById(R.id.view).setVisibility(View.VISIBLE);
                    FirebaseUser user = auth.getCurrentUser();
                    if(user!=null)
                    username = user.getEmail().substring(0, user.getEmail().indexOf("@"));
                }

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(MainActivity.this, "Sign in cancelled!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(MainActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MainActivity.this, "Unknown error!", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Sign-in error: ", response.getError());
            }
        }
    }

    private void updatePlayListTitles(){
        if(adapter!=null) {
            adapter.clear();
        }
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot videoSnapshot: dataSnapshot.getChildren()) {
                    Video video = videoSnapshot.getValue(Video.class);
                    titles.add(video.getPlaylistTitle());
                }
                Set<String> set = new LinkedHashSet<>(titles);
                titles.clear();
                titles.addAll(set);
                adapter.addAll(titles);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
