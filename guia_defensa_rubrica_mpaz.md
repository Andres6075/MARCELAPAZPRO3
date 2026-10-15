Guia de explicacion y defensa del proyecto MPAZ PRO 3 para rubricas y formularios (DSY1105)

Este documento resume como se aplica y como debes explicar cada punto del encargo y la rubrica de evaluacion de DSY1105 en tu proyecto.

1. Identificacion del proyecto y proposito del caso
- Pregunta o rubrica: ¿De que trata el proyecto y para quien fue desarrollado?
- Explicacion: El proyecto se llama MPAZ PRO 3 y es una aplicacion movil de apoyo didactico y seguimiento del aprendizaje desarrollada para la Escuela Escritora Marcela Paz de San Bernardo. Su objetivo es reemplazar las guias de papel impresas por una experiencia digital interactiva organizada por objetivos de aprendizaje para Tercero Basico.

2. Cobertura curricular de las asignaturas (Seccion 3.2)
- Pregunta o rubrica: ¿Como se representan las asignaturas y los Objetivos de Aprendizaje (OA)?
- Explicacion: El proyecto cubre las cuatro asignaturas requeridas para Tercero Basico: Lenguaje y Comunicacion, Matematica, Ciencias Naturales e Historia, Geografia y Cs. Sociales. En el archivo Repositorio.kt y Modelo.kt, cada modulo incluye la unidad, el Objetivo de Aprendizaje oficial (OA), el codigo oficial (ejemplo: OA-LENG-03-03), el eje curricular, explicaciones, audios de apoyo e instrucciones.

3. Gestion de perfiles de usuario y roles (Seccion 3.1)
- Pregunta o rubrica: ¿Como se manejan los perfiles de usuario (Estudiante, Docente y UTP)?
- Explicacion: En PantallaPerfil.kt se implemento un selector dinamico de roles.
  a) Estudiante: Consulta materiales, realiza actividades y monitorea su avance y racha de estudio sin poder modificar contenidos.
  b) Docente: Asigna contenidos, revisa resultados y gestiona intentos permitidos.
  c) UTP: Revisa y autoriza los modulos educativos y accede al Informe Pedagogico reservado con los resultados generales.

4. Condiciones de uso e interfaz apropiada (Seccion 3.3)
- Pregunta o rubrica: ¿Como se adapto la aplicacion para estudiantes de Tercero Basico?
- Explicacion: Se utilizo Jetpack Compose y Material 3 para crear una interfaz limpia con botones visibles, tarjetas claras, textos legibles y navegacion sencilla con una barra inferior (NavigationBar). No se solicitan permisos de camara, GPS, microfono ni datos biometricos, respetando la privacidad del estudiante.

5. Persistencia de datos local con Room y KSP (Secciones 3.4 y Guia de Integracion)
- Pregunta o rubrica: ¿Como se conserva el progreso y como funciona la base de datos?
- Explicacion: La aplicacion es un MVP independiente que trabaja con datos de prueba locales. Para conservar el progreso de forma permanente sin perder la informacion al cerrar la app, se integro Room Database (SQLite) usando el procesador KSP (Kotlin Symbol Processing). Los archivos principales son ModuloEntidad.kt, ModuloDao.kt y BaseDatosApp.kt.

6. Estructura visual y navegacion entre pantallas (Guia 10 - 2.2.4)
- Pregunta o rubrica: ¿Como esta organizada la navegacion del proyecto?
- Explicacion: En el archivo GrafoNavegacion.kt se utilizo un Scaffold con NavigationBar para la barra inferior, junto con NavHost y NavController para gestionar el cambio entre la Pantalla de Inicio, la Pantalla de Modulos, la Pantalla de Perfil y la Pantalla de Detalle (esta ultima recibe como argumento el identificador idModulo).

7. Arquitectura del codigo y patrones de diseño
- Pregunta o rubrica: ¿Que arquitectura utiliza el proyecto?
- Explicacion: Sigue la arquitectura oficial recomendada por Android (Clean Architecture en capas):
  a) Capa de datos (datos/): Modelo.kt, Repositorio.kt y carpeta db/ con Room.
  b) Capa de logica y estado (ui/modelovista/): ModeloVista.kt con StateFlow y corrutinas.
  c) Capa de interfaz grafica (ui/pantallas/ y ui/navegacion/): Pantallas declarativas en Jetpack Compose.

Resumen rapido para defender en la presentacion:
- Aplicacion Android nativa con Jetpack Compose y Material 3.
- Cobertura de 4 asignaturas de Tercero Basico con codigos oficiales de OA.
- Soporte para 3 roles (Estudiante, Docente, UTP) con autorizacion de modulos e informe pedagogico.
- Persistencia de datos local con Room Database y KSP.
- Codigo limpio, probado, comentado en espanol y alojado en GitHub.
