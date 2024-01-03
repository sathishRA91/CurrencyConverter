package com.rasathish.currencyconversion.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequest
import com.rasathish.currencyconversion.constant.AppConstant
import com.rasathish.currencyconversion.constant.ResultResource
import com.rasathish.currencyconversion.data.room.entity.CurrencyEntity
import com.rasathish.currencyconversion.repository.CurrencyRepository
import com.rasathish.currencyconversion.workmanager.CurrencyWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject



/**
 * Created by sathish on 03,January,2024
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val useCase: UseCase
) : ViewModel() {


    var currencyAmount = MutableStateFlow("1")

    var currencyCode = MutableStateFlow("USD")

    private var decimalFormat: NumberFormat = DecimalFormat("#0.000")

    private var currencyListFlow = MutableStateFlow<ArrayList<CurrencyItemModel>?>(null)

    val getCurrencyList: StateFlow<ArrayList<CurrencyItemModel>?> = currencyListFlow

    private var resultResponse= MutableStateFlow<ResultResource<CurrencyConvertModel>?>(null)

    val getResultResponse:StateFlow<ResultResource<CurrencyConvertModel>?> = resultResponse

    init {

        getCurrency()
    }


     private fun getCurrency() {

         resultResponse.value=ResultResource.Loading()

        viewModelScope.launch {
            val currencyData = useCase.currencyDao().getCurrency()
            if (currencyData != null && currencyData.currencyCode.isNotEmpty()) {
                val currencyList = ArrayList<CurrencyItemModel>()
                try {
                    val response = JSONObject(currencyData.currencyCode)
                    val currencyKeys = response.keys()
                    while (currencyKeys.hasNext()) {
                        val currencyCode = currencyKeys.next().toString()
                        val format = BigDecimal(response.optDouble(currencyCode))
                        val currencyAmount = decimalFormat.format(format)
                        currencyList.add(CurrencyItemModel(currencyCode, currencyAmount.toString()))
                    }
                    currencyListFlow.value = currencyList
                    resultResponse.value=ResultResource.Success()
                } catch (exception: Exception) {
                    Log.e("exceptionGetCurrency", exception.toString())
                    resultResponse.value=ResultResource.ErrorMessage(exception.toString())
                }


            }
        }

    }


}