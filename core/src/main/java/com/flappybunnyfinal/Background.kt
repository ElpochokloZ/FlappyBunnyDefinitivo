package com.flappybunnyfinal

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Background(texture: Texture) {
    val texture: Texture = texture
    var x: Float = 0f

    fun update(deltaTime: Float, speed: Float) {
        x -= speed * deltaTime
        // Si el fondo se sale de la pantalla, reinicia la posición
        if (x < -texture.width) {
            x += texture.width
        }
    }

    fun render(batch: SpriteBatch, screenWidth: Float, screenHeight: Float) {
        val numCopies = (screenWidth / texture.width).toInt() + 2 // Calcula el número de copias necesarias
        for (i in 0 until numCopies) {
            batch.draw(texture, x + i * texture.width, 0f, texture.width.toFloat(), screenHeight)
        }
    }

    fun dispose() {
        texture.dispose()
    }
}
