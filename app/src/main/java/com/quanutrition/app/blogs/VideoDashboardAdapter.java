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

public class VideoDashboardAdapter extends RecyclerView.Adapter<VideoDashboardAdapter.MyViewHolder> {

    private ArrayList<BlogsModel> video;
    private Context mCtx;
    private OnClickListener clickListener;

    public VideoDashboardAdapter(ArrayList<BlogsModel> video, Context mCtx, VideoDashboardAdapter.OnClickListener clickListener) {
        this.video = video;
        this.mCtx = mCtx;
        this.clickListener = clickListener;
    }

    public interface OnClickListener {
        void onClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,description;
        LinearLayout backLayout;
        ImageView image,video_play;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            backLayout = view.findViewById(R.id.lyt_parent);
            image = view.findViewById(R.id.image);
            video_play = view.findViewById(R.id.video_play);
        }
    }

    @NonNull
    @Override
    public VideoDashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_row, parent, false);

        return new VideoDashboardAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoDashboardAdapter.MyViewHolder holder, int position) {
        final BlogsModel model = video.get(position);
        holder.description.setVisibility(View.GONE);
        holder.video_play.setVisibility(View.VISIBLE);
        String imageLink = "http://i3.ytimg.com/vi/" + model.getUrl() + "/hqdefault.jpg";
        if (!imageLink.isEmpty()){}
            Tools.loadImageIntoImageView(imageLink, holder.image);
        holder.name.setText(model.getTitle());
        holder.backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return video.size();
    }

}

