package com.furkanesk.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.furkanesk.template.Frame.QRActivity;
import com.furkanesk.template.Home.HomeFragment;
import com.furkanesk.template.Frame.ProfileFragment;
import com.furkanesk.template.Frame.StarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FeedActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFrame = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_includer,new HomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFrame = new HomeFragment();
                            break;
                        case R.id.nav_star:
                            Intent intent = new Intent(getApplicationContext(), QRActivity.class);
                            startActivity(intent);
                            //selectedFrame = new StarFragment();
                            //a
                            break;
                        case R.id.nav_profile:
                            SharedPreferences.Editor editor = getSharedPreferences("PREF",MODE_PRIVATE).edit();
                            //editor.putString("profileId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            selectedFrame = new ProfileFragment();
                            break;
                    }
                    if(selectedFrame != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_includer,selectedFrame).commit();
                    }
                    return true;
                }
            };
}
