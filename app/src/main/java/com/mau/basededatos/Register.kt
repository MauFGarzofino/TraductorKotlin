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
import com.google.firebase.firestore.FirebaseFirestore
import com.mau.basededatos.databinding.ActivityLoginBinding
import com.mau.basededatos.databinding.ActivityRegisterBinding

public class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener{
            val Nombre=binding.name.text.toString()
            val Email=binding.Email.text.toString()
            val Password=binding.password.text.toString()

            if(Nombre.isNotBlank()&&Email.isNotBlank()&&Password.isNotBlank()){
                val dato= hashMapOf(
                    "Nombre" to Nombre,
                    "Email" to Email,
                    "Password" to Password
                )
                db.collection("users")
                    .document(Nombre)
                    .set(dato)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Registro fallido, revise los datos", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_LONG).show()
            }
            }
        }
    }


