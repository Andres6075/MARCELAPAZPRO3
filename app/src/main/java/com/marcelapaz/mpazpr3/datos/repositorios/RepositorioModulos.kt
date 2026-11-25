package com.marcelapaz.mpazpr3.datos.repositorios

import android.content.Context
import com.marcelapaz.mpazpr3.datos.db.BaseDatosApp
import com.marcelapaz.mpazpr3.datos.db.ModuloDao
import com.marcelapaz.mpazpr3.datos.db.aEntidad
import com.marcelapaz.mpazpr3.datos.db.aModeloDominio
import com.marcelapaz.mpazpr3.datos.modelos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.datos.red.ClienteRetrofit
import com.marcelapaz.mpazpr3.datos.red.ServicioMicroservicio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/*
  Repositorio de modulos: puerta de acceso a la persistencia hibrida (Room local + Microservicio Spring Boot).
  El ViewModel trabaja solo con ModuloAprendizaje (modelo de dominio) y no conoce
  el DAO, la red ni las entidades de Room, lo que separa la logica de la capa de datos.
*/
class RepositorioModulos(
    private val moduloDao: ModuloDao,
    private val servicioMicroservicio: ServicioMicroservicio = ClienteRetrofit.servicioMicroservicio
) {

    // Flujo reactivo con todos los modulos guardados, ya convertidos al modelo de dominio
    val modulos: Flow<List<ModuloAprendizaje>> =
        moduloDao.obtenerTodosLosModulos().map { entidades -> entidades.map { it.aModeloDominio() } }

    // Carga los modulos de prueba la primera vez que se abre la app (tabla vacia)
    suspend fun sembrarDatosIniciales() {
        if (moduloDao.contarModulos() == 0) {
            val iniciales = RepositorioDatos.obtenerModulos().map { it.aEntidad() }
            moduloDao.insertarModulos(iniciales)
        }
    }

    // Sincroniza la lista de modulos trayendolos desde el microservicio Spring Boot a Room
    suspend fun sincronizarConMicroservicio(): Result<List<ModuloAprendizaje>> {
        return try {
            val remotos = servicioMicroservicio.obtenerModulos()
            if (remotos.isNotEmpty()) {
                moduloDao.insertarModulos(remotos.map { it.aEntidad() })
            }
            Result.success(remotos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Inserta un modulo nuevo enviandolo al microservicio (devuelve el objeto con el id asignado por el servidor) y guardandolo en Room
    suspend fun insertar(modulo: ModuloAprendizaje): ModuloAprendizaje {
        return try {
            val creadoRemoto = servicioMicroservicio.crearModulo(modulo)
            moduloDao.insertarModulo(creadoRemoto.aEntidad())
            creadoRemoto
        } catch (e: Exception) {
            moduloDao.insertarModulo(modulo.aEntidad())
            modulo
        }
    }

    // Actualiza un modulo existente tanto en el microservicio como en Room local
    suspend fun actualizar(modulo: ModuloAprendizaje) {
        try {
            servicioMicroservicio.actualizarModulo(modulo.id, modulo)
        } catch (_: Exception) {
        }
        moduloDao.actualizarModulo(modulo.aEntidad())
    }

    // Elimina un modulo tanto del microservicio como de Room local
    suspend fun eliminar(idModulo: Int) {
        try {
            servicioMicroservicio.eliminarModulo(idModulo)
        } catch (_: Exception) {
        }
        moduloDao.eliminarModuloPorId(idModulo)
    }

    companion object {
        // Crea el repositorio con la base de datos Room y la instancia de Retrofit del microservicio
        fun crear(contexto: Context): RepositorioModulos =
            RepositorioModulos(
                BaseDatosApp.obtenerBaseDatos(contexto).moduloDao(),
                ClienteRetrofit.servicioMicroservicio
            )
    }
}
