package com.quanutrition.app.googlefit.healthconnect

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.health.connect.client.HealthConnectClient
import com.quanutrition.app.R
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.composables.ComposeConstants
import com.quanutrition.app.databinding.ActivityHealthConnectBinding
import com.quanutrition.app.googlefit.healthconnect.HealthConnectConst.HEALTH_CONNECT_SETTINGS_ACTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HealthConnectActivity : AppCompatActivity() {
    lateinit var binding: ActivityHealthConnectBinding
    private val healthConnectManager by lazy { HealthConnectManager(this) }
    private var permissionsGranted = mutableStateOf(false)
    private var clickCount = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        GlobalScope.launch(Dispatchers.Default) {
            permissionsGranted.value = healthConnectManager.hasAllPermissions(healthConnectManager.permissions)
        }
        val onPermissionsResult = {
            GlobalScope.launch {
                permissionsGranted.value = healthConnectManager.hasAllPermissions(healthConnectManager.permissions)
                if (permissionsGranted.value) {
                    Tools.getGeneralEditor(this@HealthConnectActivity).putBoolean(HealthConnectConst.HEALTH_CONNECT_ACTIVE, true).commit()
                }
            }
        }
        binding.composeView.setContent {
            val permissionsLauncher = rememberLauncherForActivityResult(healthConnectManager.requestPermissionsActivityContract()) {
                    onPermissionsResult()
            }
            if (!permissionsGranted.value) {
                HealthConnectDisconnectScreen(permission = healthConnectManager.permissions,
                    onPermissionsLaunch = { value ->
                        permissionsLauncher.launch(value)
                    })
            } else {
                HeathConnectConnectScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        healthConnectManager.checkAvailability()
        GlobalScope.launch(Dispatchers.Default) {
            permissionsGranted.value = healthConnectManager.hasAllPermissions(healthConnectManager.permissions)
            if (permissionsGranted.value){
                Tools.getGeneralEditor(this@HealthConnectActivity).putBoolean(HealthConnectConst.HEALTH_CONNECT_ACTIVE,true).commit()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    @Composable
    fun HeathConnectConnectScreen() {
//        LaunchedEffect(key1 = Unit, block = {
//                val ad = Tools.getDialog("Loading...", this@HealthConnectActivity)
//                runOnUiThread(Runnable { ad.show() })
//                try {
//                    arrOfStepsCalories.clear()
//                    arrOfStepsCalories.addAll(healthConnectManager.readStepsByWeekly(15))
//                    date.value = arrOfStepsCalories[arrOfStepsCalories.size-1].date!!
//                    steps.value = arrOfStepsCalories[arrOfStepsCalories.size-1].totalSteps!!
//                    calories.value = arrOfStepsCalories[arrOfStepsCalories.size-1].totalEnergyBurned!!
//                    Log.d("response date",date.value)
//                    Log.d("response steps",steps.value.toString())
//                    Log.d("response calories",calories.value)
//                    runOnUiThread(Runnable { ad.dismiss() })
//                }catch (e: Exception) {
//                    e.printStackTrace()
//                    runOnUiThread(Runnable { ad.dismiss() })
//                    Tools.initNetworkErrorToast(this@HealthConnectActivity)
//                }
//        })
        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.colorPrimaryDark))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = colorResource(id = R.color.grey_800), // Background color of the card
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_health_connect_logo),
                            contentDescription = "Health Connect",
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp),
                            contentScale = ContentScale.FillBounds
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "Disconnect",
                            style = ComposeConstants.smallBoldText,
                            color = colorResource(id = R.color.red_500),
                            modifier = Modifier
                                .wrapContentSize()
                                .clickable {
                                    showHealthConnectDialog()
                                }
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(color = colorResource(id = R.color.grey_600))
                    )
                    Text(
                        text = "What data we get from Health Connect",
                        modifier = Modifier.padding(start = 15.dp),
                        color = colorResource(id = R.color.grey_20),
                        style = ComposeConstants.smallBoldText
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp, start = 5.dp, end = 15.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Steps", modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f), style = ComposeConstants.smallTextDefault,
                            color = colorResource(id = R.color.grey_20)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.baseline_check_24),
//                            colorFilter = ColorFilter.tint(color = colorResource(id = R.color.textColorLight)),
                            contentDescription = "",
                            modifier = Modifier
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 5.dp, end = 15.dp, bottom = 20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total calories burned", modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f), style = ComposeConstants.smallTextDefault,
                            color = colorResource(id = R.color.grey_20)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.baseline_check_24),
//                            colorFilter = ColorFilter.tint(color = colorResource(id = R.color.textColorLight)),
                            contentDescription = "",
                            modifier = Modifier
                        )
                    }
                }
            }

            Text(text = "Settings",
                style = ComposeConstants.mediumBoldText,
                color = colorResource(id = R.color.grey_20),
                modifier = Modifier.padding(top = 20.dp))
            Card(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = colorResource(id = R.color.grey_800),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(colorResource(id = R.color.grey_800))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "To see and manage the data ${resources.getString(R.string.app_name)} can access.",
                        color = colorResource(id = R.color.grey_20),
                        style = ComposeConstants.mediumTextDefault
                    )

                    Button(
                        onClick = {
                            val settingsIntent = Intent()
                            settingsIntent.action = HEALTH_CONNECT_SETTINGS_ACTION
                            startActivity(settingsIntent)
                        },
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.colorAccent), // Change the background color
                            contentColor = colorResource(id = R.color.textColorLight)
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {

                        Text(
                            text = "Manage Access",
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }
                }
            }
//            Text(text = "Today Data ", modifier = Modifier.padding(top = 20.dp), style = ComposeConstants.mediumBoldText)
//
//            Text(text = "Date : ${date.value}\nTotal Steps: ${steps.value}\t\tTotal Calories: ${calories.value}", modifier = Modifier.padding(vertical = 20.dp), style = ComposeConstants.mediumTextDefault)
//
//            Text(text = "Last ${arrOfStepsCalories.size} Days ", modifier = Modifier.padding(top = 20.dp), style = ComposeConstants.mediumBoldText)
//
//            for (i in arrOfStepsCalories.indices){
//                Text(text = "Date : ${arrOfStepsCalories[i].date}\nTotal Steps: ${arrOfStepsCalories[i].totalSteps}\t\tTotal Calories: ${arrOfStepsCalories[i].totalEnergyBurned}", modifier = Modifier.padding(vertical = 20.dp), style = ComposeConstants.mediumTextDefault)
//            }
        }
    }

    private fun showHealthConnectDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_health_connect)
        dialog.findViewById<TextView>(R.id.text).text = "Disconnect Health Connect would remove all the data permission from ${resources.getString(R.string.app_name)}."
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.setCancelable(true)
        (dialog.findViewById<View>(R.id.close) as Button).setOnClickListener { dialog.dismiss() }
        (dialog.findViewById<View>(R.id.disconnect) as Button).setOnClickListener {
            dialog.dismiss()
            try {
                GlobalScope.launch(Dispatchers.Default) {
                    HealthConnectClient.getOrCreate(this@HealthConnectActivity).permissionController.revokeAllPermissions()
                    permissionsGranted.value = false
                    Tools.getGeneralEditor(this@HealthConnectActivity).putBoolean(HealthConnectConst.HEALTH_CONNECT_ACTIVE,false).commit()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        dialog.show()
    }

    @Composable
    fun HealthConnectDisconnectScreen(
        permission: Set<String>,
        onPermissionsLaunch: (Set<String>) -> Unit = {}
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.colorPrimaryDark))
        ) {
            Column(
                modifier = Modifier
                    .background(colorResource(id = R.color.colorPrimaryDark))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .weight(.9F)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_health_connect_logo),
                        contentDescription = "Health Connect",
                        modifier = Modifier
                            .width(80.dp)
                            .height(80.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_both),
                        modifier = Modifier
                            .padding(start = 10.dp, end = 15.dp)
                            .width(30.dp), contentDescription = ""
                    )
                    val context = LocalContext.current
                    val packageManager = context.packageManager
                    val appIcon: ImageBitmap = remember {
                        val appIconDrawable = packageManager.getApplicationIcon(context.packageName)
                        appIconDrawable.toBitmap().asImageBitmap()
                    }
                    Image(
                        bitmap = appIcon,
                        contentDescription = "Health Connect",
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }

                Text(
                    text = "Sync with Health Connect",
                    color = colorResource(id = R.color.grey_20),
                    style = ComposeConstants.mediumBoldText
                )
                Card(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = colorResource(id = R.color.grey_800), // Background color of the card
                ) {
                    // Content inside the card
                    val appName = resources.getString(R.string.app_name)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(colorResource(id = R.color.grey_800))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Allow $appName to access your heath data via Health Connect to get the latest information from your other apps, like your steps and calories.",
                            style = ComposeConstants.mediumTextDefault,
                            color = colorResource(id = R.color.grey_20)
                        )
                    }
                }
                Text(
                    text = "Permissions Required",
                    modifier = Modifier
                        .padding(top = 20.dp), style = ComposeConstants.mediumBoldText,
                    color = colorResource(id = R.color.grey_20)
                )
                Card(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = colorResource(id = R.color.grey_800), // Background color of the card
                ) {
                    // Content inside the card
                    val appName = resources.getString(R.string.app_name)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(colorResource(id = R.color.grey_800))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Please allow $appName to read all the following data when prompted:",
                            modifier = Modifier.padding(top = 10.dp),
                            style = ComposeConstants.mediumTextDefault,
                            color = colorResource(id = R.color.grey_20)
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_steps),
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(color = colorResource(id = R.color.textColorLight)),
                                modifier = Modifier
                                    .size(35.dp)
                                    .background(
                                        color = colorResource(id = R.color.grey_700),
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .padding(10.dp)
                            )
                            Text(
                                text = "Steps",
                                modifier = Modifier.padding(start = 10.dp),
                                style = ComposeConstants.smallTextDefault,
                                color = colorResource(id = R.color.grey_20)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_calories),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(35.dp)
                                    .background(
                                        color = colorResource(id = R.color.grey_700),
                                        shape = RoundedCornerShape(7.dp)
                                    )
                                    .padding(10.dp)
                            )
                            Text(
                                text = "Total calories burned",
                                modifier = Modifier.padding(start = 10.dp),
                                style = ComposeConstants.smallTextDefault,
                                color = colorResource(id = R.color.grey_20)
                            )
                        }
                        Text(
                            text = "Note: Ensure that all the above permissions are given for $appName to work property",
                            modifier = Modifier.padding(top = 20.dp),
                            colorResource(id = R.color.grey_20),
                            style = ComposeConstants.tinyTextDefault
                        )
                    }
                }
                Text(
                    text = "Manage permissions",
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .clickable {
                            val intent = applicationContext.packageManager
                                .getLaunchIntentForPackage("com.google.android.apps.healthdata")
                            applicationContext.startActivity(intent)
                        },
                    color = colorResource(id = R.color.grey_20)
                )
                if (clickCount.value) {
                    InstalledMessage()
                }
            }
            if (!clickCount.value) {
                Button(
                    onClick = {
                        onPermissionsLaunch(permission)
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            clickCount.value = true
//                        }, 1000)
                    },
                    modifier = Modifier
                        .padding(bottom = 20.dp, start = 12.dp, end = 12.dp)
                        .fillMaxSize()
                        .weight(.08f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.colorAccent),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(15.dp),
                ) {
                    Text(
                        text = "Set Up Health Connect",
                        style = ComposeConstants.smallBoldText
                    )
                }
            }
        }
    }


    @Composable
    fun InstalledMessage() {
        Card(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = colorResource(id = R.color.grey_800), // Background color of the card
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(colorResource(id = R.color.grey_800))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Last few permission request were denied. Please manage permission directly from Health Connect app.",
                    style = ComposeConstants.mediumTextDefault,
                    color = colorResource(id = R.color.grey_20)
                )

                Button(
                    onClick = {
                        val settingsIntent = Intent()
                        settingsIntent.action = HEALTH_CONNECT_SETTINGS_ACTION
                        startActivity(settingsIntent)
                    },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.colorAccent), // Change the background color
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                ) {

                    Text(
                        text = "Manage permissions",
                        modifier = Modifier.padding(vertical = 5.dp),
                    )
                }
            }
        }
    }

}