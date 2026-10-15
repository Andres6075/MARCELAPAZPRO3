package com.marcelapaz.mpazpr3

import org.junit.Test
import org.junit.Assert.*

/*
  Aqui tenemos una prueba unitaria local.
  Estas pruebas corren directamente en la computadora (en la maquina de desarrollo) 
  y sirven para comprobar pequenas funciones de codigo, calculos o logica 
  sin necesidad de abrir un telefono o emulador.
*/
class PruebaUnitariaEjemplo {
    @Test
    fun sumaEsCorrecta() {
        // Comprobamos una operacion matematica simple para validar que JUnit funciona bien
        assertEquals(4, 2 + 2)
    }
}

/*
  Glosario bien contundente de terminos de pruebas unitarias y Kotlin:
  - Prueba unitaria: Test rapido que prueba una sola funcion o parte pequeña del codigo en la PC.
  - JUnit: Framework de pruebas para Java y Kotlin.
  - Test: Etiqueta que convierte una funcion normal en una prueba ejecutable.
  - AssertEquals: Comprobacion para validar que el resultado esperado sea igual al obtenido.
  - Val: Variable constante de solo lectura en Kotlin.
  - Var: Variable que puede cambiar su valor mas adelante.
  - Fun: Palabra para crear funciones.
  - StateFlow: Canal de datos reactivo que avisa a la pantalla cuando hay cambios.
  - Repository: Objeto encargado de traer los datos (en nuestro caso, los modulos de Tercero Basico).
  - Clean Architecture: Forma de organizar el proyecto separando datos, vistas y logica.
*/
