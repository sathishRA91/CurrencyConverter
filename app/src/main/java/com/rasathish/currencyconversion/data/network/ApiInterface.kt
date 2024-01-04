package com.rasathish.currencyconversion.data.network

import com.rasathish.currencyconversion.constant.ApiConfig
import com.rasathish.currencyconversion.data.model.CurrencyModelDto
import com.rasathish.currencyconversion.data.model.IbanModelDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by sathish on 04,January,2024
 */
interface ApiInterface {

    @GET(ApiConfig.GET_CURRENCY)
    suspend fun getCurrency(@Query("base") base:String):Response<CurrencyModelDto>


    @GET(ApiConfig.VALIDATE_IBAN)
    suspend fun validateIban(@Query("iban_number") ibanNumber:String):Response<IbanModelDto>
}