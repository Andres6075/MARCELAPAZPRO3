package com.marcelapaz.mpazpr3.datos

/*
  En este archivo definimos todas las estructuras de datos (moldes) que usa nuestra app.
  Aqui creamos las clases para los modulos escolares, los recursos didacticos,
  los perfiles de usuario exigidos en el caso (Estudiante, Docente y UTP) y el informe pedagogico.
  Todo adaptado al caso de estudio de la Escuela Marcela Paz para Tercero Basico.
*/

// Aqui definimos los tres roles de usuario que pide la seccion 3.1 del caso de estudio
enum class RolUsuario(val descripcion: String) {
    ESTUDIANTE("Estudiante (3° Básico)"),
    DOCENTE("Docente de Tercero Básico"),
    UTP("Jefatura de UTP")
}

// Este molde representa cada modulo de aprendizaje con su informacion curricular completa (seccion 3.2)
data class ModuloAprendizaje(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val nivelEducativo: String, // Ejemplo: "Tercero Básico"
    val asignatura: String, // Ejemplo: "Lenguaje y Comunicación", "Matemática"
    val unidad: String, // Ejemplo: "Unidad 1"
    val objetivoAprendizaje: String, // Meta curricular oficial
    val codigoOficial: String, // Ejemplo: "OA-LENG-03-03"
    val ejeCurricular: String, // Ejemplo: "Lectura"
    val explicacion: String,
    val tieneAudioApoyo: Boolean,
    val intentosPermitidos: Int,
    val intentosRealizados: Int,
    val dificultad: String, // "Baja", "Media", "Alta"
    val progreso: Float, // Porcentaje de avance de 0.0 a 1.0
    val estaCompletado: Boolean,
    val horasEstimadas: Int,
    val aprobadoPorUtp: Boolean = true
)

// Este molde representa los recursos didacticos de apoyo (guias, videos, ejemplos e instrucciones de la seccion 3.2)
data class RecursoDidactico(
    val id: Int,
    val titulo: String,
    val tipo: String, // "Video", "Lectura", "Cuestionario", "Guía"
    val duracionOPaginas: String,
    val moduloId: Int,
    val instrucciones: String,
    val ejemplo: String,
    val tieneAudio: Boolean,
    val estaDisponible: Boolean = false
)

// Este molde representa los datos del usuario actual y su identificador ficticio (seccion 3.2 y 3.4)
data class PerfilEstudiante(
    val nombre: String,
    val correo: String,
    val codigoEstudiante: String, // Identificador ficticio para proteger la privacidad
    val modulosCompletadosTotal: Int,
    val rachaDiasEstudio: Int,
    val progresoGeneral: Float,
    val rolActual: RolUsuario = RolUsuario.ESTUDIANTE
)

// Este molde representa el informe pedagogico reservado para el docente y la UTP (secciones 3.1 y 3.2)
data class InformePedagogico(
    val asignatura: String,
    val rendimientoPromedio: String,
    val fortalezas: String,
    val contenidosAReforzar: String,
    val fechaValidacion: String,
    val estadoValidacion: String
)

/*
  Glosario bien contundente de modelos de datos y arquitectura:
  - Data Class: Una clase especial en Kotlin pensada unicamente para almacenar datos y estructuras fijas.
  - Enum Class: Un conjunto limitado de opciones constantes (en nuestro caso, los tres roles del caso).
  - Val: Propiedad de solo lectura que no puede ser modificada despues de crearse.
  - Int: Numero entero (para IDs o contadores).
  - String: Texto plano para titulos, descripciones y codigos oficiales.
  - Float: Numero decimal (utilizado para calcular el progreso de 0.0 a 1.0).
  - Boolean: Valor verdadero o falso (para saber si esta completado o aprobado).
  - OA (Objetivo de Aprendizaje): Meta curricular oficial del ministerio.
  - UTP: Unidad Tecnico Pedagogica encargada de autorizar contenidos.
  - MVP: Producto minimo viable desarrollado como prototipo academico.
  - Racha de estudio: Dias seguidos que el alumno entra a estudiar.
  - Identificador ficticio: Codigo de prueba para proteger datos personales segun la regla 5.1 del caso.
*/
