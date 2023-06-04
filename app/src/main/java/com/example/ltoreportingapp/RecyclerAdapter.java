package com.example.ltoreportingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final ArrayList<Uri> uriArrayList;
    private final Context context;
    CountOfImageWhenRemove countOfImageWhenRemove;

    private final itemClickListener itemClickListener;

    public RecyclerAdapter(ArrayList<Uri> uriArrayList, Context context, CountOfImageWhenRemove countOfImageWhenRemove,
                           itemClickListener itemClickListener) {
        this.uriArrayList = uriArrayList;
        this.context = context;
        this.countOfImageWhenRemove = countOfImageWhenRemove;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.custom_single_image,parent,false);

        return new ViewHolder(view, countOfImageWhenRemove,itemClickListener);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        //holder.imageView.setImageURI(uriArrayList.get(position));

        Glide.with(context)
                .load(uriArrayList.get(position))
                .into(holder.imageView);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriArrayList.remove(uriArrayList.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
                countOfImageWhenRemove.clicked(uriArrayList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView delete;
        ShapeableImageView imageView;
        CountOfImageWhenRemove countOfImageWhenRemove;
        itemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView, CountOfImageWhenRemove countOfImageWhenRemove, itemClickListener itemClickListener) {
            super(itemView);
            this.countOfImageWhenRemove = countOfImageWhenRemove;
            imageView=itemView.findViewById(R.id.image);
            delete= itemView.findViewById(R.id.delete);

            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
            {
                itemClickListener.itemClick(getAdapterPosition());
            }
        }
    }
    public interface CountOfImageWhenRemove{
        void clicked(int getSize);
    }
    public interface itemClickListener{

        void itemClick(int position);
    }
}
