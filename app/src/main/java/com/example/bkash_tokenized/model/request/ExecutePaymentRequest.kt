package com.example.bkash_tokenized.model.request

import com.google.gson.annotations.SerializedName

data class ExecutePaymentRequest(
  @SerializedName("paymentID")
  var paymentID: String? = null,
  )