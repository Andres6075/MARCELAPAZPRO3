package com.marcelapaz.mpazpr3.datos.repositorios

import com.marcelapaz.mpazpr3.datos.modelos.CitaPedagogica
import com.marcelapaz.mpazpr3.datos.red.ClienteRetrofit
import com.marcelapaz.mpazpr3.datos.red.ServicioApiExterna
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*
  Repositorio encargado de abstraer el origen de los datos de la API Externa (Guia 14).
  Incluye respaldo local didactico para asegurar la continuidad de la app si no hay conexion.
*/
class RepositorioApiExterna(
    private val servicio: ServicioApiExterna = ClienteRetrofit.servicioApiExterna
) {
    private val citasPedagogicasLocales = listOf(
        CitaPedagogica(1, "La educación no cambia el mundo, cambia a las personas que van a cambiar el mundo.", "Paulo Freire"),
        CitaPedagogica(2, "Enseñar no es transferir conocimiento, sino crear las posibilidades para su producción.", "Paulo Freire"),
        CitaPedagogica(3, "La primera tarea de la educación es agitar la vida, pero dejarla libre para que se desarrolle.", "Maria Montessori"),
        CitaPedagogica(4, "El objetivo de la educación es crear personas capaces de hacer cosas nuevas.", "Jean Piaget")
    )

    suspend fun obtenerCitaDelDia(): CitaPedagogica = withContext(Dispatchers.IO) {
        try {
            val respuesta = servicio.obtenerCitaPedagogica()
            if (respuesta.cita.isNotBlank()) respuesta else citasPedagogicasLocales.random()
        } catch (e: Exception) {
            citasPedagogicasLocales.random()
        }
    }
}
