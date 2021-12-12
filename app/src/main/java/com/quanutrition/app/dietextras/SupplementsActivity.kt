package com.quanutrition.app.dietextras

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivitySupplementsBinding
import com.quanutrition.app.diet.FoodDataModel
import com.quanutrition.app.diet.FoodOptionsModel
import com.quanutrition.app.diet.MealModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SupplementsActivity : AppCompatActivity() {

    lateinit var binding : ActivitySupplementsBinding
    lateinit var list : ArrayList<SupplementModel>
    lateinit var adapter: SupplementsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupplementsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        list = ArrayList()

        adapter = SupplementsAdapter(this)
        adapter.data = list
        binding.re.adapter = adapter

        fetchData()

    }

    fun fetchData(){
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
                        val listMeal = ArrayList<MealModel>()
                        listMeal.addAll(getData(ob.getJSONArray("meal")))
                        if(listMeal.size>0)
                        list.add(SupplementModel(ob.getString("day"),getData(ob.getJSONArray("meal"))))

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
            binding.noData.visibility = View.VISIBLE
            Tools.initNetworkErrorToast(this)
        }

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.GET_SUPPLEMENTS,HashMap<String,String>(),listener, errorListener, this)
    }

    @Throws(JSONException::class)
    fun getData(foodData: JSONArray) : ArrayList<MealModel> {
        val mealList = ArrayList<MealModel>()
        mealList.clear()
        for (i in 0 until foodData.length()) {
            val ob = foodData.getJSONObject(i)
            val macro = ob.getJSONObject("mealmacro")
            val foodArray = ob.getJSONArray("fooddata")
            val foodList = java.util.ArrayList<FoodDataModel>()
            for (j in 0 until foodArray.length()) {
                val food = foodArray.getJSONObject(j)
                val foodMacro = food.getJSONObject("macro")
                val options = food.getJSONArray("options")
                val optionsList = java.util.ArrayList<FoodOptionsModel>()
                for (k in 0 until options.length()) {
                    val optionFood = options.getJSONObject(k)
                    val optionMacro = optionFood.getJSONObject("macro")
                    val optionsModel = FoodOptionsModel(optionFood.getString("id"), optionFood.getString("name"), optionMacro.getString("cal"), optionFood.getString("quantity") + " " + optionFood.getString("measure"), optionFood.optString("note", ""), false)
                    optionsModel.url = optionFood.optString("url", "")
                    optionsList.add(optionsModel)
                }
                val foodDataModel = FoodDataModel(food.getString("id"), food.getString("name"), foodMacro.getString("cal"), food.getString("quantity") + " " + food.getString("measure"), food.getString("note"), false, optionsList)
                foodDataModel.url = food.optString("url", "")
                foodList.add(foodDataModel)
            }
            val meal = MealModel(i.toString() + "", ob.getString("mealname"), ob.getString("mealtime"), macro.getString("cal"), true, false, foodList)
            meal.mealNotes = ob.getString("mealnote")
            meal.isMacro_flag = false
            meal.macros = macro.toString()
            meal.isRecipe_flag = false
            if (foodList.size > 0) mealList.add(meal)
        }

        return mealList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}