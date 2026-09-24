package com.marcelapaz.mpazpr3.ui.modelovista

import androidx.lifecycle.ViewModel
import com.marcelapaz.mpazpr3.datos.InformePedagogico
import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.datos.PerfilEstudiante
import com.marcelapaz.mpazpr3.datos.RecursoDidactico
import com.marcelapaz.mpazpr3.datos.RepositorioDatos
import com.marcelapaz.mpazpr3.datos.RolUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Modelo de vista para manejar el estado de la aplicacion y los perfiles del caso (3.1)
class ModeloVistaMpaz : ViewModel() {
    private val _modulos = MutableStateFlow(RepositorioDatos.obtenerModulos())
    val modulos: StateFlow<List<ModuloAprendizaje>> = _modulos.asStateFlow()

    private val _recursos = MutableStateFlow(RepositorioDatos.obtenerRecursos())
    val recursos: StateFlow<List<RecursoDidactico>> = _recursos.asStateFlow()

    private val _perfil = MutableStateFlow(RepositorioDatos.obtenerPerfil())
    val perfil: StateFlow<PerfilEstudiante> = _perfil.asStateFlow()

    private val _informe = MutableStateFlow(RepositorioDatos.obtenerInformePedagogico())
    val informe: StateFlow<InformePedagogico> = _informe.asStateFlow()

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda.asStateFlow()

    fun actualizarTextoBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }

    fun cambiarRolUsuario(nuevoRol: RolUsuario) {
        _perfil.update { it.copy(rolActual = nuevoRol) }
    }

    fun autorizarModuloUtp(idModulo: Int) {
        _modulos.update { lista ->
            lista.map { if (it.id == idModulo) it.copy(aprobadoPorUtp = !it.aprobadoPorUtp) else it }
        }
    }

    fun cambiarEstadoModulo(idModulo: Int) {
        _modulos.update { listaActual ->
            listaActual.map { modulo ->
                if (modulo.id == idModulo) {
                    val nuevoEstado = !modulo.estaCompletado
                    modulo.copy(
                        estaCompletado = nuevoEstado,
                        progreso = if (nuevoEstado) 1.0f else modulo.progreso
                    )
                } else {
                    modulo
                }
            }
        }
        calcularProgresoGeneral()
    }

    fun actualizarProgresoModulo(idModulo: Int, nuevoProgreso: Float) {
        _modulos.update { listaActual ->
            listaActual.map { modulo ->
                if (modulo.id == idModulo) {
                    modulo.copy(
                        progreso = nuevoProgreso,
                        estaCompletado = nuevoProgreso >= 1.0f
                    )
                } else {
                    modulo
                }
            }
        }
        calcularProgresoGeneral()
    }

    private fun calcularProgresoGeneral() {
        val lista = _modulos.value
        if (lista.isEmpty()) return
        val promedio = lista.map { it.progreso }.average().toFloat()
        val completados = lista.count { it.estaCompletado }
        _perfil.update { p ->
            p.copy(
                progresoGeneral = promedio,
                modulosCompletadosTotal = completados
            )
        }
    }
}
