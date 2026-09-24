package com.marcelapaz.mpazpr3

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

// Prueba instrumentalizada para verificar el entorno de Android
@RunWith(AndroidJUnit4::class)
class PruebaInstrumentalEjemplo {
    @Test
    fun verificarContextoApp() {
        // Obtener el contexto de la aplicacion en prueba
        val contextoApp = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.marcelapaz.mpazpr3", contextoApp.packageName)
    }
}
