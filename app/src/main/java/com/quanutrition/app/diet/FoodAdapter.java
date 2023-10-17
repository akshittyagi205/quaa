package com.quanutrition.app.diet;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.selectiondialogs.DialogUtils;
import com.quanutrition.app.selectiondialogs.SingleSelectionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    private ArrayList<FoodDataModel> foodList;
    private Context mCtx;
    private boolean recipe_flag = false;

    private OnDiaryDataChanged onDiaryDataChanged;

    public void setOnDiaryDataChanged(OnDiaryDataChanged onDiaryDataChanged) {
        this.onDiaryDataChanged = onDiaryDataChanged;
    }

    private boolean taken_flag = false;

    public void setTaken_flag(boolean taken_flag) {
        this.taken_flag = taken_flag;
    }

    public FoodAdapter(ArrayList<FoodDataModel> foodList, Context mCtx, boolean recipe_flag) {
        this.foodList = foodList;
        this.mCtx = mCtx;
        this.recipe_flag = recipe_flag;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView foodName,foodQuant,foodCal,foodNotes,takenStatus;
        ImageView showOption;
        RecyclerView food_options_re;
        CardView optionsCard;
        LinearLayout foodLayout,takenLayout;
        AppCompatImageView cart,taken,missed;
        public MyViewHolder(View view) {
            super(view);
            foodName = view.findViewById(R.id.foodName);
            foodQuant =  view.findViewById(R.id.foodQuant);
            foodCal = view.findViewById(R.id.foodCal);
            foodNotes = view.findViewById(R.id.foodNotes);
            showOption = view.findViewById(R.id.showOption);
            food_options_re = view.findViewById(R.id.food_options_re);
            optionsCard = view.findViewById(R.id.optionsCard);
            foodLayout = view.findViewById(R.id.foodLayout);
            cart = view.findViewById(R.id.cart);
            takenLayout = view.findViewById(R.id.takenLayout);
            taken = view.findViewById(R.id.taken);
            missed = view.findViewById(R.id.missed);
            takenStatus = view.findViewById(R.id.takenStatus);
        }
    }


    @Override
    public FoodAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_list_item, parent, false);

        return new FoodAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FoodAdapter.MyViewHolder holder, final int position) {

        final FoodDataModel meal = foodList.get(position);
        holder.food_options_re.setVisibility(View.GONE);
        holder.optionsCard.setVisibility(View.GONE);
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
        if(meal.getOptionsList().size()==0){
            holder.showOption.setVisibility(View.GONE);
        }else{
            holder.showOption.setVisibility(View.GONE);
            FoodOptionsAdapter foodAdapter = new FoodOptionsAdapter(meal.getOptionsList(), mCtx,recipe_flag);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mCtx);
            holder.food_options_re.setLayoutManager(layoutManager);
            holder.food_options_re.setAdapter(foodAdapter);
            holder.food_options_re.setVisibility(View.VISIBLE);
            holder.optionsCard.setVisibility(View.VISIBLE);
            holder.food_options_re.startAnimation(AnimationUtils.loadAnimation(mCtx,android.R.anim.fade_in));
        }
        holder.showOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.food_options_re.getVisibility()== View.GONE) {
                    Animation an = new RotateAnimation(0.0f, 180.0f, holder.showOption.getPivotX(), holder.showOption.getPivotY());
                    // Set the animation's parameters
                    an.setDuration(300);               // duration in ms
                    an.setRepeatCount(0);                // -1 = infinite repeated
                    an.setRepeatMode(Animation.REVERSE); // reverses each repeat
                    an.setFillAfter(true);               // keep rotation after animation
                    // Aply animation to image view
                    holder.showOption.startAnimation(an);
                    FoodOptionsAdapter foodAdapter = new FoodOptionsAdapter(meal.getOptionsList(), mCtx,recipe_flag);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mCtx);
                    holder.food_options_re.setLayoutManager(layoutManager);
                    holder.food_options_re.setAdapter(foodAdapter);
                    holder.food_options_re.setVisibility(View.VISIBLE);
                    holder.optionsCard.setVisibility(View.VISIBLE);
                    holder.food_options_re.startAnimation(AnimationUtils.loadAnimation(mCtx,android.R.anim.fade_in));
                }else{
                    Animation an = new RotateAnimation(180.0f, 0f, holder.showOption.getPivotX(), holder.showOption.getPivotY());
                    // Set the animation's parameters
                    an.setDuration(300);               // duration in ms
                    an.setRepeatCount(0);                // -1 = infinite repeated
                    an.setRepeatMode(Animation.REVERSE); // reverses each repeat
                    an.setFillAfter(true);               // keep rotation after animation
                    // Aply animation to image view
                    holder.showOption.startAnimation(an);
                    holder.food_options_re.startAnimation(AnimationUtils.loadAnimation(mCtx,android.R.anim.fade_out));
                    holder.food_options_re.setVisibility(View.GONE);
                    holder.optionsCard.setVisibility(View.GONE);
                }
            }
        });

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

        if(taken_flag){
            holder.foodCal.setVisibility(View.GONE);
            if(meal.getIsTaken().equalsIgnoreCase("0")){
                holder.takenStatus.setVisibility(View.GONE);
                holder.takenLayout.setVisibility(View.VISIBLE);
            }else{
                holder.takenStatus.setVisibility(View.VISIBLE);
                holder.takenLayout.setVisibility(View.GONE);
                if(meal.getIsTaken().equalsIgnoreCase("1")){
                    //Taken
                    holder.takenStatus.setText("Taken");
                    holder.takenStatus.setBackgroundResource(R.drawable.taken_back);
                }else{
                    //Missed
                    holder.takenStatus.setText("Missed");
                    holder.takenStatus.setBackgroundResource(R.drawable.missed_back);
                }
            }
        }else{
            holder.takenLayout.setVisibility(View.GONE);
            holder.takenStatus.setVisibility(View.GONE);
            if(meal.isFoodCalorieFlag())
            holder.foodCal.setVisibility(View.VISIBLE);
        }

        holder.taken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDiaryDataChanged != null) {
                    if (meal.getIsTaken().equalsIgnoreCase("0")) {
                        if (meal.getOptionsList().size() == 0) {
                            String name = meal.getFoodName();
                            if (!meal.getFoodQuant().trim().isEmpty())
                                name += "( " + meal.getFoodQuant() + " )";
                            DiaryDataModel model = new DiaryDataModel(meal.getFoodId(), name, "", true, false);
                            onDiaryDataChanged.onDataChanged(model,holder.getAdapterPosition());
                        } else {
                            final String[] data = new String[meal.getOptionsList().size() + 1];
                            String name = meal.getFoodName();
                            if (!meal.getFoodQuant().trim().isEmpty())
                                name += "( " + meal.getFoodQuant() + " )";
                            data[0] = name;
                            Log.d("Data 0",data[0]);
                            for (int i = 1; i <=meal.getOptionsList().size(); i++) {
                                data[i] = meal.getOptionsList().get(i - 1).getFoodName();
                                Log.d("Data "+i,data[i]);
                            }

                            DialogUtils.getSingleSelectionDialog(mCtx, "What did you eat?", DialogUtils.getSingleArrayListWithStringArray(data), new DialogUtils.OnSingleItemSelectedListener() {
                                @Override
                                public void onItemSelected(int position, SingleSelectionModel item) {
                                    String selected = item.getLabel();
                                    int pos = 0;
                                    for (int i = 0; i < data.length; i++) {
                                        if (data[i].equalsIgnoreCase(selected)) {
                                            pos = i;
                                            break;
                                        }
                                    }
                                    boolean optionFlag = false;
                                    if (pos != 0)
                                        optionFlag = true;

                                    if(optionFlag) {
                                        DiaryDataModel model = new DiaryDataModel(meal.getFoodId(), selected, selected, true, true);
                                        onDiaryDataChanged.onDataChanged(model, holder.getAdapterPosition());
                                    }else{
                                        DiaryDataModel model = new DiaryDataModel(meal.getFoodId(), selected, "", true, false);
                                        onDiaryDataChanged.onDataChanged(model, holder.getAdapterPosition());
                                    }
                                }
                            });
                        }
                    } else if(meal.getIsTaken().equalsIgnoreCase("1")){
                        //edit taken food
                    }else{
                        //edit missed food
                    }
                }
            }
        });

        holder.missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDiaryDataChanged!=null){

                    if(meal.getIsTaken().equalsIgnoreCase("0")){

                        DialogUtils.getDialogWithTitle(mCtx, "What did you eat instead?", new DialogUtils.OnCustomItemPicked() {
                            @Override
                            public void onNumberPicked(String selected) {
                                DiaryDataModel model = new DiaryDataModel(meal.getFoodId(), "", selected, false, false);
                                onDiaryDataChanged.onDataChanged(model,holder.getAdapterPosition());
                            }
                        });

                    }

                }
            }
        });

        holder.takenStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDiaryDataChanged!=null){

                    if(meal.getIsTaken().equalsIgnoreCase("1")){
                        DialogUtils.getTakenDialog(mCtx, new DialogUtils.OnCustomItemPicked() {
                            @Override
                            public void onNumberPicked(String selected) {
                                if(selected.equalsIgnoreCase("2")) {
                                    meal.setIsTaken("0");
                                    holder.missed.callOnClick();
                                }
                            }
                        });
                    }else if(meal.getIsTaken().equalsIgnoreCase("2")){
                        DialogUtils.getMissedDialog(mCtx, meal.getFoodEaten(), new DialogUtils.OnCustomItemPicked() {
                            @Override
                            public void onNumberPicked(String selected) {
                                meal.setIsTaken("0");
                                holder.taken.callOnClick();
                            }
                        });
                    }

                }
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