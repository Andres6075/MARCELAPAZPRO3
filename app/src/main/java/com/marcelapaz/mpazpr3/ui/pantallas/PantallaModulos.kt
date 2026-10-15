package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.datos.RolUsuario

@Composable
fun PantallaModulos(
    modulos: List<ModuloAprendizaje>,
    rolActual: RolUsuario,
    textoBusqueda: String,
    alCambiarTextoBusqueda: (String) -> Unit,
    alAutorizarModuloUtp: (Int) -> Unit,
    alCambiarIntentosDocente: (Int, Int) -> Unit,
    alHacerClicModulo: (Int) -> Unit
) {
    val modulosFiltrados = remember(modulos, textoBusqueda) {
        if (textoBusqueda.isBlank()) {
            modulos
        } else {
            modulos.filter {
                it.titulo.contains(textoBusqueda, ignoreCase = true) ||
                        it.asignatura.contains(textoBusqueda, ignoreCase = true) ||
                        it.unidad.contains(textoBusqueda, ignoreCase = true) ||
                        it.codigoOficial.contains(textoBusqueda, ignoreCase = true) ||
                        it.descripcion.contains(textoBusqueda, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Asignaturas y Unidades (3.2)",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = alCambiarTextoBusqueda,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por asignatura, unidad o codigo OA...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        // Banner informativo de funciones especificas segun el Rol Seleccionado (Seccion 3.1)
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = when (rolActual) {
                RolUsuario.ESTUDIANTE -> MaterialTheme.colorScheme.primaryContainer
                RolUsuario.DOCENTE -> MaterialTheme.colorScheme.secondaryContainer
                RolUsuario.UTP -> MaterialTheme.colorScheme.tertiaryContainer
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = when (rolActual) {
                    RolUsuario.ESTUDIANTE -> "Modo Estudiante: Consulta tus asignaturas y practica con las actividades autorizadas."
                    RolUsuario.DOCENTE -> "Modo Docente: Configura los intentos maximos permitidos para la evaluacion final."
                    RolUsuario.UTP -> "Modo UTP: Revisa y autoriza oficialmente los contenidos pedagogicos."
                },
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(12.dp),
                color = when (rolActual) {
                    RolUsuario.ESTUDIANTE -> MaterialTheme.colorScheme.onPrimaryContainer
                    RolUsuario.DOCENTE -> MaterialTheme.colorScheme.onSecondaryContainer
                    RolUsuario.UTP -> MaterialTheme.colorScheme.onTertiaryContainer
                }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(modulosFiltrados) { modulo ->
                TarjetaModuloResumen(
                    modulo = modulo,
                    rolActual = rolActual,
                    alAutorizarUtp = { alAutorizarModuloUtp(modulo.id) },
                    alCambiarIntentos = { nuevosIntentos -> alCambiarIntentosDocente(modulo.id, nuevosIntentos) },
                    alHacerClic = { alHacerClicModulo(modulo.id) }
                )
            }
        }
    }
}

@Composable
fun TarjetaModuloResumen(
    modulo: ModuloAprendizaje,
    rolActual: RolUsuario,
    alAutorizarUtp: () -> Unit,
    alCambiarIntentos: (Int) -> Unit,
    alHacerClic: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = alHacerClic),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = modulo.asignatura,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        // Badge de Autorizacion UTP (Seccion 3.1)
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (modulo.aprobadoPorUtp) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = if (modulo.aprobadoPorUtp) "Autorizado UTP" else "Pendiente UTP",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                color = if (modulo.aprobadoPorUtp) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    Text(
                        text = modulo.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${modulo.unidad} • OA: ${modulo.codigoOficial}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                if (modulo.estaCompletado) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completado",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    CircularProgressIndicator(
                        progress = { modulo.progreso },
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 3.dp
                    )
                }
            }

            // Acciones especificas por Rol segun la Seccion 3.1 del Caso de Estudio
            when (rolActual) {
                RolUsuario.UTP -> {
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Gestion UTP:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = alAutorizarUtp,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (modulo.aprobadoPorUtp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(if (modulo.aprobadoPorUtp) "Autorizado" else "Aprobar Contenido")
                        }
                    }
                }

                RolUsuario.DOCENTE -> {
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Intentos Evaluacion: ${modulo.intentosPermitidos}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = { if (modulo.intentosPermitidos > 1) alCambiarIntentos(modulo.intentosPermitidos - 1) }
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Restar intento")
                            }
                            IconButton(
                                onClick = { if (modulo.intentosPermitidos < 5) alCambiarIntentos(modulo.intentosPermitidos + 1) }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Sumar intento")
                            }
                        }
                    }
                }

                RolUsuario.ESTUDIANTE -> {
                    // El estudiante solo consulta materiales sin modificar estado ni permisos (3.1)
                }
            }
        }
    }
}
