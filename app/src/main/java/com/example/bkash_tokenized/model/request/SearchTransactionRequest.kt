package com.example.bkash_tokenized.model.request

import com.google.gson.annotations.SerializedName

data class SearchTransactionRequest(
  @SerializedName("trxID")
  var trxID: String? = null,
  )