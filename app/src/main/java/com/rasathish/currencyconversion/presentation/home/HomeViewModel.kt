package com.rasathish.currencyconversion.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasathish.currencyconversion.constant.ResultResource
import com.rasathish.currencyconversion.domain.model.CurrencyData
import com.rasathish.currencyconversion.domain.usecase.CurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by sathish on 04,January,2024
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyUseCase: CurrencyUseCase
) : ViewModel() {

    var currencyAmount = MutableStateFlow("1")

    var currencyCode = MutableStateFlow("KWD")

    private var resultResponse = MutableStateFlow<ResultResource<CurrencyData>?>(null)

    val getResultResponse: StateFlow<ResultResource<CurrencyData>?> = resultResponse

    init {
        getCurrency()
    }

    private fun getCurrency() {
        viewModelScope.launch {
            currencyUseCase.invoke().collectLatest {
                resultResponse.value = it
            }
        }
    }

}