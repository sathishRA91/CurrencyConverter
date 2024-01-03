package com.rasathish.currencyconversion.data.model

import com.google.gson.annotations.SerializedName

data class BankData(
    @SerializedName("bank_code")
    val bankCode: String,
    val bic: String,
    val city: String,
    val name: String,
    val zip: String
)