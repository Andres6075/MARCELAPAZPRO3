package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.ui.res.stringResource
import com.marcelapaz.mpazpr3.R
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.marcelapaz.mpazpr3.datos.RecursoDidactico
import com.marcelapaz.mpazpr3.ui.componentes.BotonEscuchar
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
                title = { Text(stringResource(R.string.detalle_detalle_curricular_del_modulo)) },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.comun_volver))
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
                Text(stringResource(R.string.detalle_modulo_no_encontrado))
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
                                text = stringResource(R.string.detalle_informacion_curricular),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(text = stringResource(R.string.detalle_nivel, modulo.nivelEducativo), style = MaterialTheme.typography.bodyMedium)
                            Text(text = stringResource(R.string.detalle_objetivo, modulo.objetivoAprendizaje), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text(text = stringResource(R.string.detalle_codigo_oficial, modulo.codigoOficial), style = MaterialTheme.typography.bodySmall)
                            Text(text = stringResource(R.string.detalle_eje_curricular, modulo.ejeCurricular), style = MaterialTheme.typography.bodySmall)
                            Text(text = stringResource(R.string.detalle_dificultad_intentos, modulo.dificultad, modulo.intentosRealizados, modulo.intentosPermitidos), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                item {
                    Column {
                        Text(
                            text = stringResource(R.string.detalle_explicacion_e_instrucciones),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = modulo.explicacion,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Recurso nativo: lectura en voz alta con TextToSpeech
                        BotonEscuchar(texto = modulo.explicacion)
                        if (modulo.tieneAudioApoyo) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.VolumeUp, contentDescription = stringResource(R.string.detalle_audio), tint = MaterialTheme.colorScheme.primary)
                                Text(text = stringResource(R.string.detalle_incluye_audio_de_apoyo_para), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        // Valor local del slider: mientras se arrastra solo cambia la interfaz.
                        // El progreso se guarda en Room una sola vez, al soltar el dedo.
                        var progresoLocal by remember(modulo.id, modulo.progreso) {
                            mutableFloatStateOf(modulo.progreso)
                        }
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.detalle_progreso_modulo, (progresoLocal * 100).toInt()),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Slider(
                                value = progresoLocal,
                                onValueChange = { nuevoValor -> progresoLocal = nuevoValor },
                                onValueChangeFinished = { alActualizarProgreso(modulo.id, progresoLocal) }
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
                                Text(if (modulo.estaCompletado) stringResource(R.string.detalle_completado_marcar_pendiente) else stringResource(R.string.detalle_marcar_como_completado))
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.detalle_recursos_didacticos_y_ejemplos),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (recursosModulo.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.detalle_no_hay_recursos_didacticos_especificos),
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
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = stringResource(R.string.detalle_audio), tint = MaterialTheme.colorScheme.primary)
                }
            }

            Text(
                text = stringResource(R.string.detalle_instrucciones, recurso.instrucciones),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.detalle_ejemplo, recurso.ejemplo),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
