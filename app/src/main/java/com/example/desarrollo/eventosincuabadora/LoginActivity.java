package com.example.desarrollo.eventosincuabadora;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLCorreo, editTextLPassword;
    private Button buttonLogin;
    private TextView textViewRegistrateAqui;
    private TextView textViewEmailVerify;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setup();

        if(firebaseUser != null){
            if (firebaseUser.isEmailVerified()){
                Toast.makeText(LoginActivity.this,""+firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(LoginActivity.this,TerceraActivity.class));
            }
            else Toast.makeText(LoginActivity.this,"Your email.is not verify",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(LoginActivity.this,LoginActivity.class));

        }

        else


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = editTextLCorreo.getText().toString().trim();
                String contraseña = editTextLPassword.getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Login succesfull",Toast.LENGTH_SHORT);
                            finish();
                            startActivity(new Intent(LoginActivity.this,TerceraActivity.class));
                        }
                        else Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        textViewRegistrateAqui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    protected void setup(){

        editTextLCorreo = (EditText)findViewById(R.id.editTextLCorreo);
        editTextLPassword = (EditText)findViewById(R.id.editTextLPassword);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        textViewRegistrateAqui = (TextView)findViewById(R.id.textViewRegistrateAqui);
        textViewEmailVerify = (TextView)findViewById(R.id.textViewEmailVerify);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }

}
