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

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import static com.google.android.gms.internal.zzagy.runOnUiThread;

public class menu extends AppCompatActivity implements  BottomNavigationView.OnNavigationItemSelectedListener {
private ArrayList <String> items=new ArrayList<>();

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
                //getAmazon(bundle.getString("result"));
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
                StringBuilder name = new StringBuilder();
                StringBuilder link = new StringBuilder();
                StringBuilder imglink=new StringBuilder();
                StringBuilder price=new StringBuilder();
                try {
                    doc =  Jsoup.connect("https://www.amazon.in/s/keywords=" + s).get();
                    Element element=doc.select("a.a-link-normal.s-access-detail-page").first();
                    name.append(element.text());
                    link.append(element.attr("href"));
                    element=doc.getElementsByClass("s-access-image").first();
                    imglink.append(element.attr("src"));
                    element=doc.getElementsByClass("a-size-base a-color-base s-size-mild").first();
                    price.append(element.text());
                    builder.append("\nTitle : "+name+"\nLink: "+link+"\n img: "+imglink+"\nprice: "+price);
                }catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                   // loadfragment(new ErrorFragment());
                  //  pb=(ProgressBar)findViewById(R.id.progressBar2);
                   // pb.setVisibility(View.INVISIBLE);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb=(ProgressBar)findViewById(R.id.progressBar2);
                        pb.setVisibility(View.INVISIBLE);
                        txtt=findViewById(R.id.textView2);
                        txtt.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
    public void getFlipkart(final String  s)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc =null;
                StringBuilder name = new StringBuilder();
                StringBuilder link = new StringBuilder();
                try{
                    doc =  Jsoup.connect("https://www.flipkart.com/search?q=" + s).get();
                    Element element=doc.select("_1Nyybr").first();
                    name.append(element.attr("src"));
                    builder.append("Flip Title : "+name);
                }catch (IOException e)
                {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }
                runOnUiThread(new Runnable(){
                    @Override
                            public void run() {
                        pb=(ProgressBar)findViewById(R.id.progressBar2);
                        pb.setVisibility(View.INVISIBLE);
                        txtt=findViewById(R.id.textView2);
                        txtt.setText(builder.toString());
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
        t2=(EditText)findViewById(R.id.editText2);
        prod.append(t2.getText());
        for (int index = 0; index < prod.length(); index++) {
            if (prod.charAt(index) == ' ') {
                prod.setCharAt(index, '+');
            }
        }
        Fragment fragobj= new ResultFragment();
        loadfragment(fragobj);
        getAmazon(prod.toString());
    }
    public void searchproduct_err(View view)
    {
        t=(EditText)findViewById(R.id.editText2);
        prod.append(t2.getText());
        for (int index = 0; index < prod.length(); index++) {
            if (prod.charAt(index) == ' ') {
                prod.setCharAt(index, '+');
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
        h.append(t.getText().toString());
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