package pe.edu.upeu.pharmamobil.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class ProductoTest {

    @Test
    fun probarTieneStock() {

        val conStock = Producto(
            id = 1L,
            nombre = "Paracetamol 500mg",
            precio = 5.50,
            stock = 20

        )

        assertEquals(true, conStock.tieneStock())

        val sinStock = Producto(
            id = 2L,
            nombre = "Ibuprofeno 400mg",
            precio = 8.00,
            stock = 0
        )

        assertEquals(false, sinStock.tieneStock())
    }
}