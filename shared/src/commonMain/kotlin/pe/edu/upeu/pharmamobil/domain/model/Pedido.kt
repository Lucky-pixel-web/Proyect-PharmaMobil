package pe.edu.upeu.pharmamobil.domain.model

data class Pedido(
    val id: Long,
    val cliente: Cliente,
    val detalles: List<DetallePedido>,
    val estado: EstadoPedido
) {
    fun subtotalDeProducto(productoId: Long): Double {
        return detalles.filter { it.producto.id == productoId }
            .sumOf { it.subtotal() }
    }
}
