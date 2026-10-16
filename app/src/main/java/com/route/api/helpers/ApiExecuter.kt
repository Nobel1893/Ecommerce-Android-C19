package com.route.api.helpers

import com.route.api.data.BaseResponse
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.flow


fun<T> safeApiCall(api : suspend ()-> BaseResponse<T>) = flow<Resource<T>> {
    try {
        emit(Resource.Loading())
        val response = api.invoke()
        emit(Resource.Success(data = response) as Resource<T>)
    }catch (t: Throwable){
        emit(Resource.Error(t))
    }

}
fun<T,R> safeApiCallV2(api : suspend ()-> BaseResponse<T>,
                       converter: (T?) -> R?) = flow<Resource<R?>> {
    try {
        emit(Resource.Loading())
        val response = api.invoke()
        val convertedData = converter.invoke(response.data)
        emit(Resource.Success(data = convertedData))
    }catch (t: Throwable){
        emit(Resource.Error(t))
    }

}
