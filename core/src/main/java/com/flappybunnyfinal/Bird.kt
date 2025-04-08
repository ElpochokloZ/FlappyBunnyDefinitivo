package com.flappybunnyfinal

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class Bird(var x: Float, var y: Float, val texture: Texture) {

    var velocity: Float = 0f
    val gravity: Float = -30f // Adjust gravity as needed
    val bounds: Rectangle = Rectangle(x, y, texture.width.toFloat() / 5, texture.height.toFloat() / 5)

    fun update(delta: Float) {
        velocity += gravity * delta
        y += velocity
        bounds.y = y
    }

    fun jump() {
        velocity = 12.5f // Adjust jump force as needed
    }

    fun render(batch: SpriteBatch) {
        val width = texture.width.toFloat() / 5
        val height = texture.height.toFloat() / 5

        batch.draw(texture, x, y, width, height)

        bounds.width = width
        bounds.height = height
        bounds.x = x
    }

    fun checkBoundsCollision(screenWidth: Float, screenHeight: Float): Boolean {
        return y > screenHeight || y < 0
    }

    fun checkPipeCollision(pipe: Pipe): Boolean {
        return bounds.overlaps(pipe.bounds)
    }

    fun reset() {
        x = 100f
        y = Gdx.graphics.height / 2f
        velocity = 0f
    }

    fun dispose() {
        texture.dispose()
    }
}
