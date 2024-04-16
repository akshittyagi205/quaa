package com.quanutrition.app.googlefit.healthconnect

import android.content.Context
import androidx.health.connect.client.time.TimeRangeFilter
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_AVAILABLE
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.googlefit.healthconnect.HealthConnectConst.MIN_SUPPORTED_SDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Calendar
import java.util.Date

class HealthConnectManager(private val context: Context) {
  private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }
  var apiJob = Job()
  val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

  var availability = mutableStateOf(HealthConnectAvailability.NOT_SUPPORTED)
    private set

  val permissions = setOf(
    HealthPermission.getReadPermission(StepsRecord::class),
    HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class)
  )
  init {
    checkAvailability()
  }

  fun checkAvailability() {
    availability.value = when {
      HealthConnectClient.getSdkStatus(context) == SDK_AVAILABLE -> HealthConnectAvailability.INSTALLED
      isSupported() -> HealthConnectAvailability.NOT_INSTALLED
      else -> HealthConnectAvailability.NOT_SUPPORTED
    }
  }
  suspend fun hasAllPermissions(permissions: Set<String>): Boolean {
    return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
  }

  fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
    return PermissionController.createRequestPermissionResultContract()
  }

  suspend fun revokePermission(context: Context){
    HealthConnectClient.getOrCreate(context).permissionController.revokeAllPermissions()
  }

  interface StepsByWeeklyCallback {
    fun onStepsByWeeklyResult(result: List<SessionData>)
    fun onError(exception: Exception)
  }

  fun readStepsByWeekly(numberOfDays: Int , callback: StepsByWeeklyCallback) {
    var arr : ArrayList<SessionData> = Tools.printStartAndTimeInterval(numberOfDays)
    coroutineScope.launch {
      try {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND))
        val startTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.add(Calendar.DAY_OF_MONTH,-numberOfDays)
        val endTime =  calendar.timeInMillis
        val responseSteps = healthConnectClient.aggregateGroupByDuration(
          AggregateGroupByDurationRequest(
            metrics = setOf(StepsRecord.COUNT_TOTAL),
            timeRangeFilter = TimeRangeFilter.between(Tools.convertTimeMillisToInstant(endTime),Tools.convertTimeMillisToInstant(startTime)),
            timeRangeSlicer = Duration.ofDays(1L)
          )
        )
        val responseCalories = healthConnectClient.aggregateGroupByDuration(
          AggregateGroupByDurationRequest(
            metrics = setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL),
            timeRangeFilter = TimeRangeFilter.between(Tools.convertTimeMillisToInstant(endTime),Tools.convertTimeMillisToInstant(startTime)),
            timeRangeSlicer = Duration.ofDays(1L)
          )
        )
        for (i in arr.indices){
          for (monthlyResult in responseSteps){
            if (dateFormat.format(Date.from(monthlyResult.startTime)) == arr[i].date){
              arr[i].setTotalSteps(monthlyResult.result[StepsRecord.COUNT_TOTAL]!!)
            }
          }
          for (monthlyResult in responseCalories){
            if (dateFormat.format(Date.from(monthlyResult.startTime)) == arr[i].date){
              arr[i].setTotalEnergyBurned(((monthlyResult.result[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories)?.div(
                1000
              ))?.toInt().toString())
            }
          }
        }
        callback.onStepsByWeeklyResult(arr)
      }catch (e: Exception){
        e.printStackTrace()
        callback.onError(e)
      }
    }
  }

  private fun isSupported() = Build.VERSION.SDK_INT >= MIN_SUPPORTED_SDK

}

enum class HealthConnectAvailability {
  INSTALLED,
  NOT_INSTALLED,
  NOT_SUPPORTED
}
