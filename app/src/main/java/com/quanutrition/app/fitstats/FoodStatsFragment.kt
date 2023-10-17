package com.quanutrition.app.fitstats

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.quanutrition.app.R
import com.quanutrition.app.Utils.Constants
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.FragmentFoodStatsBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.tabs.TabLayout
import com.quanutrition.app.MainActivity
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.abs

/**
 * A simple [Fragment] subclass.
 */
class FoodStatsFragment : Fragment() {

    lateinit var binding:FragmentFoodStatsBinding
    var flag = true
    var chart : PieChart? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodStatsBinding.inflate(inflater,container,false)

//        setUpchart()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> {
                        binding.daily.visibility = View.VISIBLE
                        binding.weekly.visibility = View.GONE
                        fetchDaily()
                    }
                    1->{
                        binding.daily.visibility = View.GONE
                        binding.weekly.visibility = View.VISIBLE
                        fetchWeekly()
                    }
                }
            }

        })

        fetchDaily()


        binding.daily.visibility = View.VISIBLE
        binding.weekly.visibility = View.GONE

        binding.basicBtn.setOnClickListener {


            /*if(Tools.getGeneralSharedPref(activity).getBoolean(Constants.PLAN_FLAG,false)){
                val intent = Intent(activity,PlanViewActivity::class.java)
                startActivity(intent)
            }else{*/
                val intent = Intent(activity,MainActivity::class.java)
                intent.putExtra("tag", "3")
                startActivity(intent)

            activity?.finish()
//            }
        }


        return binding.root
    }

    override fun onDetach() {
        flag = false
        super.onDetach()
    }

    override fun onAttach(context: Context) {
        flag = true
        super.onAttach(context)
    }

    private fun setUpchart(entries: java.util.ArrayList<PieEntry>, colors : ArrayList<Int>) {
        
        binding.chart.setUsePercentValues(true)
        binding.chart.description.isEnabled = false
        binding.chart.setExtraOffsets(5f, 10f, 5f, 5f)

        binding.chart.dragDecelerationFrictionCoef = 0.95f
        binding.chart.isDrawHoleEnabled = false
        binding.chart.setDrawEntryLabels(true)

        binding.chart.rotationAngle = 0f
        // enable rotation of the binding.chart1 by touch
        // enable rotation of the binding.chart1 by touch
        binding.chart.isRotationEnabled = false
        binding.chart.isHighlightPerTapEnabled = true

//        binding.chart.setOnbinding.chart1ValueSelectedListener(this)

//        binding.chart.animateY(1400, Easing.EasingOption)
        // binding.chart.spin(2000, 0, 360);

        // binding.chart.spin(2000, 0, 360);
        val l: Legend = binding.chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling

        // entry label styling
        binding.chart.setEntryLabelColor(Color.WHITE)
        binding.chart.setEntryLabelTextSize(12f)

        binding.chart.legend.isEnabled = false


        val dataSet = PieDataSet(entries, "")

        dataSet.setDrawIcons(false)

        dataSet.setDrawValues(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors


        // add a lot of colors
//        val colors = ArrayList<Int>()

        /*colors.add(Color.rgb(1, 87, 155))
        colors.add(Color.rgb(56, 146, 60))
        colors.add(Color.rgb(194, 24, 91))
        colors.add(Color.rgb(156, 39, 179))*/

//        colors.add(Color.parseColor("#fe8a71"))
//        colors.add(Color.parseColor("#26A69A"))
//        colors.add(Color.parseColor("#f6cd61"))
//        colors.add(Color.parseColor("#35a79c"))


        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(chart))
        data.setValueTextSize(14f)
        data.setValueTextColor(Color.WHITE)
//        data.setValueTypeface(tfLight)
        binding.chart.data = data

        // undo all highlights

        // undo all highlights
        binding.chart.highlightValues(null)

        binding.chart.invalidate()

    }

    fun fetchDaily(){
        val ad = Tools.getDialog("Loading...",activity)
        ad.show()


        val url = Urls.dietStats+"?from="+Tools.getFormattedDateToday()

        NetworkManager.getInstance(activity).sendGetRequest(url,activity,object : NetworkManager.OnAPIResponse{
            override fun onResponse(response: String?) {
                ad.dismiss()
                Log.d("Response",response.toString())
                try{
                    val res = JSONObject(response!!)
                    val entries = java.util.ArrayList<PieEntry>()
                    val colors = ArrayList<Int>()

                    if(res.getInt("res")==1) {

                        val data = res.getJSONObject("data")

                        val pi_data = data.getJSONArray("pi_data")

                        if(pi_data.getString(0).toFloat()!=0f) {
                            colors.add(Color.parseColor("#EF5350"))
                            entries.add(PieEntry(pi_data.getString(0).toFloat(), "Missed", resources.getDrawable(R.drawable.ic_launcher_background)))
                        }
                        if(pi_data.getString(1).toFloat()!=0f||pi_data.getString(2).toFloat()!=0f) {
                            colors.add(Color.parseColor("#4CAF50"))
                            entries.add(PieEntry(pi_data.getString(1).toFloat() + pi_data.getString(2).toFloat(), "Taken", resources.getDrawable(R.drawable.ic_launcher_background)))
                        }
                        if(pi_data.getString(3).toFloat()!=0f) {
                            colors.add(Color.parseColor("#F9A825"))
                            entries.add(PieEntry(pi_data.getString(3).toFloat(), "Pending", resources.getDrawable(R.drawable.ic_launcher_background)))
                        }
                        if(entries.size>0) {
                            val ob = data.getJSONArray("stats").getJSONObject(0).getJSONObject("stats")
                            /*if (ob.has("total"))
                                binding.noStatusTxt.text = ob.getString("total")
                            else
                                binding.noStatusTxt.text = "-"*/

                            var totalItems = 0
                            if(ob.has("taken")&&ob.has("total")){
                                var percent = (ob.getString("taken").toFloat()/ob.getString("total").toFloat())*100
                                totalItems = ob.getString("total").toInt()
//                                binding.complaince.text = "You are ${percent.toInt()}% Compliant to your diet today"
                                val htmlData = "You are <font color = \"#4CAF50\"><big>${percent.toInt()}%</big></font> Compliant for the day"
                                Tools.setHTMLData(binding.complaince,htmlData)
                            }

                            var takenItems = 0
                            var missedItems = 0
                            if (ob.has("taken")) {
                                binding.taken.text = ob.getString("taken") + " Item(s)"
                                takenItems = ob.getString("taken").toInt()
                            } else
                                binding.taken.text = "-"

                            if (ob.has("missed")) {
                                binding.missed.text = ob.getString("missed") + " Item(s)"
                                missedItems = ob.getString("missed").toInt()
                            }
                            else
                                binding.missed.text = "-"

                            binding.pending.text = "${abs(totalItems-(takenItems+missedItems))} Items(s)"
                        }
                    }

                    if(entries.size>0) {
                        setUpchart(entries,colors)
                        binding.noData.visibility = View.GONE
                        binding.footer.visibility = View.VISIBLE
                        binding.dataAvailable.visibility = View.VISIBLE
                    }
                    else{
                        binding.noData.visibility = View.VISIBLE
                        binding.footer.visibility = View.GONE
                        binding.dataAvailable.visibility = View.GONE
                    }

                }catch (e : JSONException){
                    e.printStackTrace()
                }
            }

            override fun onError() {
                ad.dismiss()
            }

        }
        )
    }

    fun setUpBarGraph(values : java.util.ArrayList<BarEntry>,dates : ArrayList<String>) {
        if(flag) {
            binding.chart1.description.isEnabled = false
            binding.chart1.setPinchZoom(false)
            binding.chart1.setDrawBarShadow(false)
            binding.chart1.setDrawGridBackground(false)
            binding.chart1.isAutoScaleMinMaxEnabled = false
            val xAxis: XAxis = binding.chart1.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            binding.chart1.animateY(1500)
            binding.chart1.legend.isEnabled = true

            val set1: BarDataSet = BarDataSet(values, "")
            val colors = ArrayList<Int>()
            colors.add(Color.parseColor("#EF5350"))
            colors.add(Color.parseColor("#4CAF50"))
//            colors.add(Color.parseColor("#f6cd61"))
//            colors.add(Color.parseColor("#35a79c"))
            set1.colors = colors

            Log.d("Stack",set1.isStacked.toString())

//            set1.values = values
            set1.stackLabels = arrayOf("Missed", "Taken")
            set1.setDrawValues(false)
//            set1.color = Color.parseColor("#66b9a4")

            set1.setDrawValues(false)
            set1.highLightAlpha = 0
            set1.barBorderWidth = 0f
//            set1.barBorderColor = Color.parseColor("#66b9a4")
            val dataSets = java.util.ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            binding.chart1.setData(data)
            binding.chart1.getBarData().setBarWidth(0.5f)
            binding.chart1.setFitBars(true)
            binding.chart1.setHorizontalScrollBarEnabled(false)
            binding.chart1.setPinchZoom(false)
            binding.chart1.setPinchZoom(false)
            binding.chart1.setDragEnabled(false)
            binding.chart1.setMaxVisibleValueCount(7)
            binding.chart1.setDrawBarShadow(false)
            binding.chart1.setDrawGridBackground(false)
            val yAxiesRight: YAxis = binding.chart1.getAxisRight()
            yAxiesRight.isEnabled = false
            val yAxis: YAxis = binding.chart1.getAxisLeft()

            yAxis.textColor = Color.parseColor("#ffffff")
            yAxis.textSize = 0f
            yAxis.setDrawGridLines(false)
            yAxis.isEnabled = true
            yAxis.setDrawAxisLine(false)
            xAxis.setDrawAxisLine(false)
            val font = Typeface.createFromAsset(
                    activity?.assets,
                    "fonts/defaultfont.otf")
            xAxis.typeface = font
//            xAxis.textSize = 12.0f
            xAxis.axisLineColor = Color.parseColor("#ffffff")
            xAxis.gridColor = Color.parseColor("#ffffff")
            xAxis.axisLineWidth = 2f
            xAxis.textColor = Color.parseColor("#ffffff")
            val xLabel = java.util.ArrayList<String>()
            xAxis.axisMinimum = -0.5f
            xAxis.labelCount = dates.size

            val l: Legend = binding.chart1.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            l.textSize = 12f
            l.textColor = Color.parseColor("#ffffff")
            l.xEntrySpace = 14f
            l.yEntrySpace = 14f
            l.yOffset = 1f
            binding.chart1.legend.isEnabled = true


            for (j in 0 until dates.size) {
                Log.d("Label",Tools.getDayFromDate(dates[j]).split(" ").toTypedArray()[0].toUpperCase());
                Log.d("Date",dates.get(j))
                xLabel.add(Tools.getDayFromDate(dates[j]).split(" ").toTypedArray()[0].toUpperCase())
            }

            xAxis.valueFormatter = IAxisValueFormatter { value, axis -> xLabel[value.toInt()] }
            binding.chart1.invalidate()
        }
    }

    fun fetchWeekly(){
        val ad = Tools.getDialog("Loading...",activity)
        ad.show()


        val url = Urls.dietStats+"?from="+Tools.getFormattedDateAddDates(-6)+"&to="+Tools.getFormattedDateToday()

        NetworkManager.getInstance(activity).sendGetRequest(url,activity,object : NetworkManager.OnAPIResponse{
            override fun onResponse(response: String?) {
                ad.dismiss()
                Log.d("Response",response.toString())
                try{
                    val res = JSONObject(response!!)
                    if(res.getInt("res")==1){

                        val data = res.getJSONObject("data")

                        val stats = data.getJSONArray("stats")
                        val dates1 = ArrayList<String>()
                        val dates = ArrayList<String>()
                        val values = java.util.ArrayList<BarEntry>()

                        for(i in -6..0){
                            dates1.add(Tools.getFormattedDateAddDates(i))
                        }

                        var totalTaken = 0
                        var totalMissed = 0
                        var weeklyTotal = 0

                        dates.clear()
                        for (i in 0 until stats.length()) {

                            val ob = stats.getJSONObject(i)
                            val cuurent = ob.getString("date")

                            dates.add(cuurent)

                                val ob1 = ob.getJSONObject("stats")

                            totalTaken += ob1.getInt("taken")
                            totalMissed += ob1.getInt("missed")
                            weeklyTotal += ob1.getInt("total")

                            /*var flag = -1;
                            for(j in 0 until dates.size){
                                if(dates.get(j).equals(cuurent)){
                                    flag = j;
                                    break;
                                }
                            }*/

//                            if(flag==-1) {
                                values.add(BarEntry(
                                        i.toFloat(), floatArrayOf(ob1.getInt("missed").toFloat(), ob1.getInt("taken").toFloat())))
                            /*}else{
                                values.add(BarEntry(
                                        flag.toFloat(), floatArrayOf(ob1.getInt("missed").toFloat(), ob1.getInt("taken").toFloat())))
                            }*/


                        }

                        if(values.size>0) {
                            setUpBarGraph(values,dates)
                            binding.noData1.visibility = View.GONE
                            binding.footer.visibility = View.VISIBLE
                            binding.dataAvailable1.visibility = View.VISIBLE
                        }
                        else{
                            binding.noData1.visibility = View.VISIBLE
                            binding.footer.visibility = View.GONE
                            binding.dataAvailable1.visibility = View.GONE
                        }

//                        binding.footer.visibility = View.VISIBLE

                        binding.taken.text = totalTaken.toString() + " Item(s)"
                        binding.missed.text = totalMissed.toString() + " Item(s)"
                        binding.pending.text = "${abs(weeklyTotal-(totalTaken+totalMissed))} Item(s)"
                        var percent = (totalTaken.toFloat()/weeklyTotal.toFloat())*100

                        val htmlData = "You are <font color = \"#4CAF50\"><big>${percent.toInt()}%</big></font> Compliant for this week"
                        Tools.setHTMLData(binding.complaince,htmlData)
//                        binding.complaince.text = "You are ${percent.toInt()}% Compliant to your diet this week"
//                        binding.weeklyTotalTxt.text = weeklyTotal.toString()

                        Log.d("values",values.size.toString())
                        Log.d("dates",dates.size.toString())


                    }




                }catch (e : JSONException){
                    e.printStackTrace()
                }
            }

            override fun onError() {
                ad.dismiss()
            }

        }
        )
    }

}
