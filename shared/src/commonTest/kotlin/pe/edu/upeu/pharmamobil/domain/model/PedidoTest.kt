package pe.edu.upeu.pharmamobil.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class PedidoTest {

    @Test
    fun probarSubtotalDeProducto() {
        val producto = Producto(
            id = 1L,
            nombre = "Paracetamol 500mg",
            precio = 5.50,
            stock = 20
        )

        val detalle = DetallePedido(
            producto = producto,
            cantidad = 3
        )

        val cliente = Cliente(
            id = 1L,
            nombre = "Farmacia Sur",
            correo = "sur@central.pe",
            telefono = "989789123"
        )

        val pedido = Pedido(
            id = 1L,
            cliente = cliente,
            detalles = listOf(detalle),
            estado = EstadoPedido.Pendiente
        )

        assertEquals(16.50, pedido.subtotalDeProducto(1L))
    }
}