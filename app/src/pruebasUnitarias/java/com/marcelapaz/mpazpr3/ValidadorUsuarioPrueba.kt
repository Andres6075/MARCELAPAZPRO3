package com.marcelapaz.mpazpr3

import com.marcelapaz.mpazpr3.datos.RolUsuario
import com.marcelapaz.mpazpr3.datos.Seguridad
import com.marcelapaz.mpazpr3.ui.modelovista.ValidadorUsuario
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// Pruebas unitarias de las validaciones de login y registro, y del hash de contrasenas
class ValidadorUsuarioPrueba {

    @Test
    fun nombreVacioOMuyCortoEsInvalido() {
        assertNotNull(ValidadorUsuario.validarNombre(""))
        assertNotNull(ValidadorUsuario.validarNombre("Al"))
    }

    @Test
    fun nombreConNumerosEsInvalido() {
        assertEquals("Solo puede contener letras y espacios", ValidadorUsuario.validarNombre("Ana 123"))
    }

    @Test
    fun nombreValidoNoTieneError() {
        assertNull(ValidadorUsuario.validarNombre("María José"))
    }

    @Test
    fun correoSinFormatoEsInvalido() {
        assertNotNull(ValidadorUsuario.validarCorreo(""))
        assertNotNull(ValidadorUsuario.validarCorreo("correo-sin-arroba"))
        assertNotNull(ValidadorUsuario.validarCorreo("a@b"))
    }

    @Test
    fun correoValidoNoTieneError() {
        assertNull(ValidadorUsuario.validarCorreo("docente@escuelamarcelapaz.cl"))
    }

    @Test
    fun claveDebeTenerOchoCaracteresLetraYNumero() {
        assertNotNull(ValidadorUsuario.validarClave("abc12"))
        assertNotNull(ValidadorUsuario.validarClave("12345678"))
        assertNotNull(ValidadorUsuario.validarClave("abcdefgh"))
        assertNull(ValidadorUsuario.validarClave("Mpaz2026"))
    }

    @Test
    fun claveDeLoginSoloExigeQueNoEsteVacia() {
        assertNotNull(ValidadorUsuario.validarClaveLogin(""))
        assertNull(ValidadorUsuario.validarClaveLogin("x"))
    }

    @Test
    fun confirmacionDebeCoincidirConLaClave() {
        assertEquals("Las contraseñas no coinciden", ValidadorUsuario.validarConfirmacion("Mpaz2026", "Mpaz2027"))
        assertNull(ValidadorUsuario.validarConfirmacion("Mpaz2026", "Mpaz2026"))
    }

    @Test
    fun rolEsObligatorio() {
        assertNotNull(ValidadorUsuario.validarRol(null))
        assertNull(ValidadorUsuario.validarRol(RolUsuario.DOCENTE))
    }

    @Test
    fun hashEsDeterministaYDependeDeLaSal() {
        val hash1 = Seguridad.calcularHash("Mpaz2026", "sal1")
        assertEquals(hash1, Seguridad.calcularHash("Mpaz2026", "sal1"))
        assertNotEquals(hash1, Seguridad.calcularHash("Mpaz2026", "sal2"))
    }

    @Test
    fun verificarAceptaClaveCorrectaYRechazaIncorrecta() {
        val sal = Seguridad.generarSal()
        val hash = Seguridad.calcularHash("Mpaz2026", sal)
        assertTrue(Seguridad.verificar("Mpaz2026", sal, hash))
        assertFalse(Seguridad.verificar("otraClave1", sal, hash))
    }
}
