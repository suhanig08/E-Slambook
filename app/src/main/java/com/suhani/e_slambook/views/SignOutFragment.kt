package com.suhani.e_slambook.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.suhani.e_slambook.R
import com.suhani.e_slambook.viewmodel.AuthViewModel

class SignOutFragment : Fragment() {

    private lateinit var btnLogOut: Button
    private lateinit var viewModel: AuthViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)).get(AuthViewModel::class.java)
        viewModel.getUserLoggedStatus().observe(this, Observer<Boolean>{
            if(it){
                navController.navigate(R.id.action_signOutFragment2_to_signInFragment)
            }
        })
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
        btnLogOut=view.findViewById(R.id.btnSignOut)
        navController= Navigation.findNavController(view)

        btnLogOut.setOnClickListener{
            viewModel.logout()
        }
    }
}