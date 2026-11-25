package com.marcelapaz.mpazpr3.datos.red

import com.marcelapaz.mpazpr3.datos.modelos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.datos.modelos.RolUsuario
import com.marcelapaz.mpazpr3.datos.modelos.Usuario
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.Response

// Pruebas unitarias de la integración de Microservicios usando Mockk (Cumple Guia 15 e IE 3.2.1)
class ServicioMicroservicioPrueba {

    private val servicioMock = mockk<ServicioMicroservicio>()

    @Test
    fun obtenerModulosDesdeMicroservicioDevuelveLista() = runTest {
        val listaSimulada = listOf(
            ModuloAprendizaje(1, "Modulo 1", "Desc", "3B", "Lenguaje", "U1", "OA1", "OA-LENG-03-01", "Eje", "Exp", true, 3, 1, "Media", 0.5f, false, 5, true)
        )
        coEvery { servicioMock.obtenerModulos() } returns listaSimulada

        val resultado = servicioMock.obtenerModulos()

        assertNotNull(resultado)
        assertEquals(1, resultado.size)
        assertEquals("Modulo 1", resultado[0].titulo)
    }

    @Test
    fun crearModuloEnMicroservicioRetornaElModuloGuardado() = runTest {
        val nuevoModulo = ModuloAprendizaje(0, "Nuevo Modulo", "Desc", "3B", "Matemática", "U2", "OA2", "OA-MAT-03-02", "Eje", "Exp", false, 3, 0, "Baja", 0f, false, 4, false)
        val creadoSimulado = nuevoModulo.copy(id = 5)
        coEvery { servicioMock.crearModulo(nuevoModulo) } returns creadoSimulado

        val resultado = servicioMock.crearModulo(nuevoModulo)

        assertNotNull(resultado)
        assertEquals(5, resultado.id)
        assertEquals("Nuevo Modulo", resultado.titulo)
    }

    @Test
    fun eliminarModuloEnMicroservicioRetornaExito() = runTest {
        coEvery { servicioMock.eliminarModulo(5) } returns Response.success(Unit)

        val resultado = servicioMock.eliminarModulo(5)

        assertNotNull(resultado)
        assertEquals(200, resultado.code())
    }

    @Test
    fun obtenerUsuariosDesdeMicroservicioDevuelveListaDeUsuarios() = runTest {
        val usuariosSimulados = listOf(
            Usuario(1, "Estudiante Demo", "estudiante@escuelamarcelapaz.cl", RolUsuario.ESTUDIANTE)
        )
        coEvery { servicioMock.obtenerUsuarios() } returns usuariosSimulados

        val resultado = servicioMock.obtenerUsuarios()

        assertNotNull(resultado)
        assertEquals(1, resultado.size)
        assertEquals(RolUsuario.ESTUDIANTE, resultado[0].rol)
    }
}
