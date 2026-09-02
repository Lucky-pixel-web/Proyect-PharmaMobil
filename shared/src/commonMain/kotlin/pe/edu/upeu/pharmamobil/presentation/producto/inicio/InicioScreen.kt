package pe.edu.upeu.pharmamobil.presentation.inicio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pharmamobil.shared.generated.resources.Res
import pharmamobil.shared.generated.resources.img

@Composable
fun InicioScreen() {

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 600)) +
                    slideInVertically(
                        initialOffsetY = { it / 4 },
                        animationSpec = tween(durationMillis = 600)
                    )
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(Res.drawable.img),
                    contentDescription = "Logo PharmaMobil",
                    modifier = Modifier
                        .size(96.dp)
                        .padding(bottom = 12.dp)
                )

                Text(
                    text = "PharmaMobil",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Sistema de gestión farmacéutica",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}