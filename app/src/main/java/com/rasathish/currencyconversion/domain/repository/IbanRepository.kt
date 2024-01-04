package com.rasathish.currencyconversion.domain.repository

import com.rasathish.currencyconversion.data.model.IbanModelDto
import kotlinx.coroutines.flow.Flow

/**
 * Created by sathish on 04,January,2024
 */
interface IbanRepository {

    suspend fun validateIban(ibanNumber: String):Flow<Result<IbanModelDto>>

}