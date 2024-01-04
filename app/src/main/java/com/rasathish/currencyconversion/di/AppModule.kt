package com.rasathish.currencyconversion.di

import com.rasathish.currencyconversion.BuildConfig
import com.rasathish.currencyconversion.constant.ApiConfig
import com.rasathish.currencyconversion.data.network.ApiInterface
import com.rasathish.currencyconversion.data.remotedatasource.RemoteDataSource
import com.rasathish.currencyconversion.data.remotedatasource.RemoteDataSourceImpl
import com.rasathish.currencyconversion.data.repository.CurrencyRepositoryImpl
import com.rasathish.currencyconversion.data.repository.IbanRepositoryImpl
import com.rasathish.currencyconversion.domain.repository.CurrencyRepository
import com.rasathish.currencyconversion.domain.repository.IbanRepository
import com.rasathish.currencyconversion.domain.usecase.CurrencyUseCase
import com.rasathish.currencyconversion.domain.usecase.IbanUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Created by sathish on 04,January,2024
 */

@Module
@InstallIn(SingletonComponent::class)
 object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request: Request =
                    chain.request().newBuilder().addHeader("apikey", BuildConfig.CURRENCY_API).build()
                chain.proceed(request)
            })
            .connectTimeout(100L, TimeUnit.SECONDS)
            .readTimeout(100L, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiConfig.DOMAIN)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }


    @Provides
    @Singleton
    fun provideDataSource(apiInterface: ApiInterface): RemoteDataSource = RemoteDataSourceImpl(apiInterface)

    @Provides
    @Singleton
    fun provideCurrencyRepository(remoteDataSource: RemoteDataSource ): CurrencyRepository =CurrencyRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideIbanRepository(remoteDataSource: RemoteDataSource): IbanRepository = IbanRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideCurrencyUseCase(currencyRepository: CurrencyRepository): CurrencyUseCase
    {
        return CurrencyUseCase(currencyRepository)
    }

    @Provides
    @Singleton
    fun provideIbanUseCase(ibanRepository: IbanRepository): IbanUseCase
    {
        return IbanUseCase(ibanRepository)
    }
 }