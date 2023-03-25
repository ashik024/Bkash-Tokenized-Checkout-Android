package com.example.bkash_tokenized.model.request

import com.google.gson.annotations.SerializedName

data class QueryPaymentRequest(
  @SerializedName("paymentID")
  var paymentID: String? = null,
  )