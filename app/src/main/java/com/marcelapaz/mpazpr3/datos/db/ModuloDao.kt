package com.marcelapaz.mpazpr3.datos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/*
  Objeto de Acceso a Datos (DAO) de Room.
  Proporciona los metodos para consultar, insertar y actualizar los modulos en la base de datos.
*/
@Dao
interface ModuloDao {

    @Query("SELECT * FROM modulos_aprendizaje ORDER BY id ASC")
    fun obtenerTodosLosModulos(): Flow<List<ModuloEntidad>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarModulos(modulos: List<ModuloEntidad>)

    @Update
    suspend fun actualizarModulo(modulo: ModuloEntidad)

    @Query("SELECT COUNT(*) FROM modulos_aprendizaje")
    suspend fun contarModulos(): Int
}

/*
  Glosario de Room - DAO:
  - Dao (Data Access Object): Interfaz que define las consultas y operaciones con la base de datos.
  - Query: Consulta SQL directa ejecutada sobre la tabla de Room.
  - Flow: Flujo de datos asincrono que emite las actualizaciones de la base de datos en tiempo real.
*/
