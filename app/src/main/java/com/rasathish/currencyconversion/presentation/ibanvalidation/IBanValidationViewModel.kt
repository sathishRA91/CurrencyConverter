package com.rasathish.currencyconversion.presentation.ibanvalidation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasathish.currencyconversion.constant.ResultResource
import com.rasathish.currencyconversion.domain.model.CurrencyData
import com.rasathish.currencyconversion.domain.model.IbanModel
import com.rasathish.currencyconversion.domain.usecase.IbanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by sathish on 04,January,2024
 */
@HiltViewModel
class IBanValidationViewModel @Inject constructor(private val ibanUseCase: IbanUseCase) :  ViewModel(){

    var ibanNumber = MutableStateFlow("")
    var ibanBankName = MutableStateFlow("")
    var ibanCountry = MutableStateFlow("")
    var ibanCity = MutableStateFlow("")
    var ibanStatusMessage = MutableStateFlow("")

    private var resultResponse = MutableStateFlow<ResultResource<IbanModel>?>(null)
    val getResultResponse: StateFlow<ResultResource<IbanModel>?> = resultResponse

    fun validateIban()
    {
        viewModelScope.launch {
            ibanUseCase.invoke(ibanNumber.value).collectLatest {
                resultResponse.value=it
            }
        }
    }

}