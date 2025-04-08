package com.flappybunnyfinal

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import java.util.Random

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class MainGame : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var bird: Bird
    lateinit var pipes: MutableList<Pipe>
    lateinit var pipes2: MutableList<Pipe>
    lateinit var pipeTexture: Texture
    lateinit var pipeTexture2: Texture
    lateinit var background: Background
    private val speed: Float = 200f
    private var pipeSpawnTimer = 0f
    private val pipeSpawnInterval = 2f
    private val pipeSpeed = 150f
    private val random = Random()
    private lateinit var gameOverScreen: GameOver
    var gameState: GameState = GameState.PLAYING // Add game state
    var score: Int = 0 // Add score

    override fun create() {
        batch = SpriteBatch()
        val birdTexture = Texture("Conejo.png")
        bird = Bird(100f, Gdx.graphics.height / 2f, birdTexture)

        pipeTexture = Texture("pipe.png")
        pipes = mutableListOf()
        generatePipes()

        pipeTexture2 = Texture("pipe2.png")
        pipes2 = mutableListOf()
        generatePipes2()

        val backgroundTexture = Texture("Fondo.png")
        background = Background(backgroundTexture)

        gameOverScreen = GameOver(this) // Initialize GameOverScreen

        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                if (gameState == GameState.PLAYING) {
                    bird.jump()
                } else if (gameState == GameState.GAME_OVER) {
                    resetGame()
                }
                return true
            }
        }
    }

    private fun generatePipes2() {
        TODO("Not yet implemented")
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()

        when (gameState) {
            GameState.PLAYING -> {
                background.update(Gdx.graphics.deltaTime, speed)
                background.render(batch, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
                bird.update(Gdx.graphics.deltaTime)
                bird.render(batch)

                pipeSpawnTimer += Gdx.graphics.deltaTime
                if (pipeSpawnTimer >= pipeSpawnInterval) {
                    generatePipes()
                    pipeSpawnTimer = 0f
                }

                for (pipe in pipes) {
                    pipe.update(Gdx.graphics.deltaTime, pipeSpeed)
                    pipe.render(batch)
                    if (bird.checkPipeCollision(pipe)) {
                        endGame()
                    }
                    if (pipe.bounds.x + pipeTexture.width < bird.x && !pipe.passed) {
                        score++
                        pipe.passed = true
                    }
                }

                if (bird.checkBoundsCollision(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())) {
                    endGame()
                }
            }
            GameState.GAME_OVER -> {
                gameOverScreen.render(Gdx.graphics.deltaTime)
            }
        }

        batch.end()
    }

    private fun endGame() {
        println("¡Fin del juego!")
        gameState = GameState.GAME_OVER
    }

    fun generatePipes() {
        val minGap = -20f // Espacio mínimo entre las tuberías
        val pipeHeight = pipeTexture.height.toFloat() + 750f// Altura de la tubería

        // La tubería superior comienza en la parte superior de la pantalla
        val pipe1Y = Gdx.graphics.height.toFloat() // Posición Y en el borde superior
        val pipe1 = Pipe(Gdx.graphics.width.toFloat(), pipe1Y, pipeTexture)

        //La tubería inferior se coloca a una distancia fija por debajo de la tubería superior
        val pipe2Y = Gdx.graphics.height - pipeHeight - minGap // Calcula la posición Y de la tubería inferior
        val pipe2 = Pipe(Gdx.graphics.width.toFloat(), pipe2Y, pipeTexture) // Posición Y de la tubería inferior

        pipes.add(pipe1)
        pipes.add(pipe2)
    }

    fun resetGame() {
        gameState = GameState.PLAYING
        score = 0
        bird.reset()
        pipes.clear()
        generatePipes()
        for (pipe in pipes) {
            pipe.passed = false
        }
    }

    override fun dispose() {
        batch.dispose()
        bird.dispose()
        for (pipe in pipes) {
            pipe.dispose()
        }

        pipeTexture.dispose()
        background.dispose()
        gameOverScreen.dispose()
    }
}
