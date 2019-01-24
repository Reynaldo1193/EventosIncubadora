package com.example.desarrollo.eventosincuabadora;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.google.firebase.auth.GoogleAuthProvider.GOOGLE_SIGN_IN_METHOD;

public class TestActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient googleApiClient;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    private SignInButton buttonGoogleSignin;
    private Button buttonGoogleLogout;
    private TextView textViewCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_id_client))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        firebaseAuth = FirebaseAuth.getInstance();
        /*firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        }*/

        buttonGoogleSignin = (SignInButton) findViewById(R.id.buttonGoogleSignIn);
        buttonGoogleLogout = (Button) findViewById(R.id.buttonGoogleLogOut);
        buttonGoogleLogout.setVisibility(View.GONE);
        buttonGoogleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

        buttonGoogleLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleSignInClient.signOut();
                GoTest();

            }
        });
        buttonGoogleSignin.setSize(SignInButton.SIZE_WIDE);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(TestActivity.this,"Login firebase failed",Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                    else Toast.makeText(TestActivity.this,"Login firebase succeded",Toast.LENGTH_SHORT).show();
                }
            });
            updateUI(account);

        }else {
            Toast.makeText(TestActivity.this,"Login Google Failed",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            findViewById(R.id.buttonGoogleSignIn).setVisibility(View.GONE);
            findViewById(R.id.buttonGoogleLogOut).setVisibility(View.VISIBLE);
            textViewCorreo = (TextView) findViewById(R.id.textViewCorreo);
            textViewCorreo.setVisibility(View.VISIBLE);
            textViewCorreo.setText(account.getEmail());
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            Toast.makeText(TestActivity.this,"Firebase login"+firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
        } else {
            findViewById(R.id.buttonGoogleSignIn).setVisibility(View.VISIBLE);
            findViewById(R.id.buttonGoogleLogOut).setVisibility(View.GONE);
        }
    }

    private void GoTercera() {
        finish();
        startActivity(new Intent(TestActivity.this,TerceraActivity.class));
    }

    private void GoTest() {
        finish();
        startActivity(new Intent(TestActivity.this,TestActivity.class));
    }

}
