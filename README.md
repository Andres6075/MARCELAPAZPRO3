# APP ESCUELA MARCELA PAZ (MPAZ PRO 3)

Aplicación móvil Android de apoyo didáctico y seguimiento del aprendizaje para **Tercero Básico** de la Escuela Escritora Marcela Paz (San Bernardo). Es un MVP académico que reemplaza las guías impresas por módulos digitales organizados por Objetivos de Aprendizaje (OA).

- **Asignatura:** DSY1105 - Desarrollo de Aplicaciones Móviles (Duoc UC)
- **Evaluaciones:** Parcial 2 (Encargo) y Parcial 3 (Presentación/defensa)
- **Estudiante:** Andres Zuniga Saavedra
- **Modalidad:** trabajo individual [COMPLETAR: autorizado por el/la docente NOMBRE]
- **Planificación (Trello):** https://trello.com/b/DFTy2a06

## Caso que resuelve

La escuela trabaja con guías impresas, por lo que el avance por objetivo de aprendizaje queda repartido en distintos documentos. La app organiza una unidad por asignatura (Lenguaje y Comunicación, Matemática, Ciencias Naturales e Historia, Geografía y Cs. Sociales), conserva el progreso del estudiante y permite que la UTP autorice los contenidos antes de usarlos. Todos los datos (usuarios, módulos, resultados) son ficticios y no hay conexión con sistemas reales.

## Funcionalidades implementadas

- **Inicio de sesión y registro:** dos formularios con validación por campo (correo, contraseña, confirmación y perfil), íconos y mensajes de error. Las contraseñas se guardan con hash SHA-256 y sal aleatoria en Room, y el perfil de la sesión define los permisos.
- **Tres perfiles con permisos distintos:** Estudiante (consulta y avanza solo en los módulos autorizados por la UTP, sin modificar contenidos ni ver el informe), Docente (asigna contenidos y define intentos) y UTP (autoriza módulos y accede al informe pedagógico reservado).
- **Módulos y asignaturas:** lista con buscador en tiempo real por asignatura, unidad, código OA o palabra clave.
- **Detalle del módulo:** información curricular, recursos didácticos, avance ajustable con deslizador (se guarda al soltarlo) y botón para marcar como completado.
- **Formulario "Nuevo módulo" (Docente y UTP):** seis campos con validación por campo, mensaje de error e ícono de error. El módulo se guarda como pendiente de autorización de la UTP.
- **Persistencia local con Room:** usuarios, módulos, avance y autorizaciones se conservan al cerrar la app.
- **Recursos nativos:** lectura en voz alta de la explicación (TextToSpeech) y foto de perfil con el selector de fotos del sistema.
- **Animaciones:** mensaje de éxito con `AnimatedVisibility`, color de los botones con `animateColorAsState` y barras de progreso con `animateFloatAsState`.
- **Navegación:** `Scaffold` con `NavigationBar`, `NavHost`, pantallas de acceso (login y registro) y paso de argumento (`idModulo`) a la pantalla de detalle.
- **Textos en `strings.xml`:** los textos de las pantallas están fuera del código.

## Cuentas de prueba

Al abrir la app por primera vez se crean tres cuentas ficticias (contraseña `Mpaz2026`):

| Perfil | Correo |
|---|---|
| Estudiante | estudiante@escuelamarcelapaz.cl |
| Docente | docente@escuelamarcelapaz.cl |
| UTP | utp@escuelamarcelapaz.cl |

También se puede crear una cuenta nueva desde la pantalla de registro.

## Arquitectura (MVVM)

```
app/src/main/java/com/marcelapaz/mpazpr3/
├── datos/                 Modelos, repositorios y utilidades de datos
│   ├── Modelo.kt              Clases de datos (módulo, perfil, rol, informe)
│   ├── Repositorio.kt         Datos de prueba en memoria
│   ├── RepositorioModulos.kt  Acceso a Room para los módulos
│   ├── RepositorioUsuarios.kt Registro e inicio de sesión sobre Room
│   ├── Seguridad.kt           Hash SHA-256 con sal
│   ├── AlmacenAvatar.kt       Foto de perfil en almacenamiento interno
│   └── db/                    Room: entidades, DAO y base de datos
├── ui/
│   ├── modelovista/       ViewModels (StateFlow) y validadores
│   ├── pantallas/         Pantallas en Jetpack Compose
│   ├── componentes/       Componentes reutilizables (BotonEscuchar, CamposFormulario)
│   ├── navegacion/        Rutas y NavHost
│   └── tema/              Colores, tipografía y tema Material 3
└── MainActivity.kt
```

- **Model (`datos/`):** clases de datos y entidades de Room.
- **Repository (`RepositorioModulos`, `RepositorioUsuarios`):** únicos puntos de acceso a Room. Los ViewModel no conocen el DAO.
- **ViewModel (`ui/modelovista/`):** mantiene el estado con `StateFlow` y contiene la lógica. Las validaciones están en `ValidadorModulo` y `ValidadorUsuario`, sin depender de Compose.
- **View (`ui/pantallas/`):** solo dibuja el estado y envía eventos al ViewModel.

## Tecnologías

Kotlin, Jetpack Compose, Material 3, Navigation Compose, Room con KSP, ViewModel y StateFlow, TextToSpeech de Android, Photo Picker (`ActivityResultContracts.PickVisualMedia`), JUnit 4 y `kotlinx-coroutines-test`.

## Recursos nativos y permisos

| Recurso | Uso | Permisos |
|---|---|---|
| TextToSpeech | Lee en voz alta la explicación del módulo | Ninguno. Solo declara `<queries>` en el Manifest para encontrar el motor de voz (Android 11 o superior) |
| Selector de fotos | Foto de perfil, copiada al almacenamiento interno de la app | Ninguno |

La app no usa cámara, GPS, micrófono ni datos biométricos, según las condiciones del caso.

## Pruebas

Pruebas unitarias en `app/src/pruebasUnitarias`:

- `ValidadorModuloPrueba` y `ValidadorUsuarioPrueba`: reglas de validación y hash de contraseñas.
- `FormularioModuloModeloVistaPrueba`: estado y envío del formulario "Nuevo módulo".
- `AutenticacionModeloVistaPrueba`: validación de login y registro.
- `ModeloVistaMpazPrueba`: filtro de módulos por rol, avance y progreso general.

```
./gradlew testDebugUnitTest
```

En Windows (PowerShell): `.\gradlew testDebugUnitTest`.

## Cómo ejecutar

1. Instalar una versión reciente de **Android Studio** (compatible con Android Gradle Plugin 9.4.1) y el SDK de Android 37.
2. Clonar el repositorio y abrir la carpeta del proyecto en Android Studio.
3. Esperar a que termine **Sync Project with Gradle Files** (requiere conexión a internet la primera vez).
4. Conectar un teléfono Android con depuración USB (recomendado, sobre todo para la voz) o crear un emulador en **Device Manager** con una imagen que incluya Google Play. Requiere Android 7.0 (API 24) o superior.
5. Presionar **Run** (▶) e ingresar con una de las cuentas de prueba.

## Control de versiones y planificación

- **GitHub:** repositorio público con commits por cada cambio técnico (formulario con validación, recursos nativos, animaciones, repositorios de Room, autenticación, textos en `strings.xml` y pruebas).
- **Trello:** tablero con las listas *Por hacer*, *En progreso* y *Hecho* y la distribución de las tareas: https://trello.com/b/DFTy2a06

## Limitaciones conocidas

- La sesión no se conserva al cerrar la app.
- El registro permite elegir cualquier perfil, incluido UTP (aceptable para un prototipo con datos ficticios).
- El hash SHA-256 con sal basta para este prototipo; en producción se usaría un algoritmo pensado para contraseñas (como PBKDF2 o bcrypt).
- Los mensajes de error de los validadores están en el código, no en `strings.xml`.
- Aún no hay actividades ni cuestionarios para el estudiante (el contador de intentos no avanza).
- Al cambiar la versión de la base de datos, Room la recrea y se pierden los datos locales.
