package com.quanutrition.app.blogs;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

import java.util.ArrayList;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private ArrayList<BlogsModel> blogs;
    private Context mCtx;
    private OnItemClicked onItemClicked;

    public interface OnItemClicked{
        void onClick(View view, int position);
    }

    public RecipeAdapter(ArrayList<BlogsModel> blogs, Context mCtx, OnItemClicked onItemClicked) {
        this.blogs = blogs;
        this.mCtx = mCtx;
        this.onItemClicked = onItemClicked;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title,added_on,author;
        ImageView image;
        LinearLayout lyt_parent;
        public MyViewHolder(View view) {
            super(view);
            lyt_parent = view.findViewById(R.id.lyt_parent);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            added_on =  view.findViewById(R.id.added_on);
            author =  view.findViewById(R.id.author);

        }
    }


    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_row, parent, false);

        return new RecipeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipeAdapter.MyViewHolder holder, final int position) {

        final BlogsModel model = blogs.get(position);
        holder.title.setText(model.getTitle());
        if (!model.getAdded_on().isEmpty())
            holder.added_on.setText("Published On : "+model.getAdded_on());

        if (model.getAuthor().isEmpty()){
            holder.author.setVisibility(View.GONE);
        }else {
            holder.author.setVisibility(View.VISIBLE);
            holder.author.setText("Author : "+model.getAuthor());
        }

        Display display = ((Activity)mCtx).getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size. x;

        holder.image.requestLayout();
        holder.image.getLayoutParams().height = width/2;
        holder.image.getLayoutParams().width = width;

        if (!model.getImageLink().isEmpty())
            Tools.loadImageIntoImageView(model.getImageLink(),holder.image);
        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClicked.onClick(holder.image,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return blogs.size();
    }



}