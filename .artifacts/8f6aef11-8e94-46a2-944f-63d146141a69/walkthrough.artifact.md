Resumen de implementacion y cumplimiento del caso MPAZ PRO 3 para DSY1105

Este proyecto cumple con los requerimientos establecidos en el documento oficial de Duoc UC (Formato de Caso para Docentes y Estudiantes - Desarrollo de Aplicaciones Moviles). A continuacion se detallan los puntos del caso que se resuelven e implementan en esta aplicacion:

1. Contexto de la organizacion y proposito del caso
- La aplicacion esta diseñada para responder al desafio de la Escuela Escritora Marcela Paz de San Bernardo, ofreciendo un apoyo didactico y seguimiento del aprendizaje enfocado en Tercero Basico.
- Organiza los contenidos y modulos educativos facilitando el acceso a las asignaturas y permitiendo registrar el avance de los estudiantes de manera digital en lugar de usar solo guias impresas.

2. Tareas y funciones cumplidas por perfiles (Seccion 3.1)
- Estudiante: Consulta material asignado, desarrolla actividades, revisa respuestas y avance.
- Docente: Gestiona asignacion de contenidos y definicion de intentos para evaluaciones.
- UTP: Revisa y autoriza contenidos pedagogicos y accede al informe pedagogico reservado.

3. Informacion curricular y proceso (Seccion 3.2)
- Nivel educativo, asignaturas (Lenguaje, Matematica, Ciencias Naturales, Historia), unidades y Objetivos de Aprendizaje (OA) con codigos oficiales.
- Recursos didacticos con instrucciones, ejemplos y soporte para audios de apoyo.

4. Condiciones de uso y restricciones (Secciones 3.3 y 3.4)
- Interfaz adaptada para Tercero Basico mediante Jetpack Compose y Material 3, sin requerir camara, GPS, microfono ni biometria.
- MVP independiente con datos ficticios basados en el Curriculum Nacional, sin conexion a sistemas reales ni IA externa.
- Estructura visual y navegacion implementadas segun la Guia 10 (Scaffold, NavHost, NavigationBar y paso de argumentos).

Glosario de terminos:
- MVP: Producto minimo viable, una version inicial del software con las caracteristicas esenciales para probar la propuesta.
- Jetpack Compose: Kit de herramientas moderno de Android para crear interfaces de usuario de forma declarativa.
- Material 3: Sistema de diseño oficial de Google para crear interfaces atractivas y accesibles.
- ViewModel y StateFlow: Componentes encargados de gestionar y mantener el estado de la aplicacion de manera reactiva.
- Scaffold: Estructura visual basica de una pantalla que incluye barras superiores, inferiores y contenido.
- NavHost y NavController: Componentes de navegacion encargados de gestionar las rutas y el movimiento entre pantallas.
- OA (Objetivo de Aprendizaje): Meta curricular oficial definida por el Ministerio de Educacion para guiar el proceso educativo.
- UTP (Unidad Tecnico Pedagogica): Instancia encargada de revisar, validar y autorizar los contenidos educativos del establecimiento.
- Racha de estudio: Indicador que muestra los dias consecutivos que el estudiante ha ingresado a repasar y estudiar.
