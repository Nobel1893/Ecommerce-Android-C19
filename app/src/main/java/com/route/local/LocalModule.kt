package com.route.local

import android.content.Context
import android.content.SharedPreferences
import com.route.data.dataSource.AuthLocalDataSource
import com.route.local.dataSource.AuthLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

// On-device storage. Singleton because every screen must see the same stored session.
@Module
@InstallIn(SingletonComponent::class)
abstract class LocalModule {

    @Binds
    @Singleton
    abstract fun provideAuthLocalDataSource(
        dataSource: AuthLocalDataSourceImpl
    ): AuthLocalDataSource

    companion object {
        // Excluded from cloud backup in res/xml/backup_rules.xml and data_extraction_rules.xml
        const val AUTH_PREFERENCES_FILE = "auth_session"

        @Provides
        @Singleton
        @AuthPreferences
        fun provideAuthPreferences(
            @ApplicationContext context: Context
        ): SharedPreferences {
            return context.getSharedPreferences(AUTH_PREFERENCES_FILE, Context.MODE_PRIVATE)
        }
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthPreferences
