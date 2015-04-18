package com.erikbuttram.andygb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikbuttram.andygb.models.AwesomePojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
* Created by erikb on 4/17/15.
*/
class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.image);
            //If i wanted to do an onclick listener, this is where it would be implemented
            //with the containing class being the interface that listens for it (and a pos)
        }
    }


    public void setPojos(ArrayList<AwesomePojo> pojos) {
        this.mPojos.clear();
        this.mPojos = pojos;
        notifyDataSetChanged();
    }

    private ArrayList<AwesomePojo> mPojos;
    private LayoutInflater mInflater;
    //not sure I need it, buuut just in case
    private Context mContext;

    public DataAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mPojos = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View forHolder = mInflater.inflate(R.layout.cell, parent, false);

        return new ViewHolder(forHolder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AwesomePojo pojo = this.mPojos.get(position);
        holder.textView.setText(pojo.getName());
        if (pojo.getIcon() != null) {
            Picasso.with(mContext).load(pojo.getIcon()).into(holder.imageView);
        }
        //in an on click I would set the position for the holder here
        //If I was using the icon link, heres where I would set it
    }

    @Override
    public int getItemCount() {
        return mPojos.size();
    }
}
