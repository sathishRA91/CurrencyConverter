package com.rasathish.currencyconversion.presentation.currencyselection

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasathish.currencyconversion.constant.ResultResource
import com.rasathish.currencyconversion.domain.model.CurrencyModel
import com.rasathish.currencyconversion.domain.usecase.CurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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
class CurrencySelectionViewModel @Inject constructor(private val currencyUseCase: CurrencyUseCase) : ViewModel() {


    private var currencyListFlow = MutableStateFlow<ArrayList<CurrencyModel>?>(null)

    val getCurrencyList: StateFlow<ArrayList<CurrencyModel>?> =currencyListFlow

    init {
        getCurrency()
    }

    private fun getCurrency() {
        viewModelScope.launch {
            currencyUseCase().collectLatest {
                when(it)
                {
                    is ResultResource.Success ->{
                        currencyListFlow.value=it.data!!.rates
                    }
                    else -> {}
                }
            }
        }

    }

}