package com.suhani.e_slambook.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseUser
import com.suhani.e_slambook.R
import com.suhani.e_slambook.viewmodel.AuthViewModel


class SignInFragment : Fragment() {

    private lateinit var btnLogin:Button
    private lateinit var emailEt: EditText
    private lateinit var passEt: EditText
    private lateinit var signUpText: TextView
    private lateinit var viewModel: AuthViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)).get(AuthViewModel::class.java)
        viewModel.getUserData().observe(this, Observer<FirebaseUser>(){
            if(it!=null){
                navController.navigate(R.id.action_signInFragment_to_signOutFragment2)

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEt=view.findViewById(R.id.etEmailAddressL)
        passEt=view.findViewById(R.id.etPasswordL)
        btnLogin=view.findViewById(R.id.btnLogin)
        signUpText=view.findViewById(R.id.txtSignUp)
        navController= Navigation.findNavController(view)

        signUpText.setOnClickListener{
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        btnLogin.setOnClickListener{
            val email=emailEt.text.toString()
            val pass=passEt.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                    viewModel.login(email, pass)
            }else{
                Toast.makeText(context, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }

        }

    }
}