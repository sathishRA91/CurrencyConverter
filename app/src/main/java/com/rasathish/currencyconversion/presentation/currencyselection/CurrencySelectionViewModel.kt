package com.rasathish.currencyconversion.presentation.currencyselection

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasathish.currencyconversion.presentation.currencyselection.model.CurrencySelectionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject

/**
 * Created by sathish on 03,January,2024
 */
@HiltViewModel
class CurrencySelectionViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {


    var currencyListFlow = MutableStateFlow<ArrayList<CurrencySelectionModel>?>(null)

    val getCurrencyList: StateFlow<ArrayList<CurrencySelectionModel>?> =currencyListFlow

    var decimalFormat: NumberFormat = DecimalFormat("#0.000")

    init {
        getCurrency()
    }


    private fun getCurrency() {

        viewModelScope.launch {
            val currencyData = useCase.currencyDao().getCurrency()
            if(currencyData!=null && currencyData.currencyCode.isNotEmpty())
            {
                val currencyList=ArrayList<CurrencySelectionModel>()
                try {
                    val response= JSONObject(currencyData.currencyCode)
                    val currencyKeys=response.keys()
                    while (currencyKeys.hasNext())
                    {
                        val currencyCode=currencyKeys.next().toString()
                        val format= BigDecimal(response.optDouble(currencyCode))
                        val currencyAmount=decimalFormat.format(format)
                        currencyList.add(CurrencySelectionModel(currencyCode,currencyAmount.toString()))
                    }
                    currencyListFlow.value=currencyList
                }catch (exception: Exception) {
                    Log.e("exceptionGetCurrency", exception.toString())
                }


            }
        }

    }

}