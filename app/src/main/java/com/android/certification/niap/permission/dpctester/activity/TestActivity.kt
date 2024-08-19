package com.android.certification.niap.permission.dpctester.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.certification.niap.permission.dpctester.R
import com.android.certification.niap.permission.dpctester.test.log.StaticLogger

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        StaticLogger.debug("TestActivity.onCreate() invoked")
        finish()
    }
}