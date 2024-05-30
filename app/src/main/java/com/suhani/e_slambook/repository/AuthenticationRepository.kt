package com.suhani.e_slambook.repository

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@SuppressLint("SuspiciousIndentation")
class AuthenticationRepository(private var application: Application) {
    var liveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    var loggedOutliveData: MutableLiveData<Boolean> = MutableLiveData()
    private  var auth:FirebaseAuth = FirebaseAuth.getInstance()

    init {
        if(auth.currentUser!=null){
                liveData.postValue(auth.currentUser)
            }
    }

    fun register(email:String, pass:String){
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
            if(it.isSuccessful){
                Log.e("userdata","registration successful")
                liveData.postValue(auth.currentUser)
            }else{
                Toast.makeText(application, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login(email:String, pass:String){
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
            if(it.isSuccessful){
                liveData.postValue(auth.currentUser)
            }else{
                Toast.makeText(application, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun logout(){
        auth.signOut()
        loggedOutliveData.postValue(true)
    }
}