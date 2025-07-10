package com.quanutrition.app.reports

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.quanutrition.app.R
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivityReportWebviewBinding
import com.quanutrition.app.reports.network.ReportAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class ReportWebviewActivity : AppCompatActivity() {

    lateinit var binding : ActivityReportWebviewBinding
    private var apiJob = Job()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_webview)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        binding.webview.settings.builtInZoomControls = true
        binding.webview.settings.useWideViewPort = true
        binding.webview.settings.loadWithOverviewMode = true
        binding.webview.settings.javaScriptEnabled = true
        if (intent.hasExtra("link")){
            binding.webview.loadUrl(intent.getStringExtra("link")!!)
        }else{
            fetchData()
        }
    }

    private fun fetchData() {
        coroutineScope.launch {
            val ad = Tools.getDialog("Loading...", this@ReportWebviewActivity)
            runOnUiThread(Runnable { ad.show() })
            try {
                val response = ReportAPI.retrofitService.getBloodPressureReport(
                    Tools.getHeaders(this@ReportWebviewActivity),)
                runOnUiThread(Runnable { ad.dismiss() })
                Log.d("response", response)
                val res = JSONObject(response)
                if (res.getInt("res") == 1){

                    if (res.getString("link").isNotEmpty()){
                        binding.webview.loadUrl(res.getString("link"))
                    }
                } else {
                    Tools.initCustomToast(this@ReportWebviewActivity, res.getString("msg"))
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread(Runnable { ad.dismiss() })
                Tools.initNetworkErrorToast(this@ReportWebviewActivity)
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}