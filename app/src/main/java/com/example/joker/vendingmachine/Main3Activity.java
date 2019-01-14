package com.example.joker.vendingmachine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {
    Bundle extras;
    private int Id,wallet;
    Button Inc, Dec;
    ImageView image;
    private int temp;
    private TextView quantity, avail,balanceView;
    private Long availableLong,wallet1;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Long currentPrizeLong;
    private int purchaseCount=0;
        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main3);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            quantity = findViewById(R.id.quantId);
            Inc = findViewById(R.id.plusId);
            avail = findViewById(R.id.countId);
            balanceView=findViewById(R.id.balanceId);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            Dec = findViewById(R.id.minusId);
            extras = getIntent().getExtras();
            image = findViewById(R.id.imageView);
            Id = extras.getInt("id");


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    availableLong = (Long) dataSnapshot.child("items").child("" + Id).child("count").getValue();
                    avail.setText("Available : " + availableLong.toString());
                    balanceView.setText("Balance : "+dataSnapshot.child(mUser.getUid()).child("wallet").getValue().toString());
                    currentPrizeLong=(Long) dataSnapshot.child("items").child(""+Id).child("price").getValue();
                    wallet1=(Long) dataSnapshot.child(mUser.getUid()).child("wallet").getValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setImageResource(R.drawable.buy_icon);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                        temp=purchaseCount;
                        temp *= currentPrizeLong.intValue();
                        wallet=wallet1.intValue();

                        if(wallet < temp){
                            Toast.makeText(Main3Activity.this, "Insufficeint Balance", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            wallet -= temp;
                            mDatabase.child(mUser.getUid()).child("wallet").setValue(wallet);

                        }

                }
            });


            if (Id == 0) {
                image.setImageResource(R.drawable.coke_can);
            } else if (Id == 1) {
                image.setImageResource(R.drawable.kitkat_cho);
            } else if (Id == 2) {
                image.setImageResource(R.drawable.sp_can);
            } else if (Id == 3) {
                image.setImageResource(R.drawable.lays_n);
            } else if (Id == 4) {
                image.setImageResource(R.drawable.dairy_cho);
            }

            Inc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(purchaseCount < availableLong.intValue()) {
                        purchaseCount++;
                        quantity.setText("" + purchaseCount);
                    }
                    else{
                        Toast.makeText(Main3Activity.this, "Not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (purchaseCount > 0) {
                        purchaseCount--;
                        quantity.setText("" + purchaseCount);
                    }
                }
            });

        }

    }

