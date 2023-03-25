package com.example.bkash_tokenized.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bkash_tokenized.Constants
import com.example.bkash_tokenized.Constants.amount
import com.example.bkash_tokenized.Constants.bkashSandboxAppKey
import com.example.bkash_tokenized.Constants.bkashSandboxAppSecret
import com.example.bkash_tokenized.Constants.bkashSandboxPassword
import com.example.bkash_tokenized.Constants.bkashSandboxUsername
import com.example.bkash_tokenized.Constants.callbackURL
import com.example.bkash_tokenized.Constants.currency
import com.example.bkash_tokenized.Constants.intents
import com.example.bkash_tokenized.Constants.merchantAssociationInfo
import com.example.bkash_tokenized.Constants.merchantInvoiceNumber
import com.example.bkash_tokenized.Constants.mode
import com.example.bkash_tokenized.Constants.payerReference
import com.example.bkash_tokenized.Constants.paymentIDBkash
import com.example.bkash_tokenized.Constants.sessionIdToken
import com.example.bkash_tokenized.SingleLiveEvent
import com.example.bkash_tokenized.model.request.CreatePaymentRequest
import com.example.bkash_tokenized.model.request.ExecutePaymentRequest
import com.example.bkash_tokenized.model.request.GrantTokenRequest
import com.example.bkash_tokenized.model.request.QueryPaymentRequest
import com.example.bkash_tokenized.model.response.CreatePaymentResponse
import com.example.bkash_tokenized.model.response.ExecutePaymentResponse
import com.example.bkash_tokenized.model.response.GrantTokenResponse
import com.example.bkash_tokenized.model.response.QueryPaymentResponse
import com.example.bkash_tokenized.network.ApiInterface
import com.example.bkash_tokenized.network.BkashApiClient

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val job = Job()
    private val grantTokenLiveData = SingleLiveEvent<GrantTokenResponse?>()
    private val createPaymentLiveData = SingleLiveEvent<CreatePaymentResponse?>()
    private val executePaymentLiveData = SingleLiveEvent<ExecutePaymentResponse?>()
    private val queryPaymentLiveData = SingleLiveEvent<QueryPaymentResponse?>()
    fun getGrantTokenObserver(): SingleLiveEvent<GrantTokenResponse?> {
        return grantTokenLiveData
    }
    fun getCreatePaymentObserver(): SingleLiveEvent<CreatePaymentResponse?> {
        return createPaymentLiveData
    }
    fun getExecutePaymentObserver(): SingleLiveEvent<ExecutePaymentResponse?> {
        return executePaymentLiveData
    }
    fun getQueryPaymentObserver(): SingleLiveEvent<QueryPaymentResponse?> {
        return queryPaymentLiveData
    }
    fun grantTokenApiCall() {
        viewModelScope.launch(Dispatchers.IO + job) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postGrantToken(
                username = bkashSandboxUsername,
                password = bkashSandboxPassword,
                GrantTokenRequest(
                    appKey = bkashSandboxAppKey,
                    appSecret = bkashSandboxAppSecret
                )
            )
            grantTokenLiveData.postValue(response)
        }
    }

    fun createPaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + job) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postPaymentCreate(
                authorization = "Bearer $sessionIdToken",
                xAppKey = bkashSandboxAppKey,
                CreatePaymentRequest(
                    mode = mode,
                    payerReference = payerReference,
                    callbackURL = callbackURL,
                    merchantAssociationInfo = merchantAssociationInfo,
                    amount = amount,
                    currency = currency,
                    intent = intents,
                    merchantInvoiceNumber = merchantInvoiceNumber,
                )
            )
            createPaymentLiveData.postValue(response)
        }
    }

    fun executePaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + job) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postPaymentExecute(
                authorization = "Bearer $sessionIdToken",
                xAppKey = bkashSandboxAppKey,
                ExecutePaymentRequest(
                    paymentID = paymentIDBkash
                )
            )
            executePaymentLiveData.postValue(response)
        }
    }

    fun queryPaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + job) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postQueryPayment(
                authorization = "Bearer $sessionIdToken",
                xAppKey = bkashSandboxAppKey,
                QueryPaymentRequest(
                    paymentID = paymentIDBkash
                )
            )
            queryPaymentLiveData.postValue(response)
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        Constants.pd?.dismiss()
        throwable.printStackTrace()
    }
}