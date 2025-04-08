package com.flappybunnyfinal.android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.flappybunnyfinal.R

class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        findViewById<Button>(R.id.playButton).setOnClickListener {
            val intent = Intent(this, AndroidLauncher::class.java)
            startActivity(intent)
            // Opcional: agregar animación de transición
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onBackPressed() {
        // Salir de la app si presionan back en el splash
        finishAffinity()
    }
}
