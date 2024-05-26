package com.suhani.e_slambook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

//    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//    val navController = navHostFragment.navController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//    if (savedInstanceState == null) {
//        supportFragmentManager.commit {
//            setReorderingAllowed(true)
//            add<Fragment>(R.id.nav_host_fragment)
//        }
//    }
    }
}

