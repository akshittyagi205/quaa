package com.quanutrition.app.dietextras

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.android.volley.Response
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivityIngredientsBinding
import com.quanutrition.app.diet.MealModel
import org.json.JSONException
import org.json.JSONObject

class IngredientsActivity : AppCompatActivity() {
    lateinit var binding : ActivityIngredientsBinding
    lateinit var list : ArrayList<IngredientsModel>
    lateinit var adapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        list = ArrayList()

        adapter = IngredientsAdapter()
        adapter.data = list
        binding.re.adapter = adapter

        fetchData()
    }

    private fun fetchData() {
        val ad = Tools.getDialog("Loading...", this)
        ad.show()

        val listener  = Response.Listener<String> { response ->
            Log.d("response", response)
            ad.dismiss()
            try{
                val res = JSONObject(response!!)
                if(res.getInt("res")==1){
                    val data = res.getJSONArray("data")
                    list.clear()
                    for(i in 0 until data.length()){

                        val ob = data.getJSONObject(i)
                        list.add(IngredientsModel(ob.getString("name"),ob.getString("quantity"),ob.getString("calorie"),ob.getString("fat"),ob.getString("carbs"),ob.getString("protein"),false))
                    }
                    adapter.notifyDataSetChanged()

                    if(list.isEmpty()){
                        binding.noData.visibility = View.VISIBLE
                    }else{
                        binding.noData.visibility = View.GONE
                    }
                }else{
                    binding.noData.visibility = View.VISIBLE
                }
            }catch (e : JSONException){
                e.printStackTrace()
            }
        }

        val errorListener = Response.ErrorListener {
            ad.dismiss()
            Tools.initNetworkErrorToast(this)
            binding.noData.visibility = View.VISIBLE
        }

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.GET_INGREDIENTS,HashMap<String,String>(),listener, errorListener, this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}