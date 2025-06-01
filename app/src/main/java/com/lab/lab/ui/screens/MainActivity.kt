package com.lab.lab.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lab.lab.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, ListaCrimenFragment())
                .commit()
        }
    }
}
