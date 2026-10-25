package com.marcelapaz.mpazpr3.ui.modelovista

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcelapaz.mpazpr3.datos.ResultadoRegistro
import com.marcelapaz.mpazpr3.datos.RepositorioUsuarios
import com.marcelapaz.mpazpr3.datos.RolUsuario
import com.marcelapaz.mpazpr3.datos.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ----- Estado del formulario de inicio de sesion -----
data class ErroresLogin(
    val correo: String? = null,
    val clave: String? = null
) {
    val hayErrores: Boolean get() = correo != null || clave != null
}

data class EstadoLogin(
    val correo: String = "",
    val clave: String = "",
    val errores: ErroresLogin = ErroresLogin(),
    val errorGeneral: String? = null,
    val cargando: Boolean = false
)

// ----- Estado del formulario de registro -----
data class ErroresRegistro(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val confirmacion: String? = null,
    val rol: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(nombre, correo, clave, confirmacion, rol).any { it != null }
}

data class EstadoRegistro(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmacion: String = "",
    val rol: RolUsuario? = null,
    val errores: ErroresRegistro = ErroresRegistro(),
    val cargando: Boolean = false
)

/*
  ViewModel de autenticacion: guarda la sesion, el estado de los dos formularios y valida cada campo
  con ValidadorUsuario. El acceso a datos y el hash de la contrasena estan en RepositorioUsuarios.
*/
class AutenticacionModeloVista : ViewModel() {

    private var repositorio: RepositorioUsuarios? = null

    // Usuario con sesion iniciada (null = sin sesion)
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    private val _login = MutableStateFlow(EstadoLogin())
    val login: StateFlow<EstadoLogin> = _login.asStateFlow()

    private val _registro = MutableStateFlow(EstadoRegistro())
    val registro: StateFlow<EstadoRegistro> = _registro.asStateFlow()

    // Crea el repositorio y las cuentas de prueba la primera vez
    fun inicializar(contexto: Context) {
        if (repositorio != null) return
        val repo = RepositorioUsuarios.crear(contexto)
        repositorio = repo
        viewModelScope.launch { repo.sembrarUsuariosDemo() }
    }

    // ----- Inicio de sesion -----
    fun cambiarCorreoLogin(valor: String) {
        _login.update {
            it.copy(
                correo = valor,
                errores = it.errores.copy(correo = ValidadorUsuario.validarCorreo(valor)),
                errorGeneral = null
            )
        }
    }

    fun cambiarClaveLogin(valor: String) {
        _login.update {
            it.copy(
                clave = valor,
                errores = it.errores.copy(clave = ValidadorUsuario.validarClaveLogin(valor)),
                errorGeneral = null
            )
        }
    }

    fun iniciarSesion() {
        val actual = _login.value
        if (actual.cargando) return
        val errores = ErroresLogin(
            correo = ValidadorUsuario.validarCorreo(actual.correo),
            clave = ValidadorUsuario.validarClaveLogin(actual.clave)
        )
        if (errores.hayErrores) {
            _login.update { it.copy(errores = errores) }
            return
        }
        val repo = repositorio ?: return
        _login.update { it.copy(cargando = true, errorGeneral = null) }
        viewModelScope.launch {
            val encontrado = repo.iniciarSesion(actual.correo, actual.clave)
            if (encontrado == null) {
                _login.update {
                    it.copy(cargando = false, errorGeneral = "Correo o contraseña incorrectos")
                }
            } else {
                _login.value = EstadoLogin()
                _usuario.value = encontrado
            }
        }
    }

    // ----- Registro -----
    fun cambiarNombreRegistro(valor: String) {
        _registro.update {
            it.copy(nombre = valor, errores = it.errores.copy(nombre = ValidadorUsuario.validarNombre(valor)))
        }
    }

    fun cambiarCorreoRegistro(valor: String) {
        _registro.update {
            it.copy(correo = valor, errores = it.errores.copy(correo = ValidadorUsuario.validarCorreo(valor)))
        }
    }

    fun cambiarClaveRegistro(valor: String) {
        _registro.update {
            // Si ya escribio la confirmacion, se vuelve a comparar con la clave nueva
            val errorConfirmacion = if (it.confirmacion.isNotEmpty()) {
                ValidadorUsuario.validarConfirmacion(valor, it.confirmacion)
            } else {
                it.errores.confirmacion
            }
            it.copy(
                clave = valor,
                errores = it.errores.copy(
                    clave = ValidadorUsuario.validarClave(valor),
                    confirmacion = errorConfirmacion
                )
            )
        }
    }

    fun cambiarConfirmacionRegistro(valor: String) {
        _registro.update {
            it.copy(
                confirmacion = valor,
                errores = it.errores.copy(confirmacion = ValidadorUsuario.validarConfirmacion(it.clave, valor))
            )
        }
    }

    fun cambiarRolRegistro(valor: RolUsuario) {
        _registro.update {
            it.copy(rol = valor, errores = it.errores.copy(rol = ValidadorUsuario.validarRol(valor)))
        }
    }

    fun registrarUsuario() {
        val actual = _registro.value
        if (actual.cargando) return
        val errores = ErroresRegistro(
            nombre = ValidadorUsuario.validarNombre(actual.nombre),
            correo = ValidadorUsuario.validarCorreo(actual.correo),
            clave = ValidadorUsuario.validarClave(actual.clave),
            confirmacion = ValidadorUsuario.validarConfirmacion(actual.clave, actual.confirmacion),
            rol = ValidadorUsuario.validarRol(actual.rol)
        )
        if (errores.hayErrores) {
            _registro.update { it.copy(errores = errores) }
            return
        }
        val repo = repositorio ?: return
        val rol = actual.rol ?: return
        _registro.update { it.copy(cargando = true) }
        viewModelScope.launch {
            when (val resultado = repo.registrar(actual.nombre, actual.correo, actual.clave, rol)) {
                is ResultadoRegistro.Exito -> {
                    // La cuenta nueva inicia sesion de inmediato
                    _registro.value = EstadoRegistro()
                    _usuario.value = resultado.usuario
                }
                ResultadoRegistro.CorreoYaRegistrado -> {
                    _registro.update {
                        it.copy(
                            cargando = false,
                            errores = it.errores.copy(correo = "Este correo ya está registrado")
                        )
                    }
                }
            }
        }
    }

    fun cerrarSesion() {
        _usuario.value = null
        _login.value = EstadoLogin()
        _registro.value = EstadoRegistro()
    }
}
