package pe.edu.upeu.pharmamobil.domain.model

class Cliente(
    val id: Long,
    val nombre: String,
    val correo: String,
    val telefono: String?
) {
    fun obtenerTelefono(): String {
        return telefono ?: "No registrado"
    }

    fun tieneTelefonoValido(): Boolean {
        val tel = telefono ?: return false
        return Regex("^9\\d{8}$").matches(tel)
    }
}
