package com.quanutrition.app.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.integerResource
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.quanutrition.app.R
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivityNotesListBinding
import org.json.JSONException
import org.json.JSONObject

class NotesListActivity : AppCompatActivity() {
    lateinit var binding : ActivityNotesListBinding
    private lateinit var arrNotes : ArrayList<NotesModel>
    private lateinit var notesListAdapter: NotesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notes_list)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        arrNotes = ArrayList()
        binding.notesRe.layoutManager = LinearLayoutManager(this)
        notesListAdapter = NotesListAdapter()
        binding.notesRe.adapter = notesListAdapter

        notesListAdapter.clickListener = object : NotesListAdapter.OnClickListener{
            override fun onClick(position: Int) {
                val intent = Intent(this@NotesListActivity,NotesDetailActivity::class.java)
                intent.putExtra("id",arrNotes[position].id)
                startActivity(intent)
            }
        }
        fetchData()
    }

    fun fetchData() {
        val ad: AlertDialog = Tools.getDialog("Fetching data...", this@NotesListActivity)
        ad.show()
        val listener = Response.Listener<String?> { response ->
            ad.dismiss()
            Log.d("ResponseSlots", response!!)
            try {
                val ob = JSONObject(response)
                if (ob.getInt("res") === 1) {
                    val data = ob.getJSONArray("data")
                    binding.noData.visibility = View.VISIBLE
                    arrNotes.clear()
                    for (i in 0 until data.length()){
                        val noteDb = data.getJSONObject(i)
                        val notesModel = NotesModel(
                            noteDb.getString("id"),
                            noteDb.getString("title").toString(),
                            noteDb.getString("added_on").toString(),
                        )
                        arrNotes.add(notesModel)
                    }
                    if (arrNotes.size > 0){
                        notesListAdapter.data = arrNotes
                        binding.noData.visibility = View.GONE
                    }else{
                        binding.noData.visibility = View.VISIBLE
                    }
                } else {
                    Tools.initCustomToast(this@NotesListActivity, ob.getString("msg"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Log.d("myTag", "I am here")
        }
        val errorListener: Response.ErrorListener =
            Response.ErrorListener { error ->
                ad.dismiss()
                Tools.initNetworkErrorToast(this@NotesListActivity)
                Log.d("Error", error.toString())
                Log.d("myTag", "I am here")
            }
        NetworkManager.getInstance(this@NotesListActivity)
            .sendGetRequest(com.quanutrition.app.profile.Urls.GET_USER_NOTES, listener, errorListener, this@NotesListActivity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}