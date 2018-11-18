package com.techtopus.shoppingassistant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Recyclerview_Adapter extends RecyclerView.Adapter<Recyclerview_Adapter.ViewHolder>
{
    private static final String TAG = "Recyclerview_Adapter";

    private ArrayList<String> items=new ArrayList<>();
        private Context context;

    public Recyclerview_Adapter( Context context,ArrayList<String> items) {
        this.items = items;
        this.context = context;
    }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup,int i){

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
   //   Log.d(,"OnBindViewHolder called");
    try{    viewHolder.txt.setText(items.get(i));
    viewHolder.close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(context, items.get(i)+" is cleared", Toast.LENGTH_SHORT).show();
            viewHolder.txt.setText("");

            items.remove(items.get(i));



        }
    });}catch (NullPointerException e)
    {
        Toast.makeText(context, "Your wishlist is empty ", Toast.LENGTH_SHORT).show();
    }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ImageView  close;
        RelativeLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt=itemView.findViewById(R.id.itemname);
            close=itemView.findViewById(R.id.closebtn);
            parent=itemView.findViewById(R.id.parent_layout);

        }
    }
}
