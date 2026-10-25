package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.marcelapaz.mpazpr3.datos.RepositorioUsuarios
import com.marcelapaz.mpazpr3.ui.componentes.CampoClave
import com.marcelapaz.mpazpr3.ui.componentes.CampoTextoValidado
import com.marcelapaz.mpazpr3.ui.modelovista.EstadoLogin

// Pantalla de inicio de sesion. Solo dibuja el estado y envia eventos al ViewModel de autenticacion.
@Composable
fun PantallaLogin(
    estado: EstadoLogin,
    alCambiarCorreo: (String) -> Unit,
    alCambiarClave: (String) -> Unit,
    alIngresar: () -> Unit,
    alIrARegistro: () -> Unit
) {
    // Animacion de color: el boton se pone rojo si hay errores en el formulario
    val hayProblema = estado.errores.hayErrores || estado.errorGeneral != null
    val colorBoton by animateColorAsState(
        targetValue = if (hayProblema) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        label = "colorBotonLogin"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Icon(
            imageVector = Icons.Default.School,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = "Escuela Marcela Paz",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Ingresa con tu cuenta para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        // Mensaje de error general (credenciales incorrectas) con animacion de entrada y salida
        AnimatedVisibility(
            visible = estado.errorGeneral != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = estado.errorGeneral ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        CampoTextoValidado(
            valor = estado.correo,
            etiqueta = "Correo electrónico",
            error = estado.errores.correo,
            alCambiar = alCambiarCorreo,
            iconoInicial = Icons.Default.Email,
            tipoTeclado = KeyboardType.Email
        )

        CampoClave(
            valor = estado.clave,
            etiqueta = "Contraseña",
            error = estado.errores.clave,
            alCambiar = alCambiarClave
        )

        Button(
            onClick = alIngresar,
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
            Text(if (estado.cargando) "Ingresando..." else "Ingresar")
        }

        TextButton(onClick = alIrARegistro) {
            Text("¿No tienes cuenta? Regístrate")
        }

        // Ayuda para probar la app con datos ficticios
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Cuentas de prueba (datos ficticios)",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "estudiante@escuelamarcelapaz.cl\ndocente@escuelamarcelapaz.cl\nutp@escuelamarcelapaz.cl\nContraseña: ${RepositorioUsuarios.CLAVE_DEMO}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
