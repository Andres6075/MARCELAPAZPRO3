package com.marcelapaz.mpazpr3.datos.red

import com.marcelapaz.mpazpr3.datos.modelos.CitaPedagogica
import retrofit2.http.GET

/*
  Interfaz Retrofit para consumir una API REST Externa pública.
  Cumple con la Guia 14 y el Indicador IE 3.1.4 (Consumo de API Externa).
*/
interface ServicioApiExterna {

    @GET("quotes/random")
    suspend fun obtenerCitaPedagogica(): CitaPedagogica
}
