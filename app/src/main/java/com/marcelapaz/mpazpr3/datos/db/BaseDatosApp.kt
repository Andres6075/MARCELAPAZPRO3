package com.marcelapaz.mpazpr3.datos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
  Clase de Base de Datos principal de Room.
  Proporciona la instancia singleton para acceder a SQLite de forma segura.
*/
@Database(entities = [ModuloEntidad::class, UsuarioEntidad::class], version = 2, exportSchema = false)
abstract class BaseDatosApp : RoomDatabase() {

    abstract fun moduloDao(): ModuloDao

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCIA: BaseDatosApp? = null

        fun obtenerBaseDatos(contexto: Context): BaseDatosApp {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDatosApp::class.java,
                    "mpaz_pro3_database"
                )
                    // Si cambia el esquema (version), se recrea la base de datos local
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}

/*
  Glosario de Room - Base de Datos:
  - Database: Anotacion que define la base de datos de Room y sus entidades asociadas.
  - RoomDatabase: Clase base abstracta de Room que administra el acceso a SQLite.
  - Singleton: Patron de diseño que asegura que exista una sola instancia de la base de datos abierta.
*/
