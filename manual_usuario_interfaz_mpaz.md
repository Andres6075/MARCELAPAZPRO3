Manual de funcionamiento de la interfaz y elementos de la aplicacion MPAZ PRO 3

Este manual explica en detalle el funcionamiento de cada pantalla, componente, tarjeta y boton de la aplicacion movil MPAZ PRO 3.

1. Estructura general de la interfaz
La aplicacion utiliza una estructura visual basada en Material 3 y Jetpack Compose. En la parte inferior de la pantalla se mantiene siempre visible una barra de navegacion (NavigationBar) con tres botones principales: Inicio, Modulos y Perfil.

2. Pantalla de Inicio (Dashboard principal)
- Encabezado superior: Muestra la identificacion del colegio (Escuela Escritora Marcela Paz | 3° Basico) y un icono distintivo escolar.
- Tarjeta de Condiciones de Uso (Seccion 3.3): Tarjeta informativa que explica que la aplicacion esta optimizada para Android, conserva el progreso y no utiliza permisos invasivos como camara, GPS, microfono ni biometria.
- Tarjeta Principal "Mi Progreso de Aprendizaje": Muestra el porcentaje de avance general mediante una barra de progreso lineal (LinearProgressIndicator) y el conteo de modulos completados en relacion al total.
- Fila de Estadisticas Rapidas:
  a) Tarjeta "Racha de Estudio": Indica los dias consecutivos que el estudiante ha ingresado a repasar.
  b) Tarjeta "Modulos Activos": Muestra la cantidad total de asignaturas activas en el periodo.
- Seccion "Modulos y Asignaturas": Muestra un resumen de los primeros modulos. Incluye un boton "Ver todos" que al presionarlo navega automaticamente a la pestaña de Modulos.

3. Pantalla de Modulos (Asignaturas y Busqueda)
- Titulo principal: "Asignaturas y Unidades (3.2)".
- Buscador interactivo (OutlinedTextField): Permite escribir texto para filtrar los modulos en tiempo real por nombre de asignatura, unidad, codigo oficial OA o palabra clave.
- Tarjetas de Modulo (TarjetaModuloResumen):
  a) Muestra la etiqueta con la asignatura (Lenguaje, Matematica, Ciencias o Historia).
  b) Muestra el titulo del tema, la unidad y el codigo oficial de Objetivo de Aprendizaje (ejemplo: OA-LENG-03-03).
  c) Muestra un circulo de progreso (CircularProgressIndicator) o un icono verde de verificado cuando el modulo esta completado.
  d) Funcion del clic: Al presionar cualquier tarjeta, la aplicacion navega hacia la Pantalla de Detalle de ese modulo especifico.

4. Pantalla de Detalle del Modulo (Informacion Curricular y Avance)
- Barra superior (TopAppBar): Incluye el titulo "Detalle Curricular del Modulo" y el boton con icono de flecha hacia la izquierda ("Volver") para regresar a la pantalla anterior.
- Etiquetas superiores: Muestra la asignatura y la unidad a la que pertenece el tema.
- Titulo y Descripcion: Muestra el nombre completo del tema y la explicacion pedagocica.
- Tarjeta de Informacion Curricular (Seccion 3.2): Detalla el Nivel Educativo (Tercero Basico), el Objetivo de Aprendizaje (OA), el Codigo Oficial, el Eje Curricular, la Dificultad y la cantidad de Intentos Realizados / Permitidos.
- Explicacion e Instrucciones: Entrega la instruccion de lectura y muestra un indicador con icono de audio si el modulo cuenta con audio de apoyo para la lectura autonoma.
- Tarjeta Interactiva de Avance y Persistencia:
  a) Muestra el porcentaje de avance en texto.
  b) Control Deslizante (Slider): Permite al usuario deslizar con el dedo para ajustar el porcentaje de avance de 0% a 100% de forma dinamica.
  c) Boton "Marcar como Completado": Al presionarlo, cambia automaticamente el estado del modulo a completado (100%) o lo regresa a pendiente, guardando los cambios de forma permanente en la base de datos local Room.
- Seccion de Recursos Didacticos: Muestra tarjetas con guias, videos, lecturas y cuestionarios asociados, incluyendo sus instrucciones, ejemplos y badge de "Disponible".

5. Pantalla de Perfil de Usuario y Gestion de Roles (Seccion 3.1)
- Avatar / Icono de Rol: Muestra un icono dinamico que cambia segun el rol seleccionado (estudiante, docente o UTP).
- Nombre del Usuario: Muestra el nombre registrado ("Marcela Paz").
- Selector de Perfiles (ExposedDropdownMenuBox): Despliega un menu que permite cambiar entre los tres roles del caso de estudio:
  a) Rol Estudiante: Muestra el identificador ficticio (MP-3B-2026), colegio, nivel y un panel informativo aclarando que el estudiante consulta materiales pero no modifica contenidos ni accede al informe reservado.
  b) Rol Docente: Muestra el panel del docente de Tercero Basico para la asignacion de contenidos, definicion de intentos maximos y revision de resultados.
  c) Rol UTP: Despliega la herramienta para autorizar modulos mediante botones interactivos ("Autorizado / Pendiente") y muestra el Informe Pedagogico Reservado con rendimiento promedio, fortalezas, contenidos a reforzar y estado de validacion por UTP.

6. Barra de Navegacion Inferior (NavigationBar)
- Boton "Inicio": Icono de casa que regresa al Dashboard principal.
- Boton "Modulos": Icono de escuela que abre la lista completa de asignaturas y buscador.
- Boton "Perfil": Icono de persona que abre la pantalla de usuario y selector de roles.
