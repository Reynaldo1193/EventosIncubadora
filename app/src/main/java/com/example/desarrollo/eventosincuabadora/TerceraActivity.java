package com.example.desarrollo.eventosincuabadora;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TerceraActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView textViewEmailVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tercera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setup();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (!firebaseUser.isEmailVerified()){
            textViewEmailVerify.setText("Your email is not verify");
            textViewEmailVerify.setVisibility(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logoutmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Toast.makeText(TerceraActivity.this,"Logout",Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(TerceraActivity.this,LoginActivity.class));
                return true;
        }
        return true;
    }

    private void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        textViewEmailVerify = (TextView) findViewById(R.id.textViewEmailVerify);
    }
}
