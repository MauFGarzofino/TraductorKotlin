package com.mau.basededatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var  editTextPassword: TextInputEditText
    private lateinit var  buttonLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var ButtonReg: Button

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent= Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressBar)
        ButtonReg = findViewById(R.id.btn_reg_now)
        auth = FirebaseAuth.getInstance()

        ButtonReg.setOnClickListener{view ->
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }
        buttonLogin.setOnClickListener {view ->
            progressBar.visibility = View.VISIBLE
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this@Login, "Introduce el email", Toast.LENGTH_SHORT).show()
                val intent= Intent(this@Login, Login::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this@Login, "Introduce la contraseña", Toast.LENGTH_SHORT).show()
                val intent= Intent(this@Login, Login::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "Sesion Iniciada", Toast.LENGTH_SHORT,).show()
                        val intent= Intent(this@Login, UI::class.java)
                        //Pasa los datos a activityUi
                        intent.putExtra("Usuario",editTextEmail.text.toString())
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(baseContext, "Fallo en inicio de sesión", Toast.LENGTH_SHORT,).show()
                    }
                }
        }
    }
}
