package com.example.joker.vendingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import java.util.Objects;


public class Main3Activity extends AppCompatActivity {
    Bundle extras;
    private int Id, wallet;
    Button Inc, Dec, coinMode, cardMode;
    public int rec;
    public Thread ob;
    public Long recLong;
    ImageView image;
    public int count = 0;
    private int temp;
    private TextView quantity, avail, balanceView;
    private Long availableLong, wallet1;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public Long currentPrizeLong;
    private int purchaseCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Set quantity reference
        quantity = findViewById(R.id.quantId);
//        Set Increment reference
        Inc = findViewById(R.id.plusId);
        avail = findViewById(R.id.countId);
        coinMode = findViewById(R.id.coinMode);
        cardMode = findViewById(R.id.cardMode);
        balanceView = findViewById(R.id.balanceId);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        Dec = findViewById(R.id.minusId);
        extras = getIntent().getExtras();
        image = findViewById(R.id.imageView);
        Id = extras.getInt("id");

        mDatabase.child("currentUser").setValue(mUser.getUid());

        cardMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Mode").setValue("Card");
                mDatabase.child("phoneBuy").setValue(false);

            }
        });

        coinMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("Mode").setValue("Coin");

            }
        });


        mDatabase.child("selected").setValue(Id + 1);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                availableLong = (Long) dataSnapshot.child("items").child("" + (Id + 1)).child("count").getValue();
                avail.setText("Available : " + availableLong.toString());
                balanceView.setText("Balance : " + dataSnapshot.child(mUser.getUid()).child("wallet").getValue().toString());
                currentPrizeLong = (Long) dataSnapshot.child("items").child("" + (Id + 1)).child("price").getValue();
                wallet1 = (Long) dataSnapshot.child(mUser.getUid()).child("wallet").getValue();
                recLong = (Long) dataSnapshot.child("received").getValue();
                rec = recLong.intValue();
                count = 0;
                if (rec == 1 && count == 0) {
                    temp = purchaseCount;
                    count++;
                    mDatabase.child("received").setValue(0);
                    temp *= currentPrizeLong.intValue();
                    wallet = wallet1.intValue();

                    if (wallet < temp) {
                        Toast.makeText(Main3Activity.this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
                    } else {
                        if (availableLong.intValue() > 0) {
                            wallet -= temp;
                            Toast.makeText(Main3Activity.this, "Successful", Toast.LENGTH_LONG).show();
                            mDatabase.child("items").child(Objects.requireNonNull(extras.getString("idString"))).child("count").setValue(availableLong.intValue() - purchaseCount);
                            mDatabase.child(mUser.getUid()).child("wallet").setValue(wallet);

                            try {

                                Thread.sleep(5000);

                            } catch (Exception e) {

                            }
                            finish();
                            System.exit(0);
                        } else
                            Toast.makeText(Main3Activity.this, "Not Available", Toast.LENGTH_SHORT).show();
                    }
                }
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
//                Toast.makeText(getApplicationContext(), "Waiting for transaction to complete", Toast.LENGTH_SHORT).show();
                int itemPrize = currentPrizeLong.intValue();
                int walletBalance = wallet1.intValue();
                if (walletBalance < itemPrize) {
                    Toast.makeText(Main3Activity.this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
                } else {
                    int temp33 = walletBalance - itemPrize;
                    mDatabase.child(mUser.getUid()).child("wallet").setValue(temp33);
                    mDatabase.child("phoneBuy").setValue(true);
                    mDatabase.child("Mode").setValue("Phone");
                }

            }
        });


        if (Id == 0) {
            image.setImageResource(R.drawable.dairy_cho);
        } else if (Id == 1) {
            image.setImageResource(R.drawable.kitkat_cho);
        } else if (Id == 2) {
            image.setImageResource(R.drawable.dark_f);
        } else if (Id == 3) {
            image.setImageResource(R.drawable.per_ch);
        }
        Inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (purchaseCount < availableLong.intValue()) {
                    purchaseCount++;
                    quantity.setText("" + purchaseCount);
                } else {
                    Toast.makeText(Main3Activity.this, "Not available", Toast.LENGTH_SHORT).show();
                    purchaseCount = 0;
                    quantity.setText("0");
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

