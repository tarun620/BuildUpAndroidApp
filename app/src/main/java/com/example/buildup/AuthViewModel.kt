package com.example.buildup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.responses.*
import com.example.api.models.responses.property.PropertiesResponse
import com.example.api.models.responses.property.SinglePropertyResponse
import com.example.api.models.responses.updates.UpdatesResponse
import com.example.buildup.data.UserRepo
import kotlinx.coroutines.launch

class AuthViewModel:ViewModel() {

    private val _resp= MutableLiveData<SignupMobileResponse?>()
    private val _respNew=MutableLiveData<SignupMobileFinalResponse?>()
    private val _respNewImage=MutableLiveData<LoginResponse?>()
    private val _respNewImageGoogle=MutableLiveData<LoginGoogleResponse?>()
    private val _respPropertyArray=MutableLiveData<PropertiesResponse?>()
    private val _respProperty=MutableLiveData<SinglePropertyResponse?>()
    private val _respUpdatesArray=MutableLiveData<UpdatesResponse?>()

    val resp:LiveData<SignupMobileResponse?> = _resp
    val respNew:LiveData<SignupMobileFinalResponse?> = _respNew
    val respNewImage:LiveData<LoginResponse?> = _respNewImage
    val respNewImageGoogle:LiveData<LoginGoogleResponse?> = _respNewImageGoogle
    val respPropertyArray:LiveData<PropertiesResponse?> = _respPropertyArray
    val respUpdatesArray:LiveData<UpdatesResponse?> = _respUpdatesArray
    val respProperty:LiveData<SinglePropertyResponse?> = _respProperty

    fun signup(mobileNo : String)=viewModelScope.launch {
        UserRepo.signup(mobileNo).let {
            _resp.postValue(it)
        }
    }

    fun verifyOTP(mobileNo: String,otp:String)=viewModelScope.launch {
        UserRepo.verifyOTP(mobileNo,otp).let {
            _resp.postValue(it.body())
        }
    }

    fun completeProfile(mobileNo: String,name:String,email:String,password:String)=viewModelScope.launch {
        UserRepo.completeProfile(mobileNo,name,email,password).let {
            _respNew.postValue(it.body())
        }
    }

    fun login(mobileNo:String,password:String)=viewModelScope.launch {
        UserRepo.login(mobileNo,password).let {
            _respNewImage.postValue(it.body())
        }
    }

    fun loginSignupGoogle(name:String,email:String,profileImage:String?)=viewModelScope.launch{
        UserRepo.loginSignupGoogle(name,email,profileImage).let {
            _respNewImageGoogle.postValue(it.body())
        }
    }

    fun signupGoogleSaveMobile(mobileNo: String,email: String)=viewModelScope.launch {
        UserRepo.signupGoogleSaveMobile(mobileNo,email).let {
            _resp.postValue(it.body())
        }
    }

    fun completeProfileGoogle(email: String,mobileNo: String,password: String)=viewModelScope.launch {
        UserRepo.completeProfileGoogle(email, mobileNo, password).let {
            _respNew.postValue(it.body())
        }
    }

    fun addProperty(
            name:String,
            type:String,
            houseNo:String,
            colony:String,
            city:String,
            state:String,
            pincode:Int)=viewModelScope.launch {
        UserRepo.addProperty(name, type, houseNo, colony, city, state, pincode).let {
            _resp.postValue(it.body())
        }
    }

    fun getProperties()=viewModelScope.launch {
        UserRepo.getProperties().let {
            _respPropertyArray.postValue(it.body())
        }
    }

    fun getProperty(propertyId: String)=viewModelScope.launch {
        UserRepo.getProperty(propertyId).let {
            _respProperty.postValue(it.body())
        }
    }
    fun getUpdates(propertyId:String)=viewModelScope.launch {
        UserRepo.getUpdates(propertyId).let {
            _respUpdatesArray.postValue(it.body())
        }
    }

}