package com.mau.basededatos
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.mau.basededatos.R.id.regresar
class Dictionary : AppCompatActivity() {
    private lateinit var editTextSearch: EditText
    private lateinit var btnSearch: MaterialButton
    private lateinit var txtResults: TextView
    private lateinit var bregresar : ImageView
    private val db = FirebaseFirestore.getInstance()
    private val dictionaryCollection = db.collection("dictionary")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)
        editTextSearch = findViewById(R.id.editTxtSearch)
        btnSearch = findViewById(R.id.btnSubmit)
        txtResults = findViewById(R.id.txtViewResult)
        bregresar = findViewById(R.id.regresar)
        btnSearch.setOnClickListener {
            val searchWord = editTextSearch.text.toString().trim()
            searchDictionary(searchWord)
        }
        bregresar.setOnClickListener { view->
            val intent = Intent(this@Dictionary, UI::class.java)
            startActivity(intent)
        }
    }
    private fun searchDictionary(word: String) {
        dictionaryCollection
            .whereEqualTo("word", word)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val resultDocument = querySnapshot.documents[0]
                    val typeWord = resultDocument.getString("typeWord")
                    val definition = resultDocument.getString("description")
                    val resultText = "$typeWord\n$definition"
                    txtResults.text = resultText
                } else {
                    txtResults.text = "Word not found"
                }
            }
            .addOnFailureListener { exception ->
                txtResults.text = "Error: ${exception.message}"
            }
    }
}