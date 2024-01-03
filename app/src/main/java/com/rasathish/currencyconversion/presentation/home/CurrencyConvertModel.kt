package com.rasathish.currencyconversion.presentation.home

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CurrencyConvertModel(
    val base: String,
    @SerializedName("rates")
    val rates: JsonObject,
    val timestamp: Long,
    val description: String,
    val error: Boolean,
    val message: String,
    val status: Int,
)