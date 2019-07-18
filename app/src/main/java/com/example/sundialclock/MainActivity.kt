package com.example.sundialclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var testClickView = findViewById<TestClickView>(R.id.test_click_view2)
        testClickView.setOnClickListener {
            Log.i("LHD", "testClickView = $testClickView")
        }


        var btn = findViewById<Button>(R.id.test_click_view1)
        btn.setOnClickListener {
            Log.i("LHD", "button")
        }

        var sundialView = findViewById<SundialView>(R.id.sundial_view)

        var btnTest = findViewById<Button>(R.id.btn_test)
        btnTest.setOnClickListener {
            Log.i("LHD", "btnTest")
            sundialView.start()
        }

    }
}
