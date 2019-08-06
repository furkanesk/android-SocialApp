package com.furkanesk.template.Home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.furkanesk.template.FeedActivity;
import com.furkanesk.template.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class NewEventActivity extends AppCompatActivity {
    Uri resimUri;
    String myUri = "";

    StorageTask yuklemeGorevi;
    StorageReference resimyukleyol;

    ImageView backarrow, new_event_image;
    EditText new_event_name, new_event_venue, new_event_description;
    TextView share_button;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        backarrow = findViewById(R.id.backarrow);
        new_event_image = findViewById(R.id.new_event_image);
        new_event_name = findViewById(R.id.new_event_name);
        new_event_venue = findViewById(R.id.new_event_venue);
        new_event_description = findViewById(R.id.new_event_description);
        share_button = findViewById(R.id.share_new_event);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        resimyukleyol = FirebaseStorage.getInstance().getReference("events");

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewEventActivity.this, FeedActivity.class));
                finish();
            }
        });
        new_event_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kamera
                CropImage.activity().setAspectRatio(1,1).start(NewEventActivity.this);

            }
        });

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new_event_name.getText().toString().isEmpty() || new_event_description.getText().toString().isEmpty() || new_event_venue.getText().toString().isEmpty()) {
                    Toast.makeText(NewEventActivity.this, "Lütfen bütün alanları doldurunuz", Toast.LENGTH_SHORT).show();
                } else {
                    eventYukle();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            resimUri = result.getUri();

            new_event_image.setImageURI(resimUri);
        }
        else {
            Toast.makeText(this, "Resim seçilemedi", Toast.LENGTH_SHORT).show();
        }
    }

    private void eventYukle() {
        //event yukleme
        progressBar.setVisibility(View.VISIBLE);
        if(resimUri != null){
            final StorageReference dosyaYolu = resimyukleyol.child(System.currentTimeMillis()
                    +"."+dosyaUzantisiAl(resimUri));
            yuklemeGorevi = dosyaYolu.putFile(resimUri);

            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful()){

                        throw task.getException();
                    }

                    return dosyaYolu.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri indirmeUrisi = task.getResult();
                        myUri = indirmeUrisi.toString();

                        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("events");
                        String eventId = veriYolu.push().getKey();

                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("eventId",eventId);
                        hashMap.put("eventImage",myUri);
                        hashMap.put("eventName",new_event_name.getText().toString());
                        hashMap.put("eventVenue",new_event_venue.getText().toString());
                        hashMap.put("eventDescription",new_event_description.getText().toString());
                        hashMap.put("eventOwner", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        veriYolu.child(eventId).setValue(hashMap);

                        progressBar.setVisibility(View.GONE);

                        startActivity(new Intent(NewEventActivity.this,FeedActivity.class));
                        finish();
                    }else{
                        Toast.makeText(NewEventActivity.this, "Paylaşım başarısız", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewEventActivity.this,FeedActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Seçilen resim yok", Toast.LENGTH_SHORT).show();
        }
    }
    private String dosyaUzantisiAl(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
