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




public EditText t,t2,t3;
TextView txtt;
int g;
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
    public void gmail(View view)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","bbapvtltd@gmail.com", null));

        startActivity(Intent.createChooser(intent, "Choose an Email client :"));

    }
    /*Fucntions to fetch data from amazon and flipkart*/
    public void getAmazon(final String s)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;

                try {
                    doc =  Jsoup.connect("https://www.amazon.in/s/keywords=" + s).get();
                    Element element=doc.select("a.a-link-normal.s-access-detail-page").first();
                    prodname.add(element.text());
                    prodlink.add(element.attr("href"));
                    element=doc.getElementsByClass("s-access-image").first();
                    prodimg.add(element.attr("src"));
                    element=doc.select("span.a-size-base.a-color-price.s-price.a-text-bold").first();
                    prodprice.add(element.text());
                }catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                   // loadfragment(new ErrorFragment());
                  //  pb=(ProgressBar)findViewById(R.id.progressBar2);
                   // pb.setVisibility(View.INVISIBLE);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerView2();
                        pb=(ProgressBar)findViewById(R.id.progressBar2);
                        pb.setVisibility(View.INVISIBLE);

                     //   txtt=findViewById(R.id.textView2);
                       // txtt.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
    private void initRecyclerView2()
    {


        try{
            RecyclerView recycler2=(RecyclerView)findViewById(R.id.recyclerView2);
            Recyclerview_Adapter2 adapter2=new Recyclerview_Adapter2(this,prodimg,prodname,prodprice,prodlink);
            recycler2.setAdapter(adapter2) ;
            recycler2.setLayoutManager(new LinearLayoutManager(this));
        }catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void getFlipkart(final String  s)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc =null;
                try{
                    doc =  Jsoup.connect("https://www.flipkart.com/search?q=" + s).get();
                    Element element=doc.getElementsByClass("_2cLu-l").first();
                    prodname.add(element.attr("title"));
                    prodlink.add(element.attr("href"));
                    //element=doc.getElementsByClass("img._1Nyybr._30XEf0").first();
                    //imglink.append(element.attr("alt"));
                    prodimg.add("file:///C:/Users/i5/Downloads/download.png");
                    element=doc.getElementsByClass("_1vC4OE").first();
                    prodprice.add(element.text());
                    //builder.append("\nFlip Title : "+name+"\nLink : "+link+"\nImg : "+imglink+"\nPrice: "+price);
                }catch (IOException e)
                {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }
                runOnUiThread(new Runnable(){
                    @Override
                            public void run() {
                        //initRecyclerView2();
                        pb=(ProgressBar)findViewById(R.id.progressBar2);
                        pb.setVisibility(View.INVISIBLE);

                    }
                });

            }
        }).start();
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
    public void searchproduct_err(View view)
    {
        t=(EditText)findViewById(R.id.editText2);
        prod.append(t2.getText());
        for (int index = 0; index < prod.length(); index++) {
            if (prod.charAt(index) == ' ') {
                prod.deleteCharAt(index);
            }
        }
        Fragment fragobj= new ResultFragment();
        loadfragment(fragobj);
        getAmazon(prod.toString());
        Toast.makeText(this, prod, Toast.LENGTH_SHORT).show();

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
    private void initRecyclerView()
    {
//        if(fullList != null && temp != null) {
//            adapter = new ExpandableListCustom(getActivity(), fullList,temp);
//            lv.setAdapter(adapter);
//        }

try{
        RecyclerView recycler=(RecyclerView)findViewById(R.id.recycle);
        Recyclerview_Adapter adapter=new Recyclerview_Adapter(this,items);
        recycler.setAdapter(adapter) ;
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }catch(Exception e)
{
    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
}
    }
}