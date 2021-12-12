package com.quanutrition.app.general;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.AccountListModel;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.OnClickListener;
import com.quanutrition.app.Utils.Tools;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    private ArrayList<AccountListModel> list;
    private Context mCtx;
    OnClickListener onClickListener;


    public UserListAdapter(ArrayList<AccountListModel> list, Context mCtx, OnClickListener onClickListener) {
        this.list = list;
        this.mCtx = mCtx;
        this.onClickListener = onClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView email,current;
        public LinearLayout parent;
        public CircleImageView image;

        public MyViewHolder(View view) {
            super(view);

            email = view.findViewById(R.id.email);
            parent = view.findViewById(R.id.parent);
            image = view.findViewById(R.id.image);
            current = view.findViewById(R.id.current);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final AccountListModel model = list.get(position);
        holder.email.setText(model.getName());
        Tools.loadProfileImage(model.getImage(),holder.image);

        if(model.getId().equalsIgnoreCase(Tools.getGeneralSharedPref(mCtx).getString(Constants.USER_ID,"-1"))){
            holder.current.setVisibility(View.VISIBLE);
        }else{
            holder.current.setVisibility(View.GONE);
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.current.getVisibility()==View.GONE)
                    onClickListener.onClick(holder.getAdapterPosition());
                else
                    Tools.initCustomToast(mCtx,"This account is currently active!");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}