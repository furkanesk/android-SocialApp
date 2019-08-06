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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText register_text_username,register_text_name,register_text_email,register_text_password;
    Button register_button_register;
    TextView register_text_login;
    // Database login
    FirebaseAuth yetki = FirebaseAuth.getInstance();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference yol = db.getReference();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_text_username = findViewById(R.id.register_text_username);
        register_text_name = findViewById(R.id.register_text_name);
        register_text_email = findViewById(R.id.register_text_email);
        register_text_password = findViewById(R.id.register_text_password);
        register_button_register = findViewById(R.id.register_button_register);
        register_text_login = findViewById(R.id.register_text_login);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        register_text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        register_button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_username = register_text_username.getText().toString();
                String str_name = register_text_name.getText().toString();
                String str_email = register_text_email.getText().toString();
                String str_password = register_text_password.getText().toString();
                if(str_username.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Lütfen bütün alanları doldurunuz.", Toast.LENGTH_SHORT).show();
                }
                else if(str_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Parolanız minimum 6 karakter olmalıdır", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Başarılı kayıt kodları
                    progressBar.setVisibility(View.VISIBLE);
                    saveuser(str_username,str_name,str_email,str_password);
                }

            }
        });
    }
    private void saveuser(final String username, final String name, String email, final String password){
        yetki.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseuser = yetki.getCurrentUser();
                            String userId = firebaseuser.getUid();
                            yol = FirebaseDatabase.getInstance().getReference();
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",username.toLowerCase());
                            hashMap.put("name",name);
                            yol.child("users").child(userId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this,FeedActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }
                            });
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Bu e-mail ve parola ile kayıt başarısız.", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}