package com.quanutrition.app.general;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import java.util.ArrayList;



public class ContactUsAdapter extends RecyclerView.Adapter<ContactUsAdapter.MyViewHolder> {

    private ArrayList<ContactUsModel> contactList;
    private Context mCtx;


    public ContactUsAdapter(ArrayList<ContactUsModel> contactList, Context mCtx) {
        this.contactList = contactList;
        this.mCtx = mCtx;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView title,email,phone,address;
        public LinearLayout addressLayout,phoneLayout,emailLayout;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView)  view.findViewById(R.id.clinicTitle);
            email = (TextView)  view.findViewById(R.id.email);
            phone = (TextView)  view.findViewById(R.id.phone);
            address = (TextView)  view.findViewById(R.id.address);
            addressLayout = (LinearLayout)  view.findViewById(R.id.addressLayout);
            emailLayout = (LinearLayout)  view.findViewById(R.id.emailLayout);
            phoneLayout = (LinearLayout)  view.findViewById(R.id.phoneLayout);
        }
    }


    @Override
    public ContactUsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_row, parent, false);

        return new ContactUsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactUsAdapter.MyViewHolder holder, int position) {

        final ContactUsModel contact = contactList.get(position);
        holder.title.setText(contact.getTitle());
        holder.address.setText(contact.getAddress());
        holder.email.setText(contact.getEmail());
        holder.phone.setText(contact.getPhone());
//        holder.addressLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String geoUri = "https://goo.gl/maps/"+ contact.getAddressId() ;
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
//                mCtx.startActivity(intent);
//            }
//        });
        holder.emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL,new String[] { contact.getEmail() });
                mCtx.startActivity(intent);
            }
        });
        holder.phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +contact.getPhone()));
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return contactList.size();
    }



}