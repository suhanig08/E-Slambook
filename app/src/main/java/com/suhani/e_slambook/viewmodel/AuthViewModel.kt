package com.suhani.e_slambook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.suhani.e_slambook.repository.AuthenticationRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: AuthenticationRepository=AuthenticationRepository(application)
     var userData:MutableLiveData<FirebaseUser> = repository.liveData
     var loggedStatus:MutableLiveData<Boolean> = repository.loggedOutliveData

//    fun getUserData(): MutableLiveData<FirebaseUser> {
//        return userData
//    }
//
//    fun getUserLoggedStatus(): MutableLiveData<Boolean> {
//        return loggedStatus
//    }

    fun register(email:String,pass:String){
        repository.register(email,pass)
    }

    fun login(email:String,pass:String){
        repository.login(email,pass)
    }

    fun logout(){
        repository.logout()
    }
}