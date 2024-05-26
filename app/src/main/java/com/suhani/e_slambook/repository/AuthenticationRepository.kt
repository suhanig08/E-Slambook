package com.suhani.e_slambook.repository

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@SuppressLint("SuspiciousIndentation")
class AuthenticationRepository(private var application: Application) {
    private lateinit var liveData: MutableLiveData<FirebaseUser>
    private lateinit var loggedOutliveData: MutableLiveData<Boolean>
    private  var auth:FirebaseAuth

    fun getFireBaseUserMutableLiveData(): MutableLiveData<FirebaseUser> {
        return liveData
    }

    fun getUserLoggedOutMutableLiveData(): MutableLiveData<Boolean> {
        return loggedOutliveData
    }

    init {
        //liveData=MutableLiveData<FirebaseUser>()
        auth=FirebaseAuth.getInstance()

            if(auth.currentUser!=null){
                liveData.postValue(auth.currentUser)
            }
    }

    fun register(email:String, pass:String){
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
            if(it.isSuccessful){
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