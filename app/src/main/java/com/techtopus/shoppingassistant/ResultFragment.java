package com.techtopus.shoppingassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;

import static com.google.android.gms.internal.zzagy.runOnUiThread;

public class ResultFragment extends Fragment {
    StringBuilder builder;
    TextView txt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_result, container, false);
         txt=view.findViewById(R.id.textView2);
        builder=new StringBuilder();
        String strtext = getArguments().getString("result");
        getAmazon(strtext);
        return view;
    }
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
            }
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       txt.append(builder.toString());
                   }
               });
            }
        }).start();
    }
}