package com.suhani.e_slambook.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import com.suhani.e_slambook.R

class SignOutFragment : Fragment() {

    //private lateinit var btnLogOut: Button
    //private lateinit var viewModel: AuthViewModel
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerBtn:ImageView
    private lateinit var navigationView:NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel= ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
//            .getInstance(requireActivity().application)).get(AuthViewModel::class.java)
//        viewModel.loggedStatus.observe(this, Observer{
//            if(it){
//                navController.navigate(R.id.action_signOutFragment2_to_signInFragment)
//            }
//        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // btnLogOut=view.findViewById(R.id.btnSignOut)
        navController= Navigation.findNavController(view)
        drawerLayout=view.findViewById(R.id.drawerLayout)
        drawerBtn=view.findViewById(R.id.drawerBtn)
        navigationView=view.findViewById(R.id.navigationView)

        drawerBtn.setOnClickListener{
            drawerLayout.open()
        }

        navigationView.setNavigationItemSelectedListener {
            val itemId=it.itemId
            if(itemId==R.id.home){
                Toast.makeText(context, "home", Toast.LENGTH_SHORT).show()
            }
            if(itemId==R.id.profile){
                Toast.makeText(context, "profile", Toast.LENGTH_SHORT).show()
            }
            if(itemId==R.id.friends){
                Toast.makeText(context, "friends", Toast.LENGTH_SHORT).show()
                true
            }
            else
            false
        }

//        btnLogOut.setOnClickListener{
//            viewModel.logout()
//            //navController.navigate(R.id.action_signOutFragment2_to_signInFragment)
//        }
    }
}