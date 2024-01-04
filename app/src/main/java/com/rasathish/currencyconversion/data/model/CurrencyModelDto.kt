package com.rasathish.currencyconversion.data.model

import com.google.gson.JsonObject

data class CurrencyModelDto(
    val base: String,
    val date: String,
    val rates: JsonObject,
    val success: Boolean,
    val timestamp: Int
)
