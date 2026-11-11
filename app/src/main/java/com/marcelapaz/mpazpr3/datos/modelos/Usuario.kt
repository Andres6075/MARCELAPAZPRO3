package com.marcelapaz.mpazpr3.datos.modelos

// Usuario que inicio sesion. No incluye la contrasena: solo los datos que necesita la interfaz.
data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val rol: RolUsuario
)
