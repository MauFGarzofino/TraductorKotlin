package com.mau.basededatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class AgregarNombre : AppCompatActivity() {

    private lateinit var bNombre : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nombre)

        bNombre = findViewById(R.id.btn_login)

        bNombre.setOnClickListener{view ->
            val intent = Intent(this@AgregarNombre, UI::class.java)
            startActivity(intent)
        }
    }
}