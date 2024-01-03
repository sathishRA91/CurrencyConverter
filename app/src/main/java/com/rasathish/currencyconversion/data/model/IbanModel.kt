package com.rasathish.currencyconversion.data.model

import com.google.gson.annotations.SerializedName

data class IbanModel(
    @SerializedName("bank_data")
    val bankData: BankData,
    @SerializedName("country_iban_example")
    val countryIbanExample: String,
    val iban: String,
    @SerializedName("iban_data")
    val ibanData: IbanData,
    val message: String,
    val valid: Boolean
)

//fun IbanModel.toDomainModel() =