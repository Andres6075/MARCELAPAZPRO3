package com.marcelapaz.mpazpr3.datos

// Perfiles de usuario definidos en el caso (3.1)
enum class RolUsuario(val descripcion: String) {
    ESTUDIANTE("Estudiante (3° Basico)"),
    DOCENTE("Docente de Tercero Basico"),
    UTP("Jefatura de UTP")
}

// Modelo curricular detallado segun la seccion 3.2 del caso
data class ModuloAprendizaje(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val nivelEducativo: String, // "Tercero Basico"
    val asignatura: String, // "Lenguaje", "Matematica", etc.
    val unidad: String, // "Unidad 1"
    val objetivoAprendizaje: String, // "OA 1"
    val codigoOficial: String, // "OA-01"
    val ejeCurricular: String, // "Lectura"
    val explicacion: String,
    val tieneAudioApoyo: Boolean,
    val intentosPermitidos: Int,
    val intentosRealizados: Int,
    val dificultad: String, // "Baja", "Media", "Alta"
    val progreso: Float, // de 0.0 al 1.0
    val estaCompletado: Boolean,
    val horasEstimadas: Int,
    val aprobadoPorUtp: Boolean = true
)

// Modelo para recursos didacticos segun la seccion 3.2 (audios, imagenes, ejemplos, instrucciones)
data class RecursoDidactico(
    val id: Int,
    val titulo: String,
    val tipo: String, // "Video", "Lectura", "Cuestionario", "Guia"
    val duracionOPaginas: String,
    val moduloId: Int,
    val instrucciones: String,
    val ejemplo: String,
    val tieneAudio: Boolean,
    val estaDisponible: Boolean = false
)

// Modelo para representar el perfil del usuario actual con identificador ficticio (3.2)
data class PerfilEstudiante(
    val nombre: String,
    val correo: String,
    val codigoEstudiante: String, // Identificador ficticio
    val modulosCompletadosTotal: Int,
    val rachaDiasEstudio: Int,
    val progresoGeneral: Float,
    val rolActual: RolUsuario = RolUsuario.ESTUDIANTE
)

// Informe pedagogico reservado para Docente y UTP (3.1 y 3.2)
data class InformePedagogico(
    val asignatura: String,
    val rendimientoPromedio: String,
    val fortalezas: String,
    val contenidosAReforzar: String,
    val fechaValidacion: String,
    val estadoValidacion: String
)
