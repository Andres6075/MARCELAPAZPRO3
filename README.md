# APP ESCUELA MARCELA PAZ (MPAZ PRO 3)

Aplicación móvil Android de apoyo didáctico y seguimiento del aprendizaje para **Tercero Básico** de la Escuela Escritora Marcela Paz (San Bernardo). Es un MVP académico que reemplaza las guías impresas por módulos digitales organizados por Objetivos de Aprendizaje (OA).

- **Asignatura:** DSY1105 - Desarrollo de Aplicaciones Móviles (Duoc UC)
- **Evaluación:** Parcial 2 - Encargo
- **Estudiante:** Andres Zuniga Saavedra

## Caso que resuelve

La escuela trabaja con guías impresas, por lo que el avance por objetivo de aprendizaje queda repartido en distintos documentos. La app organiza una unidad por asignatura (Lenguaje y Comunicación, Matemática, Ciencias Naturales e Historia, Geografía y Cs. Sociales), conserva el progreso del estudiante y permite que la UTP autorice los contenidos antes de usarlos. Todos los datos (usuarios, módulos, resultados) son ficticios y no hay conexión con sistemas reales.

## Funcionalidades implementadas

- **Tres perfiles con permisos distintos:** Estudiante (consulta y avanza solo en los módulos autorizados por la UTP, sin modificar contenidos ni ver el informe), Docente (asigna contenidos y define intentos) y UTP (autoriza módulos y accede al informe pedagógico reservado).
- **Módulos y asignaturas:** lista con buscador en tiempo real por asignatura, unidad, código OA o palabra clave.
- **Detalle del módulo:** información curricular, recursos didácticos, avance ajustable con deslizador y botón para marcar como completado.
- **Formulario "Nuevo módulo" (Docente y UTP):** seis campos con validación por campo, mensaje de error e ícono de error. El módulo se guarda como pendiente de autorización de la UTP.
- **Persistencia local con Room:** el avance, los módulos nuevos y las autorizaciones se conservan al cerrar la app.
- **Recursos nativos:** lectura en voz alta de la explicación (TextToSpeech) y foto de perfil con el selector de fotos del sistema.
- **Animaciones:** mensaje de éxito con `AnimatedVisibility`, color del botón con `animateColorAsState` y barras de progreso con `animateFloatAsState`.
- **Navegación:** `Scaffold` con `NavigationBar`, `NavHost` y paso de argumento (`idModulo`) a la pantalla de detalle.

## Arquitectura (MVVM)

```
app/src/main/java/com/marcelapaz/mpazpr3/
├── datos/            Modelos, repositorio de datos de prueba y AlmacenAvatar
│   └── db/           Room: entidad, DAO y base de datos
├── ui/
│   ├── modelovista/  ViewModels con StateFlow y ValidadorModulo (lógica de validación)
│   ├── pantallas/    Pantallas en Jetpack Compose
│   ├── componentes/  Componentes reutilizables (BotonEscuchar)
│   ├── navegacion/   Rutas y NavHost
│   └── tema/         Colores, tipografía y tema Material 3
└── MainActivity.kt
```

- **Model (`datos/`):** clases de datos y acceso a Room.
- **ViewModel (`ui/modelovista/`):** mantiene el estado con `StateFlow` y contiene la lógica. Las validaciones del formulario están en `ValidadorModulo`, sin depender de Compose.
- **View (`ui/pantallas/`):** solo dibuja el estado y envía eventos al ViewModel.

## Tecnologías

Kotlin, Jetpack Compose, Material 3, Navigation Compose, Room con KSP, ViewModel y StateFlow, TextToSpeech de Android y Photo Picker (`ActivityResultContracts.PickVisualMedia`).

## Recursos nativos y permisos

| Recurso | Uso | Permisos |
|---|---|---|
| TextToSpeech | Lee en voz alta la explicación del módulo | Ninguno. Solo declara `<queries>` en el Manifest para encontrar el motor de voz (Android 11 o superior) |
| Selector de fotos | Foto de perfil, copiada al almacenamiento interno de la app | Ninguno |

La app no usa cámara, GPS, micrófono ni datos biométricos, según las condiciones del caso.

## Pruebas

Pruebas unitarias del validador del formulario en `app/src/pruebasUnitarias`:

```
./gradlew testDebugUnitTest
```

## Cómo ejecutar

1. Instalar una versión reciente de **Android Studio** (compatible con Android Gradle Plugin 9.4.1) y el SDK de Android 37.
2. Clonar el repositorio y abrir la carpeta del proyecto en Android Studio.
3. Esperar a que termine **Sync Project with Gradle Files**.
4. Conectar un teléfono Android con depuración USB (recomendado, sobre todo para la voz) o crear un emulador en **Device Manager** con una imagen que incluya Google Play. Requiere Android 7.0 (API 24) o superior.
5. Presionar **Run** (▶).

## Control de versiones

El historial de commits describe cada cambio técnico (formulario con validación, recursos nativos, animaciones y ajustes de interfaz).
