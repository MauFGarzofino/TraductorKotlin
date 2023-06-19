package com.mau.basededatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.checkerframework.checker.units.qual.A

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore
    val TAG = "datos"
    private lateinit var button : Button
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getData()

    }
    private fun getData(){
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        val data = ArrayList<ItemsViewModel>()
        val bundle=intent.extras
        val user= bundle?.getString("Usuario")

        if (user != null) {
            db.collection(user)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        data.add(ItemsViewModel( document.data.get("Traducir").toString(), document.data.get("Traducido").toString()))
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
}