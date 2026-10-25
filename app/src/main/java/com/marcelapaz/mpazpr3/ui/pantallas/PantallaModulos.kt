package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.ui.res.stringResource
import com.marcelapaz.mpazpr3.R
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
    alCrearModulo: () -> Unit,
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
            text = stringResource(R.string.modulos_asignaturas_y_unidades),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = alCambiarTextoBusqueda,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.modulos_buscar_por_asignatura_unidad_o)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.modulos_buscar)) },
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
                    RolUsuario.ESTUDIANTE -> stringResource(R.string.modulos_modo_estudiante_consulta_tus_asignaturas)
                    RolUsuario.DOCENTE -> stringResource(R.string.modulos_modo_docente_configura_los_intentos)
                    RolUsuario.UTP -> stringResource(R.string.modulos_modo_utp_revisa_y_autoriza)
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

        // Solo Docente y UTP pueden crear modulos nuevos (formulario con validacion)
        if (rolActual != RolUsuario.ESTUDIANTE) {
            Button(
                onClick = alCrearModulo,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.modulos_nuevo_modulo))
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (modulosFiltrados.isEmpty()) {
                item {
                    Text(
                        text = if (rolActual == RolUsuario.ESTUDIANTE) stringResource(R.string.modulos_aun_no_hay_modulos_autorizados)
                        else stringResource(R.string.modulos_no_se_encontraron_modulos),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
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
    // Animacion: el circulo de progreso se mueve suavemente cuando cambia el avance
    val progresoAnimado = animateFloatAsState(
        targetValue = modulo.progreso,
        animationSpec = tween(durationMillis = 600),
        label = "progresoModulo"
    )
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
                                text = if (modulo.aprobadoPorUtp) stringResource(R.string.modulos_autorizado_utp) else stringResource(R.string.modulos_pendiente_utp),
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
                        text = stringResource(R.string.modulos_unidad_y_oa, modulo.unidad, modulo.codigoOficial),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                if (modulo.estaCompletado) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = stringResource(R.string.modulos_completado),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    CircularProgressIndicator(
                        progress = { progresoAnimado.value },
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
                            text = stringResource(R.string.modulos_gestion_utp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = alAutorizarUtp,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (modulo.aprobadoPorUtp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(if (modulo.aprobadoPorUtp) stringResource(R.string.comun_autorizado) else stringResource(R.string.modulos_aprobar_contenido))
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
                            text = stringResource(R.string.modulos_intentos_evaluacion, modulo.intentosPermitidos),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = { if (modulo.intentosPermitidos > 1) alCambiarIntentos(modulo.intentosPermitidos - 1) }
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = stringResource(R.string.modulos_restar_intento))
                            }
                            IconButton(
                                onClick = { if (modulo.intentosPermitidos < 5) alCambiarIntentos(modulo.intentosPermitidos + 1) }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.modulos_sumar_intento))
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
