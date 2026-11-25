package com.marcelapaz.mpazpr3.datos.repositorios

import com.marcelapaz.mpazpr3.datos.modelos.CitaPedagogica
import com.marcelapaz.mpazpr3.datos.red.ServicioApiExterna
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

// Pruebas unitarias del repositorio de API Externa usando Mockk (Cumple Guia 15 e IE 3.2.1)
class RepositorioApiExternaPrueba {

    private val servicioMock = mockk<ServicioApiExterna>()

    @Test
    fun cuandoLaApiExternaRespondeExitosamenteDevuelveLaCita() = runTest {
        val citaSimulada = CitaPedagogica(10, "La tecnología es solo una herramienta.", "Bill Gates")
        coEvery { servicioMock.obtenerCitaPedagogica() } returns citaSimulada

        val repositorio = RepositorioApiExterna(servicioMock)
        val resultado = repositorio.obtenerCitaDelDia()

        assertNotNull(resultado)
        assertEquals("Bill Gates", resultado.autor)
        assertEquals("La tecnología es solo una herramienta.", resultado.cita)
    }

    @Test
    fun cuandoLaApiExternaFallaDevuelveCitaLocalDeRespaldo() = runTest {
        coEvery { servicioMock.obtenerCitaPedagogica() } throws Exception("Error de conexión red")

        val repositorio = RepositorioApiExterna(servicioMock)
        val resultado = repositorio.obtenerCitaDelDia()

        assertNotNull(resultado)
        assertNotNull(resultado.cita)
        assertNotNull(resultado.autor)
    }
}
