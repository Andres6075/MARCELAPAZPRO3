package com.marcelapaz.mpazpr3.datos

import android.content.Context
import com.marcelapaz.mpazpr3.datos.db.BaseDatosApp
import com.marcelapaz.mpazpr3.datos.db.UsuarioDao
import com.marcelapaz.mpazpr3.datos.db.UsuarioEntidad
import com.marcelapaz.mpazpr3.datos.db.aModeloDominio

// Resultado de intentar registrar un usuario nuevo
sealed class ResultadoRegistro {
    data class Exito(val usuario: Usuario) : ResultadoRegistro()
    object CorreoYaRegistrado : ResultadoRegistro()
}

/*
  Repositorio de usuarios: registro e inicio de sesion sobre Room.
  Aqui se aplica el hash de la contrasena, por lo que el ViewModel nunca la guarda ni la compara.
*/
class RepositorioUsuarios(private val usuarioDao: UsuarioDao) {

    // Crea las cuentas de prueba la primera vez que se abre la app
    suspend fun sembrarUsuariosDemo() {
        if (usuarioDao.contar() > 0) return
        registrar("Estudiante Demo", "estudiante@escuelamarcelapaz.cl", CLAVE_DEMO, RolUsuario.ESTUDIANTE)
        registrar("Docente Demo", "docente@escuelamarcelapaz.cl", CLAVE_DEMO, RolUsuario.DOCENTE)
        registrar("UTP Demo", "utp@escuelamarcelapaz.cl", CLAVE_DEMO, RolUsuario.UTP)
    }

    suspend fun registrar(nombre: String, correo: String, clave: String, rol: RolUsuario): ResultadoRegistro {
        val correoNormalizado = correo.trim().lowercase()
        if (usuarioDao.buscarPorCorreo(correoNormalizado) != null) {
            return ResultadoRegistro.CorreoYaRegistrado
        }
        val sal = Seguridad.generarSal()
        val entidad = UsuarioEntidad(
            nombre = nombre.trim(),
            correo = correoNormalizado,
            sal = sal,
            claveHash = Seguridad.calcularHash(clave, sal),
            rol = rol.name
        )
        val idGenerado = usuarioDao.insertar(entidad).toInt()
        return ResultadoRegistro.Exito(entidad.copy(id = idGenerado).aModeloDominio())
    }

    // Devuelve el usuario si el correo existe y la contrasena coincide; si no, null
    suspend fun iniciarSesion(correo: String, clave: String): Usuario? {
        val entidad = usuarioDao.buscarPorCorreo(correo.trim().lowercase()) ?: return null
        return if (Seguridad.verificar(clave, entidad.sal, entidad.claveHash)) {
            entidad.aModeloDominio()
        } else {
            null
        }
    }

    companion object {
        // Contrasena de las cuentas de prueba (datos ficticios)
        const val CLAVE_DEMO = "Mpaz2026"

        fun crear(contexto: Context): RepositorioUsuarios =
            RepositorioUsuarios(BaseDatosApp.obtenerBaseDatos(contexto).usuarioDao())
    }
}
