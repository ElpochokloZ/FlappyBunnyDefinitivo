package com.flappybunnyfinal

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class Pipe(xStart: Float, yStart: Float, val texture: Texture, val flipped: Boolean = false, val desiredHeight: Float = texture.height.toFloat()) {
    var x = xStart
    var y = yStart
    var passed = false

    val width = texture.width.toFloat()
    val height = desiredHeight // Usamos la altura deseada aquí

    val bounds = Rectangle(
        x,
        if (flipped) y - height else y,
        width,
        height
    )

    fun update(delta: Float, speed: Float) {
        x -= speed * delta
        bounds.setPosition(x, if (flipped) y - height else y)
    }

    fun render(batch: SpriteBatch) {
        if (flipped) {
            // Dibuja invertido desde la posición Y original hacia abajo con la nueva altura
            batch.draw(texture, x, y, width, -desiredHeight)
        } else {
            // Dibuja con la nueva altura
            batch.draw(texture, x, y, width, desiredHeight)
        }
    }

    fun dispose() {
        texture.dispose()
    }
}
