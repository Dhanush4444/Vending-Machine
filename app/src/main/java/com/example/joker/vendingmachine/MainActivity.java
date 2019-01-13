package com.example.joker.vendingmachine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button loginBut;
    EditText emailText,passText;
    FirebaseUser userDa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        emailText=(EditText) findViewById(R.id.emailId);
        passText=(EditText) findViewById(R.id.passId);
        loginBut=(Button) findViewById(R.id.button);
        userDa=mAuth.getCurrentUser();

        if(userDa != null){

            startActivity(new Intent(getApplicationContext(),Main2Activity.class));
        }

        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(emailText.getText().toString().trim()) || TextUtils.isEmpty(passText.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "Fileds Empty", Toast.LENGTH_SHORT).show();
                }
else{
                mAuth.signInWithEmailAndPassword(emailText.getText().toString().trim(), passText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                        } else {
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidUserException | FirebaseAuthInvalidCredentialsException usr) {
                                Toast.makeText(MainActivity.this, "incorrect email or password", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }




            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
    }
}
