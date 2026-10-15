package com.marcelapaz.mpazpr3

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/*
  Aqui hacemos una prueba instrumentalizada. 
  A diferencia de las pruebas unitarias normales, esta prueba necesita correr 
  dentro de un dispositivo Android real o un emulador para comprobar que 
  la aplicacion arranca bien y su paquete coincide con lo que esperamos.
  Es excelente para verificar que nuestra app MPAZ PRO 3 se instala y funciona 
  en telefonos como el Motorola One Macro de la escuela.
*/
@RunWith(AndroidJUnit4::class)
class PruebaInstrumentalEjemplo {
    @Test
    fun verificarContextoApp() {
        // Pedimos al sistema el contexto actual de nuestra aplicacion en ejecucion
        val contextoApp = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Verificamos que el nombre del paquete coincida exactamente con el identificador de nuestra app
        assertEquals("com.marcelapaz.mpazpr3", contextoApp.packageName)
    }
}

/*
  Glosario bien contundente de terminos de pruebas y desarrollo Android:
  - Prueba instrumentalizada: Test que se ejecuta en un telefono o emulador real para probar componentes de Android.
  - JUnit: La libreria estandar que usamos para escribir y ejecutar nuestras pruebas de codigo.
  - RunWith: Anotacion que le indica a JUnit como debe ejecutar la clase de prueba (usando AndroidJunit4).
  - Test: Anotacion que señala que una funcion es una prueba que debe ser ejecutada por el sistema.
  - Contexto (Context): Informacion sobre el estado actual de la aplicacion o del sistema Android.
  - TargetContext: El contexto especifico de nuestra propia aplicacion bajo prueba.
  - PackageName: El identificador unico de la app (com.marcelapaz.mpazpr3).
  - AssertEquals: Funcion de comprobacion que verifica si dos valores son iguales; si fallan, la prueba se detiene.
  - Emulador: Un telefono virtual que corre en la computadora para probar apps.
  - Depuracion USB: Opcion para conectar un celular fisico y pasarle la app directamente sin emulador.
  - Gradle: La herramienta que compila y arma nuestra aplicacion lista para instalar.
  - ViewModel: El encargado de guardar los datos para que no se pierdan al girar la pantalla.
  - Scaffold: El molde visual de nuestras pantallas con barras superiores e inferiores.
  - MVP: Producto minimo viable, nuestro prototipo escolar para Tercero Basico.
  - UTP y Docente: Los perfiles del caso de estudio encargados de autorizar y gestionar los aprendizajes.
*/
