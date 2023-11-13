package com.example.nycschools.di

import com.example.nycschools.repository.SchoolRepository
import com.example.nycschools.utils.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    // Providing a Retrofit instance for network operations
    @Provides
    fun providesRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Utils.NetworkUtils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // Providing a SchoolRepository instance with Retrofit dependency
    @Provides
    fun providesSchoolRepository(retrofit: Retrofit): SchoolRepository {
        return SchoolRepository(retrofit)
    }
}