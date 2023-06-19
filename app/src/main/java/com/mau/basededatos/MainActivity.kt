package com.mau.basededatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore
    val TAG = "datos"
    private lateinit var button : Button
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        button = findViewById(R.id.logout)

        if(user == null){
            val intent= Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
            finish()
        }
        else{
            Log.w(TAG, "Problema1.")

        }
        getData()
        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent= Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun getData(){
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val data = ArrayList<ItemsViewModel>()

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    data.add(ItemsViewModel("Clientes",document.data.get("direccion").toString()))
                    data.add(ItemsViewModel("Clientes",document.data.get("telef").toString()))
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                val adapter = CustomAdapter(data)
                recyclerview.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}