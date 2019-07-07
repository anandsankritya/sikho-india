package com.anandsankritya.sikhoindia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TitleListActivity extends AppCompatActivity {

    public static final String API_KEY = "AIzaSyCHsZl8zBB0INCoZLCNJSjGUBs9Y--IBRU";

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ArrayList<VideoItemModel> videoItemModels;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_list);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        String title = getIntent().getStringExtra("title");


        recyclerView = findViewById(R.id.recycleViewVideoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        videoItemModels = new ArrayList<>();

        Query query = reference.orderByChild("playlistTitle").equalTo(title);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot returns all children with title Title
                    videoItemModels.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Video v = snapshot.getValue(Video.class);
                        videoItemModels.add(new VideoItemModel(v.getVideoId(), v.getVideoTitle()));
                    }
                    TitleListAdapter adapter = new TitleListAdapter(videoItemModels, TitleListActivity.this, API_KEY);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
