package com.quanutrition.app.blogs;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RecipesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    RecyclerView blogs_re;
    ArrayList<BlogsModel> blogList;
    RecipeAdapter blogsAdapter;
    View rootView;
    RelativeLayout noData;
    Context context;

    public RecipesFragment() {
        // Required empty public constructor
    }

    public static RecipesFragment newInstance(String param1, String param2) {
        RecipesFragment fragment = new RecipesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_blogs, container, false);
        context = getActivity();
        blogs_re = rootView.findViewById(R.id.blogs_re);
        blogList = new ArrayList<>();
        noData = rootView.findViewById(R.id.noData);
        noData.setVisibility(View.GONE);
        blogsAdapter = new RecipeAdapter(blogList, context, new RecipeAdapter.OnItemClicked() {
            @Override
            public void onClick(View view, int position) {
                BlogsModel model = blogList.get(position);
                if(!model.getType().equalsIgnoreCase("2")) {
                    Intent intent = new Intent(getActivity(), BlogDetailsActivity.class);
                    intent.putExtra("id", model.getId() + "");
                    final ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, view.getTransitionName());
                    startActivity(intent,options.toBundle());
                }else{
                    Intent intent = new Intent(getActivity(), YoutubePlayActivity.class);
                    intent.putExtra("video", model.getLink() + "");
                    startActivity(intent);
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        blogs_re.setLayoutManager(layoutManager);
        blogs_re.setAdapter(blogsAdapter);

        fetchData();

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...",getActivity());
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1){
                        blogList.clear();
                        JSONArray data = ob.getJSONArray("data");
                        for(int i=0;i<data.length();i++){
                            JSONObject blog = data.getJSONObject(i);
                            ArrayList<String> tags = new ArrayList<>();
                            tags.add("Trending");
                            BlogsModel model = new BlogsModel(blog.getInt("id")+"",blog.getString("title"),blog.optString("url"),blog.getString("image"),blog.getString("author"),blog.getString("content"));
                            model.setType(blog.optString("type","3"));
                            model.cal = blog.getString("calories");
                            model.cookingTime = blog.getString("cooking_time");
                            blogList.add(model);
                        }
                        blogsAdapter.notifyDataSetChanged();
                    }else{
                        Tools.initCustomToast(getActivity(),ob.getString("msg"));
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
                Tools.initNetworkErrorToast(getActivity());
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        NetworkManager.getInstance(getActivity()).sendGetRequest(Urls.GET_ALL_RECIPES,listener,errorListener,getActivity());

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu_list,menu);


        MenuItem searchViewItem = menu.findItem(R.id.actionsearch);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search blog posts...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);// Do not iconify the widget; expand it by defaul

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(final String newText) {
                // This is your adapter that will be filtered
                //Toast.makeText(getApplicationContext(),"textChanged :"+newText,Toast.LENGTH_LONG).show();


                if (newText.trim().isEmpty()) {
//                    searchText = null;
                    Log.d("Dietitian SearchContent", "Text is Empty");
//                    search_re.setVisibility(View.GONE);
//                    re.setVisibility(View.VISIBLE);
                    blogs_re.setAdapter(blogsAdapter);
                    if(blogList.size()>0){
                        noData.setVisibility(View.GONE);
                    }else{
                        noData.setVisibility(View.VISIBLE);
                    }
                }else {
                    Log.d("New Text", newText);

                    final ArrayList<BlogsModel> filteredList = new ArrayList<>();
                    for(int i=0;i<blogList.size();i++){
                        BlogsModel overviewModel = blogList.get(i);
                        if(overviewModel.getTitle().toLowerCase().contains(newText.toLowerCase())){
                            filteredList.add(overviewModel);
                        }
                    }

                    BlogsAdapter filterAdapter = new BlogsAdapter(filteredList, context, new BlogsAdapter.OnItemClicked() {
                        @Override
                        public void onClick(View view, int position) {
                            BlogsModel model = filteredList.get(position);
                            if(!model.getType().equalsIgnoreCase("2")) {
                                Intent intent = new Intent(getActivity(), BlogDetailsActivity.class);
                                intent.putExtra("id", model.getId() + "");
                                startActivity(intent);
                            }else{
                                //type 1
                                Intent intent = new Intent(getActivity(), YoutubePlayActivity.class);
                                intent.putExtra("video", model.getLink() + "");
                                startActivity(intent);
                            }
                        }
                    });
                    blogs_re.setAdapter(filterAdapter);

                    if(filteredList.size()>0){
                        noData.setVisibility(View.GONE);
                    }else{
                        noData.setVisibility(View.VISIBLE);
                    }
                }

                return true;
            }

            public boolean onQueryTextSubmit(final String query) {
                // **Here you can get the value "query" which is entered in the search box.**
                Log.d("New Text",query);

                if (query.trim().isEmpty()) {
                    blogs_re.setAdapter(blogsAdapter);
                    if(blogList.size()>0){
                        noData.setVisibility(View.GONE);
                    }else{
                        noData.setVisibility(View.VISIBLE);
                    }
                }else {
                    Log.d("New Text", query);
                    final ArrayList<BlogsModel> filteredList = new ArrayList<>();
                    for(int i=0;i<blogList.size();i++){
                        BlogsModel overviewModel = blogList.get(i);
                        if(overviewModel.getTitle().toLowerCase().contains(query.toLowerCase())){
                            filteredList.add(overviewModel);
                        }
                    }

                    BlogsAdapter filterAdapter = new BlogsAdapter(filteredList, context, new BlogsAdapter.OnItemClicked() {
                        @Override
                        public void onClick(View view, int position) {
                            BlogsModel model = filteredList.get(position);
                            if(!model.getType().equalsIgnoreCase("2")) {
                                Intent intent = new Intent(getActivity(), BlogDetailsActivity.class);
                                intent.putExtra("id", model.getId() + "");
                                startActivity(intent);
                            }else{
                                //type 1
                                Intent intent = new Intent(getActivity(), YoutubePlayActivity.class);
                                intent.putExtra("video", model.getLink() + "");
                                startActivity(intent);
                            }
                        }
                    });
                    blogs_re.setAdapter(filterAdapter);
                    if(filteredList.size()>0){
                        noData.setVisibility(View.GONE);
                    }else{
                        noData.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        };
        searchView.setOnQueryTextListener(queryTextListener);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                Log.d("On Close","I fucked up!");
                return false;
            }
        });



    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.filter){
            showFilters();
        }

        return super.onOptionsItemSelected(item);
    }*/


    void showFilters(){

        final String[] items = {
                "All",
                "Blog Posts",
                "Videos",
                "Recipes"
        };


        new AlertDialog.Builder(getActivity())
                .setTitle("Apply Filters")
                .setSingleChoiceItems(items, 0, null)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        if (!(selectedPosition == 0)) {
                            final ArrayList<BlogsModel> filteredList = new ArrayList<>();
                            for (int i = 0; i < blogList.size(); i++) {

                                if(blogList.get(i).getType().equalsIgnoreCase(selectedPosition+"")){
                                    filteredList.add(blogList.get(i));
                                }

                            }
                            if (filteredList.size() > 0) {

                                BlogsAdapter filteredAdapter = new BlogsAdapter( filteredList,getActivity(), new BlogsAdapter.OnItemClicked() {
                                    @Override
                                    public void onClick(View v, int position) {
                                        BlogsModel model = filteredList.get(position);
                                        if(!model.getType().equalsIgnoreCase("2")) {
                                            Intent intent = new Intent(getActivity(), BlogDetailsActivity.class);
                                            intent.putExtra("id", model.getId() + "");
                                            startActivity(intent);
                                        }else{
                                            //type 1
                                            Intent intent = new Intent(getActivity(), YoutubePlayActivity.class);
                                            intent.putExtra("video", model.getLink() + "");
                                            startActivity(intent);
                                        }
                                    }
                                });

                                blogs_re.setAdapter(filteredAdapter);
                            } else {
                                Tools.initCustomToast(getActivity(), "No Posts in this category");
                            }
                            // Do something useful withe the position of the selected radio button
                        }else{
                            blogs_re.setAdapter(blogsAdapter);
                        }
                    }
                })
                .show();
    }


}
