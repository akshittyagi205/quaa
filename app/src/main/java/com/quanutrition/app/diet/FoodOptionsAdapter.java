package com.quanutrition.app.diet;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FoodOptionsAdapter extends RecyclerView.Adapter<FoodOptionsAdapter.MyViewHolder> {

    private ArrayList<FoodOptionsModel> foodList;
    private Context mCtx;
    private boolean recipe_flag;


    public FoodOptionsAdapter(ArrayList<FoodOptionsModel> foodList, Context mCtx, boolean recipe_flag) {
        this.foodList = foodList;
        this.mCtx = mCtx;
        this.recipe_flag = recipe_flag;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView foodName,foodQuant,foodCal,foodNotes;
        LinearLayout foodLayout;
        AppCompatImageView cart;
        public MyViewHolder(View view) {
            super(view);
            foodName = view.findViewById(R.id.foodName);
            foodQuant =  view.findViewById(R.id.foodQuant);
            foodCal = view.findViewById(R.id.foodCal);
            foodNotes = view.findViewById(R.id.foodNotes);
            foodLayout = view.findViewById(R.id.foodLayout);
            cart = view.findViewById(R.id.cart);
        }
    }


    @Override
    public FoodOptionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_option_list_row, parent, false);

        return new FoodOptionsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FoodOptionsAdapter.MyViewHolder holder, final int position) {

        final FoodOptionsModel meal = foodList.get(position);
        if(meal.isFoodCalorieFlag()){
            holder.foodCal.setVisibility(View.VISIBLE);
            holder.foodCal.setText(meal.getFoodCalories()+" KCal.");
        }else{
            holder.foodCal.setVisibility(View.GONE);
        }
        holder.foodName.setText(meal.getFoodName());
        holder.foodQuant.setText(meal.getFoodQuant());
        if(meal.getFoodNotes().trim().isEmpty()){
            holder.foodNotes.setVisibility(View.GONE);
        }else {
            holder.foodNotes.setText(meal.getFoodNotes());
        }

        if(recipe_flag) {
            holder.foodLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchData(meal.getFoodId());
                }
            });
        }

        if(meal.getUrl().trim().isEmpty()){
            holder.cart.setVisibility(View.GONE);
        }else{
            holder.cart.setVisibility(View.VISIBLE);
        }

        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = meal.getUrl();
                if (!link.contains("https://"))
                    link = "https://"+link;

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return foodList.size();
    }

    void fetchData(final String id){
        final AlertDialog ad = Tools.getDialog("Fetching data...", mCtx);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1){
                        JSONObject data = ob.getJSONObject("data");
                        String title = data.getString("title");
                        String body = data.getString("content");
                        String image = data.getString("image");

                        Intent intent = new Intent(mCtx,RecipeActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("title",title);
                        intent.putExtra("body",body);
                        intent.putExtra("image",image);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mCtx.startActivity(intent);
                    }else{
                        Tools.initCustomToast(mCtx,ob.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(mCtx);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        String url = Urls.GET_RECIPE +"?id="+id;
        NetworkManager.getInstance(mCtx).sendGetRequest(url,listener,errorListener, mCtx);

    }

}