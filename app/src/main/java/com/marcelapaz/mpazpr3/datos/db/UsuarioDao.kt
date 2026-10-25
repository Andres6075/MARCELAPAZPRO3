package com.marcelapaz.mpazpr3.datos.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/*
  DAO de usuarios: consultas para el registro y el inicio de sesion.
*/
@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): UsuarioEntidad?

    // Devuelve el id generado por Room
    @Insert
    suspend fun insertar(usuario: UsuarioEntidad): Long

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}
