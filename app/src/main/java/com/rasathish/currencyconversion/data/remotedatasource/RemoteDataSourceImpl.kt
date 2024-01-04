package com.rasathish.currencyconversion.data.remotedatasource

import com.rasathish.currencyconversion.constant.AppConstant
import com.rasathish.currencyconversion.data.model.CurrencyModelDto
import com.rasathish.currencyconversion.data.model.IbanModelDto
import com.rasathish.currencyconversion.data.network.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by sathish on 04,January,2024
 */
class RemoteDataSourceImpl @Inject constructor(private val apiInterface: ApiInterface) : RemoteDataSource {
    override suspend fun loadCurrency(): Flow<Result<CurrencyModelDto>> {
        return flow {
            val response = apiInterface.getCurrency(AppConstant.BASE_CURRENCY)
            if (response.isSuccessful) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Throwable()))
            }
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun validateIban(ibanNumber: String): Flow<Result<IbanModelDto>> {
        return flow {
            val response = apiInterface.validateIban(ibanNumber)
            if (response.isSuccessful) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Throwable(response.message())))
            }
        }.flowOn(Dispatchers.IO)

    }
}