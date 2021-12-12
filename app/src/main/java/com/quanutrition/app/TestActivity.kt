package com.quanutrition.app

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.quanutrition.app.diet.DietPlanFragment

class TestActivity : AppCompatActivity(),DietPlanFragment.OnFragmentInteractionListener {

    var transaction: FragmentTransaction? = null
    var fragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        fragmentManager = supportFragmentManager
        val fragment = DietPlanFragment()
        val transaction = fragmentManager!!.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(R.id.main_fragment_frame, fragment).commit()

    }

    override fun onFragmentInteraction(uri: Uri?) {

    }
}