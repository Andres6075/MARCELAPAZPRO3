package com.marcelapaz.mpazpr3.datos.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.marcelapaz.mpazpr3.datos.RolUsuario
import com.marcelapaz.mpazpr3.datos.Usuario

/*
  Entidad de Room para la tabla de usuarios.
  El correo es unico. Se guarda la sal y el hash de la contrasena, nunca la contrasena.
*/
@Entity(tableName = "usuarios", indices = [Index(value = ["correo"], unique = true)])
data class UsuarioEntidad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val sal: String,
    val claveHash: String,
    val rol: String
)

// Conversion de la entidad de Room al modelo que usa la interfaz
fun UsuarioEntidad.aModeloDominio(): Usuario = Usuario(
    id = id,
    nombre = nombre,
    correo = correo,
    rol = RolUsuario.valueOf(rol)
)
