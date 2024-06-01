package com.suhani.e_slambook

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.suhani.e_slambook.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var binding: ActivityMain2Binding
    private lateinit var auth:FirebaseAuth
    lateinit var toggle:ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth=FirebaseAuth.getInstance()

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout=binding.drawerLayout
        //drawerBtn=findViewById(R.id.drawerBtn)
        navigationView=findViewById(R.id.navigationView)

        toggle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        replaceFragment(HomeFragment(),"E-slambook")

        fetchData()

        navigationView.setNavigationItemSelectedListener {
            it.isChecked=true
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment(),"E-slambook")
                R.id.profile->replaceFragment(ProfileFragment(),it.title.toString())
                R.id.friends->replaceFragment(FriendsFragment(),it.title.toString())
            }
            true
        }
    }

    private fun fetchData() {
        var dbRef=FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)
        dbRef.get().addOnSuccessListener {
            findViewById<TextView>(R.id.headerName).text=it.child("firstName").value.toString()+" "+
                    it.child("lastName").value.toString()
        }
        dbRef=FirebaseDatabase.getInstance().getReference("UserImages").child(auth.currentUser!!.uid)
        dbRef.get().addOnSuccessListener {
            val imgView=findViewById<ImageView>(R.id.headerProfile)
            val url=it.child("url").value.toString()
            Glide.with(this).load(url).into(imgView)
        }

    }

    private fun replaceFragment(fragment: Fragment,title:String){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        setTitle(title)
        drawerLayout.close()
    }
}