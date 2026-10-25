package com.marcelapaz.mpazpr3

import com.marcelapaz.mpazpr3.ui.modelovista.FormularioModuloModeloVista
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

// Pruebas del ViewModel del formulario "Nuevo modulo": estado, validacion por campo y envio
class FormularioModuloModeloVistaPrueba {

    @get:Rule
    val reglaDispatcher = ReglaDispatcherPrincipal()

    // Llena todos los campos con datos validos
    private fun llenarFormularioValido(vm: FormularioModuloModeloVista) {
        vm.cambiarTitulo("Lectura de cuentos")
        vm.cambiarAsignatura("Lenguaje y Comunicación")
        vm.cambiarUnidad("Unidad 1")
        vm.cambiarCodigoOa("OA-LENG-03-03")
        vm.cambiarHoras("4")
        vm.cambiarIntentos("3")
    }

    @Test
    fun elEstadoInicialNoTieneErroresNiGuardado() {
        val estado = FormularioModuloModeloVista().estado.value
        assertFalse(estado.errores.hayErrores)
        assertFalse(estado.guardadoExitoso)
        assertEquals("3", estado.intentos)
    }

    @Test
    fun cambiarTituloValidaElCampoAlEscribir() {
        val vm = FormularioModuloModeloVista()
        vm.cambiarTitulo("Ab")
        assertNotNull(vm.estado.value.errores.titulo)
        vm.cambiarTitulo("Lectura inicial")
        assertNull(vm.estado.value.errores.titulo)
    }

    @Test
    fun cambiarCodigoOaLoPasaAMayusculas() {
        val vm = FormularioModuloModeloVista()
        vm.cambiarCodigoOa("oa-leng-03-03")
        assertEquals("OA-LENG-03-03", vm.estado.value.codigoOa)
        assertNull(vm.estado.value.errores.codigoOa)
    }

    @Test
    fun enviarConCamposVaciosDevuelveNullYMuestraLosErrores() {
        val vm = FormularioModuloModeloVista()
        val resultado = vm.enviar()
        val errores = vm.estado.value.errores
        assertNull(resultado)
        assertTrue(errores.hayErrores)
        assertNotNull(errores.titulo)
        assertNotNull(errores.asignatura)
        assertNotNull(errores.unidad)
        assertNotNull(errores.codigoOa)
        assertNotNull(errores.horas)
        // Los intentos parten en "3", que es un valor valido
        assertNull(errores.intentos)
        assertFalse(vm.estado.value.guardadoExitoso)
    }

    @Test
    fun enviarConDatosValidosDevuelveUnModuloPendienteDeAutorizacion() {
        val vm = FormularioModuloModeloVista()
        llenarFormularioValido(vm)
        val modulo = vm.enviar()
        assertNotNull(modulo)
        assertEquals(0, modulo!!.id)
        assertEquals("Lectura de cuentos", modulo.titulo)
        assertEquals("OA-LENG-03-03", modulo.codigoOficial)
        assertEquals(4, modulo.horasEstimadas)
        assertEquals(3, modulo.intentosPermitidos)
        // Un modulo nuevo queda pendiente hasta que la UTP lo autorice
        assertFalse(modulo.aprobadoPorUtp)
    }

    @Test
    fun trasGuardarElFormularioSeLimpiaYMarcaGuardadoExitoso() {
        val vm = FormularioModuloModeloVista()
        llenarFormularioValido(vm)
        vm.enviar()
        assertTrue(vm.estado.value.guardadoExitoso)
        assertEquals("", vm.estado.value.titulo)
        // Al volver a escribir, el mensaje de exito desaparece
        vm.cambiarTitulo("Otro modulo")
        assertFalse(vm.estado.value.guardadoExitoso)
    }
}
