package com.rasathish.currencyconversion.constant

/**
 * Created by sathish on 03,January,2024
 */
sealed class ResultResource<T>(val data:T?=null, val message:String?=null)
{
    class Success<T>(data: T?=null):ResultResource<T>(data)

    class ErrorMessage<T>(message: String?,data: T?=null):ResultResource<T>(data,message)

    class Loading<T>():ResultResource<T>()
}
