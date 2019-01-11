package com.example.joker.vendingmachine;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import java.lang.Long;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    TextView wallet,usrname;
    FirebaseUser mUser;
    Long walletDatal;
    int walletData;
    Button logut;
    ArrayList<Integer> prices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        wallet = (TextView) findViewById(R.id.wallet);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        logut=(Button) findViewById(R.id.logoutButton);
        usrname=(TextView) findViewById(R.id.usrname);
        final ListView listview = (ListView) findViewById(R.id.listview);
        prices=new ArrayList<Integer>(){
            {
                add(20);
                add(20);
                add(10);
                add(30);
                add(30);

            }
        };

        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        String[] values = new String[]{"Dairy Milk : 20rs","Kitkat: 20rs","Lays : 10rs","Coke : 30rs","7Up : 30rs"};
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wallet.setText("Wallet Balance : " + dataSnapshot.child(mUser.getUid()).child("wallet").getValue().toString().trim());
                walletDatal=(Long) dataSnapshot.child(mUser.getUid()).child("wallet").getValue();
                usrname.setText("Username : "+dataSnapshot.child(mUser.getUid()).child("Name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                walletData=walletDatal.intValue();
                if(walletData>=prices.get(position))
                walletData-=prices.get(position);
                else
                    Toast.makeText(Main2Activity.this, "Insufficient wallet balance", Toast.LENGTH_SHORT).show();
                mDatabase.child(mUser.getUid()).child("wallet").setValue(walletData);








                Toast.makeText(Main2Activity.this, "Purchased "+item, Toast.LENGTH_SHORT).show();

            }

        });

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        StableArrayAdapter(Context context, int textViewResourceId,
                           List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }



}

