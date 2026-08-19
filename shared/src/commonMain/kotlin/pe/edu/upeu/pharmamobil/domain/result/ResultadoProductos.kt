package pe.edu.upeu.pharmamobil.domain.result

import pe.edu.upeu.pharmamobil.domain.model.Producto

sealed class ResultadoProductos {
    data object Cargando : ResultadoProductos()
    data class Exito(val list: List<Producto>) : ResultadoProductos()
    data class Error(val msg: String) : ResultadoProductos()
}