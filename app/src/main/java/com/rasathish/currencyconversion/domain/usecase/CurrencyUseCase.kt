package com.rasathish.currencyconversion.domain.usecase

import com.rasathish.currencyconversion.constant.ResultResource
import com.rasathish.currencyconversion.domain.model.CurrencyData
import com.rasathish.currencyconversion.domain.model.CurrencyModel
import com.rasathish.currencyconversion.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject

/**
 * Created by sathish on 04,January,2024
 */
class CurrencyUseCase @Inject constructor(private val currencyRepository: CurrencyRepository) {

    private var decimalFormat: NumberFormat = DecimalFormat("#0.000")

    operator fun invoke(): Flow<ResultResource<CurrencyData>> = channelFlow {
        send(ResultResource.Loading())
        currencyRepository.loadCurrency().collectLatest {
            it.fold(
                onSuccess = { result ->
                    val currencyList = ArrayList<CurrencyModel>()
                    try {
                        val response = JSONObject(result.rates.toString())
                        val currencyKeys = response.keys()
                        while (currencyKeys.hasNext()) {
                            val currencyCode = currencyKeys.next().toString()
                            val format = BigDecimal(response.optDouble(currencyCode))
                            val currencyAmount = decimalFormat.format(format)
                            currencyList.add(CurrencyModel(currencyCode, currencyAmount.toString()))
                        }
                        send(ResultResource.Success(CurrencyData(currencyList)))
                    } catch (exception: Exception) {
                        send(ResultResource.ErrorMessage(exception.message))
                    }
                },
                onFailure = { error -> send(ResultResource.ErrorMessage(error.message)) }
            )
        }

    }
}