package com.marcelapaz.mpazpr3

import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.datos.RolUsuario
import com.marcelapaz.mpazpr3.ui.modelovista.ModeloVistaMpaz
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

// Pruebas del ViewModel principal: perfil por rol, filtro de modulos y avance.
// No se llama a inicializarRoom, por lo que no se usa la base de datos (solo los datos de prueba en memoria).
class ModeloVistaMpazPrueba {

    @get:Rule
    val reglaDispatcher = ReglaDispatcherPrincipal()

    private fun ModeloVistaMpaz.modulo(id: Int): ModuloAprendizaje = modulos.value.first { it.id == id }

    @Test
    fun establecerUsuarioActualizaElPerfil() {
        val vm = ModeloVistaMpaz()
        vm.establecerUsuario("Ana Docente", "ana@escuelamarcelapaz.cl", RolUsuario.DOCENTE)
        val perfil = vm.perfil.value
        assertEquals("Ana Docente", perfil.nombre)
        assertEquals("ana@escuelamarcelapaz.cl", perfil.correo)
        assertEquals(RolUsuario.DOCENTE, perfil.rolActual)
    }

    @Test
    fun elEstudianteNoVeModulosSinAutorizar() {
        val vm = ModeloVistaMpaz()
        // El perfil inicial es estudiante. Se revoca la autorizacion del modulo 1 (los datos de prueba parten autorizados).
        vm.autorizarModuloUtp(1)
        assertFalse(vm.modulo(1).aprobadoPorUtp)
        assertTrue(vm.modulosVisibles.value.none { it.id == 1 })
        assertTrue(vm.modulosVisibles.value.all { it.aprobadoPorUtp })
    }

    @Test
    fun docenteYUtpVenTodosLosModulos() {
        val vm = ModeloVistaMpaz()
        vm.autorizarModuloUtp(1)
        vm.establecerUsuario("Docente", "d@escuelamarcelapaz.cl", RolUsuario.DOCENTE)
        assertEquals(vm.modulos.value.size, vm.modulosVisibles.value.size)
        vm.establecerUsuario("UTP", "u@escuelamarcelapaz.cl", RolUsuario.UTP)
        assertEquals(vm.modulos.value.size, vm.modulosVisibles.value.size)
    }

    @Test
    fun autorizarModuloUtpAlternaLaAutorizacion() {
        val vm = ModeloVistaMpaz()
        val antes = vm.modulo(2).aprobadoPorUtp
        vm.autorizarModuloUtp(2)
        assertEquals(!antes, vm.modulo(2).aprobadoPorUtp)
        vm.autorizarModuloUtp(2)
        assertEquals(antes, vm.modulo(2).aprobadoPorUtp)
    }

    @Test
    fun elEstudianteNoPuedeAvanzarUnModuloPendiente() {
        val vm = ModeloVistaMpaz()
        vm.autorizarModuloUtp(1)
        val progresoAntes = vm.modulo(1).progreso
        vm.actualizarProgresoModulo(1, 0.8f)
        assertEquals(progresoAntes, vm.modulo(1).progreso, 0.0001f)
    }

    @Test
    fun alActualizarElProgresoSeMarcaCompletadoSoloAlLlegarAlCien() {
        val vm = ModeloVistaMpaz()
        vm.establecerUsuario("Docente", "d@escuelamarcelapaz.cl", RolUsuario.DOCENTE)
        vm.actualizarProgresoModulo(2, 0.5f)
        assertEquals(0.5f, vm.modulo(2).progreso, 0.0001f)
        assertFalse(vm.modulo(2).estaCompletado)
        vm.actualizarProgresoModulo(2, 1.0f)
        assertTrue(vm.modulo(2).estaCompletado)
    }

    @Test
    fun cambiarEstadoModuloAlternaCompletadoYPoneElProgresoAlMaximo() {
        val vm = ModeloVistaMpaz()
        vm.establecerUsuario("Docente", "d@escuelamarcelapaz.cl", RolUsuario.DOCENTE)
        val antes = vm.modulo(3).estaCompletado
        vm.cambiarEstadoModulo(3)
        val despues = vm.modulo(3)
        assertNotEquals(antes, despues.estaCompletado)
        if (despues.estaCompletado) assertEquals(1.0f, despues.progreso, 0.0001f)
    }

    @Test
    fun elProgresoGeneralSeCalculaConLosModulosDelRol() {
        val vm = ModeloVistaMpaz()
        vm.establecerUsuario("Docente", "d@escuelamarcelapaz.cl", RolUsuario.DOCENTE)
        vm.modulos.value.forEach { vm.actualizarProgresoModulo(it.id, 1.0f) }
        assertEquals(1.0f, vm.perfil.value.progresoGeneral, 0.0001f)
        assertEquals(vm.modulos.value.size, vm.perfil.value.modulosCompletadosTotal)
    }

    @Test
    fun actualizarTextoBusquedaGuardaElTexto() {
        val vm = ModeloVistaMpaz()
        vm.actualizarTextoBusqueda("OA-MAT")
        assertEquals("OA-MAT", vm.textoBusqueda.value)
    }
}
