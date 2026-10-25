package com.marcelapaz.mpazpr3.ui.modelovista

import com.marcelapaz.mpazpr3.datos.RolUsuario

/*
  Validaciones de los formularios de inicio de sesion y registro.
  Es logica pura (sin Compose ni Android): cada funcion devuelve el mensaje de error o null si el campo es correcto.
*/
object ValidadorUsuario {

    private val formatoCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validarNombre(nombre: String): String? = when {
        nombre.isBlank() -> "El nombre es obligatorio"
        nombre.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        nombre.trim().length > 50 -> "Máximo 50 caracteres"
        nombre.trim().any { !it.isLetter() && it != ' ' } -> "Solo puede contener letras y espacios"
        else -> null
    }

    fun validarCorreo(correo: String): String? = when {
        correo.isBlank() -> "El correo es obligatorio"
        !formatoCorreo.matches(correo.trim()) -> "Formato inválido. Ejemplo: nombre@escuela.cl"
        else -> null
    }

    // Reglas de la contrasena al crear una cuenta
    fun validarClave(clave: String): String? = when {
        clave.isBlank() -> "La contraseña es obligatoria"
        clave.length < 8 -> "Debe tener al menos 8 caracteres"
        clave.none { it.isLetter() } -> "Debe incluir al menos una letra"
        clave.none { it.isDigit() } -> "Debe incluir al menos un número"
        else -> null
    }

    // Al iniciar sesion solo se exige que no este vacia (no se revelan las reglas de creacion)
    fun validarClaveLogin(clave: String): String? =
        if (clave.isBlank()) "Ingresa tu contraseña" else null

    fun validarConfirmacion(clave: String, confirmacion: String): String? = when {
        confirmacion.isBlank() -> "Confirma tu contraseña"
        clave != confirmacion -> "Las contraseñas no coinciden"
        else -> null
    }

    fun validarRol(rol: RolUsuario?): String? =
        if (rol == null) "Selecciona un perfil" else null
}
