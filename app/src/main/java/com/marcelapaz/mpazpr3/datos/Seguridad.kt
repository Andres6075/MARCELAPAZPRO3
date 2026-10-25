package com.marcelapaz.mpazpr3.datos

import java.security.MessageDigest
import java.security.SecureRandom

/*
  Utilidades de seguridad para las contrasenas.
  Nunca se guarda la contrasena original: se guarda un hash SHA-256 calculado
  con una sal aleatoria distinta para cada usuario.
*/
object Seguridad {

    // Genera una sal aleatoria de 16 bytes en formato hexadecimal
    fun generarSal(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return aHexadecimal(bytes)
    }

    // Calcula el hash SHA-256 de (sal + clave)
    fun calcularHash(clave: String, sal: String): String {
        val resumen = MessageDigest.getInstance("SHA-256")
            .digest((sal + clave).toByteArray(Charsets.UTF_8))
        return aHexadecimal(resumen)
    }

    // Compara el hash guardado con el de la clave ingresada (comparacion de tiempo constante)
    fun verificar(clave: String, sal: String, hashGuardado: String): Boolean {
        val calculado = calcularHash(clave, sal)
        return MessageDigest.isEqual(
            calculado.toByteArray(Charsets.UTF_8),
            hashGuardado.toByteArray(Charsets.UTF_8)
        )
    }

    private fun aHexadecimal(bytes: ByteArray): String =
        bytes.joinToString("") { "%02x".format(it) }
}
