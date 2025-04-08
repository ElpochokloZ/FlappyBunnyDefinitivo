package com.flappybunnyfinal

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport

class GameOver(private val game:MainGame) {
    private val stage: Stage = Stage(ScreenViewport())
    private val batch: SpriteBatch = SpriteBatch()
    private val fuente: BitmapFont
    private val restartButton: TextButton
    private val scoreLabel: Label
    private val gameOverText: Label
    private val background: Texture = Texture("Fondo.png")

    init {
        Gdx.input.inputProcessor = stage

        // Generar fuente con FreeType
        val generator = FreeTypeFontGenerator(Gdx.files.internal("Roboto_Condensed-Black.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = 45 // Cambia este valor para aumentar o disminuir el tamaño de la fuente
            color = Color.WHITE // Puedes establecer el color aquí si lo deseas
        }
        fuente = generator.generateFont(parameter)
        generator.dispose() // No olvides liberar el generador

        // Button style
        val buttonStyle = TextButton.TextButtonStyle().apply {
            this.font = fuente // Usa la fuente generada
        }

        // Create buttons
        restartButton = TextButton("Restart", buttonStyle).apply {
            setPosition(Gdx.graphics.width / 2f - width / 2f, Gdx.graphics.height / 2f - height / 2f)
            addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.resetGame()
                }
            })
        }

        // Create score label
        val labelStyle = Label.LabelStyle(fuente, Color.WHITE)
        scoreLabel = Label("Score: 0", labelStyle).apply {
            setPosition(Gdx.graphics.width / 2f - width / 2f, Gdx.graphics.height / 2f + 100)
        }
        gameOverText = Label("Game Over", labelStyle).apply {
            setPosition(Gdx.graphics.width / 2f - width / 2f, Gdx.graphics.height / 2f + 200)
        }

        // Add actors to the stage
        stage.addActor(restartButton)
        stage.addActor(scoreLabel)
        stage.addActor(gameOverText)
    }

    fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.end()

        stage.act(delta)
        stage.draw()
        // Update score label
        scoreLabel.setText("Score: ${game.score / 2}")
    }

    fun dispose() {
        stage.dispose()
        batch.dispose()
        fuente.dispose()
    }
}
