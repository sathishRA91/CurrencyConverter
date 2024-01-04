package com.rasathish.currencyconversion.domain.usecase

import com.rasathish.currencyconversion.constant.ResultResource
import com.rasathish.currencyconversion.data.model.toDomainModel
import com.rasathish.currencyconversion.domain.model.CurrencyModel
import com.rasathish.currencyconversion.domain.model.IbanModel
import com.rasathish.currencyconversion.domain.repository.IbanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Created by sathish on 04,January,2024
 */
class IbanUseCase @Inject constructor(private val ibanRepository: IbanRepository)
{
    operator fun invoke(ibanNumber: String): Flow<ResultResource<IbanModel>> = channelFlow {
        send(ResultResource.Loading())
        ibanRepository.validateIban(ibanNumber).collectLatest {
            it.fold(
                onSuccess = {result -> send(ResultResource.Success(result.toDomainModel()))},
                onFailure = {error -> send(ResultResource.ErrorMessage(error.message))}
            )
        }

    }
}