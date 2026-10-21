package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.marcelapaz.mpazpr3.datos.InformePedagogico
import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje
import com.marcelapaz.mpazpr3.datos.PerfilEstudiante
import com.marcelapaz.mpazpr3.datos.RolUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    perfil: PerfilEstudiante,
    modulos: List<ModuloAprendizaje>,
    informe: InformePedagogico,
    alCambiarRol: (RolUsuario) -> Unit,
    alAutorizarModulo: (Int) -> Unit,
    avatar: Bitmap?,
    alElegirFoto: (Uri) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    // Recurso nativo: selector de fotos del sistema (no requiere permisos). Si se cancela, uri es null.
    val lanzadorFoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) alElegirFoto(uri)
    }
    val elegirFoto = {
        lanzadorFoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Avatar / Icono de Rol
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(90.dp).clickable { elegirFoto() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (avatar != null) {
                        Image(
                            bitmap = avatar.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = when (perfil.rolActual) {
                                RolUsuario.ESTUDIANTE -> Icons.Default.Person
                                RolUsuario.DOCENTE -> Icons.Default.Verified
                                RolUsuario.UTP -> Icons.Default.AdminPanelSettings
                            },
                            contentDescription = "Rol",
                            modifier = Modifier.size(45.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        item {
            TextButton(onClick = { elegirFoto() }) {
                Text("Cambiar foto de perfil")
            }
        }
        item {
            Text(
                text = perfil.nombre,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = perfil.rolActual.descripcion,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            // Selector de Perfiles segun la seccion 3.1 del caso
            ExposedDropdownMenuBox(
                expanded = expandido,
                onExpandedChange = { expandido = !expandido }
            ) {
                OutlinedTextField(
                    value = "Cambiar Perfil: ${perfil.rolActual.descripcion}",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandido,
                    onDismissRequest = { expandido = false }
                ) {
                    RolUsuario.values().forEach { rol ->
                        DropdownMenuItem(
                            text = { Text(rol.descripcion) },
                            onClick = {
                                alCambiarRol(rol)
                                expandido = false
                            }
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilaInformacionPerfil(etiqueta = "Identificador Ficticio", valor = perfil.codigoEstudiante)
                    HorizontalDivider()
                    FilaInformacionPerfil(etiqueta = "Establecimiento", valor = "Escuela Escritora Marcela Paz")
                    HorizontalDivider()
                    FilaInformacionPerfil(etiqueta = "Nivel", valor = "Tercero Básico (3° Básico)")
                    HorizontalDivider()
                    FilaInformacionPerfil(etiqueta = "Base de Datos", valor = "Independiente de Prueba")
                }
            }
        }

        // Tarjeta explicativa de la seccion 3.4 (Sin conexion a sistemas reales, datos ficticios, sin IA obligatoria)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DataObject,
                        contentDescription = "Datos Ficticios",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "MVP Independiente y Ficticio",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "No se conecta al Libro MPAZ ni sistemas institucionales. Trabaja con datos ficticios basados en el Currículum Nacional, sin requerir IA generativa.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }

        // Vista especifica segun el Rol seleccionado (Seccion 3.1 del Caso)
        when (perfil.rolActual) {
            RolUsuario.ESTUDIANTE -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Panel del Estudiante",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Puedes consultar tu material asignado, desarrollar actividades y revisar tus respuestas correctas e incorrectas.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            RolUsuario.DOCENTE -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Panel del Docente de Tercero Básico",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Asignación de contenidos, definición de intentos máximos para evaluaciones y revisión de resultados de los estudiantes.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            RolUsuario.UTP -> {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Panel de Jefatura UTP (Revisión de Contenidos)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = "Autoriza los contenidos pedagógicos y verifica su relación con los objetivos de aprendizaje oficial.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )

                            HorizontalDivider()

                            Text(
                                text = "Autorización de Módulos:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )

                            modulos.forEach { mod ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = mod.titulo,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Button(
                                        onClick = { alAutorizarModulo(mod.id) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (mod.aprobadoPorUtp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Text(if (mod.aprobadoPorUtp) "Autorizado" else "Pendiente")
                                    }
                                }
                            }

                            HorizontalDivider()

                            Text(
                                text = "Informe Pedagógico Reservado:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = "Rendimiento: ${informe.rendimientoPromedio}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Fortalezas: ${informe.fortalezas}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "A reforzar: ${informe.contenidosAReforzar}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Estado: ${informe.estadoValidacion}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilaInformacionPerfil(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}