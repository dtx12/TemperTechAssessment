package com.dtx12.tempertechassessment.core.di

import com.dtx12.tempertechassessment.BuildConfig
import com.dtx12.tempertechassessment.core.OffsetDateTimeTypeAdapter
import com.dtx12.tempertechassessment.core.data.endpoints.TemperApi
import com.dtx12.tempertechassessment.core.data.repositories.ShiftsRepositoryImpl
import com.dtx12.tempertechassessment.core.domain.repositories.ShiftsRepository
import com.dtx12.tempertechassessment.core.platform.*
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.OffsetDateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideApi(): TemperApi {
        return Retrofit.Builder()
            .baseUrl("https://temper.works/api/v3/")
            .client(createClient())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeHierarchyAdapter(
                            OffsetDateTime::class.java,
                            OffsetDateTimeTypeAdapter()
                        ).create()
                )
            )
            .build()
            .create(TemperApi::class.java)
    }

    private fun createClient(): OkHttpClient {
        val timeoutInSeconds = 30L
        val okHttpClientBuilder = OkHttpClient
            .Builder()
            .readTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            .connectTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutInSeconds, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideProductsRepository(impl: ShiftsRepositoryImpl): ShiftsRepository = impl

    @Provides
    @Singleton
    fun provideLocationPermissionsChecker(
        locationPermissionsCheckerImpl: LocationPermissionsCheckerImpl
    ): LocationPermissionsChecker = locationPermissionsCheckerImpl

    @Provides
    @Singleton
    fun provideNetworkHandler(
        networkHandlerImpl: NetworkHandlerImpl
    ): NetworkHandler = networkHandlerImpl

    @Provides
    @Singleton
    fun provideLocationRequester(
        locationRequesterImpl: LocationRequesterImpl
    ): LocationRequester = locationRequesterImpl
}