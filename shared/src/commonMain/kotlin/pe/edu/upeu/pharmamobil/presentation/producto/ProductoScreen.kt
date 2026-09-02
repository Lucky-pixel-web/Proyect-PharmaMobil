package pe.edu.upeu.pharmamobil.presentation.producto

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import pe.edu.upeu.pharmamobil.domain.model.Producto
import pe.edu.upeu.pharmamobil.domain.productosMock

@Composable
fun ProductoScreen() {

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var precioError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }

    var mensajeExito by remember { mutableStateOf<String?>(null) }

    val productosRegistrados = remember { mutableStateListOf<Producto>() }
    var mostrarLista by remember { mutableStateOf(false) }

    var tabSeleccionada by remember { mutableStateOf(0) }
    val titulosTabs = listOf("Activos", "Inactivos", "Bajo stock")

    fun validar(): Boolean {
        val soloLetras = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")
        nombreError = when {
            nombre.isBlank() -> "Ingrese nombre del producto, es obligatorio"
            !soloLetras.matches(nombre) -> "El nombre no debe contener números"
            else -> null
        }

        val precioValor = precio.toDoubleOrNull()
        precioError = when {
            precioValor == null -> "Ingrese un precio numerico"
            precioValor <= 0 -> "El precio debe ser mayor que cero"
            else -> null
        }

        val stockValor = stock.toIntOrNull()
        stockError = when {
            stockValor == null -> "Ingrese un stock entero"
            stockValor < 0 -> "El stock no puede ser negativo"
            else -> null
        }

        return nombreError == null && precioError == null && stockError == null
    }

    val productosFiltrados = when (tabSeleccionada) {
        0 -> productosMock.filter { it.activo && !it.esBajoStock() }
        1 -> productosMock.filter { !it.activo }
        2 -> productosMock.filter { it.esBajoStock() }
        else -> emptyList()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            "PharmaMobil",
            color = MaterialTheme.colorScheme.primary
        )
        Text("Registro de Producto")

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            isError = nombreError != null,
            supportingText = { nombreError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            isError = precioError != null,
            supportingText = { precioError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock") },
            isError = stockError != null,
            supportingText = { stockError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                mensajeExito = null
                if (validar()) {
                    val nuevoProducto = Producto(
                        id = Random.nextLong(1, Long.MAX_VALUE),
                        nombre = nombre,
                        precio = precio.toDouble(),
                        stock = stock.toInt()
                    )
                    productosRegistrados.add(0, nuevoProducto)
                    mensajeExito = "Producto registrado correctamente"
                    println(nuevoProducto)
                    nombre = ""
                    precio = ""
                    stock = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        mensajeExito?.let { Text(it) }

        OutlinedButton(
            onClick = { mostrarLista = !mostrarLista },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (mostrarLista) "Ocultar registrados (${productosRegistrados.size})"
                else "Ver productos registrados (${productosRegistrados.size})"
            )
        }

        AnimatedVisibility(
            visible = mostrarLista,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                items(productosRegistrados) { p ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(p.nombre)
                            Text("S/ ${p.precio}  ·  Stock: ${p.stock}")
                        }
                    }
                }
            }
        }

        Text("Inventario (Mock)")

        TabRow(selectedTabIndex = tabSeleccionada) {
            titulosTabs.forEachIndexed { index, titulo ->
                Tab(
                    selected = tabSeleccionada == index,
                    onClick = { tabSeleccionada = index },
                    text = { Text(titulo) }
                )
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            items(productosFiltrados) { p ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(p.nombre)
                        Text("S/ ${p.precio}  ·  Stock: ${p.stock}")
                    }
                }
            }
        }
    }
}