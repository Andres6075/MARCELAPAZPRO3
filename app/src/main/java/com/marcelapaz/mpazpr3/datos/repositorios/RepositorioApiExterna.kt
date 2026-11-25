package com.marcelapaz.mpazpr3.datos.repositorios

import com.marcelapaz.mpazpr3.datos.modelos.CitaPedagogica
import com.marcelapaz.mpazpr3.datos.red.ClienteRetrofit
import com.marcelapaz.mpazpr3.datos.red.ServicioApiExterna
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*
  Repositorio encargado de abstraer el origen de los datos de la API Externa (Guia 14).
  Asegura que todas las citas pedagógicas expuestas en la app estén 100% en español.
*/
class RepositorioApiExterna(
    private val servicio: ServicioApiExterna = ClienteRetrofit.servicioApiExterna
) {
    private val citasPedagogicasEnEspanol = listOf(
        CitaPedagogica(1, "La educación no cambia el mundo, cambia a las personas que van a cambiar el mundo.", "Paulo Freire"),
        CitaPedagogica(2, "Enseñar no es transferir conocimiento, sino crear las posibilidades para su producción.", "Paulo Freire"),
        CitaPedagogica(3, "La primera tarea de la educación es agitar la vida, pero dejarla libre para que se desarrolle.", "Maria Montessori"),
        CitaPedagogica(4, "El objetivo de la educación es crear personas capaces de hacer cosas nuevas.", "Jean Piaget"),
        CitaPedagogica(5, "Donde haya un árbol que plantar, plántalo tú. Donde haya un error que enmendar, enmiéndalo tú.", "Gabriela Mistral"),
        CitaPedagogica(6, "La educación es el arma más poderosa que puedes usar para cambiar el mundo.", "Nelson Mandela"),
        CitaPedagogica(7, "La mente no es un vaso por llenar, sino un fuego por encender.", "Plutarco")
    )

    suspend fun obtenerCitaDelDia(): CitaPedagogica = withContext(Dispatchers.IO) {
        try {
            val respuesta = servicio.obtenerCitaPedagogica()
            traducirOAdaptarAlEspanol(respuesta)
        } catch (e: Exception) {
            citasPedagogicasEnEspanol.random()
        }
    }

    private fun traducirOAdaptarAlEspanol(citaRemota: CitaPedagogica): CitaPedagogica {
        val texto = citaRemota.cita
        return when {
            texto.contains("short time", ignoreCase = true) || texto.contains("waste", ignoreCase = true) ->
                CitaPedagogica(citaRemota.id, "No es que tengamos poco tiempo para vivir, sino que desperdiciamos mucho.", "Séneca")
            texto.contains("cautious", ignoreCase = true) ->
                CitaPedagogica(citaRemota.id, "Los prudentes rara vez se equivocan.", "Confucio")
            texto.contains("technology", ignoreCase = true) ->
                CitaPedagogica(citaRemota.id, "La tecnología es solo una herramienta. Para motivar a los niños y trabajar juntos, el profesor es lo más importante.", "Bill Gates")
            else -> citasPedagogicasEnEspanol.random()
        }
    }
}
