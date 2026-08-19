package pe.edu.upeu.pharmamobil.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DetallePedidoTest {

    @Test
    fun probarSubtotal() {

        val producto = Producto(
            id = 1L,
            nombre = "Paracetamol 500mg",
            precio = 5.50,
            stock = 20
        )

        val detalle = DetallePedido(producto = producto, cantidad = 3)

        assertEquals(16.50, detalle.subtotal())
    }

    @Test
    fun probarCantidadInvalidaLanzaExcepcion() {

        val producto = Producto(
            id = 1L,
            nombre = "Paracetamol 500mg",
            precio = 5.50,
            stock = 20
        )

        assertFailsWith<IllegalArgumentException> {
            DetallePedido(producto = producto, cantidad = 0)
        }
    }
}