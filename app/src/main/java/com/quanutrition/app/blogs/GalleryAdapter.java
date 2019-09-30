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
import android.widget.TextView;

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

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private ArrayList<ListModel> blogList;
    private Context mCtx;
    private OnShare onShare;
    int pos = 0;


    public interface OnShare{
        void onShare(String message, Uri image);
    }

    public GalleryAdapter(Context mCtx, ArrayList<ListModel> blogList, OnShare onShare) {
        this.blogList = blogList;
        this.mCtx = mCtx;
        this.onShare = onShare;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {



        public ImageView image;
        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }


    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_list_row, parent, false);

        return new GalleryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.MyViewHolder holder, final int position) {

        final ListModel image = blogList.get(position);

        Display display = ((Activity)mCtx).getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size. x;

        holder.image.requestLayout();
        holder.image.getLayoutParams().height = width/3;
        holder.image.getLayoutParams().width = width/3;


        Tools.loadMythFact(image.getName(),holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pos = position;
                final Dialog dialog = new Dialog(mCtx);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.gallery_list_big);
                dialog.setCancelable(true);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());

                Display display = ((Activity)mCtx).getWindowManager(). getDefaultDisplay();
                Point size = new Point();
                display. getSize(size);
                int width = size. x;



                final ImageView image = dialog.findViewById(R.id.image);
                final TextView next = dialog.findViewById(R.id.next);
                final TextView prev = dialog.findViewById(R.id.prev);
                image.requestLayout();
                image.getLayoutParams().height = width;
                image.getLayoutParams().width = width;

                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                Tools.loadMythFact(blogList.get(pos).getName(),image);
                LinearLayout share = dialog.findViewById(R.id.share);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable mDrawable = image.getDrawable();
                        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
                        saveImage(mBitmap);

                        /*Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.putExtra(Intent.EXTRA_TEXT,Message);
                        shareIntent.setType("image/*");
                        mCtx.startActivity(Intent.createChooser(shareIntent, "Share Image"));*/


                    }
                });

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(++pos <= (blogList.size()-1)) {
                            Tools.loadMythFact(blogList.get(pos).getName(), image);
                        }else{
                            pos--;
                            Tools.initCustomToast(mCtx,"No more images to show.");
                        }
                    }
                });
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(--pos >= 0) {
                            Tools.loadMythFact(blogList.get(pos).getName(), image);
                        }else{
                            pos++;
                            Tools.initCustomToast(mCtx,"No more images to show.");
                        }
                    }
                });
                dialog.show();
                dialog.getWindow().setAttributes(lp);
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
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
                            String Message = "Hey!\n\nI Found this in Nmami Life Application.\n\nDownload the app now.\n\nAndroid -- http://bit.ly/nmamiapp\nIos -- http://bit.ly/nmamiios";
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