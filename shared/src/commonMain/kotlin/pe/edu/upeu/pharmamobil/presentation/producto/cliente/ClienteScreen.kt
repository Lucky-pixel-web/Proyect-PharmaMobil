package pe.edu.upeu.pharmamobil.presentation.cliente

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import pe.edu.upeu.pharmamobil.domain.model.Cliente

@Composable
fun ClienteScreen() {

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var telefonoError by remember { mutableStateOf<String?>(null) }

    var mensajeExito by remember { mutableStateOf<String?>(null) }

    val clientesRegistrados = remember { mutableStateListOf<Cliente>() }
    var mostrarLista by remember { mutableStateOf(false) }

    fun validar(): Boolean {

        nombreError = ClienteValidator.validarNombre(nombre)
        correoError = ClienteValidator.validarCorreo(correo)
        telefonoError = ClienteValidator.validarTelefono(telefono)

        return nombreError == null && correoError == null && telefonoError == null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("PharmaMobil")
        Text("Registro de Cliente")

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            isError = nombreError != null,
            supportingText = { nombreError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            isError = correoError != null,
            supportingText = { correoError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { nuevo ->
                if (nuevo.all { it.isDigit() }) telefono = nuevo
            },
            label = { Text("Teléfono") },
            isError = telefonoError != null,
            supportingText = { telefonoError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                mensajeExito = null
                if (validar()) {
                    val nuevoCliente = Cliente(
                        id = Random.nextLong(1, Long.MAX_VALUE),
                        nombre = nombre,
                        correo = correo,
                        telefono = telefono
                    )
                    clientesRegistrados.add(0, nuevoCliente)
                    mensajeExito = "Cliente \"$nombre\" registrado correctamente"
                    nombre = ""
                    correo = ""
                    telefono = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        mensajeExito?.let {
            Text(it)
        }
        OutlinedButton(
            onClick = { mostrarLista = !mostrarLista },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (mostrarLista) "Ocultar registrados (${clientesRegistrados.size})"
                else "Ver clientes registrados (${clientesRegistrados.size})"
            )
        }

        AnimatedVisibility(
            visible = mostrarLista,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                items(clientesRegistrados) { c ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(c.nombre)
                            Text("${c.correo}  ·  Tel: ${c.obtenerTelefono()}")
                        }
                    }
                }
            }
        }
    }
}