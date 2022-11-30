package com.utsman.tokocot.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.Duration
import java.util.concurrent.TimeUnit

interface WebServices {

    @GET("/v1/customer/product")
    suspend fun getProduct(
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int
    ): Response<ProductListResponse>

    companion object {
        private const val BASE_URL = "https://aurel-store.herokuapp.com/"

        private fun builder(): WebServices {
            val timeoutDuration = 30L
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .callTimeout(timeoutDuration, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(WebServices::class.java)
        }

        fun modules(): Module {
            return module {
                single { builder() }
            }
        }
    }
}