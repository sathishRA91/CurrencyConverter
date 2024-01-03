package com.rasathish.currencyconversion.data.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class ConversationModel(
    val base: String,
    val date: String,
    @SerializedName("rates")
    val rates: JsonObject,
    val success: Boolean,
    val timestamp: Int
)