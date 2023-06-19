package com.mau.basededatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout


class UI : AppCompatActivity() {

    private lateinit var bTraductor: LinearLayout
    private lateinit var bVocabulario: LinearLayout
    private lateinit var bHist: LinearLayout
    private lateinit var bJuegos: LinearLayout
    private lateinit var bsalirCuenta: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui)
        bTraductor = findViewById(R.id.translator)
        bVocabulario = findViewById(R.id.vocabulary)
        bHist = findViewById(R.id.record)
        bJuegos = findViewById(R.id.games)
        bsalirCuenta = findViewById(R.id.logout)

        bTraductor.setOnClickListener {view ->
            val intent = Intent(this@UI, VistaTraductor::class.java)
            startActivity(intent)
        }
        bVocabulario.setOnClickListener {view ->
            val intent = Intent(this@UI, Dictionary::class.java)
            startActivity(intent)
        }
        bHist.setOnClickListener {view ->
            val intent = Intent(this@UI, MainActivity::class.java)
            startActivity(intent)
        }
        bJuegos.setOnClickListener {view ->
            val intent = Intent(this@UI, Quiz::class.java)
            startActivity(intent)
        }
        bsalirCuenta.setOnClickListener {view ->
            val intent = Intent(this@UI, Login::class.java)
            startActivity(intent)
        }

    }
}