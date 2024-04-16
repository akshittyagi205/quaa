package com.quanutrition.app.profile

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.quanutrition.app.R
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivityNotesDetailBinding
import org.json.JSONException
import org.json.JSONObject

class NotesDetailActivity : AppCompatActivity() {

    lateinit var binding : ActivityNotesDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notes_detail)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        fetchData()
    }

    fun fetchData() {
        val ad: AlertDialog = Tools.getDialog("Fetching data...", this@NotesDetailActivity)
        ad.show()
        val listener = Response.Listener<String?> { response ->
            ad.dismiss()
            Log.d("ResponseSlots", response!!)
            try {
                val ob = JSONObject(response)
                if (ob.getInt("res") === 1) {
                    val data = ob.getJSONObject("data")
                    binding.textNote.visibility = View.VISIBLE
                    Tools.setHTMLData(binding.textNote,data.getString("content"))
                } else {
                    binding.textNote.visibility = View.GONE
                    Tools.initCustomToast(this@NotesDetailActivity, ob.getString("msg"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Log.d("myTag", "I am here")
        }
        val errorListener: Response.ErrorListener =
            Response.ErrorListener { error ->
                ad.dismiss()
                binding.textNote.visibility = View.GONE
                Tools.initNetworkErrorToast(this@NotesDetailActivity)
                Log.d("Error", error.toString())
                Log.d("myTag", "I am here")
            }
        val url = Urls.GET_USER_NOTES + "?note_id="+intent.getStringExtra("id")
        NetworkManager.getInstance(this@NotesDetailActivity)
            .sendGetRequest(url, listener, errorListener, this@NotesDetailActivity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}