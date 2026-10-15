package com.marcelapaz.mpazpr3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.marcelapaz.mpazpr3.ui.navegacion.GrafoNavegacionMpaz
import com.marcelapaz.mpazpr3.ui.tema.MPAZPRO3Theme
import com.marcelapaz.mpazpr3.ui.modelovista.ModeloVistaMpaz

class MainActivity : ComponentActivity() {
    private val modeloVista: ModeloVistaMpaz by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Inicializa la base de datos local Room
        modeloVista.inicializarRoom(this)

        setContent {
            MPAZPRO3Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GrafoNavegacionMpaz(modeloVista = modeloVista)
                }
            }
        }
    }
}
