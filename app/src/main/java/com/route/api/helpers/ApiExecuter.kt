package com.route.api.helpers

import com.google.gson.Gson
import com.route.api.data.BaseResponse
import com.route.api.data.ErrorResponseDto
import com.route.domain.data.Resource
import com.route.domain.data.ServerException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException


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

/**
 * Like [safeApiCallV2] for endpoints whose body isn't wrapped in [BaseResponse] (auth).
 * An HTTP error becomes a [ServerException] carrying the server's message from the error body,
 * so the UI can show "Incorrect email or password" instead of "HTTP 401".
 */
fun<T,R> safeApiCallWithoutEnvelope(gson: Gson,
                                    api : suspend ()-> T,
                                    converter: (T) -> R?): Flow<Resource<R?>> = flow {
    emit(Resource.Loading())
    val resource: Resource<R?> = try {
        Resource.Success(data = converter.invoke(api.invoke()))
    }catch (e: CancellationException){
        throw e
    }catch (e: HttpException){
        Resource.Error(e.toServerException(gson))
    }catch (t: Throwable){
        Resource.Error(t)
    }
    emit(resource)
}

// Retrofit has already buffered the error body, so reading it here doesn't touch the network
private fun HttpException.toServerException(gson: Gson): ServerException {
    val errorResponse = try {
        response()?.errorBody()?.string()
            ?.let { body -> gson.fromJson(body, ErrorResponseDto::class.java) }
    }catch (e: Exception){
        null
    }
    return (errorResponse ?: ErrorResponseDto()).toServerException(cause = this)
}
