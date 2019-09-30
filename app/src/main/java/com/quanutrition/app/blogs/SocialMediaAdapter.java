package com.quanutrition.app.blogs;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

import java.util.ArrayList;


public class SocialMediaAdapter extends RecyclerView.Adapter<SocialMediaAdapter.MyViewHolder> {

    private ArrayList<BlogsModel> blogList;
    private Context mCtx;
    private OnShare onShare;


    public interface OnShare{
        void onShare(String message, Uri image);
        void onSeeMoreClick(View v, int position);
    }

    public SocialMediaAdapter(Context mCtx, ArrayList<BlogsModel> blogList, OnShare onShare) {
        this.blogList = blogList;
        this.mCtx = mCtx;
        this.onShare = onShare;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {



        public ImageView image;
        public LinearLayout seeMore;
        public MyViewHolder(View view) {
            super(view);
            seeMore = view.findViewById(R.id.seeMore);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }


    @Override
    public SocialMediaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myth_fact_list_item, parent, false);

        return new SocialMediaAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SocialMediaAdapter.MyViewHolder holder, final int position) {

        final BlogsModel image = blogList.get(position);
        Tools.loadMythFact(image.getImageLink(),holder.image);

        if(holder.getAdapterPosition()==(blogList.size()-1)){
            holder.seeMore.setVisibility(View.VISIBLE);
        }else{
            holder.seeMore.setVisibility(View.GONE);
        }


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.getAdapterPosition() == (blogList.size() - 1))
                    onShare.onSeeMoreClick(view, holder.getAdapterPosition());
                else {
                    final Dialog dialog = new Dialog(mCtx);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(R.layout.image_view_dialog);
                    dialog.setCancelable(true);

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());

                    Display display = ((Activity) mCtx).getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;

                    lp.width = width;
                    lp.height = width;

                    final ImageView image = dialog.findViewById(R.id.image);

                    Tools.loadMythFact(blogList.get(position).getImageLink(), image);
                    ImageView share = dialog.findViewById(R.id.share);
                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Drawable mDrawable = image.getDrawable();
                            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
                            saveImage(mBitmap);

                        /*Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.putExtra(Intent.EXTRA_TEXT,Message);
                        shareIntent.setType("image/*");
                        mCtx.startActivity(Intent.createChooser(shareIntent, "Share Image"));*/


                        }

                    });
                    dialog.show();
                    dialog.getWindow().setAttributes(lp);
                }
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return blogList.size();
    }

    void saveImage(final Bitmap bm){
//        Uri uri = null;
        Dexter.withActivity((Activity)mCtx)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        try {

                            String path = MediaStore.Images.Media.insertImage(mCtx.getContentResolver(), bm, "Image I want to share", null);
                            Uri uri = Uri.parse(path);
                            String Message = "Hey!\n\nI Found this in Qua Nutrition Application.\n\nDownload the app now.";
                            onShare.onShare(Message,uri);

                        } catch(Exception e){
                            // some action
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                            Tools.initCustomToast(mCtx,"Need Permission to share images");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }



}