package com.marcelapaz.mpazpr3.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.marcelapaz.mpazpr3.datos.ModuloAprendizaje

@Composable
fun PantallaModulos(
    modulos: List<ModuloAprendizaje>,
    textoBusqueda: String,
    alCambiarTextoBusqueda: (String) -> Unit,
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
            text = "Asignaturas y Unidades (3.2)",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = alCambiarTextoBusqueda,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por asignatura, unidad o codigo OA...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(modulosFiltrados) { modulo ->
                TarjetaModuloResumen(modulo = modulo, alHacerClic = { alHacerClicModulo(modulo.id) })
            }
        }
    }
}
