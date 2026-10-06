package pe.edu.upeu.pharmamobil.data.repository

import kotlinx.coroutines.CompletableDeferred
import pe.edu.upeu.pharmamobil.domain.error.ErrorApi
import pe.edu.upeu.pharmamobil.domain.error.ErrorApiException
import pe.edu.upeu.pharmamobil.domain.model.Producto
import pe.edu.upeu.pharmamobil.domain.repository.ProductoRepository

class FakeProductoRepository(
    private val productos: MutableList<Producto> = mutableListOf()
) : ProductoRepository {

    var fallaAlListar: Throwable? = null
    var fallaAlRegistrar: Throwable? = null

    /* Mientras la compuerta no se complete, la operación queda detenida a mitad de camino. */
    var compuertaListar: CompletableDeferred<Unit>? = null
    var compuertaEliminar: CompletableDeferred<Unit>? = null

    var vecesQueSeLlamoRegistrar: Int = 0
        private set
    var vecesQueSeLlamoListar: Int = 0
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
        vecesQueSeLlamoListar++
        compuertaListar?.await()
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
        compuertaEliminar?.await()
        val indice = productos.indexOfFirst { it.id == id }
        if (indice < 0) throw ErrorApiException(ErrorApi.NoEncontrado)
        productos[indice] = productos[indice].copy(activo = false)
    }
}