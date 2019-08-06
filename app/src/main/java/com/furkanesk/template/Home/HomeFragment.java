package com.furkanesk.template.Home;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.furkanesk.template.Adapter.EventAdapter;
import com.furkanesk.template.Model.Event;
import com.furkanesk.template.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    EditText search;
    Button new_event_button;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<String> takipListesi;
    private List<Event> eventList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        search = view.findViewById(R.id.homeSearch);
        recyclerView = view.findViewById(R.id.recyclerviewHome);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        eventList = new ArrayList<>();

        eventAdapter = new EventAdapter(getContext(),eventList);
        recyclerView.setAdapter(eventAdapter);
        takipkontrol();


        new_event_button = view.findViewById(R.id.new_event_button);
        new_event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),NewEventActivity.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frame_includer,new SearchFragment()).commit();
            }
        });

        return view;
    }
    private void takipkontrol(){
        takipListesi = new ArrayList<>();
        DatabaseReference takipyolu = FirebaseDatabase.getInstance().getReference("Takip")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("TakipEdilenler");
        takipyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                takipListesi.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    takipListesi.add(snapshot.getKey());
                }
                gonderioku();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gonderioku(){
        DatabaseReference gonderiyolu = FirebaseDatabase.getInstance().getReference("events");

        gonderiyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Event event = snapshot.getValue(Event.class);
                    for(String id : takipListesi){
                        if(event.getEventOwner().equals(id)){
                            eventList.add(event);

                        }
                    }
                }

                eventAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
