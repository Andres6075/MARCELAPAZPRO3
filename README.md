# APP ESCUELA MARCELA PAZ (MPAZ PRO 3)

Aplicación móvil Android de apoyo didáctico y seguimiento del aprendizaje para **Tercero Básico** de la Escuela Escritora Marcela Paz (San Bernardo). Es un proyecto académico funcional e integrado que reemplaza las guías impresas por módulos digitales organizados por Objetivos de Aprendizaje (OA), conectando componentes de Front-end en Jetpack Compose, almacenamiento local con Room, consumo de API REST Externa con Retrofit y un Microservicio propio en Spring Boot.

- **Asignatura:** DSY1105 - Desarrollo de Aplicaciones Móviles (Duoc UC)
- **Evaluaciones:** Evaluación Parcial 4 (Encargo 6%) y Evaluación Parcial 5 (Defensa Individual 24%)
- **Estudiante:** Andres Zuniga Saavedra
- **Planificación (Trello):** https://trello.com/b/DFTy2a06
- **Repositorio GitHub:** https://github.com/Andres6075/MARCELAPAZPRO3

---

## Caso que resuelve

La escuela trabaja con guías impresas, por lo que el avance por objetivo de aprendizaje queda repartido en distintos documentos. La app organiza una unidad por asignatura (Lenguaje y Comunicación, Matemática, Ciencias Naturales e Historia, Geografía y Cs. Sociales), conserva el progreso del estudiante y permite que la UTP autorice los contenidos antes de usarlos.

---

## Funcionalidades e Integraciones Implementadas

- **Inicio de Sesión y Registro:** Formularios con validación por campo (correo, contraseña, confirmación y perfil). Las contraseñas se gestionan con hash SHA-256 y sal aleatoria.
- **Tres Perfiles de Usuario:** Estudiante (consulta y avanza solo en módulos autorizados), Docente (crea módulos y ajusta intentos) y UTP (autoriza módulos y revisa el informe pedagógico).
- **Consumo de API Externa con Retrofit (Guía 14 & IE 3.1.4):** Integración en tiempo real con una API REST de citas pedagógicas e inspiración docente (`DummyJSON Quotes / ServicioApiExterna`), expuesta dinámicamente en la pantalla de inicio.
- **Microservicio propio en Spring Boot (IE 3.1.2 e IE 3.1.3):** Proyecto independiente en `backend-microservicio/` que expone endpoints REST con Spring Data JPA y base de datos H2 para operaciones CRUD completas sobre módulos y usuarios.
- **Persistencia Híbrida Local y Remota (Room + Retrofit):** Usuarios y módulos se sincronizan en SQLite mediante Room localmente y a través del microservicio Spring Boot.
- **Recursos Nativos:** Lectura en voz alta de explicaciones (`TextToSpeech`) y foto de perfil mediante el selector nativo del sistema (`PickVisualMedia`).
- **Animaciones y Material 3:** Transiciones dinámicas con `AnimatedVisibility`, `animateColorAsState` y `animateFloatAsState`.
- **Firma Digital de APK (IE 3.3.1):** Configuración de firma digital en Gradle con el archivo KeyStore `llave-firmada-mpaz.jks` y generación del APK firmado `app-release-firmado.apk`.

---

## Endpoints de la Aplicación y Microservicio

### 1. API Externa (Retrofit)
- **URL Base:** `https://dummyjson.com/`
- **GET** `quotes/random` - Obtiene una frase/cita pedagógica al azar para el área escolar.

### 2. Microservicio propio en Spring Boot
- **URL Base:** `http://localhost:8080/api/` (en emulador Android: `http://10.0.2.2:8080/api/`)
- **GET** `/api/modulos` - Obtiene la lista completa de módulos de aprendizaje.
- **POST** `/api/modulos` - Registra un nuevo módulo de aprendizaje desde la app.
- **PUT** `/api/modulos/{id}` - Actualiza el avance, estado o autorización UTP de un módulo.
- **DELETE** `/api/modulos/{id}` - Elimina un módulo de la base de datos.
- **GET** `/api/usuarios` - Obtiene la lista de usuarios registrados.

---

## Arquitectura (MVVM + Microservicios)

```
app/src/main/java/com/marcelapaz/mpazpr3/
├── datos/
│   ├── db/                Room: entidades, DAO y base de datos local (BaseDatosApp)
│   ├── modelos/           Clases de datos (Modelo.kt, Usuario.kt, CitaPedagogica.kt)
│   ├── red/               Capa de red Retrofit (ServicioApiExterna, ServicioMicroservicio, ClienteRetrofit)
│   ├── repositorios/      Repositorios de abstracción de datos (RepositorioDatos, RepositorioModulos, RepositorioApiExterna)
│   └── utilidades/        Seguridad (SHA-256/Sal) y AlmacenAvatar (imágenes)
├── ui/
│   ├── modelovista/       ViewModels con StateFlow (ModeloVistaMpaz, AutenticacionModeloVista)
│   ├── pantallas/         Pantallas en Jetpack Compose (Inicio, Módulos, Perfil, Formulario, Detalle)
│   ├── componentes/       Componentes reutilizables (BotonEscuchar, CamposFormulario)
│   └── navegacion/        NavHost y rutas de navegación
└── MainActivity.kt

backend-microservicio/     Proyecto Spring Boot independiente
├── pom.xml                Dependencias de Spring Web, Spring Data JPA y H2
└── src/main/java/com/marcelapaz/microservicio/
    ├── controlador/       ModuloController (/api/modulos) y UsuarioController (/api/usuarios)
    ├── modelo/            ModuloEntity y UsuarioEntity
    └── repositorio/       ModuloRepository y UsuarioRepository
```

---

## Pruebas Unitarias (Guía 15 & IE 3.2.1)

Pruebas unitarias en `app/src/pruebasUnitarias/java/com/marcelapaz/mpazpr3`:

- `ServicioMicroservicioPrueba`: Validación mockeada con **Mockk** de endpoints REST del microservicio (GET, POST, DELETE).
- `RepositorioApiExternaPrueba`: Validación mockeada de respuestas de Retrofit y resiliencia offline.
- `ValidadorModuloPrueba` y `ValidadorUsuarioPrueba`: Validación de reglas de negocio en formularios y cifrado.
- `FormularioModuloModeloVistaPrueba`: Validación de creación de módulos.
- `AutenticacionModeloVistaPrueba` y `ModeloVistaMpazPrueba`: Flujos de login, roles y progreso de aprendizaje.

### Ejecución de Pruebas
En Windows (PowerShell):
```powershell
.\gradlew testDebugUnitTest
```

---

## Firma Digital y Generación del APK (IE 3.3.1)

La aplicación cuenta con su configuración de firma digital en `app/build.gradle.kts` mediante el bloque `signingConfigs`:

- **Archivo KeyStore (.jks):** `llave-firmada-mpaz.jks` (ubicado en la raíz del proyecto).
- **Alias:** `mpazkey`
- **Algoritmo:** RSA 2048-bit (válido por 10.000 días)
- **Ejecutable firmado:** `app-release-firmado.apk` (disponible en la raíz del proyecto).

---

## Cómo Ejecutar el Proyecto

### 1. Ejecutar el Microservicio Backend (Spring Boot)
1. Abrir la carpeta `backend-microservicio` en una consola de comandos o IDE (IntelliJ / Eclipse / VS Code).
2. Ejecutar con Maven:
   ```bash
   mvn spring-boot:run
   ```
3. El microservicio quedará escuchando en `http://localhost:8080/api/modulos`.

### 2. Ejecutar la Aplicación Móvil (Android)
1. Abrir el proyecto en **Android Studio**.
2. Esperar a que finalice la sincronización de Gradle.
3. Conectar un emulador o teléfono Android físico.
4. Presionar **Run** (▶) para instalar y probar la app.

---

## Cuentas de Prueba

| Perfil | Correo | Contraseña |
|---|---|---|
| Estudiante | estudiante@escuelamarcelapaz.cl | Mpaz2026 |
| Docente | docente@escuelamarcelapaz.cl | Mpaz2026 |
| UTP | utp@escuelamarcelapaz.cl | Mpaz2026 |
