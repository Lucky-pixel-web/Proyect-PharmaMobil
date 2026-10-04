package pe.edu.upeu.pharmamobil.domain.error

class ErrorDeRedException(
    val mensajeUsuario: String,
    causa: Throwable? = null
) : Exception(mensajeUsuario, causa)