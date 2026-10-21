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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.marcelapaz.mpazpr3.ui.pantallas.PantallaDetalle
import com.marcelapaz.mpazpr3.ui.pantallas.PantallaFormularioModulo
import com.marcelapaz.mpazpr3.ui.pantallas.PantallaInicio
import com.marcelapaz.mpazpr3.ui.pantallas.PantallaModulos
import com.marcelapaz.mpazpr3.ui.pantallas.PantallaPerfil
import com.marcelapaz.mpazpr3.datos.RolUsuario
import com.marcelapaz.mpazpr3.ui.modelovista.ModeloVistaMpaz

// Definicion de pantallas y rutas de navegacion (Cumple Guia 10: Navegacion y estructura visual)
sealed class PantallaNavegacion(val ruta: String, val titulo: String, val icono: ImageVector) {
    object Inicio : PantallaNavegacion("inicio", "Inicio", Icons.Default.Home)
    object Modulos : PantallaNavegacion("modulos", "Módulos", Icons.Default.School)
    object Perfil : PantallaNavegacion("perfil", "Perfil", Icons.Default.Person)
    object NuevoModulo : PantallaNavegacion("nuevoModulo", "Nuevo Módulo", Icons.Default.School)
    object Detalle : PantallaNavegacion("detalle/{idModulo}", "Detalle", Icons.Default.School) {
        fun crearRuta(idModulo: Int) = "detalle/$idModulo"
    }
}

// Grafo principal de navegacion y estructura visual con Scaffold y NavHost (Guia 2.2.4)
@Composable
fun GrafoNavegacionMpaz(modeloVista: ModeloVistaMpaz) {
    val controladorNav = rememberNavController()
    val modulos by modeloVista.modulosVisibles.collectAsStateWithLifecycle()
    val recursos by modeloVista.recursos.collectAsStateWithLifecycle()
    val perfil by modeloVista.perfil.collectAsStateWithLifecycle()
    val informe by modeloVista.informe.collectAsStateWithLifecycle()
    val textoBusqueda by modeloVista.textoBusqueda.collectAsStateWithLifecycle()
    val avatar by modeloVista.avatar.collectAsStateWithLifecycle()
    val contexto = LocalContext.current

    // El estudiante solo ve los módulos que la UTP ya autorizó (regla del caso 3.1)
    val modulosVisibles = remember(modulos, perfil.rolActual) {
        if (perfil.rolActual == RolUsuario.ESTUDIANTE) modulos.filter { it.aprobadoPorUtp } else modulos
    }

    val elementos = listOf(
        PantallaNavegacion.Inicio,
        PantallaNavegacion.Modulos,
        PantallaNavegacion.Perfil
    )

    val entradaActual by controladorNav.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    // Muestra la barra inferior solo en las pantallas principales
    val mostrarBarraInferior = rutaActual in elementos.map { val pantalla = it; pantalla.ruta }

    // Estructura visual principal utilizando Scaffold y NavigationBar (Guia 10)
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
            // Host de navegacion para gestionar las transiciones entre pantallas (Guia 2.2.4)
            NavHost(
                navController = controladorNav,
                startDestination = PantallaNavegacion.Inicio.ruta,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno)
            ) {
                // Ruta 1: Pantalla de Inicio / Dashboard
                composable(PantallaNavegacion.Inicio.ruta) {
                    PantallaInicio(
                        perfil = perfil,
                        modulos = modulosVisibles,
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

                // Ruta 2: Pantalla de Modulos y Asignaturas con funciones adaptadas por Rol (Seccion 3.1)
                composable(PantallaNavegacion.Modulos.ruta) {
                    PantallaModulos(
                        modulos = modulosVisibles,
                        rolActual = perfil.rolActual,
                        textoBusqueda = textoBusqueda,
                        alCambiarTextoBusqueda = { modeloVista.actualizarTextoBusqueda(it) },
                        alAutorizarModuloUtp = { id -> modeloVista.autorizarModuloUtp(id) },
                        alCambiarIntentosDocente = { id, intentos -> modeloVista.cambiarIntentosDocente(id, intentos) },
                        alCrearModulo = { controladorNav.navigate(PantallaNavegacion.NuevoModulo.ruta) },
                        alHacerClicModulo = { idModulo ->
                            controladorNav.navigate(PantallaNavegacion.Detalle.crearRuta(idModulo))
                        }
                    )
                }

                // Ruta 3: Pantalla de Perfil de Usuario y Roles (Estudiante, Docente, UTP)
                composable(PantallaNavegacion.Perfil.ruta) {
                    PantallaPerfil(
                        perfil = perfil,
                        modulos = modulos,
                        informe = informe,
                        alCambiarRol = { rol -> modeloVista.cambiarRolUsuario(rol) },
                        alAutorizarModulo = { id -> modeloVista.autorizarModuloUtp(id) },
                        avatar = avatar,
                        alElegirFoto = { uri -> modeloVista.guardarAvatar(contexto, uri) }
                    )
                }

                // Ruta 5: Formulario para crear un modulo nuevo (Docente / UTP)
                composable(PantallaNavegacion.NuevoModulo.ruta) {
                    // Protección: el estudiante no puede crear módulos aunque llegue a esta ruta
                    if (perfil.rolActual == RolUsuario.ESTUDIANTE) {
                        LaunchedEffect(Unit) { controladorNav.popBackStack() }
                    } else {
                        PantallaFormularioModulo(
                            alGuardar = { modulo -> modeloVista.agregarModulo(modulo) },
                            alVolver = { controladorNav.popBackStack() }
                        )
                    }
                }
                // Ruta 4: Pantalla de Detalle con paso de argumentos (idModulo)
                composable(
                    route = PantallaNavegacion.Detalle.ruta,
                    arguments = listOf(navArgument("idModulo") { type = NavType.IntType })
                ) { entradaPila ->
                    val idModulo = entradaPila.arguments?.getInt("idModulo") ?: 1
                    PantallaDetalle(
                        idModulo = idModulo,
                        modulos = modulosVisibles,
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

/*
GLOSARIO GENERAL COMPLETO DE KOTLIN, UI, ANDROID Y CURRICULO:
- val: Palabra clave en Kotlin para declarar una variable de solo lectura (constante).
- var: Palabra clave en Kotlin para declarar una variable cuyo valor puede cambiar.
- fun: Palabra clave para definir una funcion o bloque de codigo reutilizable.
- class: Plantilla para crear objetos que agrupan propiedades y funciones.
- object: Permite crear una clase de instancia unica (objeto estatico o repositorio).
- package: Define la carpeta o paquete logico donde se ubica el archivo de codigo.
- import: Permite utilizar clases, funciones o librerias ubicadas en otros paquetes.
- return: Devuelve un resultado al finalizar la ejecucion de una funcion.
- String: Tipo de dato que representa texto o cadenas de caracteres.
- Int: Tipo de dato que representa numeros enteros.
- Boolean: Tipo de dato logico que almacena verdadero o falso.
- List: Estructura de datos para almacenar una coleccion ordenada de elementos.
- Lambda: Funcion anonima que se puede pasar como parametro (ej. bloques de codigo entre llaves).
- If / Else: Estructura condicional para tomar decisiones segun si se cumple o no una condicion.
- When: Estructura de seleccion multiple util para comparar varios casos posibles.
- Scaffold: Estructura visual basica que organiza la pantalla con barra inferior y contenido.
- NavController: Controlador encargado de gestionar el movimiento entre pantallas.
- NavHost: Contenedor que muestra las pantallas segun la ruta activa actual.
- Ruta: Texto que identifica de forma unica a cada pantalla en la navegacion.
- Argumento: Dato que se envia de una pantalla a otra (por ejemplo, el id del modulo).
- NavigationBar: Barra inferior visible para cambiar de seccion facilmente.
- ViewModel (Modelo Vista): Componente que almacena y gestiona los datos y estados de la interfaz.
- StateFlow: Flujo de datos reactivo que actualiza la pantalla automaticamente cuando cambian los valores.
- Modulo de aprendizaje: Contenido educativo organizado por asignaturas, unidades y objetivos de aprendizaje.
- Recurso didactico: Material de apoyo como guias, videos, ejemplos y audios para la lectura autonoma.
- Rol UTP / Docente / Estudiante: Perfiles de usuario definidos en el caso para cumplir las tareas de autorizacion, asignacion y consulta.
- Racha de estudio: Indicador de dias consecutivos que el estudiante ingresa a repasar y completar actividades.
- MVP: Producto minimo variable desarrollado con fines academicos y datos ficticios.
- Jetpack Compose: Kit de herramientas moderno de Android para crear interfaces de usuario de forma declarativa.
- Material 3: Sistema de diseño oficial de Google que aporta componentes visuales modernos y accesibles.
- Composable: Funcion especial de Jetpack Compose que dibuja y construye elementos en la pantalla.
- LazyColumn: Lista vertical optimizada que carga elementos a medida que el usuario desliza la pantalla.
- Card: Tarjeta visual con bordes redondeados ideal para agrupar informacion relacionada.
- Surface: Superficie base para aplicar colores de fondo y elevacion visual.
- MutableStateFlow: Contenedor de datos reactivo que permite modificar su valor y notifica cambios en tiempo real.
- Remember: Funcion para recordar y conservar un valor mientras la pantalla sigue activa.
- Corrutinas: Tareas en segundo plano que permiten ejecutar procesos sin congelar la interfaz principal.
- Android Studio: Entorno de desarrollo oficial utilizado para programar y compilar aplicaciones Android.
- Gradle: Sistema de automatizacion de construccion que compila y empaqueta el proyecto.
- SDK: Kit de desarrollo de software que provee las herramientas necesarias para crear apps para Android.
- Manifest: Archivo de configuracion principal que declara actividades, nombre y ajustes generales de la app.
- APK: Paquete de instalacion ejecutable generado para dispositivos Android.
- OA (Objetivo de Aprendizaje): Meta curricular oficial definida por el Ministerio de Educacion para guiar el proceso educativo.
- UTP (Unidad Tecnico Pedagogica): Instancia encargada de revisar, validar y autorizar los contenidos educativos del establecimiento.
*/
