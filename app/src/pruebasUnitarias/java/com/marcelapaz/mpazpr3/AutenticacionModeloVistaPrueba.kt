package com.marcelapaz.mpazpr3

import com.marcelapaz.mpazpr3.datos.RolUsuario
import com.marcelapaz.mpazpr3.ui.modelovista.AutenticacionModeloVista
import com.marcelapaz.mpazpr3.ui.modelovista.EstadoLogin
import com.marcelapaz.mpazpr3.ui.modelovista.EstadoRegistro
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

// Pruebas del ViewModel de autenticacion: validacion de los formularios de login y registro.
// El acceso a Room (iniciar sesion o registrar de verdad) se prueba en la app, no en estas pruebas de JVM.
class AutenticacionModeloVistaPrueba {

    @get:Rule
    val reglaDispatcher = ReglaDispatcherPrincipal()

    @Test
    fun alComenzarNoHayUsuarioConSesion() {
        val vm = AutenticacionModeloVista()
        assertNull(vm.usuario.value)
        assertEquals(EstadoLogin(), vm.login.value)
        assertEquals(EstadoRegistro(), vm.registro.value)
    }

    @Test
    fun cambiarCorreoLoginValidaElFormato() {
        val vm = AutenticacionModeloVista()
        vm.cambiarCorreoLogin("no-es-un-correo")
        assertNotNull(vm.login.value.errores.correo)
        vm.cambiarCorreoLogin("docente@escuelamarcelapaz.cl")
        assertNull(vm.login.value.errores.correo)
    }

    @Test
    fun iniciarSesionConCamposVaciosMuestraErroresYNoQuedaCargando() {
        val vm = AutenticacionModeloVista()
        vm.iniciarSesion()
        val estado = vm.login.value
        assertNotNull(estado.errores.correo)
        assertNotNull(estado.errores.clave)
        assertFalse(estado.cargando)
        assertNull(vm.usuario.value)
    }

    @Test
    fun registrarConCamposVaciosMuestraTodosLosErrores() {
        val vm = AutenticacionModeloVista()
        vm.registrarUsuario()
        val errores = vm.registro.value.errores
        assertNotNull(errores.nombre)
        assertNotNull(errores.correo)
        assertNotNull(errores.clave)
        assertNotNull(errores.confirmacion)
        assertNotNull(errores.rol)
        assertFalse(vm.registro.value.cargando)
        assertNull(vm.usuario.value)
    }

    @Test
    fun cambiarClaveRegistroVuelveAValidarLaConfirmacion() {
        val vm = AutenticacionModeloVista()
        vm.cambiarClaveRegistro("Mpaz2026")
        vm.cambiarConfirmacionRegistro("Mpaz2026")
        assertNull(vm.registro.value.errores.confirmacion)
        // Si la clave cambia despues, la confirmacion deja de coincidir
        vm.cambiarClaveRegistro("Mpaz2027")
        assertNotNull(vm.registro.value.errores.confirmacion)
    }

    @Test
    fun elegirPerfilQuitaElErrorDelCampo() {
        val vm = AutenticacionModeloVista()
        vm.registrarUsuario()
        assertNotNull(vm.registro.value.errores.rol)
        vm.cambiarRolRegistro(RolUsuario.DOCENTE)
        assertEquals(RolUsuario.DOCENTE, vm.registro.value.rol)
        assertNull(vm.registro.value.errores.rol)
    }

    @Test
    fun cerrarSesionLimpiaLosFormularios() {
        val vm = AutenticacionModeloVista()
        vm.cambiarCorreoLogin("alguien@escuelamarcelapaz.cl")
        vm.cambiarNombreRegistro("Ana")
        vm.cerrarSesion()
        assertNull(vm.usuario.value)
        assertEquals(EstadoLogin(), vm.login.value)
        assertEquals(EstadoRegistro(), vm.registro.value)
        assertTrue(vm.login.value.correo.isEmpty())
    }
}
