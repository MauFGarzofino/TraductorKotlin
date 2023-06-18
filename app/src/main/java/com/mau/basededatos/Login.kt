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

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.btnLogin.setOnClickListener(){
            val Nombre=binding.name.text.toString()
            val Password=binding.password.text.toString()
            db.collection("users")
                .whereEqualTo("Nombre",Nombre)
                .whereEqualTo("Password",Password)
                .get()
                .addOnSuccessListener {documents ->
                    if(!documents.isEmpty){
                        Toast.makeText(this,"Inicio de sesion exitoso" , Toast.LENGTH_LONG).show();
                        val intent = Intent(this@Login, UI::class.java)
                        intent.putExtra("Usuario",Nombre)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,"Inicio de sesion fallido, verifique los datos" , Toast.LENGTH_LONG).show();
                    }
                }


        }

    }
}