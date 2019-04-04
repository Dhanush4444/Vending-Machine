package com.example.joker.vendingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class Main2Activity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    TextView usrname;
    private FirebaseUser mUser;
     ArrayList<String> list;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        list = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        Button logut = findViewById(R.id.logoutButton);
        usrname = findViewById(R.id.usrname);
        final ListView listview = findViewById(R.id.listview);

        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                mUser = null;
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

String[] values= new String[]{"Dairy Milk : 20rs","Kitkat : 20rs","Dark Fantasy : 10rs","Perk : 10rs"};
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usrname.setText(String.format("Username : %s", Objects.requireNonNull(dataSnapshot.child(mUser.getUid()).child("Name").getValue()).toString()));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        list.addAll(Arrays.asList(values));
        listview.setAdapter(new ArrayAdapter<String>(Main2Activity.this, R.layout.my_list_view, list));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
//                final String item = (String) parent.getItemAtPosition(position);
                mDatabase.child("selected").setValue(position+1);
                    startActivity(new Intent(getApplicationContext(),Main3Activity.class).putExtra("id",position).putExtra("idString",Integer.toString(position+1)));



            }

        });

    }


}

