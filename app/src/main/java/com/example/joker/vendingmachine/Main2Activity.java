package com.example.joker.vendingmachine;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.lang.Long;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    TextView wallet;
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
        logut=(Button) findViewById(R.id.logoutButton);
        final ListView listview = (ListView) findViewById(R.id.listview);
        prices=new ArrayList<Integer>(){
            {
                add(10);
                add(10);
                add(200);
                add(500);
            }
        };

        logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        String[] values = new String[]{"Coke - 10rs", "Fanta - 10rs", "Vodka-200rs", "100pipers - 500rs"};
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wallet.setText("Wallet Balance : " + dataSnapshot.child("wallet").getValue().toString().trim());
                walletDatal=(Long) dataSnapshot.child("wallet").getValue();
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
//                final String item = (String) parent.getItemAtPosition(position);

                walletData=walletDatal.intValue();
                if(walletData>=prices.get(position))
                walletData-=prices.get(position);
                else
                    Toast.makeText(Main2Activity.this, "Insufficient wallet balance", Toast.LENGTH_SHORT).show();
                mDatabase.child("wallet").setValue(walletData);








//                Toast.makeText(Main2Activity.this, "Clciked : "+Integer.toString(position), Toast.LENGTH_SHORT).show();

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

