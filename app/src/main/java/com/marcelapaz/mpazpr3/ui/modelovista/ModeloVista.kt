package com.marcelapaz.mpazpr3.ui.modelovista

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelapaz.mpazpr3.datos.InformePedagogico
import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.datos.PerfilEstudiante
import com.marcelapaz.mpazpr3.datos.RecursoDidactico
import com.marcelapaz.mpazpr3.datos.RepositorioDatos
import com.marcelapaz.mpazpr3.datos.RolUsuario
import com.marcelapaz.mpazpr3.datos.db.BaseDatosApp
import com.marcelapaz.mpazpr3.datos.db.ModuloDao
import com.marcelapaz.mpazpr3.datos.db.aEntidad
import com.marcelapaz.mpazpr3.datos.db.aModeloDominio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Modelo de vista para manejar el estado de la aplicacion, perfiles y persistencia local con Room
class ModeloVistaMpaz : ViewModel() {
    private var moduloDao: ModuloDao? = null

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

    // Inicializa Room Database para almacenar el progreso de forma persistente
    fun inicializarRoom(contexto: Context) {
        if (moduloDao != null) return
        val db = BaseDatosApp.obtenerBaseDatos(contexto)
        moduloDao = db.moduloDao()

        viewModelScope.launch {
            moduloDao?.let { dao ->
                if (dao.contarModulos() == 0) {
                    val iniciales = RepositorioDatos.obtenerModulos().map { it.aEntidad() }
                    dao.insertarModulos(iniciales)
                }
                dao.obtenerTodosLosModulos().collect { listaEntidades ->
                    if (listaEntidades.isNotEmpty()) {
                        _modulos.value = listaEntidades.map { it.aModeloDominio() }
                        calcularProgresoGeneral()
                    }
                }
            }
        }
    }

    fun actualizarTextoBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }

    fun cambiarRolUsuario(nuevoRol: RolUsuario) {
        _perfil.update { it.copy(rolActual = nuevoRol) }
    }

    fun autorizarModuloUtp(idModulo: Int) {
        _modulos.update { lista ->
            lista.map { modulo ->
                if (modulo.id == idModulo) {
                    val modificado = modulo.copy(aprobadoPorUtp = !modulo.aprobadoPorUtp)
                    guardarEnRoom(modificado)
                    modificado
                } else modulo
            }
        }
    }

    fun cambiarEstadoModulo(idModulo: Int) {
        _modulos.update { listaActual ->
            listaActual.map { modulo ->
                if (modulo.id == idModulo) {
                    val nuevoEstado = !modulo.estaCompletado
                    val modificado = modulo.copy(
                        estaCompletado = nuevoEstado,
                        progreso = if (nuevoEstado) 1.0f else modulo.progreso
                    )
                    guardarEnRoom(modificado)
                    modificado
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
                    val modificado = modulo.copy(
                        progreso = nuevoProgreso,
                        estaCompletado = nuevoProgreso >= 1.0f
                    )
                    guardarEnRoom(modificado)
                    modificado
                } else {
                    modulo
                }
            }
        }
        calcularProgresoGeneral()
    }

    private fun guardarEnRoom(modulo: ModuloAprendizaje) {
        viewModelScope.launch {
            moduloDao?.actualizarModulo(modulo.aEntidad())
        }
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
