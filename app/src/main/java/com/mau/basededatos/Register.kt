package com.mau.basededatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

public class Register : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonLiciniaSession: Button
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent= Intent(this@Register, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonReg= findViewById(R.id.btn_register)
        progressBar= findViewById(R.id.progressBar)
        buttonLiciniaSession = findViewById(R.id.btn_inises)
        auth = FirebaseAuth.getInstance()


        buttonLiciniaSession.setOnClickListener { view ->
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
            finish()
        }

        buttonReg.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                progressBar.visibility = View.VISIBLE
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()

                if (password.isEmpty()) {
                    Toast.makeText(this@Register, "Enter password", Toast.LENGTH_SHORT).show()
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        progressBar.visibility= View.GONE
                        if (task.isSuccessful) {
                            Toast.makeText(this@Register, "Cuentra creada.",
                                Toast.LENGTH_SHORT,).show()
                            val intent= Intent(this@Register, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                this@Register,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        })
    }
}


