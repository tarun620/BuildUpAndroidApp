package com.example.buildup

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.responses.*
import com.example.api.models.responses.expenditure.ExpenditureArrayResponse
import com.example.api.models.responses.expenditure.TotalExpenditureResponse
import com.example.api.models.responses.products.ProductCategoriesResponse
import com.example.api.models.responses.products.ProductResponse
import com.example.api.models.responses.products.ProductSubCategoriesResponse
import com.example.api.models.responses.products.ProductsArrayResponse
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
    private val _respTotalExpenditure= MutableLiveData<TotalExpenditureResponse>()
    private val _respExpenditureArray=MutableLiveData<ExpenditureArrayResponse>()
    private val _respProductCategoryArray= MutableLiveData<ProductCategoriesResponse>()
    private val _respProductSubCategoryArray= MutableLiveData<ProductSubCategoriesResponse>()
    private val _respProducts=MutableLiveData<ProductsArrayResponse>()
    private val _respProduct=MutableLiveData<ProductResponse>()

    val resp:LiveData<SignupMobileResponse?> = _resp
    val respNew:LiveData<SignupMobileFinalResponse?> = _respNew
    val respNewImage:LiveData<LoginResponse?> = _respNewImage
    val respNewImageGoogle:LiveData<LoginGoogleResponse?> = _respNewImageGoogle
    val respPropertyArray:LiveData<PropertiesResponse?> = _respPropertyArray
    val respUpdatesArray:LiveData<UpdatesResponse?> = _respUpdatesArray
    val respProperty:LiveData<SinglePropertyResponse?> = _respProperty
    val respTotalExpenditure:LiveData<TotalExpenditureResponse?> = _respTotalExpenditure
    val respExpenditureArray:LiveData<ExpenditureArrayResponse?> = _respExpenditureArray
    val respProductCategoryArray:LiveData<ProductCategoriesResponse?> = _respProductCategoryArray
    val respProductSubCategoryArray:LiveData<ProductSubCategoriesResponse> = _respProductSubCategoryArray
    val respProducts:LiveData<ProductsArrayResponse> = _respProducts
    val respProduct:LiveData<ProductResponse> = _respProduct

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

//    fun login(mobileNo:String,password:String)=viewModelScope.launch {
//
//        UserRepo.login(mobileNo,password).let {
//            _respNewImage.postValue(it.body())
//        }
//
//
//    }

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

    fun getTotalExpenditure(propertyId: String)=viewModelScope.launch {
        UserRepo.getTotalExpenditure(propertyId).let {
            _respTotalExpenditure.postValue(it.body())
        }
    }

    fun getExpenditureArray(propertyId: String)=viewModelScope.launch {
        UserRepo.getExpenditureArray(propertyId).let {
            _respExpenditureArray.postValue(it.body())
        }
    }

    fun getProductCategories()=viewModelScope.launch {
        UserRepo.getProductCategories().let {
            _respProductCategoryArray.postValue(it.body())
        }
    }

    fun getProductSubCategories(productCategoryId:String)=viewModelScope.launch {
        UserRepo.getProductSubCategories(productCategoryId).let {
            _respProductSubCategoryArray.postValue(it.body())
        }
    }

    fun getProducts(productSubCategoryId:String)=viewModelScope.launch {
        UserRepo.getProducts(productSubCategoryId).let {
            _respProducts.postValue(it.body())
        }
    }

    fun getProduct(productId:String)=viewModelScope.launch {
        UserRepo.getProduct(productId).let {
            _respProduct.postValue(it.body())
        }
    }

}