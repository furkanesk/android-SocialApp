package com.furkanesk.template;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button main_button_login,main_button_register;
    FirebaseUser mainuser;

// Sürekli giriş yapmayı önlemek için
   /* @Override
    protected void onStart() {
        super.onStart();
        mainuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mainuser != null){
            startActivity(new Intent(MainActivity.this,FeedActivity.class));
            finish();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_button_login = findViewById(R.id.main_button_login);
        main_button_register = findViewById(R.id.main_button_register);
        main_button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        main_button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
    }
}
