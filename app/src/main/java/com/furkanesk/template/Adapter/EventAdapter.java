package com.furkanesk.template.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.furkanesk.template.Model.Event;
import com.furkanesk.template.Model.Kullanici;
import com.furkanesk.template.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    public Context context;
    public List<Event> eventList;

    private FirebaseUser mevcutUser;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.event_ogesi,parent,false);


        return new EventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        mevcutUser = FirebaseAuth.getInstance().getCurrentUser();
        Event event = eventList.get(position);
        //holder.owner_name.setText(event.getOwnerUsername());

        Glide.with(context).load(event.getEventImage()).into(holder.event_image);
        holder.event_name.setText(event.getEventName());
        holder.venue_name.setText(event.getEventVenue());
        holder.desc.setText(event.getEventDescription());

        //gonderenBilgisi(holder.owner_image,holder.owner_name,event.getOwnerId());
        gonderenBilgisi(holder.owner_image,holder.owner_name,holder.owner_name,event.getEventOwner());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView owner_image,event_image,white_heart,red_heart,checkin;
        public TextView owner_name,venue_name,likes,desc,time_posted,event_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            event_name = itemView.findViewById(R.id.event_name);
            owner_image = itemView.findViewById(R.id.owner_photo);
            venue_name = itemView.findViewById(R.id.venue_name);
            event_image = itemView.findViewById(R.id.event_image);
            white_heart = itemView.findViewById(R.id.image_white_heart);
            red_heart = itemView.findViewById(R.id.image_heart_red);
            checkin = itemView.findViewById(R.id.image_checking);
            owner_name = itemView.findViewById(R.id.eventOwner);
            likes = itemView.findViewById(R.id.image_likes);
            desc = itemView.findViewById(R.id.image_description);
            time_posted = itemView.findViewById(R.id.image_time_posted);

        }
    }
    private void gonderenBilgisi(final ImageView owner_image, final TextView owner_name, final TextView owner, String userId){
        DatabaseReference veriyolu = FirebaseDatabase.getInstance().getReference("users").child(userId);

        veriyolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Kullanici kullanici = dataSnapshot.getValue(Kullanici.class);
                Glide.with(context).load(kullanici.getResimurl()).into(owner_image);
                owner_name.setText((kullanici.getUsername()));
                owner.setText(kullanici.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
