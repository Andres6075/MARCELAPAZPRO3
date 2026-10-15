package com.marcelapaz.mpazpr3.datos.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje

/*
  Entidad de Room para representar la tabla de modulos en la base de datos local SQLite.
  Permite la persistencia de datos solicitada para la aplicacion.
*/
@Entity(tableName = "modulos_aprendizaje")
data class ModuloEntidad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val nivelEducativo: String,
    val asignatura: String,
    val unidad: String,
    val objetivoAprendizaje: String,
    val codigoOficial: String,
    val ejeCurricular: String,
    val explicacion: String,
    val tieneAudioApoyo: Boolean,
    val intentosPermitidos: Int,
    val intentosRealizados: Int,
    val dificultad: String,
    val progreso: Float,
    val estaCompletado: Boolean,
    val horasEstimadas: Int,
    val aprobadoPorUtp: Boolean
)

// Funciones de conversion entre el modelo de dominio y la entidad de Room
fun ModuloEntidad.aModeloDominio(): ModuloAprendizaje {
    return ModuloAprendizaje(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        nivelEducativo = nivelEducativo,
        asignatura = asignatura,
        unidad = unidad,
        objetivoAprendizaje = objetivoAprendizaje,
        codigoOficial = codigoOficial,
        ejeCurricular = ejeCurricular,
        explicacion = explicacion,
        tieneAudioApoyo = tieneAudioApoyo,
        intentosPermitidos = intentosPermitidos,
        intentosRealizados = intentosRealizados,
        dificultad = dificultad,
        progreso = progreso,
        estaCompletado = estaCompletado,
        horasEstimadas = horasEstimadas,
        aprobadoPorUtp = aprobadoPorUtp
    )
}

fun ModuloAprendizaje.aEntidad(): ModuloEntidad {
    return ModuloEntidad(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        nivelEducativo = nivelEducativo,
        asignatura = asignatura,
        unidad = unidad,
        objetivoAprendizaje = objetivoAprendizaje,
        codigoOficial = codigoOficial,
        ejeCurricular = ejeCurricular,
        explicacion = explicacion,
        tieneAudioApoyo = tieneAudioApoyo,
        intentosPermitidos = intentosPermitidos,
        intentosRealizados = intentosRealizados,
        dificultad = dificultad,
        progreso = progreso,
        estaCompletado = estaCompletado,
        horasEstimadas = horasEstimadas,
        aprobadoPorUtp = aprobadoPorUtp
    )
}

/*
  Glosario de Room - Entidades:
  - Entity: Anotacion de Room que convierte una data class de Kotlin en una tabla de SQLite.
  - PrimaryKey: Clave primaria de la tabla para identificar cada registro de forma unica.
*/
