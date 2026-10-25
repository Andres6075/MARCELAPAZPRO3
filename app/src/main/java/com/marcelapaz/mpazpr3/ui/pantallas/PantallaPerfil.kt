package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.ui.res.stringResource
import com.marcelapaz.mpazpr3.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
    alCerrarSesion: () -> Unit,
    alAutorizarModulo: (Int) -> Unit,
    avatar: Bitmap?,
    alElegirFoto: (Uri) -> Unit
) {
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
                            contentDescription = stringResource(R.string.perfil_foto_de_perfil),
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
                            contentDescription = stringResource(R.string.perfil_rol),
                            modifier = Modifier.size(45.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        item {
            TextButton(onClick = { elegirFoto() }) {
                Text(stringResource(R.string.perfil_cambiar_foto_de_perfil))
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
            // Cierra la sesion y vuelve a la pantalla de ingreso
            OutlinedButton(onClick = alCerrarSesion, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.perfil_cerrar_sesion))
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
                    FilaInformacionPerfil(etiqueta = stringResource(R.string.perfil_identificador_ficticio), valor = perfil.codigoEstudiante)
                    HorizontalDivider()
                    FilaInformacionPerfil(etiqueta = stringResource(R.string.perfil_establecimiento), valor = stringResource(R.string.perfil_establecimiento_valor))
                    HorizontalDivider()
                    FilaInformacionPerfil(etiqueta = stringResource(R.string.perfil_nivel), valor = stringResource(R.string.perfil_nivel_valor))
                    HorizontalDivider()
                    FilaInformacionPerfil(etiqueta = stringResource(R.string.perfil_base_de_datos), valor = stringResource(R.string.perfil_independiente_de_prueba))
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
                        contentDescription = stringResource(R.string.perfil_datos_ficticios),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(32.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = stringResource(R.string.perfil_mvp_independiente_y_ficticio),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = stringResource(R.string.perfil_no_se_conecta_al_libro),
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
                                text = stringResource(R.string.perfil_panel_del_estudiante),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = stringResource(R.string.perfil_puedes_consultar_tu_material_asignado),
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
                                text = stringResource(R.string.perfil_panel_del_docente_de_tercero),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.perfil_asignacion_de_contenidos_definicion_de),
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
                                text = stringResource(R.string.perfil_panel_de_jefatura_utp_revision),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = stringResource(R.string.perfil_autoriza_los_contenidos_pedagogicos_y),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )

                            HorizontalDivider()

                            Text(
                                text = stringResource(R.string.perfil_autorizacion_de_modulos),
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
                                        Text(if (mod.aprobadoPorUtp) stringResource(R.string.comun_autorizado) else stringResource(R.string.perfil_pendiente))
                                    }
                                }
                            }

                            HorizontalDivider()

                            Text(
                                text = stringResource(R.string.perfil_informe_pedagogico_reservado),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = stringResource(R.string.perfil_rendimiento, informe.rendimientoPromedio), style = MaterialTheme.typography.bodySmall)
                            Text(text = stringResource(R.string.perfil_fortalezas, informe.fortalezas), style = MaterialTheme.typography.bodySmall)
                            Text(text = stringResource(R.string.perfil_a_reforzar, informe.contenidosAReforzar), style = MaterialTheme.typography.bodySmall)
                            Text(text = stringResource(R.string.perfil_estado, informe.estadoValidacion), style = MaterialTheme.typography.bodySmall)
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