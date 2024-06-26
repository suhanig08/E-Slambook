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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.suhani.e_slambook.R
import com.suhani.e_slambook.R.layout
import com.suhani.e_slambook.data.User
import com.suhani.e_slambook.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {

    private lateinit var btnSignUp:Button
    private lateinit var emailEt:EditText
    private lateinit var passEt:EditText
    private lateinit var cnfPassEt:EditText
    private lateinit var signInText:TextView
    private lateinit var viewModel:AuthViewModel
    private lateinit var navController:NavController
    private lateinit var dbRef:DatabaseReference
    private var userData= arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))[AuthViewModel::class.java]
        viewModel.userData.observe(this, Observer<FirebaseUser>(){
            if(it!=null){
                navController.navigate(R.id.action_signUpFragment_to_signInFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEt=view.findViewById(R.id.etEmailAddress)
        passEt=view.findViewById(R.id.etPassword)
        cnfPassEt=view.findViewById(R.id.etCnfPassword)
        btnSignUp=view.findViewById(R.id.btnSignUp)
        signInText=view.findViewById(R.id.txtLogin)
        navController=Navigation.findNavController(view)
        dbRef=FirebaseDatabase.getInstance().getReference("Users")

        signInText.setOnClickListener{
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        btnSignUp.setOnClickListener{
            val email=emailEt.text.toString()
            val pass=passEt.text.toString()
            val cnfPass=cnfPassEt.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()&&cnfPass.isNotEmpty()) {
                if(pass==cnfPass) {
                    viewModel.register(email, pass)
//                    val obj=User(
//                        FirebaseAuth.getInstance().currentUser!!.uid, "", "", "",
//                        Uri.parse("android.resources://com.suhani.e_slambook/${R.drawable.noprofile}")
//                            .toString(),
//                        "nothing_happen",
//                    )
//                    userData.add(obj)
//                    dbRef.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userData)
                    //navController.navigate(R.id.action_signUpFragment_to_signOutFragment2)
                }else{
                    Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
//        observeViewModel()
    }

//    private fun observeViewModel() {
//        Log.e("userdata","inside observeViewModel")
//        viewModel.userData.observe(viewLifecycleOwner){
//            Log.e("userdata","this=${it}")
//                 if(it!=null)
//                navController.navigate(R.id.action_signUpFragment_to_signInFragment)
//        }
//    }

}