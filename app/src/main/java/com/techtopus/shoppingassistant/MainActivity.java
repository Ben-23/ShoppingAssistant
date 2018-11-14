package com.techtopus.shoppingassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                android.content.Intent Activity1 = new android.content.Intent(MainActivity.this,menu.class);
                startActivity(Activity1);
                finish();
            }
        }, 2000);
    }
}
