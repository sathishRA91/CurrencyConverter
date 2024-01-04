package com.rasathish.currencyconversion.constant

/**
 * Created by sathish on 04,January,2024
 */
object Validation {

    fun isBanNumberValid(number: String): Boolean {
        return number.isNotEmpty() && number.length > 3
    }

}