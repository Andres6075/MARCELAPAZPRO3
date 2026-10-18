package com.marcelapaz.mpazpr3

import com.marcelapaz.mpazpr3.ui.modelovista.ValidadorModulo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

// Pruebas unitarias de la validacion del formulario "Nuevo modulo"
class ValidadorModuloPrueba {

    @Test
    fun tituloVacioEsInvalido() {
        assertEquals("El titulo es obligatorio", ValidadorModulo.validarTitulo(""))
    }

    @Test
    fun tituloCorrectoEsValido() {
        assertNull(ValidadorModulo.validarTitulo("Fabulas y cuentos"))
    }

    @Test
    fun codigoOaConFormatoCorrectoEsValido() {
        assertNull(ValidadorModulo.validarCodigoOa("OA-LENG-03-03"))
        assertNull(ValidadorModulo.validarCodigoOa("OA-MAT-03-04"))
    }

    @Test
    fun codigoOaConFormatoIncorrectoEsInvalido() {
        assertNotNull(ValidadorModulo.validarCodigoOa("LENG-3"))
    }

    @Test
    fun horasFueraDeRangoSonInvalidas() {
        assertNotNull(ValidadorModulo.validarHoras("0"))
        assertNotNull(ValidadorModulo.validarHoras("21"))
        assertNotNull(ValidadorModulo.validarHoras("abc"))
        assertNull(ValidadorModulo.validarHoras("6"))
    }

    @Test
    fun intentosFueraDeRangoSonInvalidos() {
        assertNotNull(ValidadorModulo.validarIntentos("6"))
        assertNull(ValidadorModulo.validarIntentos("3"))
    }
}
