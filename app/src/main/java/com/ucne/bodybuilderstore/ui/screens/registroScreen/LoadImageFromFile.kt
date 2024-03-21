package com.ucne.bodybuilderstore.ui.screens.registroScreen

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import java.io.File

@OptIn(ExperimentalCoilApi::class)
@Composable
fun LoadImageFromFile(
    filePath: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    painterBuilder: (ImageRequest.Builder.() -> Unit) = {}
) {
    val context = LocalContext.current

    val painter: Painter = rememberImagePainter(
        data = File(filePath),
        builder = {
            scale(Scale.FILL)
            crossfade(true)
            this.apply(painterBuilder)
        }
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}