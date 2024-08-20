package com.uvg.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.uvg.lemonade.ui.theme.LemonadeTheme
//Clase principal que hereda componentActivity
class MainActivity : ComponentActivity() {
    // Método onCreate: punto de entrada para la actividad cuando se crea
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Activa el diseño de pantalla completa sin bordes
        super.onCreate(savedInstanceState) // Llama al método onCreate de la clase base
        setContent {
            // Establece el contenido de la actividad utilizando Jetpack Compose
            LemonadeTheme { // Aplica el tema definido para la aplicación
                LemonadeApp() // Llama a la función composable principal que construye la interfaz de usuario
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LemonadeApp() {
    // Variable mutable que guarda el estado actual del paso en el proceso de hacer limonada
    var currentStep by remember { mutableStateOf(1) }
    // Variable mutable que guarda el conteo de cuántas veces se debe exprimir el limón
    var squeezeCount by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lemonade", // Texto que aparece en la barra superior
                    )
                },
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Controla la lógica de navegación entre los pasos del proceso
            when (currentStep) {
                1 -> {
                    // Paso 1: Selección de un limón del árbol
                    LemonTextAndImage(
                        textLabelResourceId = R.string.lemon_select,// Texto que explica la acción
                        drawableResourceId = R.drawable.lemon_tree, // Imagen del árbol de limones
                        contentDescriptionResourceId = R.string.lemon_tree_content_description, // Descripción de la imagen para accesibilidad
                        onImageClick = {
                            currentStep = 2
                            squeezeCount = (2..6).random() // Genera aleatoriamente cuántas veces hay que exprimir el limón
                        }
                    )
                }
                2 -> {
                    // Paso 2: Exprimir el limón
                    LemonTextAndImage(
                        textLabelResourceId = R.string.lemon_squeeze,
                        drawableResourceId = R.drawable.lemon_squeeze,
                        contentDescriptionResourceId = R.string.lemon_content_description,
                        onImageClick = {
                            squeezeCount-- // Decrementa el número de veces que queda exprimir
                            if (squeezeCount == 0) {
                                currentStep = 3 // Si se ha exprimido suficiente, avanza al siguiente paso
                            }
                        }
                    )
                }

                3 -> {
                    // Paso 3: Beber la limonada
                    LemonTextAndImage(
                        textLabelResourceId = R.string.lemon_drink, // Texto que indica beber la limonada
                        drawableResourceId = R.drawable.lemon_drink, // Imagen de beber limonada
                        contentDescriptionResourceId = R.string.lemonade_content_description,
                        onImageClick = {
                            currentStep = 4 // Avanza al último paso
                        }
                    )
                }
                4 -> {
                    // Paso 4: Vaso vacío, reiniciar el proceso
                    LemonTextAndImage(
                        textLabelResourceId = R.string.lemon_empty_glass,
                        drawableResourceId = R.drawable.lemon_restart,
                        contentDescriptionResourceId = R.string.empty_glass_content_description,
                        onImageClick = {
                            currentStep = 1 // Reinicia el proceso volviendo al paso 1
                        }
                    )
                }
            }
        }
    }
}
// Función composable que muestra un texto y una imagen, con un comportamiento al hacer clic
@Composable
fun LemonTextAndImage(
    textLabelResourceId: Int, // ID del recurso de texto para el texto que describe la acción
    drawableResourceId: Int, // ID del recurso drawable para la imagen a mostrar
    contentDescriptionResourceId: Int, // ID del recurso de texto para la descripción de accesibilidad de la imagen
    onImageClick: () -> Unit, // Lambda que define la acción a realizar cuando se hace clic en la imagen
    modifier: Modifier = Modifier // Modificador opcional para aplicar al contenedo
) {
    Box(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Botón que contiene una imagen y que ejecuta onImageClick cuando se presiona
            Button(
                onClick = onImageClick,
                shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                // Imagen que se muestra dentro del botón
                Image(
                    painter = painterResource(drawableResourceId), // Asigna la imagen a partir del recurso drawable
                    contentDescription = stringResource(contentDescriptionResourceId),
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.button_image_width))
                        .height(dimensionResource(R.dimen.button_image_height))
                        .padding(dimensionResource(R.dimen.button_interior_padding))
                )
            }
            // Texto que aparece debajo de la imagen, describiendo la acción actual
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_vertical)))
            Text(
                text = stringResource(textLabelResourceId),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun LemonPreview() {
    LemonadeTheme {
        LemonadeApp()
    }
}