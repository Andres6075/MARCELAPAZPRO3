package com.marcelapaz.mpazpr3.ui.navegacion

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.marcelapaz.mpazpr3.ui.pantallas.PantallaDetalle
import com.marcelapaz.mpazpr3.ui.pantallas.PantallaInicio
import com.marcelapaz.mpazpr3.ui.pantallas.PantallaModulos
import com.marcelapaz.mpazpr3.ui.pantallas.PantallaPerfil
import com.marcelapaz.mpazpr3.ui.modelovista.ModeloVistaMpaz

sealed class PantallaNavegacion(val ruta: String, val titulo: String, val icono: ImageVector) {
    object Inicio : PantallaNavegacion("inicio", "Inicio", Icons.Default.Home)
    object Modulos : PantallaNavegacion("modulos", "Modulos", Icons.Default.School)
    object Perfil : PantallaNavegacion("perfil", "Perfil", Icons.Default.Person)
    object Detalle : PantallaNavegacion("detalle/{idModulo}", "Detalle", Icons.Default.School) {
        fun crearRuta(idModulo: Int) = "detalle/$idModulo"
    }
}

@Composable
fun GrafoNavegacionMpaz(modeloVista: ModeloVistaMpaz) {
    val controladorNav = rememberNavController()
    val modulos by modeloVista.modulos.collectAsStateWithLifecycle()
    val recursos by modeloVista.recursos.collectAsStateWithLifecycle()
    val perfil by modeloVista.perfil.collectAsStateWithLifecycle()
    val informe by modeloVista.informe.collectAsStateWithLifecycle()
    val textoBusqueda by modeloVista.textoBusqueda.collectAsStateWithLifecycle()

    val elementos = listOf(
        PantallaNavegacion.Inicio,
        PantallaNavegacion.Modulos,
        PantallaNavegacion.Perfil
    )

    val entradaActual by controladorNav.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    val mostrarBarraInferior = rutaActual in elementos.map { val pantalla = it; pantalla.ruta }

    Scaffold(
        bottomBar = {
            if (mostrarBarraInferior) {
                NavigationBar {
                    elementos.forEach { pantalla ->
                        NavigationBarItem(
                            icon = { Icon(pantalla.icono, contentDescription = pantalla.titulo) },
                            label = { Text(pantalla.titulo) },
                            selected = rutaActual == pantalla.ruta,
                            onClick = {
                                if (rutaActual != pantalla.ruta) {
                                    controladorNav.navigate(pantalla.ruta) {
                                        popUpTo(controladorNav.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        },
        content = { rellenoInterno ->
            NavHost(
                navController = controladorNav,
                startDestination = PantallaNavegacion.Inicio.ruta,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno)
            ) {
                composable(PantallaNavegacion.Inicio.ruta) {
                    PantallaInicio(
                        perfil = perfil,
                        modulos = modulos,
                        alHacerClicModulo = { idModulo ->
                            controladorNav.navigate(PantallaNavegacion.Detalle.crearRuta(idModulo))
                        },
                        alIrAModulos = {
                            controladorNav.navigate(PantallaNavegacion.Modulos.ruta) {
                                popUpTo(controladorNav.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                composable(PantallaNavegacion.Modulos.ruta) {
                    PantallaModulos(
                        modulos = modulos,
                        textoBusqueda = textoBusqueda,
                        alCambiarTextoBusqueda = { modeloVista.actualizarTextoBusqueda(it) },
                        alHacerClicModulo = { idModulo ->
                            controladorNav.navigate(PantallaNavegacion.Detalle.crearRuta(idModulo))
                        }
                    )
                }

                composable(PantallaNavegacion.Perfil.ruta) {
                    PantallaPerfil(
                        perfil = perfil,
                        modulos = modulos,
                        informe = informe,
                        alCambiarRol = { rol -> modeloVista.cambiarRolUsuario(rol) },
                        alAutorizarModulo = { id -> modeloVista.autorizarModuloUtp(id) }
                    )
                }

                composable(
                    route = PantallaNavegacion.Detalle.ruta,
                    arguments = listOf(navArgument("idModulo") { type = NavType.IntType })
                ) { entradaPila ->
                    val idModulo = entradaPila.arguments?.getInt("idModulo") ?: 1
                    PantallaDetalle(
                        idModulo = idModulo,
                        modulos = modulos,
                        recursos = recursos,
                        alCambiarEstado = { id -> modeloVista.cambiarEstadoModulo(id) },
                        alActualizarProgreso = { id, progreso -> modeloVista.actualizarProgresoModulo(id, progreso) },
                        alVolver = { controladorNav.popBackStack() }
                    )
                }
            }
        }
    )
}
