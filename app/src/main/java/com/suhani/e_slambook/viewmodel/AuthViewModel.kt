package com.suhani.e_slambook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.suhani.e_slambook.repository.AuthenticationRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var repository: AuthenticationRepository
    private lateinit var userData:MutableLiveData<FirebaseUser>
    private lateinit var loggedStatus:MutableLiveData<Boolean>

    fun getUserData(): MutableLiveData<FirebaseUser> {
        return userData
    }

    fun getUserLoggedStatus(): MutableLiveData<Boolean> {
        return loggedStatus
    }

    init {
        repository=AuthenticationRepository(application)
        userData=repository.getFireBaseUserMutableLiveData()
        loggedStatus=repository.getUserLoggedOutMutableLiveData()
    }

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