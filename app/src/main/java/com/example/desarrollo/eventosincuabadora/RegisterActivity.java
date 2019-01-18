package com.example.desarrollo.eventosincuabadora;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final Pattern PASSWORDVALIDATION = Pattern.compile("^" + "(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[a-zA-Z])" + ".{6,}" + "$");
    private static final Pattern EMAILVALIDATION = Pattern.compile(".{1,250}"+"\\@"+"[a-zA-Z0-9]{1,60}"+"\\."+"[a-zA-Z0-9]{1,25}");

    private EditText userEmail,userPassword,userCPassword;
    private Button buttonRegistrar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setup();

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo,contraseña,confContraseña;
                correo = userEmail.getText().toString().trim();
                contraseña = userPassword.getText().toString().trim();
                confContraseña = userCPassword.getText().toString().trim();

                if (validation(correo,contraseña,confContraseña)){
                    firebaseAuth.createUserWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "The registration succeeded", Toast.LENGTH_SHORT).show();
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this,"Se envio un email a tu correo, por favor verifica tu correo",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                            Toast.makeText(RegisterActivity.this,"Hubo un problema al enviarte el correo de verificacion",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Intent intent = new Intent(RegisterActivity.this ,LoginActivity.class);
                                startActivity(intent);
                            }
                            else if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(RegisterActivity.this,"El usuario ya existe",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "The registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });



    }

    private void setup(){
        userEmail = (EditText) findViewById(R.id.editTextRCorreo);
        userPassword = (EditText) findViewById(R.id.editTextRPassword);
        userCPassword = (EditText) findViewById(R.id.editTextRCPassword);
        buttonRegistrar = (Button) findViewById(R.id.buttonRegistrar);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private boolean validation(String correo, String contraseña,String confContraseña){

        if(correo.isEmpty() ||contraseña.isEmpty() || confContraseña.isEmpty()){
            Toast.makeText(RegisterActivity.this,"Llene todos los campos",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!EMAILVALIDATION.matcher(correo).matches()){
            Toast.makeText(RegisterActivity.this,"El correo es inválido",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!PASSWORDVALIDATION.matcher(contraseña).matches()){
            Toast.makeText(RegisterActivity.this,"La contraseña es demasiado débil; debe ser de al menos 6 caracteres y contener al menos una mayuscula y un caracter especial",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!contraseña.equals(confContraseña)){
            Toast.makeText(RegisterActivity.this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }
}
