package com.quanutrition.app.blogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivityTestimonialBinding
import org.json.JSONException
import org.json.JSONObject


class TestimonialActivity : AppCompatActivity() {

    lateinit var binding : ActivityTestimonialBinding

    private lateinit var arrTestimonial : ArrayList<TestimonialModel>
    private lateinit var testimonialListAdapter: TestimonialListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestimonialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        arrTestimonial = ArrayList()

        binding.testimonialRe.layoutManager = LinearLayoutManager(this)
        testimonialListAdapter = TestimonialListAdapter()
        binding.testimonialRe.adapter = testimonialListAdapter

        testimonialListAdapter.clickListener = object : TestimonialListAdapter.OnClickListener {
            override fun onClick(position: Int) {
                val intent = Intent(this@TestimonialActivity, BlogDetailsActivity::class.java)
                intent.putExtra("id", arrTestimonial[position].id)
                intent.putExtra("type", "4")
                startActivity(intent)
            }

        }
        fetchData()
    }
    fun fetchData() {
        val ad: AlertDialog = Tools.getDialog("Fetching data...", this@TestimonialActivity)
        ad.show()
        val listener = Response.Listener<String?> { response ->
            ad.dismiss()
            Log.d("ResponseSlots", response!!)
            try {
                val ob = JSONObject(response)
                if (ob.getInt("res") === 1) {
                    val data = ob.getJSONArray("data")
                    binding.noData.visibility = View.VISIBLE
                    arrTestimonial.clear()
                    for (i in 0 until data.length()){
                        val testimonial = data.getJSONObject(i)
                        val testimonialModel = TestimonialModel(
                            testimonial.getInt("id").toString(),
                            testimonial.getString("title"),
                            testimonial.getString("client_name"),
                            testimonial.getString("content"),
                            testimonial.getString("image"),
                            testimonial.getString("added_on"),
                            )
                        arrTestimonial.add(testimonialModel)
                    }
                    if (arrTestimonial.size > 0){
                        testimonialListAdapter.data = arrTestimonial
                        binding.noData.visibility = View.GONE
                    }else{
                        binding.noData.visibility = View.VISIBLE
                    }
                } else {
                    Tools.initCustomToast(this@TestimonialActivity, ob.getString("msg"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Log.d("myTag", "I am here")
        }
        val errorListener: Response.ErrorListener =
            Response.ErrorListener { error ->
                ad.dismiss()
                Tools.initNetworkErrorToast(this@TestimonialActivity)
                Log.d("Error", error.toString())
                Log.d("myTag", "I am here")
            }
        val url = Urls.GET_ALL_BLOG + "?type=4"
        NetworkManager.getInstance(this@TestimonialActivity)
            .sendGetRequest(url, listener, errorListener, this@TestimonialActivity)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}