package com.marcelapaz.mpazpr3.datos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File

/*
  Guarda y recupera la foto de perfil en el almacenamiento interno de la app.
  La imagen elegida con el selector de fotos del sistema se copia a filesDir,
  asi no depende de permisos y sigue disponible aunque se cierre la app.
*/
object AlmacenAvatar {
    private const val NOMBRE_ARCHIVO = "avatar.jpg"
    private const val TAMANO_MAXIMO = 512

    // Carga la foto guardada (o null si todavia no hay ninguna)
    fun cargar(contexto: Context): Bitmap? {
        val archivo = File(contexto.filesDir, NOMBRE_ARCHIVO)
        return if (archivo.exists()) decodificar(archivo) else null
    }

    // Copia la imagen elegida al almacenamiento interno y la devuelve lista para mostrar
    fun guardar(contexto: Context, uri: Uri): Bitmap? {
        return try {
            val archivo = File(contexto.filesDir, NOMBRE_ARCHIVO)
            val entrada = contexto.contentResolver.openInputStream(uri) ?: return null
            entrada.use { flujo ->
                archivo.outputStream().use { salida -> flujo.copyTo(salida) }
            }
            decodificar(archivo)
        } catch (e: Exception) {
            null
        }
    }

    // Reduce el tamano de la imagen para no gastar memoria de mas
    private fun decodificar(archivo: File): Bitmap? {
        val medidas = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(archivo.path, medidas)
        var muestra = 1
        while (medidas.outWidth / muestra > TAMANO_MAXIMO || medidas.outHeight / muestra > TAMANO_MAXIMO) {
            muestra *= 2
        }
        val opciones = BitmapFactory.Options().apply { inSampleSize = muestra }
        return BitmapFactory.decodeFile(archivo.path, opciones)
    }
}
