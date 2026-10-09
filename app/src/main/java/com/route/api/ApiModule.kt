package com.route.api

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    @LoggingInterceptor
    fun provideOkHttpLoggingInterceptor(): Interceptor{
        return HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.d("RetroFit",message)
                }
            }
        )
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        @LoggingInterceptor loggingInterceptor: Interceptor
    ): OkHttpClient{
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        return okhttp



    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit{
        val retrofit = Retrofit.Builder()
            .client(okHttp)
            .baseUrl("https://ecommerce.routemisr.com/api/")
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build()
        return retrofit
    }

    @Provides
    @Singleton
    fun provideServices(
        retrofit: Retrofit
    ): ApiServices{
        return retrofit.create(ApiServices::class.java)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HeadersInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptor
