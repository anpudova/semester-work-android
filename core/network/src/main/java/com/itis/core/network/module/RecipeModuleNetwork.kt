package com.itis.core.network.module

import com.itis.core.network.api.RecipeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeModuleNetwork {

    private const val APP_ID = "171788f6d7494f90a4c08f99f73fd24f"
    private const val BASE_URL = "https://api.spoonacular.com/"

    @Provides
    fun okHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val modifiedUrl = chain.request().url.newBuilder()
                    .addQueryParameter("apiKey", APP_ID)
                    .build()

                val request = chain.request().newBuilder().url(modifiedUrl).build()
                chain.proceed(request)
            }

        return client.build()
    }

    @Provides
    @Singleton
    fun createRetrofitInstance(okHttpClient: OkHttpClient): RecipeApi {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RecipeApi::class.java)
    }

}