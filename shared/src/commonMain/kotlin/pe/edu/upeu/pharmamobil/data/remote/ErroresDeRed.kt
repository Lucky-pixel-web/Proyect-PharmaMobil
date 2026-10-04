package pe.edu.upeu.pharmamobil.data.remote

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.serialization.SerializationException
import pe.edu.upeu.pharmamobil.domain.error.ErrorDeRedException

internal fun Throwable.aErrorDeRed(): ErrorDeRedException {
    val mensaje = when {
        this is ClientRequestException -> when (response.status.value) {
            400 -> "La solicitud enviada no es válida."
            404 -> "No se encontró la información solicitada."
            409 -> "La operación entra en conflicto con los datos existentes."
            else -> "No se pudo completar la solicitud (código ${response.status.value})."
        }
        this is ServerResponseException ->
            "El servidor tuvo un problema. Inténtalo más tarde."
        this is HttpRequestTimeoutException ->
            "El servidor tardó demasiado en responder. Inténtalo de nuevo."
        tieneCausaDeSerializacion() ->
            "La respuesta del servidor no tiene el formato esperado."
        else ->
            "No se pudo conectar con el servidor. Revisa tu conexión a internet."
    }
    return ErrorDeRedException(mensaje, this)
}

private fun Throwable.tieneCausaDeSerializacion(): Boolean =
    generateSequence(this) { it.cause }.any { it is SerializationException }