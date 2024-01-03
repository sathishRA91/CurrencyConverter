package com.rasathish.currencyconversion.data.model

import com.google.gson.annotations.SerializedName

data class IbanData(
    @SerializedName("BBAN")
    val bBAN: String,
    @SerializedName("account_number")
    val accountNumber: String,
    @SerializedName("bank_code")
    val bankCode: String,
    val branch: String,
    val checksum: String,
    val country: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("country_iban_format_as_regex")
    val countryIbanFormatAsRegex: String,
    @SerializedName("country_iban_format_as_swift")
    val countryIbanFormatAsSwift: String,
    @SerializedName("national_checksum")
    val nationalChecksum: String,
    @SerializedName("sepa_country")
    val sepaCountry: Boolean
)