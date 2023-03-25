package com.example.bkash_tokenized.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bkash_tokenized.Constants
import com.example.bkash_tokenized.SingleLiveEvent
import com.example.bkash_tokenized.model.request.SearchTransactionRequest
import com.example.bkash_tokenized.model.response.SearchTransactionResponse
import com.example.bkash_tokenized.network.ApiInterface
import com.example.bkash_tokenized.network.BkashApiClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val job = Job()
    private val searchPaymentLiveData = SingleLiveEvent<SearchTransactionResponse?>()
    fun getSearchPaymentObserver(): SingleLiveEvent<SearchTransactionResponse?> {
        return searchPaymentLiveData
    }
    fun searchPaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + job) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postSearchTransaction(
                authorization = "Bearer ${Constants.sessionIdToken}",
                xAppKey = Constants.bkashSandboxAppKey,
                SearchTransactionRequest(
                    trxID = Constants.searchTextInput
                )
            )
            searchPaymentLiveData.postValue(response)
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.printStackTrace()
    }
}