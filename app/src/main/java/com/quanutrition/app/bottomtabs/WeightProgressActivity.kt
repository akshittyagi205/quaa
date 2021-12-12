package com.quanutrition.app.bottomtabs

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.quanutrition.app.R
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivityWeightProgressBinding
import com.quanutrition.app.selectiondialogs.DialogUtils
import org.json.JSONException
import org.json.JSONObject

class WeightProgressActivity : AppCompatActivity() {

    lateinit var binding : ActivityWeightProgressBinding
    lateinit var list : ArrayList<WeightLogModel>
    lateinit var adapter: WeightLogAdapter
    var alertDialog1 : AlertDialog? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeightProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        list = ArrayList()
        adapter = WeightLogAdapter(list, this)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.re.layoutManager = layoutManager
        binding.re.adapter = adapter

        binding.daysSelect.text = "30 Days"

        binding.daysSelect.setOnClickListener {
            val days = arrayOf("30 Days", "60 Days","3 Months","6 Months","1 Year")
            DialogUtils.getCustomPicker(this, "Select Days", days) { selected ->
                binding.daysSelect.text = selected
                val selectedDays: Int = when(selected){
                    "30 Days" -> 30
                    "60 Days" -> 60
                    "3 Months" -> 90
                    "6 Months" -> 180
                    "1 Year" -> 365
                    else -> 30
                }
                fetchData(selectedDays)
            }
        }

        binding.update.setOnClickListener {
            updateDialog()
        }
        binding.basicBtn.setOnClickListener {
            updateDialog()
        }

        fetchData(30)
    }

    fun updateDialog(){
        val alertDialog = AlertDialog.Builder(this)
        val linf = LayoutInflater.from(this)
        val inflator: View = linf.inflate(R.layout.dialog_update_weight, null)
        alertDialog.setView(inflator)
        alertDialog.setCancelable(true)
        val editText = inflator.findViewById<EditText>(R.id.editText)
        val selectWeightUnit = inflator.findViewById<EditText>(R.id.selectWeightUnit)

        selectWeightUnit.isFocusable = false
        selectWeightUnit.setOnClickListener { DialogUtils.getSingleSelectionDialog(this, DialogUtils.getSingleArrayListWithResource(this, R.array.weightUnit)) { position, item -> selectWeightUnit.setText(item.label) } }

        val done = inflator.findViewById<Button>(R.id.done)

        done.setOnClickListener {
            if (Tools.validateNormalText(editText)) {
                var weight = editText.text.toString().trim { it <= ' ' }.toFloat()
                if (selectWeightUnit.text.toString().trim { it <= ' ' }.equals("Kgs", ignoreCase = true)) {
                    if (weight > 250f) {
                        Tools.initCustomToast(this, "Weight cannot be greater 250 Kgs")
                    } else {
                        updateWeight(weight.toString() + "")
                        alertDialog1?.dismiss()
                    }
                } else {
                    weight *= 0.45f
                    if (weight > 250f) {
                        Tools.initCustomToast(this, "Weight cannot be greater 550 Lbs")
                    } else {
                        updateWeight(weight.toString() + "")
                        alertDialog1?.dismiss()
                    }
                }
            } else {
                Tools.initCustomToast(this, "Weight cannot be empty!")
            }
        }

        alertDialog1 = null
        alertDialog1 = alertDialog.show()
        alertDialog1?.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.dialog_background_drawable))
    }

    private fun fetchData(flag: Int){

        val ad = Tools.getDialog("Fetching...", this)
        ad.show()

        NetworkManager.getInstance(this).sendGetRequest(Urls.user_progress+"?days=$flag", { response ->
            run {
                ad.dismiss()
                Log.d("Response", response!!)
                try {
                    val res = JSONObject(response)
                    if(res.getInt("res")==1){
                        list.clear()
                        val entries = ArrayList<Entry>()
                        val data = res.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val ob = data.getJSONObject(i)
                            list.add(WeightLogModel(ob.getString("weight"),ob.getString("date")))
                            entries.add(Entry(ob.getInt("day").toFloat(),ob.getString("weight").toFloat()))
                        }
                        adapter.notifyDataSetChanged()
                        setUpGraph(entries)

                        if(list.size>0)
                            binding.noProgress.visibility = View.GONE
                        else
                            binding.noProgress.visibility = View.VISIBLE

                    }else{
                        binding.noProgress.visibility = View.VISIBLE
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, {
            ad.dismiss()
            Tools.initNetworkErrorToast(this)
            binding.noProgress.visibility = View.VISIBLE
        }, this)

    }

    private fun updateWeight(weight: String){
        val ad = Tools.getDialog("Updating...", this)
        ad.show()

        val params = HashMap<String, String>()
        params["date"] = Tools.getFormattedDateToday()
        params["weight"] = weight

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.user_progress, params, { response ->
            run {
                ad.dismiss()
                Log.d("Response", response!!)
                try {
                    val res = JSONObject(response)
                    if (res.getInt("res") == 1) {
                        Tools.initCustomToast(this@WeightProgressActivity, "Weight updated!")

                        val days : Int = when(binding.daysSelect.text.toString()){
                            "30 Days" -> 30
                            "60 Days" -> 60
                            "3 Months" -> 90
                            "6 Months" -> 180
                            "1 Year" -> 365
                            else -> 30
                        }
                        fetchData(days)
                    } else {
                        Tools.initCustomToast(this@WeightProgressActivity, "Some error occurred!")
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, {
            ad.dismiss()
            Tools.initNetworkErrorToast(this)
        }, this)
    }

    fun setUpGraph(values: ArrayList<Entry>) {
        binding.chart.setBackgroundColor(Color.TRANSPARENT)
        binding.chart.getDescription().setEnabled(false)
        binding.chart.setTouchEnabled(true)
        binding.chart.setDrawGridBackground(false)
        binding.chart.setDragEnabled(false)
        binding.chart.setScaleEnabled(true)
        binding.chart.setPinchZoom(false)
        val xAxis: XAxis = binding.chart.getXAxis()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.axisMinimum = 1f
        xAxis.setDrawAxisLine(true)
        xAxis.axisLineColor = resources.getColor(R.color.grey_400)
        xAxis.textSize = 10f
        xAxis.textColor = Color.WHITE
        val font = Typeface.createFromAsset(
                assets,
                "defaultfont.otf")
        xAxis.typeface = font
        binding.chart.animateY(500)
        binding.chart.legend.isEnabled = false
        // create marker to display box when values are selected
        val mv = MyMarkerView(this, R.layout.custom_marker_view)

        // Set the marker to the binding.chart
        mv.setChartView(binding.chart)
        binding.chart.setMarker(mv)
        var yAxis: YAxis
        run {
            yAxis = binding.chart.getAxisLeft()
            yAxis.setDrawGridLines(true)
            yAxis.textSize = 10f
            //            yAxis.setLabelCount(5);
            yAxis.setLabelCount(5, true)
            yAxis.textColor = Color.WHITE
            yAxis.setDrawAxisLine(true)
            yAxis.axisLineColor = resources.getColor(R.color.grey_400)
            yAxis.isEnabled = true
            binding.chart.getAxisRight().setEnabled(false)
        }
        val set1: LineDataSet
        set1 = LineDataSet(values, "DataSet 1")
        set1.setDrawIcons(false)
        set1.color = resources.getColor(R.color.colorAccent)
        set1.fillColor = resources.getColor(R.color.colorAccent)
        set1.setCircleColor(resources.getColor(R.color.colorAccent))
        set1.lineWidth = 2f
        set1.circleRadius = 3f
        set1.setDrawCircleHole(false)
        set1.formSize = 15f
        set1.valueTextSize = 9f
        set1.setDrawCircles(true)
        set1.setDrawValues(false)
        set1.setDrawFilled(true)
        set1.mode = LineDataSet.Mode.LINEAR
        val dataSets = java.util.ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val data = LineData(dataSets)
        binding.chart.setData(data)
        binding.chart.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}