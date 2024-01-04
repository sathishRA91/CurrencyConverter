package com.rasathish.currencyconversion.data.remotedatasource

import com.rasathish.currencyconversion.data.model.CurrencyModelDto
import com.rasathish.currencyconversion.data.model.IbanModelDto
import kotlinx.coroutines.flow.Flow

/**
 * Created by sathish on 04,January,2024
 */
interface RemoteDataSource {

    suspend fun loadCurrency(): Flow<Result<CurrencyModelDto>>

    suspend fun validateIban(ibanNumber: String):Flow<Result<IbanModelDto>>
}