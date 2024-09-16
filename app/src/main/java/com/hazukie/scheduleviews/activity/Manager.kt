package com.hazukie.scheduleviews.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.hazukie.scheduleviews.R
import com.hazukie.scheduleviews.base.BaseActivity
import com.hazukie.scheduleviews.fragments.scheManagerFr
import com.hazukie.scheduleviews.fragments.TimeManageFrag
import com.hazukie.scheduleviews.utils.StatusHelper


class Manager : BaseActivity() {
    private lateinit var shceTab: TextView
    private lateinit var timeTab: TextView
    private var timemanageFrag: TimeManageFrag?=null
    private var schemakeFrag:scheManagerFr?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)
        StatusHelper.controlStatusLightOrDark(this, StatusHelper.Mode.Status_Dark_Text)
        inits()
    }

    private fun inits() {
        shceTab = findViewById(R.id.activity_sche_tab)
        timeTab = findViewById(R.id.activity_time_tab)

        val returnTab = findViewById<TextView>(R.id.activity_manage_return)
        returnTab.setOnClickListener { v2: View? -> finish() }

        timeTab.setTextColor(resources.getColor(R.color.text_gray))
        shceTab.setTextColor(resources.getColor(R.color.iosbutton_cancel))

        shceTab.setOnClickListener({ v: View? ->
            timeTab.setTextColor(resources.getColor(R.color.text_gray))
            shceTab.setTextColor(resources.getColor(R.color.qmuibtn_text))
            showFragment1()
        })

        timeTab.setOnClickListener({ v1: View? ->
            shceTab.setTextColor(resources.getColor(R.color.text_gray))
            timeTab.setTextColor(resources.getColor(R.color.iosbutton_cancel))
            showFragment2()
        })

        showFragment1()
    }

    private fun showFragment1() {
        val transactin = supportFragmentManager.beginTransaction()
        if (schemakeFrag == null) {
            schemakeFrag = scheManagerFr()
            transactin.add(R.id.activity_manage_root, schemakeFrag!!)
        }
        try {
            hideAllFrags(transactin)
            transactin.show(schemakeFrag!!)
            transactin.commit()
            schemakeFrag!!.reshData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showFragment2() {
        val transactin = supportFragmentManager.beginTransaction()
        if (timemanageFrag == null) {
            timemanageFrag = TimeManageFrag()
            transactin.add(R.id.activity_manage_root, timemanageFrag!!)
        }
        try {
            hideAllFrags(transactin)
            transactin.show(timemanageFrag!!)
            transactin.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideAllFrags(transactio: FragmentTransaction) {
        if (timemanageFrag != null) transactio.hide(timemanageFrag!!)
        if (schemakeFrag != null) transactio.hide(schemakeFrag!!)
    }

}