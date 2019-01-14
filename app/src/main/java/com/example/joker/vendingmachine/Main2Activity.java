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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class Main2Activity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    TextView wallet, usrname;
    String json="";
    private int prevCount=0;
    private FirebaseUser mUser;
    private Long walletDatal;
    JSONObject ob1;
    private int walletData;
    private ArrayList<Integer> prices;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final ArrayList<String> list = new ArrayList<>();
        wallet = findViewById(R.id.wallet);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        Button logut = findViewById(R.id.logoutButton);
        usrname = findViewById(R.id.usrname);
        final ListView listview = findViewById(R.id.listview);
//        prices = new ArrayList<Integer>() {
//            {
//                add(20);
//                add(20);
//                add(10);
//                add(30);
//                add(30);
//
//            }
//        };

        prices=new ArrayList<>();



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


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wallet.setText(String.format("Wallet Balance : %s", dataSnapshot.child(mUser.getUid()).child("wallet").getValue().toString().trim()));
                walletDatal = (Long) dataSnapshot.child(mUser.getUid()).child("wallet").getValue();
                usrname.setText(String.format("Username : %s", dataSnapshot.child(mUser.getUid()).child("Name").getValue().toString()));
                json=dataSnapshot.child("itemsJSON").getValue().toString();
                json=json.replace(':','=');
                try {
                    ob1=new JSONObject("{"+json+"}");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Main2Activity.this, e.getMessage() + json, Toast.LENGTH_SHORT).show();
                }
                try {

                    if(prevCount!= ob1.getInt("itemCount")) {
                        list.clear();
                        for (int i = 1; i <= ob1.getInt("itemCount"); i++) {
                            list.add(ob1.getJSONObject("" + i).getString("name"));
                            prices.add(ob1.getJSONObject(""+i).getInt("price"));
                        }
                        prevCount=ob1.getInt("itemCount");
                        listview.setAdapter(new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_list_item_1, list));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Main2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        list.addAll(Arrays.asList(values));




        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    final int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);

                walletData = walletDatal.intValue();
                if (walletData >= prices.get(position))
                    walletData -= prices.get(position);
                else
                    Toast.makeText(Main2Activity.this, "Insufficient wallet balance", Toast.LENGTH_SHORT).show();
                mDatabase.child(mUser.getUid()).child("wallet").setValue(walletData);


                Toast.makeText(Main2Activity.this, "Purchased " + item, Toast.LENGTH_SHORT).show();

            }

        });

    }


}

//                Toast.makeText(getApplicationContext(),ob.length(),Toast.LENGTH_LONG).show();
//                itemCoun=(Long)dataSnapshot.child("items").child("itemCount").getValue();
//                itemCount=itemCoun.intValue();
//                for(int i=0;i<5;i++){
//                    list.add(dataSnapshot.child("items").child("1").child("name").getValue().toString());
//                    list.add(dataSnapshot.child("items").child("2").child("name").getValue().toString());
//                    list.add(dataSnapshot.child("items").child("3").child("name").getValue().toString());
//                    list.add(dataSnapshot.child("items").child("4").child("name").getValue().toString());
//                    list.add(dataSnapshot.child("items").child("5").child("name").getValue().toString());
//                }


//    private class StableArrayAdapter extends ArrayAdapter<String> {
//
//        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
//
//        StableArrayAdapter(Context context, int textViewResourceId,
//                           List<String> objects) {
//            super(context, textViewResourceId, objects);
//            for (int i = 0; i < objects.size(); ++i) {
//                mIdMap.put(objects.get(i), i);
//            }
//        }
//
//        @Override
//        public long getItemId(int position) {
//            String item = getItem(position);
//            return mIdMap.get(item);
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return true;
//        }
//
//    }
//
