package com.quanutrition.app.general


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.quanutrition.app.R
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivityNotificationBinding
import org.json.JSONException
import org.json.JSONObject

class NotificationActivity : AppCompatActivity() {
    lateinit var binding : ActivityNotificationBinding
    lateinit var arrNotification : ArrayList<NotificationModel>
    lateinit var notificationAdapter: NotificationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        arrNotification = ArrayList()

        binding.notificationRe.layoutManager = LinearLayoutManager(this)
        notificationAdapter = NotificationAdapter()
        binding.notificationRe.adapter = notificationAdapter

        fetchData()
    }

    fun fetchData() {
        val ad: AlertDialog = Tools.getDialog("Fetching data...", this@NotificationActivity)
        ad.show()
        val listener = Response.Listener<String?> { response ->
            ad.dismiss()
            Log.d("ResponseSlots", response!!)
            try {
                val ob = JSONObject(response)
                if (ob.getInt("res") === 1) {
                    val data = ob.getJSONArray("data")
                    binding.noData.visibility = View.VISIBLE
                    for (i in 0 until data.length()){
                        val notification = data.getJSONObject(i)
                        val notificationModel = NotificationModel(notification.getString("id"),notification.getString("title"),notification.getString("text"),notification.getString("tag"),notification.getString("timestamp"))
                        arrNotification.add(notificationModel)
                    }
                    if (arrNotification.size > 0){
                        notificationAdapter.data = arrNotification
                        binding.noData.visibility = View.GONE
                    }else{
                        binding.noData.visibility = View.VISIBLE
                    }
                } else {
                    Tools.initCustomToast(this@NotificationActivity, ob.getString("msg"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Log.d("myTag", "I am here")
        }
        val errorListener: Response.ErrorListener =
            Response.ErrorListener { error ->
                ad.dismiss()
                Tools.initNetworkErrorToast(this@NotificationActivity)
                Log.d("Error", error.toString())
                Log.d("myTag", "I am here")
            }
        NetworkManager.getInstance(this@NotificationActivity)
            .sendGetRequest(com.quanutrition.app.general.Urls.Get_notification, listener, errorListener, this@NotificationActivity)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}