package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.marcelapaz.mpazpr3.datos.RecursoDidactico
import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    idModulo: Int,
    modulos: List<ModuloAprendizaje>,
    recursos: List<RecursoDidactico>,
    alCambiarEstado: (Int) -> Unit,
    alActualizarProgreso: (Int, Float) -> Unit,
    alVolver: () -> Unit
) {
    val modulo = modulos.find { it.id == idModulo }
    val recursosModulo = recursos.filter { it.moduloId == idModulo }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Curricular del Modulo") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { rellenoInterno ->
        if (modulo == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno),
                contentAlignment = Alignment.Center
            ) {
                Text("Modulo no encontrado")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(rellenoInterno)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = modulo.asignatura,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = modulo.unidad,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = modulo.titulo,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Informacion Curricular (Seccion 3.2)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(text = "Nivel: ${modulo.nivelEducativo}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Objetivo: ${modulo.objetivoAprendizaje}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text(text = "Codigo Oficial: ${modulo.codigoOficial}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Eje Curricular: ${modulo.ejeCurricular}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Dificultad: ${modulo.dificultad} | Intentos: ${modulo.intentosRealizados}/${modulo.intentosPermitidos}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                item {
                    Text(
                        text = "Explicacion e Instrucciones:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = modulo.explicacion,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (modulo.tieneAudioApoyo) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Audio", tint = MaterialTheme.colorScheme.primary)
                            Text(text = "Incluye audio de apoyo para la lectura autonoma", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Progreso del Modulo: ${(modulo.progreso * 100).toInt()}%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Slider(
                                value = modulo.progreso,
                                onValueChange = { nuevoValor -> alActualizarProgreso(modulo.id, nuevoValor) }
                            )
                            Button(
                                onClick = { alCambiarEstado(modulo.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (modulo.estaCompletado) "Completado (Marcar Pendiente)" else "Marcar como Completado")
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Recursos Didacticos y Ejemplos (3.2)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (recursosModulo.isEmpty()) {
                    item {
                        Text(
                            text = "No hay recursos didacticos especificos para este modulo.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(recursosModulo) { recurso ->
                        TarjetaRecurso(recurso = recurso)
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaRecurso(recurso: RecursoDidactico) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = recurso.tipo,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = recurso.titulo,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${recurso.tipo} • ${recurso.duracionOPaginas}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (recurso.tieneAudio) {
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Audio", tint = MaterialTheme.colorScheme.primary)
                }
            }

            Text(
                text = "Instrucciones: ${recurso.instrucciones}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Ejemplo: ${recurso.ejemplo}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
