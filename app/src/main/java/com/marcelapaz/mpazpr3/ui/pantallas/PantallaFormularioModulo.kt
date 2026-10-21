package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.ui.modelovista.FormularioModuloModeloVista
import com.marcelapaz.mpazpr3.ui.modelovista.ValidadorModulo

// Pantalla del formulario "Nuevo modulo" (Docente / UTP). Solo dibuja el estado y envia eventos al ViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioModulo(
    alGuardar: (ModuloAprendizaje) -> Unit,
    alVolver: () -> Unit,
    modeloVista: FormularioModuloModeloVista = viewModel()
) {
    val estado by modeloVista.estado.collectAsStateWithLifecycle()
    val errores = estado.errores
    var menuAsignaturaAbierto by remember { mutableStateOf(false) }

    // Animacion de color: el boton cambia de color segun si el formulario tiene errores
    val colorBoton by animateColorAsState(
        targetValue = if (errores.hayErrores) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.primary,
        label = "colorBoton"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Módulo") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Mensaje de exito con animacion de entrada y salida
            AnimatedVisibility(
                visible = estado.guardadoExitoso,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Guardado")
                        Text(
                            "Módulo guardado. Queda pendiente de autorización de la UTP.",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            CampoFormulario(
                valor = estado.titulo,
                etiqueta = "Título del módulo",
                error = errores.titulo,
                alCambiar = modeloVista::cambiarTitulo
            )

            // Selector de asignatura (lista desplegable con validacion)
            ExposedDropdownMenuBox(
                expanded = menuAsignaturaAbierto,
                onExpandedChange = { menuAsignaturaAbierto = !menuAsignaturaAbierto }
            ) {
                OutlinedTextField(
                    value = estado.asignatura,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Asignatura") },
                    isError = errores.asignatura != null,
                    supportingText = { errores.asignatura?.let { Text(it) } },
                    trailingIcon = {
                        if (errores.asignatura != null) {
                            Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        } else {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAsignaturaAbierto)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = menuAsignaturaAbierto,
                    onDismissRequest = { menuAsignaturaAbierto = false }
                ) {
                    ValidadorModulo.asignaturasPermitidas.forEach { asignatura ->
                        DropdownMenuItem(
                            text = { Text(asignatura) },
                            onClick = {
                                modeloVista.cambiarAsignatura(asignatura)
                                menuAsignaturaAbierto = false
                            }
                        )
                    }
                }
            }

            CampoFormulario(
                valor = estado.unidad,
                etiqueta = "Unidad (ej: Unidad 1: Descubriendo Historias)",
                error = errores.unidad,
                alCambiar = modeloVista::cambiarUnidad
            )

            CampoFormulario(
                valor = estado.codigoOa,
                etiqueta = "Código OA (ej: OA-LENG-03-03)",
                error = errores.codigoOa,
                alCambiar = modeloVista::cambiarCodigoOa
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoFormulario(
                    valor = estado.horas,
                    etiqueta = "Horas (1-20)",
                    error = errores.horas,
                    alCambiar = modeloVista::cambiarHoras,
                    soloNumeros = true,
                    modifier = Modifier.weight(1f)
                )
                CampoFormulario(
                    valor = estado.intentos,
                    etiqueta = "Intentos (1-5)",
                    error = errores.intentos,
                    alCambiar = modeloVista::cambiarIntentos,
                    soloNumeros = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = {
                    // Si el ViewModel valida todo, retorna el modulo y lo enviamos a guardar en Room
                    modeloVista.enviar()?.let(alGuardar)
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorBoton),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar módulo")
            }
        }
    }
}

// Campo de texto reutilizable con icono de error y mensaje debajo (isError + supportingText)
@Composable
private fun CampoFormulario(
    valor: String,
    etiqueta: String,
    error: String?,
    alCambiar: (String) -> Unit,
    modifier: Modifier = Modifier,
    soloNumeros: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        trailingIcon = {
            if (error != null) {
                Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (soloNumeros) KeyboardType.Number else KeyboardType.Text
        ),
        modifier = modifier.fillMaxWidth()
    )
}
