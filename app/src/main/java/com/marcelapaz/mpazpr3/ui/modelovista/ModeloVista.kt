package com.marcelapaz.mpazpr3.ui.modelovista

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.graphics.Bitmap
import android.net.Uri
import com.marcelapaz.mpazpr3.datos.modelos.CitaPedagogica
import com.marcelapaz.mpazpr3.datos.modelos.InformePedagogico
import com.marcelapaz.mpazpr3.datos.modelos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.datos.modelos.PerfilEstudiante
import com.marcelapaz.mpazpr3.datos.modelos.RecursoDidactico
import com.marcelapaz.mpazpr3.datos.modelos.RolUsuario
import com.marcelapaz.mpazpr3.datos.repositorios.RepositorioApiExterna
import com.marcelapaz.mpazpr3.datos.repositorios.RepositorioDatos
import com.marcelapaz.mpazpr3.datos.repositorios.RepositorioModulos
import com.marcelapaz.mpazpr3.datos.utilidades.AlmacenAvatar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Modelo de vista para manejar el estado de la aplicacion, perfiles y persistencia local con Room
class ModeloVistaMpaz : ViewModel() {
    private var repositorio: RepositorioModulos? = null
    private val repoApiExterna = RepositorioApiExterna()

    private val _citaPedagogica = MutableStateFlow<CitaPedagogica?>(null)
    val citaPedagogica: StateFlow<CitaPedagogica?> = _citaPedagogica.asStateFlow()

    init {
        cargarCitaPedagogica()
    }

    fun cargarCitaPedagogica() {
        viewModelScope.launch {
            _citaPedagogica.value = repoApiExterna.obtenerCitaDelDia()
        }
    }

    private val _modulos = MutableStateFlow(RepositorioDatos.obtenerModulos())
    val modulos: StateFlow<List<ModuloAprendizaje>> = _modulos.asStateFlow()

    private val _recursos = MutableStateFlow(RepositorioDatos.obtenerRecursos())
    val recursos: StateFlow<List<RecursoDidactico>> = _recursos.asStateFlow()

    private val _perfil = MutableStateFlow(RepositorioDatos.obtenerPerfil())
    val perfil: StateFlow<PerfilEstudiante> = _perfil.asStateFlow()

    // Regla del caso (3.1): el estudiante solo ve los modulos autorizados por la UTP.
    // Docente y UTP ven todos, porque el docente los crea y la UTP los autoriza.
    val modulosVisibles: StateFlow<List<ModuloAprendizaje>> =
        combine(_modulos, _perfil) { lista, perfilActual ->
            modulosSegunRol(lista, perfilActual.rolActual)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = modulosSegunRol(_modulos.value, _perfil.value.rolActual)
        )

    private val _informe = MutableStateFlow(RepositorioDatos.obtenerInformePedagogico())
    val informe: StateFlow<InformePedagogico> = _informe.asStateFlow()

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda.asStateFlow()

    // Crea el repositorio (Room + Microservicio Spring Boot) y mantiene la lista de modulos sincronizada
    fun inicializarRoom(contexto: Context) {
        if (repositorio != null) return
        val repo = RepositorioModulos.crear(contexto)
        repositorio = repo

        viewModelScope.launch {
            repo.sembrarDatosIniciales()
            // Intenta sincronizar datos con el microservicio Spring Boot si está disponible
            repo.sincronizarConMicroservicio()
            repo.modulos.collect { lista ->
                if (lista.isNotEmpty()) {
                    _modulos.value = lista
                    calcularProgresoGeneral()
                }
            }
        }
    }

    // Foto de perfil guardada en el almacenamiento interno (recurso nativo: selector de fotos)
    private val _avatar = MutableStateFlow<Bitmap?>(null)
    val avatar: StateFlow<Bitmap?> = _avatar.asStateFlow()

    fun cargarAvatar(contexto: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _avatar.value = AlmacenAvatar.cargar(contexto.applicationContext)
        }
    }

    fun guardarAvatar(contexto: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            AlmacenAvatar.guardar(contexto.applicationContext, uri)?.let { _avatar.value = it }
        }
    }

    fun actualizarTextoBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }

    // Carga en el perfil los datos del usuario que inicio sesion (nombre, correo y rol)
    fun establecerUsuario(nombre: String, correo: String, rol: RolUsuario) {
        _perfil.update { it.copy(nombre = nombre, correo = correo, rolActual = rol) }
        // El avance general depende de los modulos que ve el rol actual
        calcularProgresoGeneral()
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
        calcularProgresoGeneral()
    }

    fun cambiarIntentosDocente(idModulo: Int, nuevosIntentos: Int) {
        _modulos.update { lista ->
            lista.map { modulo ->
                if (modulo.id == idModulo) {
                    val modificado = modulo.copy(intentosPermitidos = nuevosIntentos)
                    guardarEnRoom(modificado)
                    modificado
                } else modulo
            }
        }
    }

    fun cambiarEstadoModulo(idModulo: Int) {
        if (!puedeAvanzar(idModulo)) return
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
        if (!puedeAvanzar(idModulo)) return
        val actual = _modulos.value.firstOrNull { it.id == idModulo } ?: return
        val modificado = actual.copy(
            progreso = nuevoProgreso,
            estaCompletado = nuevoProgreso >= 1.0f
        )
        // Primero se actualiza el estado (sin efectos secundarios dentro de update) y luego se guarda en Room
        _modulos.update { lista -> lista.map { if (it.id == idModulo) modificado else it } }
        guardarEnRoom(modificado)
        calcularProgresoGeneral()
    }

    // Guarda en el microservicio en Spring Boot y en Room un modulo nuevo creado desde el formulario
    fun agregarModulo(modulo: ModuloAprendizaje) {
        viewModelScope.launch {
            val guardado = repositorio?.insertar(modulo) ?: modulo
            if (repositorio == null) {
                _modulos.update { it + guardado }
            }
        }
    }

    // Elimina un modulo tanto en el microservicio Spring Boot como en la base de datos local Room
    fun eliminarModulo(idModulo: Int) {
        viewModelScope.launch {
            repositorio?.eliminar(idModulo)
            _modulos.update { lista -> lista.filter { it.id != idModulo } }
            calcularProgresoGeneral()
        }
    }

    // Filtra los modulos segun el rol: el estudiante solo accede a los autorizados por la UTP
    private fun modulosSegunRol(lista: List<ModuloAprendizaje>, rol: RolUsuario): List<ModuloAprendizaje> =
        if (rol == RolUsuario.ESTUDIANTE) lista.filter { it.aprobadoPorUtp } else lista

    // Un estudiante no puede avanzar ni completar un modulo que la UTP no ha autorizado
    private fun puedeAvanzar(idModulo: Int): Boolean {
        val modulo = _modulos.value.find { it.id == idModulo } ?: return false
        return _perfil.value.rolActual != RolUsuario.ESTUDIANTE || modulo.aprobadoPorUtp
    }

    private fun guardarEnRoom(modulo: ModuloAprendizaje) {
        viewModelScope.launch {
            repositorio?.actualizar(modulo)
        }
    }

    private fun calcularProgresoGeneral() {
        val lista = modulosSegunRol(_modulos.value, _perfil.value.rolActual)
        val promedio = if (lista.isEmpty()) 0f else lista.map { it.progreso }.average().toFloat()
        val completados = lista.count { it.estaCompletado }
        _perfil.update { p ->
            p.copy(
                progresoGeneral = promedio,
                modulosCompletadosTotal = completados
            )
        }
    }
}
