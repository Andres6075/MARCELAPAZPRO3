package com.marcelapaz.mpazpr3.datos

/*
  Aqui creamos nuestro repositorio de datos. Como este MVP no se conecta 
  a bases de datos externas ni plataformas institucionales (seccion 3.4 del caso),
  creamos una base de datos local ficticia. 
  Aqui guardamos los modulos de Tercero Basico para Lenguaje, Matematica, Ciencias e Historia, 
  junto con los recursos didacticos y el informe pedagogico de UTP.
*/
object RepositorioDatos {
    
    // Funcion que entrega la lista con las 4 asignaturas y sus objetivos curriculares
    fun obtenerModulos(): List<ModuloAprendizaje> {
        return listOf(
            ModuloAprendizaje(
                id = 1,
                titulo = "Comprension y Narrativa Literaria",
                descripcion = "Lectura guiada de textos breves, fabulas y cuentos con preguntas de comprension.",
                nivelEducativo = "Tercero Basico",
                asignatura = "Lenguaje y Comunicacion",
                unidad = "Unidad 1: Descubriendo Historias",
                objetivoAprendizaje = "OA 3: Leer y familiarizarse con un amplio repertorio de literatura.",
                codigoOficial = "OA-LENG-03-03",
                ejeCurricular = "Lectura",
                explicacion = "Lee atentamente la fabula y responde las alternativas segun las acciones de los personajes.",
                tieneAudioApoyo = true,
                intentosPermitidos = 3,
                intentosRealizados = 1,
                dificultad = "Media",
                progreso = 0.85f,
                estaCompletado = false,
                horasEstimadas = 6,
                aprobadoPorUtp = true
            ),
            ModuloAprendizaje(
                id = 2,
                titulo = "Numeros Hasta el 1000 y Adicion",
                descripcion = "Resolucion de problemas cotidianos utilizando la adicion y sustraccion con numeros de tres cifras.",
                nivelEducativo = "Tercero Basico",
                asignatura = "Matematica",
                unidad = "Unidad 1: Numeros y Operaciones",
                objetivoAprendizaje = "OA 4: Representar y describir numeros del 0 al 1000.",
                codigoOficial = "OA-MAT-03-04",
                ejeCurricular = "Numeros y Operaciones",
                explicacion = "Utiliza la tabla de valor posicional para sumar unidades, decenas y centenas.",
                tieneAudioApoyo = true,
                intentosPermitidos = 3,
                intentosRealizados = 2,
                dificultad = "Media",
                progreso = 0.50f,
                estaCompletado = false,
                horasEstimadas = 8,
                aprobadoPorUtp = true
            ),
            ModuloAprendizaje(
                id = 3,
                titulo = "Los Seres Vivos y sus Habitats",
                descripcion = "Exploracion de plantas y animales nativos de Chile, sus caracteristicas y necesidades.",
                nivelEducativo = "Tercero Basico",
                asignatura = "Ciencias Naturales",
                unidad = "Unidad 1: El Entorno Natural",
                objetivoAprendizaje = "OA 1: Observar y describir las caracteristicas de los animales nativos.",
                codigoOficial = "OA-CIE-03-01",
                ejeCurricular = "Ciencias de la Vida",
                explicacion = "Observa las imagenes de los animales y clasificalos segun su cubierta corporal y desplazamiento.",
                tieneAudioApoyo = false,
                intentosPermitidos = 2,
                intentosRealizados = 1,
                dificultad = "Baja",
                progreso = 0.30f,
                estaCompletado = false,
                horasEstimadas = 10,
                aprobadoPorUtp = true
            ),
            ModuloAprendizaje(
                id = 4,
                titulo = "Paisajes de Chile y Ubicacion",
                descripcion = "Reconocimiento de paisajes de la zona norte, centro y sur de Chile mediante mapas y recursos visuales.",
                nivelEducativo = "Tercero Basico",
                asignatura = "Historia, Geografia y Cs. Sociales",
                unidad = "Unidad 1: Descubriendo Paisajes de Chile",
                objetivoAprendizaje = "OA 8: Describir paisajes de Chile por medio de fuentes.",
                codigoOficial = "OA-HIS-03-08",
                ejeCurricular = "Geografia",
                explicacion = "Identifica las principales caracteristicas geograficas y climaticas de las zonas naturales.",
                tieneAudioApoyo = true,
                intentosPermitidos = 3,
                intentosRealizados = 3,
                dificultad = "Media",
                progreso = 1.0f,
                estaCompletado = true,
                horasEstimadas = 5,
                aprobadoPorUtp = true
            )
        )
    }

    // Funcion que entrega los recursos de apoyo con instrucciones y ejemplos (seccion 3.2)
    fun obtenerRecursos(): List<RecursoDidactico> {
        return listOf(
            RecursoDidactico(1, "Guia 9: Adaptabilidad del diseño", "Guia", "12 paginas", 2, "Lee la introduccion y responde los ejercicios.", "Ejemplo: Suma 345 + 123", true, true),
            RecursoDidactico(2, "Audio explicativo de operaciones", "Video", "15 min", 2, "Escucha atentamente el audio de apoyo antes de resolver.", "Ejemplo de llevadas en la adicion", true, false),
            RecursoDidactico(3, "Cuestionario de Autoevaluacion M1", "Cuestionario", "10 preguntas", 1, "Selecciona la alternativa correcta.", "Pregunta sobre la fabula leida", false, true),
            RecursoDidactico(4, "Lectura guiada: Flora nativa", "Lectura", "15 paginas", 3, "Revisa las imagenes de la flora chilena.", "Ejemplo de adaptacion de plantas", true, false),
            RecursoDidactico(5, "Taller de paisajes de Chile", "Guia", "20 paginas", 4, "Ubica las regiones en el mapa interactivo.", "Ejemplo de la zona norte de Chile", true, true)
        )
    }

    // Funcion que entrega los datos de prueba del estudiante con su identificador ficticio (seccion 3.2 y 5.1)
    fun obtenerPerfil(): PerfilEstudiante {
        return PerfilEstudiante(
            nombre = "Marcela Paz",
            correo = "marcela.paz@escuelamarcelapaz.cl",
            codigoEstudiante = "MP-3B-2026",
            modulosCompletadosTotal = 1,
            rachaDiasEstudio = 5,
            progresoGeneral = 0.66f,
            rolActual = RolUsuario.ESTUDIANTE
        )
    }

    // Funcion que entrega el informe pedagogico reservado para UTP y Docente (seccion 3.1 y 3.2)
    fun obtenerInformePedagogico(): InformePedagogico {
        return InformePedagogico(
            asignatura = "Tercero Basico (4 Asignaturas MVP)",
            rendimientoPromedio = "82% de logro general",
            fortalezas = "Excelente comprension de lectura y participacion en desafios matematicos.",
            contenidosAReforzar = "Reforzar unidades de Ciencias Naturales y medicion de tiempo.",
            fechaValidacion = "07-09-2026",
            estadoValidacion = "Validado y autorizado por UTP"
        )
    }
}

/*
  Glosario bien contundente de repositorio y gestion de datos:
  - Repositorio (Repository): Objeto o clase centralizada que provee los datos a la aplicacion.
  - Object: Declaracion en Kotlin para un objeto unico que no necesita instanciarse (patron Singleton).
  - List: Coleccion ordenada de elementos (en este caso, una lista de modulos).
  - MVP (Producto Minimo Viable): Version simplificada del software para validar la solucion escolar.
  - Datos ficticios: Informacion de prueba que no utiliza datos reales de alumnos para proteger su privacidad.
  - Currículum Nacional: Marco de referencia oficial del ministerio que usamos para los objetivos de aprendizaje.
  - Informe Pedagogico: Reporte reservado para docentes y UTP con rendimientos y areas a reforzar.
  - UTP: Unidad Tecnico Pedagogica que revisa y autoriza los contenidos educativos.
  - Asignatura: Materia escolar (Lenguaje, Matematica, Ciencias e Historia).
  - Unidad: Subdivision de la asignatura segun el plan curricular.
*/
