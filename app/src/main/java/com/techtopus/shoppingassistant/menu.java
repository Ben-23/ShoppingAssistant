package com.techtopus.shoppingassistant;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.Serializable;
import java.util.ArrayList;

public class menu extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener {
private ArrayList <String> items=new ArrayList<>();

public EditText t;
TextView txt;
StringBuilder h=new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //txt=(TextView)findViewById(R.id.itemss);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        Bundle bundle=getIntent().getExtras();
        if(bundle==null)
            loadfragment(new HomeFragment());

        if(bundle!=null)
        {
            String str=bundle.getString("result",null);
            if(str=="null")
            {Toast.makeText(this, "No barcode found!!", Toast.LENGTH_SHORT).show();
                loadfragment(new HomeFragment());

            }
            if(str!=null)
            {
                Fragment fragobj= new ResultFragment();
                fragobj.setArguments(bundle);
                loadfragment(fragobj);

            }


        }
    }

    private boolean loadfragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.home:
                fragment = new HomeFragment();
                break;
            case R.id.account:
                fragment = new AccountFragment();
                break;
            case R.id.scan:
                Intent i = new Intent(menu.this, ScanActivity.class);
                startActivity(i);
                break;
        }
        return loadfragment(fragment);
    }
    public void gmail(View view)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","bbapvtltd@gmail.com", null));

        startActivity(Intent.createChooser(intent, "Choose an Email client :"));

    }
    public void fb(View view)
    {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/techtopus"));
            startActivity(intent);
        } catch(Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/techtopus")));
        }
    }
    public void wish(View view)
    {
loadfragment(new WishlistFragment());
    }
    public void contact(View view)
    {
        final Dialog mydialogue = new Dialog(this);

        //startActivity(intent);
        mydialogue.setContentView(R.layout.popup_contact);
        TextView t=(TextView)mydialogue.findViewById(R.id.closer);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialogue.dismiss();


            }
        });
        mydialogue.show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

    public void add(View view)
    {
        t=(EditText)findViewById(R.id.editText3);
        h.append(t.getText().toString());
        items.add(h.toString());
        t.setText("");
        initRecyclerView();
        h.delete(0,h.length());
    }
    private void initRecyclerView()
    {
        RecyclerView recycler=(RecyclerView)findViewById(R.id.recycle);
        Recyclerview_Adapter adapter=new Recyclerview_Adapter(this,items);
        recycler.setAdapter(adapter) ;
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }
}