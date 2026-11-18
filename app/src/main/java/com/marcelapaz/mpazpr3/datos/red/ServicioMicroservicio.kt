package com.marcelapaz.mpazpr3.datos.red

import com.marcelapaz.mpazpr3.datos.modelos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.datos.modelos.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/*
  Interfaz Retrofit para conectar la app móvil con los Microservicios en Spring Boot.
  Cumple con los Indicadores IE 3.1.2 e IE 3.1.3 (Backend propio y operaciones CRUD).
*/
interface ServicioMicroservicio {

    @GET("modulos")
    suspend fun obtenerModulos(): List<ModuloAprendizaje>

    @POST("modulos")
    suspend fun crearModulo(@Body modulo: ModuloAprendizaje): ModuloAprendizaje

    @PUT("modulos/{id}")
    suspend fun actualizarModulo(@Path("id") id: Int, @Body modulo: ModuloAprendizaje): ModuloAprendizaje

    @DELETE("modulos/{id}")
    suspend fun eliminarModulo(@Path("id") id: Int): Response<Unit>

    @GET("usuarios")
    suspend fun obtenerUsuarios(): List<Usuario>
}
