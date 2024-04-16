package com.quanutrition.app.googlefit.healthconnect

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.quanutrition.app.R
import com.quanutrition.app.databinding.ActivityHealthConnectBinding
import com.quanutrition.app.databinding.ActivityHealthConnectWebviewBinding

class HealthConnectWebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityHealthConnectWebviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthConnectWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.htmlNotes.loadUrl("https://www.zoconut.com/privacy")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}