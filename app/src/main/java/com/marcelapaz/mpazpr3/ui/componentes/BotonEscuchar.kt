package com.marcelapaz.mpazpr3.ui.componentes

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Locale

/*
  Recurso nativo: sintesis de voz de Android (TextToSpeech).
  Lee en voz alta el texto recibido como apoyo a la lectura autonoma.
  No necesita permisos; solo se declara <queries> en el Manifest para poder encontrar el motor de voz.
*/
@Composable
fun BotonEscuchar(texto: String, modifier: Modifier = Modifier) {
    val contexto = LocalContext.current
    var listo by remember { mutableStateOf(false) }
    var hablando by remember { mutableStateOf(false) }
    var lector by remember { mutableStateOf<TextToSpeech?>(null) }

    // Crea el motor de voz al entrar a la pantalla y lo libera al salir
    DisposableEffect(contexto) {
        val motor = TextToSpeech(contexto) { estado ->
            listo = estado == TextToSpeech.SUCCESS
        }
        lector = motor
        onDispose {
            motor.stop()
            motor.shutdown()
        }
    }

    OutlinedButton(
        modifier = modifier,
        enabled = listo,
        onClick = {
            lector?.let { motor ->
                if (hablando) {
                    motor.stop()
                    hablando = false
                } else {
                    motor.setLanguage(Locale.forLanguageTag("es-CL"))
                    motor.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {}
                        override fun onDone(utteranceId: String?) { hablando = false }
                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) { hablando = false }
                    })
                    hablando = true
                    motor.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "lectura_modulo")
                }
            }
        }
    ) {
        Icon(
            imageVector = if (hablando) Icons.Default.Stop else Icons.Default.VolumeUp,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(if (hablando) "Detener" else "Escuchar explicacion")
    }
}
