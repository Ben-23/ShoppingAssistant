package com.techtopus.shoppingassistant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techtopus.shoppingassistant.Recyclerview_Adapter.ViewHolder;

import java.util.ArrayList;

public class Recyclerview_Adapter2 extends RecyclerView.Adapter<Recyclerview_Adapter2.viewholder> {



    private ArrayList<String> prodimg = new ArrayList<String>();
    private ArrayList<String> prodname = new ArrayList<String>();
    private ArrayList<String> prodprice = new ArrayList<String>();
    private ArrayList<String> prodlink = new ArrayList<String>();

    private Context context;

    public Recyclerview_Adapter2(Context context, ArrayList<String> prodimg, ArrayList<String> prodname, ArrayList<String> prodprice,ArrayList<String> prodlink) {
        this.prodimg = prodimg;
        this.prodname = prodname;
        this.prodprice = prodprice;
        this.prodlink = prodlink;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list, viewGroup, false);
        viewholder holder = new viewholder(view2);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final viewholder viewHolder2, final int i) {
     viewHolder2.t1.setText(prodname.get(i));
        viewHolder2.t2.setText(prodprice.get(i));

        viewHolder2.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(prodlink.get(i)));
                context.startActivity(browserIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return prodname.size();
    }

    public class viewholder extends ViewHolder {
        ImageView prodimg;
        TextView t1, t2;
        RelativeLayout rl;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            prodimg = (ImageView) itemView.findViewById(R.id.prodimg);
            t1 = (TextView) itemView.findViewById(R.id.prod_name);
            t2 = (TextView) itemView.findViewById(R.id.prod_price);
            rl = (RelativeLayout) itemView.findViewById(R.id.pro_layout);


        }
    }
}
