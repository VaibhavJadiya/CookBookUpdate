package com.printoverit.cookbookupdate.modules

import com.printoverit.cookbookupdate.api.FoodApi
import com.printoverit.cookbookupdate.utils.Constaints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkhttpClient():OkHttpClient{
        return  OkHttpClient.Builder()
            .readTimeout(15,TimeUnit.SECONDS)
            .connectTimeout(15,TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson():GsonConverterFactory {
        return GsonConverterFactory.create()
    }
    @Singleton
    @Provides
    //Top Dependency Provider
    fun provideRetrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory):Retrofit
    {
      return Retrofit.Builder()
          .baseUrl(Constaints.BASE_URL)
          .client(okHttpClient)
          .addConverterFactory(gsonConverterFactory)
          .build()
    }
    @Singleton
    @Provides
    //Top Dependency
    fun provideApiService(retrofit: Retrofit): FoodApi{
        return retrofit.create(FoodApi::class.java)
    }
}