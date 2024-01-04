package com.rasathish.currencyconversion.data.repository

import com.rasathish.currencyconversion.data.model.IbanModelDto
import com.rasathish.currencyconversion.data.remotedatasource.RemoteDataSource
import com.rasathish.currencyconversion.domain.repository.IbanRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by sathish on 04,January,2024
 */
class IbanRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource):IbanRepository {
    override suspend fun validateIban(ibanNumber:String): Flow<Result<IbanModelDto>> {
       return remoteDataSource.validateIban(ibanNumber)
    }
}