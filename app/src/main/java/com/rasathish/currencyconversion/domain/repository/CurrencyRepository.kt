package com.rasathish.currencyconversion.domain.repository

import com.rasathish.currencyconversion.data.model.CurrencyModelDto
import kotlinx.coroutines.flow.Flow

/**
 * Created by sathish on 04,January,2024
 */
interface CurrencyRepository {

suspend fun loadCurrency():Flow<Result<CurrencyModelDto>>

}