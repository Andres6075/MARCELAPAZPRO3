package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.ui.res.stringResource
import com.marcelapaz.mpazpr3.R
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.datos.PerfilEstudiante
import com.marcelapaz.mpazpr3.datos.RolUsuario

@Composable
fun PantallaInicio(
    perfil: PerfilEstudiante,
    modulos: List<ModuloAprendizaje>,
    alHacerClicModulo: (Int) -> Unit,
    alIrAModulos: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Encabezado de bienvenida adaptado dinamicamente al rol activo (Seccion 3.1)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.inicio_rol_y_nivel, perfil.rolActual.descripcion),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (perfil.rolActual) {
                                RolUsuario.ESTUDIANTE -> Icons.Default.School
                                RolUsuario.DOCENTE -> Icons.Default.Verified
                                RolUsuario.UTP -> Icons.Default.AdminPanelSettings
                            },
                            contentDescription = stringResource(R.string.inicio_icono_rol),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        item {
            // Tarjeta de condiciones de uso (Seccion 3.3)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = stringResource(R.string.inicio_android),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = stringResource(R.string.inicio_apoyo_didactico_y_seguimiento),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.inicio_interfaz_sencilla_para_tercero_basico),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Contenido dinamico segun el Rol Seleccionado (Seccion 3.1)
        when (perfil.rolActual) {
            RolUsuario.ESTUDIANTE -> {
                item {
                    // Tarjeta de progreso general de aprendizaje del estudiante
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.inicio_mi_progreso_de_aprendizaje_3),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = stringResource(R.string.inicio_progreso)
                                )
                            }

                            val progresoAnimado = animateFloatAsState(
                                targetValue = perfil.progresoGeneral,
                                animationSpec = tween(durationMillis = 800),
                                label = "progresoGeneral"
                            )
                            LinearProgressIndicator(
                                progress = { progresoAnimado.value },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = MaterialTheme.colorScheme.onPrimary,
                                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(R.string.inicio_porcentaje_completado, (perfil.progresoGeneral * 100).toInt()),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = stringResource(R.string.inicio_modulos_completados, perfil.modulosCompletadosTotal, modulos.size),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                item {
                    // Estadisticas rapidas del estudiante
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TarjetaEstadistica(
                            modifier = Modifier.weight(1f),
                            titulo = stringResource(R.string.inicio_racha_de_estudio),
                            valor = stringResource(R.string.inicio_racha_dias, perfil.rachaDiasEstudio),
                            icono = Icons.Default.TrendingUp
                        )
                        TarjetaEstadistica(
                            modifier = Modifier.weight(1f),
                            titulo = stringResource(R.string.inicio_modulos_activos),
                            valor = "${modulos.size}",
                            icono = Icons.Default.School
                        )
                    }
                }
            }

            RolUsuario.DOCENTE -> {
                item {
                    // Tarjeta de resumen para el Docente de Tercero Basico
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.inicio_panel_docente_tercero_basico),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.inicio_supervision_de_las_4_asignaturas),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TarjetaEstadistica(
                            modifier = Modifier.weight(1f),
                            titulo = stringResource(R.string.inicio_estudiantes_3b),
                            valor = stringResource(R.string.inicio_alumnos_valor),
                            icono = Icons.Default.Person
                        )
                        TarjetaEstadistica(
                            modifier = Modifier.weight(1f),
                            titulo = stringResource(R.string.inicio_rendimiento_curso),
                            valor = stringResource(R.string.inicio_rendimiento_valor),
                            icono = Icons.Default.TrendingUp
                        )
                    }
                }
            }

            RolUsuario.UTP -> {
                item {
                    // Tarjeta de resumen para Jefatura de UTP
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.inicio_panel_jefatura_utp_revision_curricular),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.inicio_validacion_y_autorizacion_de_contenidos),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TarjetaEstadistica(
                            modifier = Modifier.weight(1f),
                            titulo = stringResource(R.string.inicio_autorizados_utp),
                            valor = "${modulos.count { it.aprobadoPorUtp }}/${modulos.size}",
                            icono = Icons.Default.Verified
                        )
                        TarjetaEstadistica(
                            modifier = Modifier.weight(1f),
                            titulo = stringResource(R.string.inicio_estado_informe),
                            valor = stringResource(R.string.inicio_validado),
                            icono = Icons.Default.AdminPanelSettings
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = alIrAModulos,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.inicio_ir_a_modulos_y_asignaturas))
            }
        }
    }
}

@Composable
fun TarjetaEstadistica(modifier: Modifier = Modifier, titulo: String, valor: String, icono: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
