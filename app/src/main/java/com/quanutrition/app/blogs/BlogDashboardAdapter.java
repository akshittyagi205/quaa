package com.quanutrition.app.blogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

import java.util.ArrayList;

public class BlogDashboardAdapter extends RecyclerView.Adapter<BlogDashboardAdapter.MyViewHolder>{

    private ArrayList<BlogsModel> blog;
    private Context mCtx;
    private OnClickListener clickListener;

    public BlogDashboardAdapter(ArrayList<BlogsModel> blog, Context mCtx, BlogDashboardAdapter.OnClickListener clickListener) {
        this.blog = blog;
        this.mCtx = mCtx;
        this.clickListener = clickListener;
    }

    public interface OnClickListener{
        void onClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        LinearLayout backLayout;
        ImageView image;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            backLayout =  view.findViewById(R.id.backLayout);
            image = view.findViewById(R.id.image);
        }
    }
    @NonNull
    @Override
    public BlogDashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.programs_dashborad_list_item, parent, false);

        return new BlogDashboardAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogDashboardAdapter.MyViewHolder holder, int position) {
        final BlogsModel model = blog.get(position);

        if(!model.getImageLink().isEmpty())
            Tools.loadImageIntoImageView(model.getImageLink(),holder.image);
        holder.name.setText(model.getTitle());
        holder.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(view,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return blog.size();
    }

}
