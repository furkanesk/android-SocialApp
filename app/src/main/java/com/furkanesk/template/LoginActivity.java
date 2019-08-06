package com.furkanesk.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.furkanesk.template.Home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText login_text_username,login_text_password;
    Button login_button_login;
    TextView login_text_register;
    FirebaseAuth girisyetkisi;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_text_username = findViewById(R.id.login_text_username);
        login_text_password = findViewById(R.id.login_text_password);
        login_button_login = findViewById(R.id.login_button_login);
        login_text_register = findViewById(R.id.login_text_register);
        girisyetkisi = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        login_button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Login kodları
                String str_emailgiris = login_text_username.getText().toString();
                String str_passwordgiris = login_text_password.getText().toString();
                if(str_emailgiris.isEmpty() || str_passwordgiris.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Boş alanları doldurunuz", Toast.LENGTH_SHORT).show();
                }
                else {
                    girisyetkisi.signInWithEmailAndPassword(str_emailgiris,str_passwordgiris)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){

                                        DatabaseReference yolgiris = FirebaseDatabase.getInstance().getReference().child("users").child(girisyetkisi.getCurrentUser().getUid());
                                        yolgiris.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                progressBar.setVisibility(View.GONE);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else{
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "Giriş Başarısız", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
        login_text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
}
