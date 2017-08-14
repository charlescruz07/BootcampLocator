package com.cruz.bootcamplocator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Acer on 14/08/2017.
 */

public class BootcampListAdapter extends RecyclerView.Adapter<BootcampListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Bootcamp> bootcamps;

    public BootcampListAdapter(Context context, ArrayList<Bootcamp> bootcamps) {
        this.context = context;
        this.bootcamps = bootcamps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bootcamp_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(bootcamps.get(position).getName());
        holder.location.setText(bootcamps.get(position).getAddress());
        Glide.with(context).load(bootcamps.get(position).getPictureUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return bootcamps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, location;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
        }
    }
}
