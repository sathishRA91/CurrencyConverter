package com.rasathish.currencyconversion.data.repository

import com.rasathish.currencyconversion.data.model.CurrencyModelDto
import com.rasathish.currencyconversion.data.remotedatasource.RemoteDataSource
import com.rasathish.currencyconversion.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by sathish on 04,January,2024
 */
class CurrencyRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource):CurrencyRepository {
    override suspend fun loadCurrency(): Flow<Result<CurrencyModelDto>> {
       return remoteDataSource.loadCurrency()
    }
}