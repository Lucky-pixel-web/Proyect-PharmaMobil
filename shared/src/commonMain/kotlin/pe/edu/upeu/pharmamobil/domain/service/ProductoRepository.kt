package pe.edu.upeu.pharmamobil.domain.service

import pe.edu.upeu.pharmamobil.domain.model.Producto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pe.edu.upeu.pharmamobil.domain.result.ResultadoProductos

class ProductoRepository {

    private val productosSimulados = listOf(
        Producto(
            id = 1L,
            nombre = "Paracetamol",
            precio = 8.50,
            stock = 100
        ),
        Producto(
            id = 2L,
            nombre = "Ibuprofeno",
            precio = 12.00,
            stock = 50
        ),
        Producto(
            id = 3L,
            nombre = "Amoxicilina",
            precio = 18.50,
            stock = 20
        )
    )

    suspend fun obtenerProductos(): List<Producto> {
        delay(1000)
        return productosSimulados
    }

    fun observarEstados(): Flow<String> = flow {
        emit("Iniciando")
        delay(1000)
        emit("Finalizado")
    }

    fun observarProductos(): Flow<List<Producto>> = flow {
        emit(emptyList())
        delay(1000)
        emit(productosSimulados)
    }

    fun cargarProductos(): Flow<ResultadoProductos> = flow {
        emit(ResultadoProductos.Cargando)
        delay(1000)
        emit(ResultadoProductos.Exito(productosSimulados))
    }
}