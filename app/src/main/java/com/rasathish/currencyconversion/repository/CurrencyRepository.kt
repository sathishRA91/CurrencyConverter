package com.rasathish.currencyconversion.repository

import com.google.gson.JsonObject
import com.rasathish.currencyconversion.BuildConfig
import com.rasathish.currencyconversion.constant.ApiConfig
import com.rasathish.currencyconversion.data.network.ApiInterface
import com.rasathish.currencyconversion.presentation.home.CurrencyConvertModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by sathish on 03,January,2024
 */
class CurrencyRepository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun loadCurrency(): Response<JsonObject> {       return withContext(Dispatchers.IO)
        {
            apiInterface.getCurrency(ApiConfig.GET_CURRENCY)
        }
    }

    suspend fun loadCurrencyConvert(): Response<CurrencyConvertModel> {
        return withContext(Dispatchers.IO)
        {
            apiInterface.getCurrencyConvert(ApiConfig.GET_CURRENCY_CONVERT + BuildConfig.CURRENCY_API + "&base=USD")
        }
    }
}