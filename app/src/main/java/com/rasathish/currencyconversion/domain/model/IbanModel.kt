package com.rasathish.currencyconversion.domain.model

data class IbanModel(
    val bankData: BankData,
    val iban: String,
    val ibanData: IbanData,
    val message: String,
    val valid: Boolean
)
