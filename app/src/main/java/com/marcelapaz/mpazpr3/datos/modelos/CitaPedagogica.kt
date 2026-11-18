package com.marcelapaz.mpazpr3.datos.modelos

import com.google.gson.annotations.SerializedName

/*
  Modelo para la API Externa consumida via Retrofit.
  Representa una cita/frase pedagogica diaria de motivacion docente y escolar.
*/
data class CitaPedagogica(
    @SerializedName("id") val id: Int = 1,
    @SerializedName("quote") val cita: String = "La educación es el arma más poderosa que puedes usar para cambiar el mundo.",
    @SerializedName("author") val autor: String = "Nelson Mandela"
)
