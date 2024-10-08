package com.example.buildup

import android.util.Log
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entities.User
import com.example.api.models.responsesAndData.address.GetAddressbyByIdResponse
import com.example.api.models.responsesAndData.address.GetAddressesResponse
import com.example.api.models.responsesAndData.appData.GetAppDataResponse
import com.example.api.models.responsesAndData.appData.GetContactDetailsResponse
import com.example.api.models.responsesAndData.brand.GetBrandsResponse
import com.example.api.models.responsesAndData.cart.cartResponses.GetCostDeliveryDetailsResponse
import com.example.api.models.responsesAndData.cart.cartResponses.GetProductsFromCartResponse
import com.example.api.models.responsesAndData.expenditure.expenditureResponses.ExpendituresResponse
import com.example.api.models.responsesAndData.expenditure.expenditureResponses.TotalExpenditureResponse
import com.example.api.models.responsesAndData.loginSignup.loginSignupResponses.*
import com.example.api.models.responsesAndData.order.CancelOrderResponse
import com.example.api.models.responsesAndData.order.GetAllOrdersResponse
import com.example.api.models.responsesAndData.order.GetOrderByIdResponse
import com.example.api.models.responsesAndData.products.productsResponses.*
import com.example.api.models.responsesAndData.property.propertyEntities.AddPropertyData
import com.example.api.models.responsesAndData.property.propertyResponses.PropertiesResponse
import com.example.api.models.responsesAndData.property.propertyResponses.SinglePropertyResponse
import com.example.api.models.responsesAndData.rating.AddProductRatingDataCount
import com.example.api.models.responsesAndData.rating.GetProductRatingResponse
import com.example.api.models.responsesAndData.rating.GetUserProductRatingResponse
import com.example.api.models.responsesAndData.returnOrder.GetOrderReturnDetailsResponse
import com.example.api.models.responsesAndData.returnOrder.PlaceOrderReturnRequestData
import com.example.api.models.responsesAndData.search.GetAutoCompleteQueriesResponse
import com.example.api.models.responsesAndData.updates.UpdatesResponse
import com.example.api.models.responsesAndData.wishlist.GetWishlistResponse
import com.example.buildup.data.UserRepo
import kotlinx.coroutines.launch

class AuthViewModel:ViewModel() {

    private val _resp= MutableLiveData<SuccessMessageResponse?>()
    private val _respNew=MutableLiveData<SignupMobileResponse?>()
    private val _respNewImage=MutableLiveData<LoginResponse?>()
    private val _respNewImageGoogle=MutableLiveData<LoginGoogleResponse?>()
    private val _respPropertyArray=MutableLiveData<PropertiesResponse?>()
    private val _respProperty=MutableLiveData<SinglePropertyResponse?>()
    private val _respUpdatesArray=MutableLiveData<UpdatesResponse?>()
    private val _respTotalExpenditure= MutableLiveData<TotalExpenditureResponse>()
    private val _respExpenditureArray=MutableLiveData<ExpendituresResponse>()
    private val _respProductCategoryArray= MutableLiveData<ProductCategoriesResponse>()
    private val _respProductSubCategoryArray= MutableLiveData<ProductSubCategoriesResponse>()
    private val _respProducts=MutableLiveData<ProductsResponse>()
    private val _respProduct=MutableLiveData<ProductResponse>()
    private val _respIsUserExist=MutableLiveData<UserExistResponse>()
    private val _respForgetPassword=MutableLiveData<SuccessMessageResponse?>()
    private val _respSetNewPassword=MutableLiveData<SuccessMessageResponse?>()
    private val _respAddProductToCart=MutableLiveData<SuccessMessageResponse?>()
    private val _respGetProductsFromCart=MutableLiveData<GetProductsFromCartResponse?>()
    private val _respUpdateProductQuantityCart=MutableLiveData<SuccessMessageResponse?>()
    private val _respRemoveProductFromCart=MutableLiveData<SuccessMessageResponse?>()
    private val _respAddProductToWishlist=MutableLiveData<SuccessMessageResponse?>()
    private val _respRemoveProductFromWishlist=MutableLiveData<SuccessMessageResponse?>()
    private val _respGetWishlist= MutableLiveData<GetWishlistResponse?>()
    private val _respCreateOrder= MutableLiveData<SuccessMessageResponse?>()
    private val _respGetAllOrders=MutableLiveData<GetAllOrdersResponse?>()
    private val _respGetOrderById=MutableLiveData<GetOrderByIdResponse?>()
    private val _respGetAddresses=MutableLiveData<GetAddressesResponse?>()
    private val _respGetAddressById=MutableLiveData<GetAddressbyByIdResponse?>()
//    private val _respGetProductsBySearchQueryData = MutableLiveData<GetProductsBySearchQueryData?>()
    private val _respEditPropertyAddress = MutableLiveData<SuccessMessageResponse?>()
    private val _respDeletePropertyAddress = MutableLiveData<SuccessMessageResponse?>()
    private val _respGetRecentlyViewedProducts = MutableLiveData<RecentlyViewedProductsResponse?>()
    private val _respDeleteWishlist = MutableLiveData<SuccessMessageResponse?>()
    private val _respGetAutocompleteQueries = MutableLiveData<GetAutoCompleteQueriesResponse?>()
    private val _respGetBrands = MutableLiveData<GetBrandsResponse?>()
    private val _respAddProductRating = MutableLiveData<SuccessMessageResponse?>()
    private val _respGetProductRating = MutableLiveData<GetProductRatingResponse?>()
    private val _respGetUserProductrating = MutableLiveData<GetUserProductRatingResponse?>()
    private val _respGetOrderReturnDetails = MutableLiveData<GetOrderReturnDetailsResponse?>()
    private val _respPlaceOrderReturnRequest = MutableLiveData<SuccessMessageResponse?>()
    private val _respCancelOrder = MutableLiveData<CancelOrderResponse?>()
    private val _respGetCostDeliveryDetails = MutableLiveData<GetCostDeliveryDetailsResponse?>()
    private val _respGetAppData = MutableLiveData<GetAppDataResponse?>()
    private val _respGetContactDetails = MutableLiveData<GetContactDetailsResponse?>()

    val resp:LiveData<SuccessMessageResponse?> = _resp
    val respNew:LiveData<SignupMobileResponse?> = _respNew
    val respNewImage:LiveData<LoginResponse?> = _respNewImage
    val respNewImageGoogle:LiveData<LoginGoogleResponse?> = _respNewImageGoogle
    val respPropertyArray:LiveData<PropertiesResponse?> = _respPropertyArray
    val respUpdatesArray:LiveData<UpdatesResponse?> = _respUpdatesArray
    val respProperty:LiveData<SinglePropertyResponse?> = _respProperty
    val respTotalExpenditure:LiveData<TotalExpenditureResponse?> = _respTotalExpenditure
    val respExpenditureArray:LiveData<ExpendituresResponse?> = _respExpenditureArray
    val respProductCategoryArray:LiveData<ProductCategoriesResponse?> = _respProductCategoryArray
    val respProductSubCategoryArray:LiveData<ProductSubCategoriesResponse> = _respProductSubCategoryArray
    val respProducts:LiveData<ProductsResponse> = _respProducts
    val respProduct:LiveData<ProductResponse> = _respProduct
    val respIsUserExist:LiveData<UserExistResponse> = _respIsUserExist
    val respForgetPassword:LiveData<SuccessMessageResponse?> = _respForgetPassword
    val respSetNewPassword:LiveData<SuccessMessageResponse?> = _respSetNewPassword
    val respAddProductToCart:LiveData<SuccessMessageResponse?> = _respAddProductToCart
    val respGetProductsFromCart:LiveData<GetProductsFromCartResponse?> = _respGetProductsFromCart
    val respUpdateProductQuantityCart:LiveData<SuccessMessageResponse?> = _respUpdateProductQuantityCart
    val respRemoveProductFromCart :LiveData<SuccessMessageResponse?> = _respRemoveProductFromCart
    val respAddProductToWishlist :LiveData<SuccessMessageResponse?> = _respAddProductToWishlist
    val respRemoveProductFromWishlist :LiveData<SuccessMessageResponse?> = _respRemoveProductFromWishlist
    val respGetWishlist : LiveData<GetWishlistResponse?> = _respGetWishlist
    val respCreateOrder : LiveData<SuccessMessageResponse?> = _respCreateOrder
    val respGetAllOrders : LiveData<GetAllOrdersResponse?> = _respGetAllOrders
    val respGetOrderById : LiveData<GetOrderByIdResponse?> = _respGetOrderById
    val respGetAddresses : LiveData<GetAddressesResponse?> = _respGetAddresses
    val respGetAddressById : LiveData<GetAddressbyByIdResponse?> = _respGetAddressById
//    val respGetProductsBySearchQueryData : LiveData<GetProductsBySearchQueryData>
    val respEditPropertyAddress : LiveData<SuccessMessageResponse?> = _respEditPropertyAddress
    val respDeletePropertyAddress : LiveData<SuccessMessageResponse?> =_respDeletePropertyAddress
    val respGetRecentlyViewedProducts : LiveData<RecentlyViewedProductsResponse?> = _respGetRecentlyViewedProducts
    val respDeleteWishlist : LiveData<SuccessMessageResponse?> = _respDeleteWishlist
    val respGetAutocompleteQueries : LiveData<GetAutoCompleteQueriesResponse?> = _respGetAutocompleteQueries
    val respGetBrands : LiveData<GetBrandsResponse?> = _respGetBrands
    val respAddProductRating : LiveData<SuccessMessageResponse?> = _respAddProductRating
    val respGetProductRating : LiveData<GetProductRatingResponse?> = _respGetProductRating
    val respGetUserProductrating : LiveData<GetUserProductRatingResponse?> = _respGetUserProductrating
    val respGetOrderReturnDetails : LiveData<GetOrderReturnDetailsResponse?> = _respGetOrderReturnDetails
    val respPlaceOrderReturnRequest : LiveData<SuccessMessageResponse?>  = _respPlaceOrderReturnRequest
    val respCancelOrder : LiveData<CancelOrderResponse?> = _respCancelOrder
    val respGetCostDeliveryDetails : LiveData<GetCostDeliveryDetailsResponse?> = _respGetCostDeliveryDetails
    val respGetAppData : LiveData<GetAppDataResponse?> = _respGetAppData
    val respGetContactDetails : LiveData<GetContactDetailsResponse?> = _respGetContactDetails


    fun signup(mobileNo : String)=viewModelScope.launch {
        UserRepo.signup(mobileNo).let {
            _resp.postValue(it)
        }
    }

    fun verifyOtpSignup(mobileNo: String, otp:String)=viewModelScope.launch {
        UserRepo.verifyOTPSignup(mobileNo,otp).let {
            _resp.postValue(it)
        }
    }

    fun completeProfile(mobileNo: String,name:String,email:String)=viewModelScope.launch {
        UserRepo.completeProfile(mobileNo,name,email).let {
            _respNew.postValue(it)
        }
    }

    fun login(mobileNo:String)=viewModelScope.launch {
        UserRepo.login(mobileNo).let {
            _resp.postValue(it)
        }
    }

    fun verifyOTPLogin(mobileNo: String, otp:String)=viewModelScope.launch {
        UserRepo.verifyOTPLogin(mobileNo, otp).let {
            _respNewImage.postValue(it)
        }
    }

    fun loginSignupGoogle(name:String,email:String,profileImage:String?)=viewModelScope.launch{
        Log.d("google123AuthViewModel","reached here")
        UserRepo.loginSignupGoogle(name,email,profileImage).let {
            _respNewImageGoogle.postValue(it)
        }
    }

    fun signupGoogleSaveMobile(mobileNo: String,email: String)=viewModelScope.launch {
        UserRepo.signupGoogleSaveMobile(mobileNo,email).let {
            _resp.postValue(it)
        }
    }

    fun verifyOTPFuncSignupGoogle(mobileNo: String, otp:String)=viewModelScope.launch {
        UserRepo.verifyOTPFuncSignupGoogle(mobileNo, otp).let {
            _respNewImageGoogle.postValue(it)
        }
    }

    fun completeProfileGoogle(email: String,mobileNo: String,password: String)=viewModelScope.launch {
        UserRepo.completeProfileGoogle(email, mobileNo, password).let {
            _respNew.postValue(it)
        }
    }

    fun addProperty(
            name:String,
            mobileNo:String,
            type:String,
            houseNo:String,
            colony:String,
            city:String,
            state:String,
            pincode:Int,
            coordinates:ArrayList<Double>,
            landmark:String?)=viewModelScope.launch {
        UserRepo.addProperty(name, mobileNo, type, houseNo, colony, city, state, pincode,coordinates,landmark).let {
            _resp.postValue(it)
        }
    }

    fun getProperties()=viewModelScope.launch {
        UserRepo.getProperties().let {
            _respPropertyArray.postValue(it)
        }
    }

    fun getProperty(propertyId: String)=viewModelScope.launch {
        UserRepo.getProperty(propertyId).let {
            _respProperty.postValue(it)
        }
    }
    fun getUpdates(propertyId:String)=viewModelScope.launch {
        UserRepo.getUpdates(propertyId).let {
            _respUpdatesArray.postValue(it)
        }
    }

    fun getTotalExpenditure(propertyId: String)=viewModelScope.launch {
        UserRepo.getTotalExpenditure(propertyId).let {
            _respTotalExpenditure.postValue(it)
        }
    }

    fun getExpenditureArray(propertyId: String)=viewModelScope.launch {
        UserRepo.getExpenditureArray(propertyId).let {
            _respExpenditureArray.postValue(it)
        }
    }

    fun getProductCategories(isImage:Boolean,limit:Int?)=viewModelScope.launch {
        UserRepo.getProductCategories(isImage,limit).let {
            _respProductCategoryArray.postValue(it)
        }
    }

    fun getProductSubCategories(productCategoryId:String)=viewModelScope.launch {
        UserRepo.getProductSubCategories(productCategoryId).let {
            _respProductSubCategoryArray.postValue(it)
        }
    }

    fun getProducts(productSubCategoryId:String)=viewModelScope.launch {
        UserRepo.getProducts(productSubCategoryId).let {
            _respProducts.postValue(it)
        }
    }
    fun getProductsByProductCategoryId(productCategoryId:String)=viewModelScope.launch {
        UserRepo.getProductsByProductCategoryId(productCategoryId).let {
            _respProducts.postValue(it)
        }
    }

    fun getProduct(productId:String,isBrand:Boolean,inCart:Boolean,isWishlisted:Boolean)=viewModelScope.launch {
        UserRepo.getProduct(productId,isBrand, inCart, isWishlisted).let {
            _respProduct.postValue(it)
        }
    }

//    fun isUserExist(email:String)=viewModelScope.launch {
//        UserRepo.isUserExist(email).let {
//            _respIsUserExist.postValue(it)
//        }
//    }

    fun forgetPassword(mobileNo: String)=viewModelScope.launch {
        UserRepo.forgetPassword(mobileNo).let {
            _respForgetPassword.postValue(it)
        }
    }

    fun setNewPassword(mobileNo: String,password:String)=viewModelScope.launch {
        UserRepo.setNewPassword(mobileNo, password).let{
            _respSetNewPassword.postValue(it)
        }
    }

    fun addProductToCart(productId: String,fromWishlist:Boolean)=viewModelScope.launch {
        UserRepo.addProductToCart(productId,fromWishlist).let {
            _respAddProductToCart.postValue(it)
        }
    }

    fun getProductsFromCart()=viewModelScope.launch {
        UserRepo.getProductsFromCart().let {
            _respGetProductsFromCart.postValue(it)
        }
    }

    fun updateProductQuantityCart(productId: String, quantity:Int)=viewModelScope.launch {
        UserRepo.updateProductQuantityCart(productId,quantity).let {
            _respUpdateProductQuantityCart.postValue(it)
        }
    }

    fun removeProductFromCart(productId:String)=viewModelScope.launch {
        UserRepo.removeProductFromCart(productId).let {
            _respRemoveProductFromCart.postValue(it)
        }
    }

    fun addProductToWishlist(productId: String)=viewModelScope.launch {
        UserRepo.addProductToWishlist(productId).let {
            _respAddProductToWishlist.postValue(it)
        }
    }
    fun removeProductFromWishlist(productId: String)=viewModelScope.launch {
        UserRepo.removeProductFromWishlist(productId).let {
            _respRemoveProductFromWishlist.postValue(it)
        }
    }
    fun getWishlist(pageNum:Int)=viewModelScope.launch {
        UserRepo.getWishlist(pageNum).let {
            _respGetWishlist.postValue(it)
        }
    }
    fun createOrder(propertyId: String,paymentMethod:String,transactionId:String,isPersonalUse:Boolean)=viewModelScope.launch {
        UserRepo.createOrder(propertyId, paymentMethod, transactionId, isPersonalUse).let {
            _respCreateOrder.postValue(it)
        }
    }

    fun getAllOrders(pageNum:Int)=viewModelScope.launch {
        UserRepo.getAllOrders(pageNum).let {
            _respGetAllOrders.postValue(it)
        }
    }
    fun getOrderById(orderId:String)=viewModelScope.launch {
        UserRepo.getOrderById(orderId).let {
            _respGetOrderById.postValue(it)
        }
    }

    fun getAddresses()=viewModelScope.launch {
        UserRepo.getAddresses().let {
            _respGetAddresses.postValue(it)
        }
    }

    fun getAddressById(propertyId: String)=viewModelScope.launch {
        UserRepo.getAddressById(propertyId).let {
            _respGetAddressById.postValue(it)
        }
    }
    fun getProductsBySearchQuery(pageNum:Int,getProductsBySearchQueryData: GetProductsBySearchQueryData)=viewModelScope.launch {
        UserRepo.getProductsBySearchQuery(pageNum,getProductsBySearchQueryData).let {
            _respProducts.postValue(it)
        }
    }
    fun getProductsBySearchQuery2(searchQuery:String?,pageNum:Int?,getProductsBySearchQueryData: GetProductsBySearchQueryData)=viewModelScope.launch {
        UserRepo.getProductsBySearchQuery2(searchQuery,pageNum,getProductsBySearchQueryData).let {
            _respProducts.postValue(it)
        }
    }
    fun editPropertyAddress(propertyId: String,addPropertyData: AddPropertyData)=viewModelScope.launch {
        UserRepo.editPropertyAddress(propertyId, addPropertyData).let {
            _respEditPropertyAddress.postValue(it)
        }
    }
    fun deletePropertyAddress(propertyId: String)=viewModelScope.launch {
        UserRepo.deletePropertyAddress(propertyId).let {
            _respDeletePropertyAddress.postValue(it)
        }
    }

    fun getRecentlyViewedProducts()=viewModelScope.launch {
        UserRepo.getRecentlyViewedProducts().let {
            _respGetRecentlyViewedProducts.postValue(it)
        }
    }

    fun deleteWishlist()=viewModelScope.launch {
        UserRepo.deleteWishlist().let {
            _respDeleteWishlist.postValue(it)
        }
    }

    fun getAutocompleteQueries(searchQuery: String)=viewModelScope.launch {
        UserRepo.getAutocompleteQueries(searchQuery).let {
            _respGetAutocompleteQueries.postValue(it)
        }
    }

    fun getBrands(isImage:Boolean,limit:Int?)=viewModelScope.launch {
        UserRepo.getBrands(isImage, limit).let {
            _respGetBrands.postValue(it)
        }
    }

    fun addProductRating(productId: String,count:Int)=viewModelScope.launch {
        UserRepo.addProductRating(productId, count).let {
            _respAddProductRating.postValue(it)
        }
    }

    fun getProductRating(productId: String)=viewModelScope.launch {
        UserRepo.getProductRating(productId).let {
            _respGetProductRating.postValue(it)
        }
    }

    fun getUserProductRating(productId: String)=viewModelScope.launch {
        UserRepo.getUserProductRating(productId).let {
            _respGetUserProductrating.postValue(it)
        }
    }
    fun getOrderReturnDetails(orderId: String)=viewModelScope.launch {
        UserRepo.getOrderReturnDetails(orderId).let {
            _respGetOrderReturnDetails.postValue(it)
        }
    }
    fun placeOrderReturnRequest(orderId: String,placeOrderReturnRequestData:PlaceOrderReturnRequestData)=viewModelScope.launch {
        UserRepo.placeOrderReturnRequest(orderId, placeOrderReturnRequestData).let {
            _respPlaceOrderReturnRequest.postValue(it)
        }
    }
    fun cancelOrder(orderId: String, forceAll:Boolean)=viewModelScope.launch {
        UserRepo.cancelOrder(orderId,forceAll).let {
            _respCancelOrder.postValue(it)
        }
    }

    fun getCostDeliveryDetails(propertyId: String?)=viewModelScope.launch {
        UserRepo.getCostDeliveryDetails(propertyId).let {
            _respGetCostDeliveryDetails.postValue(it)
        }
    }
    fun getAppData(screenType:String)=viewModelScope.launch {
        UserRepo.getAppData(screenType).let {
            _respGetAppData.postValue(it)
        }
    }
    fun getContactDetails()=viewModelScope.launch {
        UserRepo.getContactDetails().let {
            _respGetContactDetails.postValue(it)
        }
    }
}

