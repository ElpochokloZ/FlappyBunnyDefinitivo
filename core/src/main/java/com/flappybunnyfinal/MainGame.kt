package com.flappybunnyfinal

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.audio.Sound
import java.util.Random

class MainGame : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var bird: Bird
    lateinit var pipes: MutableList<Pipe>
    lateinit var pipeTexture: Texture
    lateinit var background: Background
    lateinit var passSound: Sound
    private val speed: Float = 200f
    private var pipeSpawnTimer = 0f
    private val pipeSpawnInterval = 2.5f // Aumenta este valor (ejemplo: 2.5 segundos)
    private val pipeSpeed = 150f
    private val random = Random()
    private lateinit var gameOverScreen: GameOver
    var gameState: GameState = GameState.PLAYING
    var score: Int = 0

    override fun create() {
        batch = SpriteBatch()
        val birdTexture = Texture("Conejo.png")
        bird = Bird(100f, Gdx.graphics.height / 2f, birdTexture)

        pipeTexture = Texture("pipe.png")
        pipes = mutableListOf()
        generatePipePair()

        passSound = Gdx.audio.newSound(Gdx.files.internal("Coin.mp3")) // Carga el archivo de sonido

        val backgroundTexture = Texture("Fondo.png")
        background = Background(backgroundTexture)

        gameOverScreen = GameOver(this)

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

    fun generatePipePair() {
        val fixedGap = 300f
        val maxBottomPipeHeight = Gdx.graphics.height - fixedGap - 100f
        val bottomPipeHeight = random.nextFloat() * maxBottomPipeHeight + 100f

        val pipeX = Gdx.graphics.width.toFloat()

        val bottomPipeY = 0f
        val bottomPipe = Pipe(pipeX, bottomPipeY, pipeTexture, desiredHeight = bottomPipeHeight)

        val topPipeY = Gdx.graphics.height.toFloat()
        val topPipeHeight = Gdx.graphics.height - (bottomPipeHeight + fixedGap)
        val topPipe = Pipe(pipeX, topPipeY, pipeTexture, flipped = true, desiredHeight = topPipeHeight)

        pipes.add(bottomPipe)
        pipes.add(topPipe)
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
                    generatePipePair()
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
                        passSound.play()
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

    fun resetGame() {
        gameState = GameState.PLAYING
        score = 0
        bird.reset()
        pipes.clear()
        generatePipePair()
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
        passSound.dispose()
        gameOverScreen.dispose()
    }
}
