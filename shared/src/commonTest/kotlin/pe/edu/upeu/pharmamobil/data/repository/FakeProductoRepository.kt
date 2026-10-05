package pe.edu.upeu.pharmamobil.data.repository

import pe.edu.upeu.pharmamobil.domain.error.ErrorApi
import pe.edu.upeu.pharmamobil.domain.error.ErrorApiException
import pe.edu.upeu.pharmamobil.domain.model.Producto
import pe.edu.upeu.pharmamobil.domain.repository.ProductoRepository

class FakeProductoRepository(
    private val productos: MutableList<Producto> = mutableListOf()
) : ProductoRepository {

    var fallaAlListar: Throwable? = null
    var fallaAlRegistrar: Throwable? = null
    var vecesQueSeLlamoRegistrar: Int = 0
        private set

    private var siguienteId = 1L

    override suspend fun registrar(producto: Producto): Producto {
        vecesQueSeLlamoRegistrar++
        fallaAlRegistrar?.let { throw it }
        val guardado = producto.copy(id = siguienteId++)
        productos.add(guardado)
        return guardado
    }

    override suspend fun listar(): List<Producto> {
        fallaAlListar?.let { throw it }
        return productos.toList()
    }

    override suspend fun obtener(id: Long): Producto =
        productos.firstOrNull { it.id == id }
            ?: throw ErrorApiException(ErrorApi.NoEncontrado)

    override suspend fun actualizar(producto: Producto): Producto {
        val indice = productos.indexOfFirst { it.id == producto.id }
        if (indice < 0) throw ErrorApiException(ErrorApi.NoEncontrado)
        productos[indice] = producto
        return producto
    }

    override suspend fun eliminar(id: Long) {
        val indice = productos.indexOfFirst { it.id == id }
        if (indice < 0) throw ErrorApiException(ErrorApi.NoEncontrado)
        productos[indice] = productos[indice].copy(activo = false)
    }
}