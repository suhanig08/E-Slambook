package com.suhani.e_slambook

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.suhani.e_slambook.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var binding: ActivityMain2Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var listener:NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        auth = FirebaseAuth.getInstance()

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host_fragment2) as NavHostFragment
        navController = findNavController(R.id.nav_host_fragment2)

        drawerLayout = binding.drawerLayout
        binding.navigationView.setupWithNavController(navController)
        //drawerBtn=findViewById(R.id.drawerBtn)
//        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appBarConfiguration = AppBarConfiguration(
            navController.graph,
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        fetchData()

//        replaceFragment(HomeFragment(), "E-slambook")
//
//        navigationView.setNavigationItemSelectedListener {
//            it.isChecked = true
//            when (it.itemId) {
//                R.id.home -> replaceFragment(HomeFragment(), "E-slambook")
//                R.id.profile -> replaceFragment(ProfileFragment(), it.title.toString())
//                R.id.friends -> replaceFragment(FriendsFragment(), it.title.toString())
//                R.id.addFriend -> replaceFragment(AddFriendFragment(), it.title.toString())
//            }
//            true
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(
//            navController,
//            appBarConfiguration
//        )
        val navController=findNavController(R.id.nav_host_fragment2)
        return navController.navigateUp(appBarConfiguration)
           || super.onSupportNavigateUp()
    }

    private fun fetchData() {
        var dbRef =
            FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)
        dbRef.get().addOnSuccessListener {
            findViewById<TextView>(R.id.headerName).text = it.child("userName").value.toString()
        }
        dbRef =
            FirebaseDatabase.getInstance().getReference("UserImages").child(auth.currentUser!!.uid)
        dbRef.get().addOnSuccessListener {
            val imgView = findViewById<ImageView>(R.id.headerProfile)
            val url = it.child("url").value.toString()
            Glide.with(this).load(url).into(imgView)
        }

    }

//    private fun replaceFragment(fragment: Fragment, title: String) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frameLayout, fragment)
//        fragmentTransaction.commit()
//        setTitle(title)
//        drawerLayout.close()
//    }
}
