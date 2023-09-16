package com.example.foodie.util

//sealed class can have multiple instances with different properties and behaviors,
//here it is parameterized with a generic type T, which allows it to wrap different types of data
sealed class NetworkResult<T>(
    //data returned by the network request
    val data:T? = null,
    //description of the network result
    val message:String? = null
) {

    //parameter data, which is of type T represents the successfully retrieved data
    //need to pass data to it from networkresult<t>
    class Success<T>(data:T?): NetworkResult<T>(data)
    class Error<T>(message:String?,data: T?=null): NetworkResult<T>(data,message)
    class Loading<T>:NetworkResult<T>()

}