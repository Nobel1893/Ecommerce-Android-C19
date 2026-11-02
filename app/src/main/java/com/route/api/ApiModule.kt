package com.route.api

import android.util.Log
import com.google.gson.Gson
import com.route.firstapp.BuildConfig
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
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor{
        val interceptor =  HttpLoggingInterceptor { message -> Log.e("RetroFit", message) }
        // bodies now carry passwords and tokens (v1/auth/*), so they're only logged in debug builds
        interceptor.setLevel(
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttp(
         loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient{
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        return okhttp



    }

    // shared by Retrofit's converter and the error-body parsing in safeApiCallWithoutEnvelope
    @Provides
    @Singleton
    fun provideGson(): Gson{
        return Gson()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient, gson: Gson): Retrofit{
        val retrofit = Retrofit.Builder()
            .client(okHttp)
            .baseUrl("https://ecommerce.routemisr.com/api/")
            .addConverterFactory(
                GsonConverterFactory.create(gson)
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
