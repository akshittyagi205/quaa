package com.quanutrition.app.reports

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import com.quanutrition.app.R
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.composables.ComposeConstants
import com.quanutrition.app.databinding.ActivityGeneticReportBinding
import com.quanutrition.app.reports.network.ReportAPI
import io.reactivex.annotations.NonNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class GeneticReportActivity : AppCompatActivity() {
    lateinit var binding: ActivityGeneticReportBinding
    var arrReport = mutableStateListOf<ReportModel>()
    private var apiJob = Job()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_genetic_report)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        binding.composeView.setContent {

            if (arrReport.size > 0) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        itemsIndexed(arrReport) { index, model ->
                            ReportListView(index, model) {
                                if (it.isNullOrEmpty()) {
                                    Tools.initCustomToast(
                                        this@GeneticReportActivity,
                                        "Link is not available!"
                                    )
                                } else {
                                    val intent = Intent(
                                        this@GeneticReportActivity,
                                        ReportWebviewActivity::class.java
                                    )
                                    intent.putExtra("link", it)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
            } else {
                NoData()
            }
        }

        fetchData()
    }

    @Composable
    fun ReportListView(index: Int, model: ReportModel, onclick: (String) -> Unit) {
        Column(modifier = Modifier
            .clickable { onclick(model.link) }
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.plan_item_type2),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(color = colorResource(id = R.color.colorAccent)),
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Report Delivered On:",
                        style = ComposeConstants.mediumTextDefault,
                        color = colorResource(id = R.color.textColorLight)
                    )
                    Text(
                        text = model.timeStamp,
                        style = ComposeConstants.smallTextDefault,
                        modifier = Modifier.padding(top = 5.dp),
                        color = colorResource(id = R.color.colorAccent)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_left_arrow_white),
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(20.dp)
                        .rotate(180f),
                    contentDescription = ""
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(id = R.color.textColorLight))
            )
        }
    }

    @Composable
    private fun NoData() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.illustration_no_data),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .width(150.dp)
                    .wrapContentHeight()
            )
            Text(
                text = "No data received, Please try again!",
                modifier = Modifier.padding(10.dp),
                color = colorResource(id = R.color.textColorLight),
                style = ComposeConstants.smallTextDefault
            )
        }
    }


    private fun fetchData() {
        coroutineScope.launch {
            val ad = Tools.getDialog("Loading...", this@GeneticReportActivity)
            runOnUiThread(Runnable { ad.show() })
            try {
                val response = ReportAPI.retrofitService.getGeneticReport(
                    Tools.getHeaders(this@GeneticReportActivity),
                )
                runOnUiThread(Runnable { ad.dismiss() })
                Log.d("response", response)
                val res = JSONObject(response)
                if (res.getInt("res") == 1) {
                    val data = res.getJSONArray("data")
                    arrReport.clear()
                    for (i in 0 until data.length()) {
                        val ob = data.getJSONObject(i)
                        arrReport.add(ReportModel(ob.getString("timestamp"), ob.getString("link")))
                    }
                } else {
                    Tools.initCustomToast(this@GeneticReportActivity, res.getString("msg"))
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread(Runnable { ad.dismiss() })
                Tools.initNetworkErrorToast(this@GeneticReportActivity)
            }
        }
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}