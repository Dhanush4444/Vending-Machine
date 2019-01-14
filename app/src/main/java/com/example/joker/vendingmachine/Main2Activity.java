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
import java.util.Arrays;
import java.util.Objects;


public class Main2Activity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    TextView usrname;
//    String json="";
    private int prevCount=0;
    private FirebaseUser mUser;
    private Long walletDatal;
     ArrayList<String> list;
//    JSONObject ob1;
    private int walletData;
    private ArrayList<Integer> prices;
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
        prices=new ArrayList<Integer>(){
            {
                add(20);
                add(10);
                add(30);
                add(10);
                add(30);
            }
        };
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

String[] values= new String[]{"Coke : 20rs","Kitkat : 10rs","7up : 30rs","Lays : 10rs","Dairy Milk : 30rs"};
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                walletDatal = (Long) dataSnapshot.child(mUser.getUid()).child("wallet").getValue();
                usrname.setText(String.format("Username : %s", Objects.requireNonNull(dataSnapshot.child(mUser.getUid()).child("Name").getValue()).toString()));
//                json= Objects.requireNonNull(dataSnapshot.child("itemsJSON").getValue()).toString();




// json=json.replace(':','=');
//                try {
//                    ob1=new JSONObject("{"+json+"}");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(Main2Activity.this, e.getMessage() + json, Toast.LENGTH_SHORT).show();
//                }
//                try {
//                    if(ob1.length() == 0) {
//                        list.clear();
//                        prices.clear();
//                    }
//                    if(prevCount!= ob1.getInt("itemCount")) {
//                        list.clear();
//                        prices.clear();
//                        for (int i = 1; i <= ob1.getInt("itemCount"); i++) {
//                            list.add(ob1.getJSONObject("" + i).getString("name"));
//                            prices.add(ob1.getJSONObject(""+i).getInt("price"));
//                        }
//                        prevCount=ob1.getInt("itemCount");
//                        listview.setAdapter(new ArrayAdapter<String>(Main2Activity.this, R.layout.my_list_view, list));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(Main2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//


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
                final String item = (String) parent.getItemAtPosition(position);

                walletData = walletDatal.intValue();
                if (walletData >= prices.get(position)) {
                    walletData -= prices.get(position);
                    startActivity(new Intent(getApplicationContext(),Main3Activity.class).putExtra("id",position));
                }
                else
                    Toast.makeText(Main2Activity.this, "Insufficient wallet balance", Toast.LENGTH_SHORT).show();


            }

        });

    }


}

