package com.marcelapaz.mpazpr3.datos

import android.content.Context
import com.marcelapaz.mpazpr3.datos.db.BaseDatosApp
import com.marcelapaz.mpazpr3.datos.db.ModuloDao
import com.marcelapaz.mpazpr3.datos.db.aEntidad
import com.marcelapaz.mpazpr3.datos.db.aModeloDominio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/*
  Repositorio de modulos: unica puerta de acceso a la persistencia local (Room).
  El ViewModel trabaja solo con ModuloAprendizaje (modelo de dominio) y no conoce
  el DAO ni las entidades de Room, lo que separa la logica de la capa de datos.
*/
class RepositorioModulos(private val moduloDao: ModuloDao) {

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

    // Inserta un modulo nuevo (Room asigna el id cuando es 0)
    suspend fun insertar(modulo: ModuloAprendizaje) {
        moduloDao.insertarModulo(modulo.aEntidad())
    }

    // Actualiza un modulo existente (progreso, autorizacion, intentos)
    suspend fun actualizar(modulo: ModuloAprendizaje) {
        moduloDao.actualizarModulo(modulo.aEntidad())
    }

    companion object {
        // Crea el repositorio con la base de datos Room de la aplicacion
        fun crear(contexto: Context): RepositorioModulos =
            RepositorioModulos(BaseDatosApp.obtenerBaseDatos(contexto).moduloDao())
    }
}
