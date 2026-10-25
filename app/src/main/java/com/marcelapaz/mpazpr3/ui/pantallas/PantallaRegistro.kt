package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.ui.res.stringResource
import com.marcelapaz.mpazpr3.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.marcelapaz.mpazpr3.datos.RolUsuario
import com.marcelapaz.mpazpr3.ui.componentes.CampoClave
import com.marcelapaz.mpazpr3.ui.componentes.CampoTextoValidado
import com.marcelapaz.mpazpr3.ui.modelovista.EstadoRegistro

// Pantalla de registro de cuenta nueva. Solo dibuja el estado y envia eventos al ViewModel de autenticacion.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    estado: EstadoRegistro,
    alCambiarNombre: (String) -> Unit,
    alCambiarCorreo: (String) -> Unit,
    alCambiarClave: (String) -> Unit,
    alCambiarConfirmacion: (String) -> Unit,
    alCambiarRol: (RolUsuario) -> Unit,
    alCrearCuenta: () -> Unit,
    alVolver: () -> Unit
) {
    var menuRolAbierto by remember { mutableStateOf(false) }

    val colorBoton by animateColorAsState(
        targetValue = if (estado.errores.hayErrores) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.primary,
        label = "colorBotonRegistro"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.registro_crear_cuenta)) },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.comun_volver))
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CampoTextoValidado(
                valor = estado.nombre,
                etiqueta = stringResource(R.string.registro_nombre_completo),
                error = estado.errores.nombre,
                alCambiar = alCambiarNombre,
                iconoInicial = Icons.Default.Person
            )

            CampoTextoValidado(
                valor = estado.correo,
                etiqueta = stringResource(R.string.comun_correo_electronico),
                error = estado.errores.correo,
                alCambiar = alCambiarCorreo,
                iconoInicial = Icons.Default.Email,
                tipoTeclado = KeyboardType.Email
            )

            CampoClave(
                valor = estado.clave,
                etiqueta = stringResource(R.string.registro_contrasena_minimo_8_letras_y),
                error = estado.errores.clave,
                alCambiar = alCambiarClave
            )

            CampoClave(
                valor = estado.confirmacion,
                etiqueta = stringResource(R.string.registro_confirmar_contrasena),
                error = estado.errores.confirmacion,
                alCambiar = alCambiarConfirmacion
            )

            // Selector de perfil (Estudiante, Docente o UTP) con validacion
            ExposedDropdownMenuBox(
                expanded = menuRolAbierto,
                onExpandedChange = { menuRolAbierto = !menuRolAbierto }
            ) {
                OutlinedTextField(
                    value = estado.rol?.descripcion ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.registro_perfil)) },
                    isError = estado.errores.rol != null,
                    supportingText = { estado.errores.rol?.let { Text(it) } },
                    trailingIcon = {
                        if (estado.errores.rol != null) {
                            Icon(Icons.Default.Error, contentDescription = stringResource(R.string.comun_error), tint = MaterialTheme.colorScheme.error)
                        } else {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuRolAbierto)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = menuRolAbierto,
                    onDismissRequest = { menuRolAbierto = false }
                ) {
                    RolUsuario.values().forEach { rol ->
                        DropdownMenuItem(
                            text = { Text(rol.descripcion) },
                            onClick = {
                                alCambiarRol(rol)
                                menuRolAbierto = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = alCrearCuenta,
                enabled = !estado.cargando,
                colors = ButtonDefaults.buttonColors(containerColor = colorBoton),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (estado.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (estado.cargando) stringResource(R.string.registro_creando_cuenta) else stringResource(R.string.registro_crear_cuenta))
            }
        }
    }
}
