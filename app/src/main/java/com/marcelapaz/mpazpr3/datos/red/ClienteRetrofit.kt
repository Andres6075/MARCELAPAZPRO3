package com.marcelapaz.mpazpr3.datos.red

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
  Instancia Singleton de clientes Retrofit para la API Externa y Microservicios.
  Cumple con el patron de construccion de Retrofit expuesto en la Guia 14.
*/
object ClienteRetrofit {

    // URL base de la API Externa pública (DummyJSON Quotes)
    private const val URL_BASE_API_EXTERNA = "https://dummyjson.com/"

    // URL base del Microservicio Spring Boot local (10.0.2.2 es localhost para el emulador Android)
    private const val URL_BASE_MICROSERVICIO = "http://10.0.2.2:8080/api/"

    private val clienteOkHttp: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    val servicioApiExterna: ServicioApiExterna by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE_API_EXTERNA)
            .client(clienteOkHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServicioApiExterna::class.java)
    }

    val servicioMicroservicio: ServicioMicroservicio by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE_MICROSERVICIO)
            .client(clienteOkHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServicioMicroservicio::class.java)
    }
}
