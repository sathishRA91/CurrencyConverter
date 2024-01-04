package com.rasathish.currencyconversion.data.model

import com.google.gson.annotations.SerializedName
import com.rasathish.currencyconversion.domain.model.IbanModel

data class IbanModelDto(
    @SerializedName("bank_data")
    val bankData: BankDataDto,
    @SerializedName("country_iban_example")
    val countryIbanExample: String,
    val iban: String,
    @SerializedName("iban_data")
    val ibanData: IbanDataDto,
    val message: String,
    val valid: Boolean
)

fun IbanModelDto.toDomainModel() = IbanModel(
    bankData = bankData.toDomainModel(),
    iban = iban,
    ibanData = ibanData.toDomainModel(),
    message = message,
    valid = valid


)