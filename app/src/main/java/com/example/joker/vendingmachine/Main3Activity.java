package com.example.joker.vendingmachine;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class Main3Activity extends AppCompatActivity {
    Bundle extras;
    private int Id;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        extras= getIntent().getExtras();
        image= findViewById(R.id.imageView);
        Id=extras.getInt("id");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.buy_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

if(Id==0){
    image.setImageResource(R.drawable.coke_can);
}
else if(Id==1){
    image.setImageResource(R.drawable.kitkat_cho);
}
else if(Id==2){
    image.setImageResource(R.drawable.sp_can);
}
else if(Id==3){
    image.setImageResource(R.drawable.lays_n);
}
else if(Id==4){
    image.setImageResource(R.drawable.dairy_cho);
}

    }

}
