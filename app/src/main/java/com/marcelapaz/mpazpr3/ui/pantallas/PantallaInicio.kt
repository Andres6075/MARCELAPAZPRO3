package com.marcelapaz.mpazpr3.ui.pantallas

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
                        text = "APP ESCUELA MARCELA PAZ",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Rol: ${perfil.rolActual.descripcion} | 3° Basico",
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
                            contentDescription = "Icono Rol",
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
                        contentDescription = "Android",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Apoyo Didactico y Seguimiento (3.3)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Interfaz sencilla para Tercero Basico. Conserva tu progreso y no requiere camara, GPS, microfono ni datos biometricos.",
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
                                    text = "Mi Progreso de Aprendizaje (3° Basico)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = "Progreso"
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
                                    text = "${(perfil.progresoGeneral * 100).toInt()}% completado",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "${perfil.modulosCompletadosTotal}/${modulos.size} Modulos",
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
                            titulo = "Racha de Estudio",
                            valor = "${perfil.rachaDiasEstudio} dias",
                            icono = Icons.Default.TrendingUp
                        )
                        TarjetaEstadistica(
                            modifier = Modifier.weight(1f),
                            titulo = "Modulos Activos",
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
                                text = "Panel Docente - Tercero Basico",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Supervision de las 4 asignaturas MVP, gestion de intentos maximos para evaluaciones y seguimiento del curso.",
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
                            titulo = "Estudiantes 3°B",
                            valor = "27 Alumnos",
                            icono = Icons.Default.Person
                        )
                        TarjetaEstadistica(
                            modifier = Modifier.weight(1f),
                            titulo = "Rendimiento Curso",
                            valor = "82% Logro",
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
                                text = "Panel Jefatura UTP - Revision Curricular",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Validacion y autorizacion de contenidos pedagogicos segun Objetivos de Aprendizaje oficiales (Seccion 3.1).",
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
                            titulo = "Autorizados UTP",
                            valor = "${modulos.count { it.aprobadoPorUtp }}/${modulos.size}",
                            icono = Icons.Default.Verified
                        )
                        TarjetaEstadistica(
                            modifier = Modifier.weight(1f),
                            titulo = "Estado Informe",
                            valor = "Validado",
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
                Text("Ir a Modulos y Asignaturas")
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
