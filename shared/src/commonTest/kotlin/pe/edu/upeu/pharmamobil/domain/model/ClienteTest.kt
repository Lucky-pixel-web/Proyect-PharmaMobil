package pe.edu.upeu.pharmamobil.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ClienteTest {

    @Test
    fun probarCliente() {

        val cliente = Cliente(
            id = 1L,
            nombre = "Farmacia Nueva Vida",
            correo = "ventas@central.pe",
            telefono = "989789123"
        )

        val resultado = cliente.obtenerTelefono()

        assertEquals(
            expected = "989789123",
            actual = resultado
        )
    }

    @Test
    fun probarTelefonoValido() {
        val clienteValido = Cliente(
            id = 1L,
            nombre = "Farmacia Nueva Vida",
            correo = "ventas@central.pe",
            telefono = "989789123"
        )
        assertEquals(true, clienteValido.tieneTelefonoValido())

        val clienteInvalido = Cliente(
            id = 2L,
            nombre = "Farmacia Sur",
            correo = "sur@central.pe",
            telefono = "12345"
        )
        assertEquals(false, clienteInvalido.tieneTelefonoValido())
    }
}
