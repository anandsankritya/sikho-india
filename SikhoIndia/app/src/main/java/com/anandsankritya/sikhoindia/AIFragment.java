package com.anandsankritya.sikhoindia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AIFragment extends Fragment {

    private ArrayList<VideoItemModel> videoItemModels;
    private RecyclerView recyclerView;
    private ArrayList<String> playListTitles;

    public AIFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Artificial Intelligence");
        View view = inflater.inflate(R.layout.fragment_ai, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        videoItemModels = new ArrayList<>();
        playListTitles = new ArrayList<>();



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.orderByChild("category").equalTo("AI");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot returns all children with category General
                    videoItemModels.clear();
                    playListTitles.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Video v = snapshot.getValue(Video.class);

                        if(!playListTitles.contains(v.getPlaylistTitle())){
                            playListTitles.add(v.getPlaylistTitle());
                            videoItemModels.add(new VideoItemModel(v.getVideoId(), v.getPlaylistTitle()));
                        }

                    }
                    VideoRecyclerViewAdapter adapter=new VideoRecyclerViewAdapter(videoItemModels, getActivity().getApplicationContext(), Common.API_KEY, Common.COURSE_GENERAL);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
