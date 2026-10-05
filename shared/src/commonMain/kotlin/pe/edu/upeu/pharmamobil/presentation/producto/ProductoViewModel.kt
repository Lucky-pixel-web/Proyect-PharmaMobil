package pe.edu.upeu.pharmamobil.presentation.producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.pharmamobil.domain.usecase.ActualizarProductoUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.EliminarProductoUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.ListarProductosUseCase
import pe.edu.upeu.pharmamobil.domain.usecase.ProductoInvalidoException
import pe.edu.upeu.pharmamobil.domain.usecase.RegistrarProductoUseCase
import pe.edu.upeu.pharmamobil.presentation.producto.ProductoUiState.Operacion
import pe.edu.upeu.pharmamobil.presentation.producto.ProductoUiState.Operacion.Tipo
import pe.edu.upeu.pharmamobil.domain.error.ErrorApi
import pe.edu.upeu.pharmamobil.domain.error.ErrorApiException
import pe.edu.upeu.pharmamobil.domain.error.mensajeUsuario

class ProductoViewModel(
    private val registrarProducto: RegistrarProductoUseCase,
    private val listarProductos: ListarProductosUseCase,
    private val actualizarProducto: ActualizarProductoUseCase,
    private val eliminarProducto: EliminarProductoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = ProductoUiState.Fase.Cargando) }
            val fase = obtenerFase()
            _uiState.update { it.copy(fase = fase) }
        }
    }

    private suspend fun refrescarLista() {
        val fase = obtenerFase()
        _uiState.update { it.copy(fase = fase) }
    }

    private suspend fun obtenerFase(): ProductoUiState.Fase =
        listarProductos().fold(
            onSuccess = { productos ->
                if (productos.isEmpty()) {
                    ProductoUiState.Fase.SinProductos
                } else {
                    ProductoUiState.Fase.ConProductos(productos.map { it.aUi() })
                }
            },
            onFailure = { fallo ->
                ProductoUiState.Fase.Error(
                    fallo.message ?: "No se pudo cargar el inventario"
                )
            }
        )

    fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(nombre = nombre, nombreError = null),
                mensajeExito = null
            )
        }
    }

    fun onPrecioChange(precio: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(precio = precio, precioError = null),
                mensajeExito = null
            )
        }
    }

    fun onStockChange(stock: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(stock = stock, stockError = null),
                mensajeExito = null
            )
        }
    }

    fun editar(producto: ProductoUi) {
        if (hayOperacionEnCurso()) return
        _uiState.update {
            it.copy(
                formulario = FormularioProducto(
                    idEnEdicion = producto.id,
                    activoEnEdicion = producto.activo,
                    nombre = producto.nombre,
                    precio = producto.precioValor,
                    stock = producto.stockValor
                ),
                operacion = Operacion.Inactiva,
                mensajeExito = null
            )
        }
    }

    fun cancelarEdicion() {
        _uiState.update { it.copy(formulario = FormularioProducto()) }
    }

    fun guardar() {
        if (_uiState.value.formulario.editando) actualizar() else registrar()
    }

    fun registrar() {
        if (hayOperacionEnCurso()) return
        marcarEnCurso(Tipo.Crear)

        viewModelScope.launch {
            val formulario = _uiState.value.formulario

            registrarProducto(
                nombre = formulario.nombre,
                precio = formulario.precio,
                stock = formulario.stock
            )
                .onSuccess { producto ->
                    refrescarLista()
                    _uiState.update {
                        it.copy(
                            operacion = Operacion.Inactiva,
                            formulario = FormularioProducto(),
                            mensajeExito = "Producto \"${producto.nombre}\" registrado correctamente"
                        )
                    }
                }
                .onFailure { fallo -> manejarFallo(fallo) }
        }
    }

    fun actualizar() {
        val id = _uiState.value.formulario.idEnEdicion ?: return
        if (hayOperacionEnCurso()) return
        marcarEnCurso(Tipo.Actualizar)

        viewModelScope.launch {
            val formulario = _uiState.value.formulario

            actualizarProducto(
                id = id,
                nombre = formulario.nombre,
                precio = formulario.precio,
                stock = formulario.stock,
                activo = formulario.activoEnEdicion
            )
                .onSuccess { producto ->
                    refrescarLista()
                    _uiState.update {
                        it.copy(
                            operacion = Operacion.Inactiva,
                            formulario = FormularioProducto(),
                            mensajeExito = "Producto \"${producto.nombre}\" actualizado correctamente"
                        )
                    }
                }
                .onFailure { fallo -> manejarFallo(fallo) }
        }
    }

    fun eliminar(id: Long) {
        if (hayOperacionEnCurso()) return
        marcarEnCurso(Tipo.Eliminar)

        viewModelScope.launch {
            eliminarProducto(id)
                .onSuccess {
                    refrescarLista()
                    _uiState.update {
                        it.copy(
                            operacion = Operacion.Inactiva,
                            formulario = if (it.formulario.idEnEdicion == id) {
                                FormularioProducto()
                            } else {
                                it.formulario
                            },
                            mensajeExito = "Producto dado de baja: ahora figura en Inactivos"
                        )
                    }
                }
                .onFailure { fallo -> manejarFallo(fallo) }
        }
    }

    private fun hayOperacionEnCurso(): Boolean =
        _uiState.value.operacion is Operacion.EnCurso

    private fun marcarEnCurso(tipo: Tipo) {
        _uiState.update {
            it.copy(
                operacion = Operacion.EnCurso(tipo),
                mensajeExito = null,
                formulario = it.formulario.copy(
                    nombreError = null,
                    precioError = null,
                    stockError = null
                )
            )
        }
    }

    private fun manejarFallo(fallo: Throwable) {
        val error = (fallo as? ErrorApiException)?.error

        when {
            fallo is ProductoInvalidoException -> _uiState.update {
                it.copy(
                    operacion = Operacion.Inactiva,
                    formulario = it.formulario.copy(
                        nombreError = fallo.errores.nombre,
                        precioError = fallo.errores.precio,
                        stockError = fallo.errores.stock
                    )
                )
            }

            error is ErrorApi.Validacion -> mostrarErroresDelServidor(error)

            else -> _uiState.update {
                it.copy(
                    operacion = Operacion.Fallida(
                        fallo.message ?: "No se pudo completar la operación"
                    )
                )
            }
        }
    }

    private fun mostrarErroresDelServidor(error: ErrorApi.Validacion) {
        val campos = error.porCampo
        val sinCampo = campos.filterKeys { it !in CAMPOS_DEL_FORMULARIO }

        val avisoGeneral = when {
            sinCampo.isNotEmpty() -> sinCampo.values.joinToString(". ")
            campos.isEmpty() -> error.mensajeUsuario()
            else -> null
        }

        _uiState.update {
            it.copy(
                operacion = avisoGeneral?.let { mensaje -> Operacion.Fallida(mensaje) }
                    ?: Operacion.Inactiva,
                formulario = it.formulario.copy(
                    nombreError = campos["nombre"],
                    precioError = campos["precio"],
                    stockError = campos["stock"]
                )
            )
        }
    }
    private val CAMPOS_DEL_FORMULARIO = setOf("nombre", "precio", "stock")
}