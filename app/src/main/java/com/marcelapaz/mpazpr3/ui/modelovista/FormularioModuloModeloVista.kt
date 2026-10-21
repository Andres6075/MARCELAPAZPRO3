package com.marcelapaz.mpazpr3.ui.modelovista

import androidx.lifecycle.ViewModel
import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Un mensaje de error por campo (null significa que el campo esta correcto)
data class ErroresFormularioModulo(
    val titulo: String? = null,
    val asignatura: String? = null,
    val unidad: String? = null,
    val codigoOa: String? = null,
    val horas: String? = null,
    val intentos: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(titulo, asignatura, unidad, codigoOa, horas, intentos).any { it != null }
}

// Estado completo del formulario: lo que escribio el usuario, los errores y si se guardo
data class EstadoFormularioModulo(
    val titulo: String = "",
    val asignatura: String = "",
    val unidad: String = "",
    val codigoOa: String = "",
    val horas: String = "",
    val intentos: String = "3",
    val errores: ErroresFormularioModulo = ErroresFormularioModulo(),
    val guardadoExitoso: Boolean = false
)

// ViewModel del formulario: mantiene el estado con StateFlow y valida cada campo al escribir
class FormularioModuloModeloVista : ViewModel() {

    private val _estado = MutableStateFlow(EstadoFormularioModulo())
    val estado: StateFlow<EstadoFormularioModulo> = _estado.asStateFlow()

    fun cambiarTitulo(valor: String) = _estado.update {
        it.copy(
            titulo = valor,
            errores = it.errores.copy(titulo = ValidadorModulo.validarTitulo(valor)),
            guardadoExitoso = false
        )
    }

    fun cambiarAsignatura(valor: String) = _estado.update {
        it.copy(
            asignatura = valor,
            errores = it.errores.copy(asignatura = ValidadorModulo.validarAsignatura(valor)),
            guardadoExitoso = false
        )
    }

    fun cambiarUnidad(valor: String) = _estado.update {
        it.copy(
            unidad = valor,
            errores = it.errores.copy(unidad = ValidadorModulo.validarUnidad(valor)),
            guardadoExitoso = false
        )
    }

    fun cambiarCodigoOa(valor: String) = _estado.update {
        val normalizado = valor.uppercase()
        it.copy(
            codigoOa = normalizado,
            errores = it.errores.copy(codigoOa = ValidadorModulo.validarCodigoOa(normalizado)),
            guardadoExitoso = false
        )
    }

    fun cambiarHoras(valor: String) = _estado.update {
        it.copy(
            horas = valor,
            errores = it.errores.copy(horas = ValidadorModulo.validarHoras(valor)),
            guardadoExitoso = false
        )
    }

    fun cambiarIntentos(valor: String) = _estado.update {
        it.copy(
            intentos = valor,
            errores = it.errores.copy(intentos = ValidadorModulo.validarIntentos(valor)),
            guardadoExitoso = false
        )
    }

    // Valida todos los campos juntos. Si hay errores los muestra y retorna null.
    // Si todo esta correcto retorna el modulo listo para guardar y limpia el formulario.
    fun enviar(): ModuloAprendizaje? {
        val actual = _estado.value
        val errores = ErroresFormularioModulo(
            titulo = ValidadorModulo.validarTitulo(actual.titulo),
            asignatura = ValidadorModulo.validarAsignatura(actual.asignatura),
            unidad = ValidadorModulo.validarUnidad(actual.unidad),
            codigoOa = ValidadorModulo.validarCodigoOa(actual.codigoOa),
            horas = ValidadorModulo.validarHoras(actual.horas),
            intentos = ValidadorModulo.validarIntentos(actual.intentos)
        )
        if (errores.hayErrores) {
            _estado.update { it.copy(errores = errores, guardadoExitoso = false) }
            return null
        }

        val nuevo = ModuloAprendizaje(
            id = 0, // Room asigna el id automaticamente
            titulo = actual.titulo.trim(),
            descripcion = "Módulo creado por el docente.",
            nivelEducativo = "Tercero Básico",
            asignatura = actual.asignatura,
            unidad = actual.unidad.trim(),
            objetivoAprendizaje = "Objetivo de aprendizaje ${actual.codigoOa.trim()}",
            codigoOficial = actual.codigoOa.trim(),
            ejeCurricular = "General",
            explicacion = "Sigue las instrucciones de tu docente para completar este módulo.",
            tieneAudioApoyo = false,
            intentosPermitidos = actual.intentos.trim().toInt(),
            intentosRealizados = 0,
            dificultad = "Media",
            progreso = 0f,
            estaCompletado = false,
            horasEstimadas = actual.horas.trim().toInt(),
            aprobadoPorUtp = false // Queda pendiente hasta que la UTP lo autorice
        )
        _estado.value = EstadoFormularioModulo(guardadoExitoso = true)
        return nuevo
    }
}
