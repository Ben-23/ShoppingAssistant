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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class menu extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener {
private ArrayList <String> items=new ArrayList<>();

    private ArrayList <String> prodname=new ArrayList<>();
    private ArrayList <String> prodimg=new ArrayList<>();
    private ArrayList <String> prodlink=new ArrayList<>();
    private ArrayList <String> prodprice=new ArrayList<>();
    private ArrayList <Integer> prodsite=new ArrayList<>();
    int flag=0;
    public EditText t,t2,t3;
    int g;
    TextView txtt;
    ProgressBar pb;
    StringBuilder h=new StringBuilder();
    StringBuilder builder=new StringBuilder();
    StringBuilder prod=new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        Bundle bundle=getIntent().getExtras();
        if(bundle==null)
            loadfragment(new HomeFragment());

        if(bundle!=null)
        {
            String str=bundle.getString("result",null);
            if(str=="null")
            {
                Toast.makeText(this, "No barcode found!!", Toast.LENGTH_SHORT).show();
                loadfragment(new HomeFragment());
            }
            if(str!=null)
            {
                loadfragment(new ResultFragment());
                getAmazon(bundle.getString("result"));
                 getFlipkart(bundle.getString("result"));

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

    /* Fuctions for fb,gmail,wishlist*/
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

    public void gmail(View view)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","bbapvtltd@gmail.com", null));

        startActivity(Intent.createChooser(intent, "Choose an Email client :"));

    }
    /*Fucntions to fetch data from amazon and flipkart*/
    public void getAmazon(final String s)
    {
        final StringBuilder cur=new StringBuilder("₹");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                prodname.clear();
                prodlink.clear();
                prodimg.clear();
                prodprice.clear();
                prodsite.clear();
                try {
                    doc =  Jsoup.connect("https://www.amazon.in/s/keywords=" + s).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36").get();
                    Element element=doc.select("a.a-link-normal.s-access-detail-page").first();
                    prodname.add(element.text());
                    prodlink.add(element.attr("href"));
                    element=doc.getElementsByClass("s-access-image").first();
                    prodimg.add(element.attr("src"));
                    prodsite.add(0);
                    element=doc.select("span.a-size-base.a-color-price.s-price.a-text-bold").first();
                    cur.append(element.text());
                    prodprice.add(cur.toString());
                }catch (Exception e) {
                    prodname.add("Sorry, This product is not available  on Amazon ");
                    prodimg.add(null);
                    prodsite.add(0);
                    prodlink.add("https://amazon.in/");
                    prodprice.add("________");
                    //loadfragment(new ErrorFragment());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                            initRecyclerView2();
                            pb = (ProgressBar) findViewById(R.id.progressBar2);
                            pb.setVisibility(View.INVISIBLE);


                     }
                });
            }
        }).start();
    }
    public void getFlipkart(final String  s)
    {
        new Thread(new Runnable() {
            StringBuilder link=new StringBuilder("https://www.flipkart.com");
            @Override
            public void run() {
                Document doc =null;
                try{
                    doc =  Jsoup.connect("https://www.flipkart.com/search?q=" + s).get();
                    Element element=doc.getElementsByClass("_2cLu-l").first();
                    prodname.add(element.text());
                    link.append(element.attr("href"));
                    prodlink.add(link.toString());
                    prodsite.add(1);
                    prodimg.add(null);
                    element=doc.getElementsByClass("_1vC4OE").first();
                    prodprice.add(element.text());
                }catch (Exception e)
                {
                    prodname.add("Sorry, This product is not available  on Flipkart ");
                    prodimg.add(null);
                    prodsite.add(1);
                    prodlink.add("https://www.flipkart.com/");
                    prodprice.add("________");
                }
                runOnUiThread(new Runnable(){
                    @Override
                            public void run() {

                                initRecyclerView2();
                                pb = findViewById(R.id.progressBar2);
                                pb.setVisibility(View.INVISIBLE);

                    }
                });

            }
        }).start();
    }
    /*Functions that generate Recycler  view */
    private void initRecyclerView()
    {
        try{
            RecyclerView recycler=(RecyclerView)findViewById(R.id.recycle);
            Recyclerview_Adapter adapter=new Recyclerview_Adapter(this,items);
            recycler.setAdapter(adapter) ;
            recycler.setLayoutManager(new LinearLayoutManager(this));
        }catch(Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void initRecyclerView2()
    {


        try{
            RecyclerView recycler2=(RecyclerView)findViewById(R.id.recyclerView2);
            Recyclerview_Adapter2 adapter2=new Recyclerview_Adapter2(this,prodimg,prodname,prodprice,prodlink,prodsite);
            recycler2.setAdapter(adapter2) ;
            recycler2.setLayoutManager(new LinearLayoutManager(this));
        }catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }
    public void searchproduct(View view)
    {

        prod.delete(0, prod.length());
        t2=(EditText)findViewById(R.id.editText2);
        prod.append(t2.getText());
        for (int index = 0; index < prod.length(); index++) {
            if (prod.charAt(index) == ' ')
            {
                prod.deleteCharAt(index);
            }
        }
        Fragment fragobj= new ResultFragment();
        loadfragment(fragobj);
        Toast.makeText(this,prod.toString(), Toast.LENGTH_SHORT).show();
        getAmazon(prod.toString());
        getFlipkart(prod.toString());
    }
    public void add(View view)
    {

        t=(EditText)findViewById(R.id.editText3);
        h.append(t.getText());
        items.add(h.toString());
        t.setText("");
        initRecyclerView();
        h.delete(0,h.length());
    }

}