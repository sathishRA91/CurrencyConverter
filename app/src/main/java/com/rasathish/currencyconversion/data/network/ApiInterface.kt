package com.rasathish.currencyconversion.data.network

import com.google.gson.JsonObject
import com.rasathish.currencyconversion.presentation.home.CurrencyConvertModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by sathish on 03,January,2024
 */
interface ApiInterface {

    @GET
    suspend fun getCurrency(@Url urlName:String):Response<JsonObject>

    @GET
    suspend fun getCurrencyConvert(@Url urlName: String):Response<CurrencyConvertModel>
}