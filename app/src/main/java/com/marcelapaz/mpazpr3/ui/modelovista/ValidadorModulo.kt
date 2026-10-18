package com.marcelapaz.mpazpr3.ui.modelovista

/*
  Logica de validacion del formulario "Nuevo modulo".
  Son funciones puras: no dependen de Compose ni de Android, por eso se pueden
  probar con una prueba unitaria y la pantalla solo muestra el mensaje que devuelven.
  Cada funcion retorna null si el campo es valido, o el texto del error si no lo es.
*/
object ValidadorModulo {

    val asignaturasPermitidas = listOf(
        "Lenguaje y Comunicacion",
        "Matematica",
        "Ciencias Naturales",
        "Historia, Geografia y Cs. Sociales"
    )

    // Formato oficial usado en la app, ejemplo: OA-LENG-03-03 u OA-MAT-03-04
    private val formatoCodigoOa = Regex("^OA-(LENG|MAT|CIE|HIS)-\\d{2}-\\d{2}$")

    fun validarTitulo(titulo: String): String? = when {
        titulo.isBlank() -> "El titulo es obligatorio"
        titulo.trim().length < 5 -> "Debe tener al menos 5 caracteres"
        titulo.trim().length > 60 -> "Maximo 60 caracteres"
        else -> null
    }

    fun validarAsignatura(asignatura: String): String? = when {
        asignatura.isBlank() -> "Selecciona una asignatura"
        asignatura !in asignaturasPermitidas -> "Asignatura no valida"
        else -> null
    }

    fun validarUnidad(unidad: String): String? = when {
        unidad.isBlank() -> "La unidad es obligatoria"
        unidad.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        else -> null
    }

    fun validarCodigoOa(codigo: String): String? = when {
        codigo.isBlank() -> "El codigo OA es obligatorio"
        !formatoCodigoOa.matches(codigo.trim()) -> "Formato invalido. Ejemplo: OA-LENG-03-03"
        else -> null
    }

    fun validarHoras(horas: String): String? {
        val valor = horas.trim().toIntOrNull()
        return when {
            horas.isBlank() -> "Las horas son obligatorias"
            valor == null -> "Ingresa solo numeros"
            valor !in 1..20 -> "Debe estar entre 1 y 20 horas"
            else -> null
        }
    }

    fun validarIntentos(intentos: String): String? {
        val valor = intentos.trim().toIntOrNull()
        return when {
            intentos.isBlank() -> "Los intentos son obligatorios"
            valor == null -> "Ingresa solo numeros"
            valor !in 1..5 -> "Debe estar entre 1 y 5 intentos"
            else -> null
        }
    }
}
