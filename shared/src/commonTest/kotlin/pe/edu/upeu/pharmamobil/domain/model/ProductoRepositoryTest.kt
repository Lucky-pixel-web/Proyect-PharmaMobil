package pe.edu.upeu.pharmamobil.domain.service

import kotlin.test.Test
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import pe.edu.upeu.pharmamobil.domain.result.ResultadoProductos

class ProductoRepositoryTest {

    private val repo = ProductoRepository()

    @Test
    fun probarObtenerProductosSuspend() = runBlocking {
        val productos = repo.obtenerProductos()
        println("Suspend - Productos obtenidos: $productos")
    }

    @Test
    fun probarObservarEstadosFlow() = runBlocking {
        repo.observarEstados().collect { estado ->
            println("Flow estados - Emitido: $estado")
        }
    }

    @Test
    fun probarObservarProductosFlow() = runBlocking {
        repo.observarProductos().collect { lista ->
            println("Flow productos - Emitido: $lista")
        }
    }

    @Test
    fun probarCargarProductosConSealedClass() = runBlocking {
        repo.cargarProductos().collect { resultado ->
            when (resultado) {
                is ResultadoProductos.Cargando -> println("Estado: Cargando...")
                is ResultadoProductos.Exito -> println("Estado: Éxito -> ${resultado.list}")
                is ResultadoProductos.Error -> println("Estado: Error -> ${resultado.msg}")
            }
        }
    }
}