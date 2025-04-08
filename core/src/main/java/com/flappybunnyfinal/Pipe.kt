package com.flappybunnyfinal

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class Pipe(x:Float, y:Float, val texture: Texture) {
    val bounds: Rectangle = Rectangle(x, y, texture.width.toFloat(), texture.height.toFloat())
    var passed: Boolean = false

    fun render(batch: SpriteBatch) {
        val width = texture.width.toFloat()
        val height = texture.height.toFloat()
        batch.draw(texture, bounds.x, bounds.y, width, height)
        bounds.width = width
        bounds.height = height
    }

    fun update(deltaTime: Float, speed: Float) {
        bounds.x -= speed * deltaTime
    }

    fun dispose() {
        texture.dispose()
    }
}
