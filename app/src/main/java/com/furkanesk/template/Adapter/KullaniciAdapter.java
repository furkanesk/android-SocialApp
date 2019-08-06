package com.furkanesk.template.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.furkanesk.template.Frame.ProfileFragment;
import com.furkanesk.template.Model.Kullanici;
import com.furkanesk.template.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.ViewHolder> {

    private Context context;
    private List<Kullanici> kullanicilar;
    private FirebaseUser firebaseUser;

    public KullaniciAdapter(Context context, List<Kullanici> kullanicilar) {
        this.context = context;
        this.kullanicilar = kullanicilar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.kullanici_ogesi,parent,false);


        return new KullaniciAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Kullanici kullanici = kullanicilar.get(position);
        holder.btn_friend.setVisibility(View.VISIBLE);

        holder.kullaniciadi.setText(kullanici.getUsername());
        holder.ad.setText(kullanici.getName());
        Glide.with(context).load(kullanici.getResimurl()).into(holder.profil_resmi);
        takipediliyor(kullanici.getId(),holder.btn_friend);

        if(kullanici.getId().equals(firebaseUser.getUid())){
            holder.btn_friend.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileId",kullanici.getId());
                editor.apply();

                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.homeSearch, new ProfileFragment()).commit();
            }
        });

        holder.btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btn_friend.getText().toString().equals("ADD FRIEND")){

                    FirebaseDatabase.getInstance().getReference().child("Takip")
                            .child(kullanici.getId()).child("TakipEdilenler").child(firebaseUser.getUid()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Takip")
                            .child(firebaseUser.getUid()).child("TakipEdilenler").child(kullanici.getId()).setValue(true);
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Takip")
                            .child(kullanici.getId()).child("TakipEdilenler").child(firebaseUser.getUid()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Takip")
                            .child(firebaseUser.getUid()).child("TakipEdilenler").child(kullanici.getId()).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return kullanicilar.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView kullaniciadi;
        public TextView ad;
        public CircleImageView profil_resmi;
        public Button btn_friend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            kullaniciadi = itemView.findViewById(R.id.kullaniciadi_oge);
            ad = itemView.findViewById(R.id.txt_ad_oge);
            profil_resmi = itemView.findViewById(R.id.profil_resmi);
            btn_friend = itemView.findViewById(R.id.btn_friend_oge);
        }
    }
    private void takipediliyor(final String kullaniciId, final Button button){
        DatabaseReference yol = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(firebaseUser.getUid()).child("TakipEdilenler");
        yol.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(kullaniciId).exists()){
                    button.setText("FRIEND");
                    String a = button.getText().toString();
                }else{
                    button.setText("ADD FRIEND");
                    String a = button.getText().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
