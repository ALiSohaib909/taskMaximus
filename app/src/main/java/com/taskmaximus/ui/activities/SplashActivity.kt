package com.taskmaximus.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.taskmaximus.R
import com.taskmaximus.ui.activities.mainAct.MainActivity
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    lateinit var btnGo: Button
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        btnGo = findViewById(R.id.btnGo)
        progressBar = findViewById(R.id.progressBar)

        btnGo.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val delay: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                } catch (e: InterruptedException) {
                }
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    btnGo.visibility = View.VISIBLE
                }
            }
        }
        delay.start()
    }

}
