package pe.edu.upeu.pharmamobil.data.repository

import kotlin.coroutines.cancellation.CancellationException
import pe.edu.upeu.pharmamobil.data.mapper.toDomain
import pe.edu.upeu.pharmamobil.data.remote.ProductoApi
import pe.edu.upeu.pharmamobil.data.remote.aErrorDeRed
import pe.edu.upeu.pharmamobil.domain.model.Producto
import pe.edu.upeu.pharmamobil.domain.repository.ProductoRepository

class ProductoRepositorioRest(
    private val api: ProductoApi,
    private val respaldoLocal: ProductoRepositorioEnMemoria
) : ProductoRepository {

    override suspend fun listar(): List<Producto> =
        try {
            listOf(api.obtenerPorId(99999).toDomain())
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (fallo: Throwable) {
            println("KtorClient: ${fallo::class.simpleName} - ${fallo.message}")
            throw fallo.aErrorDeRed()
        }

    override suspend fun registrar(producto: Producto): Producto =
        respaldoLocal.registrar(producto)
}