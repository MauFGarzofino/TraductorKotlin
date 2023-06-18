package com.mau.basededatos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Dictionary : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dataList: ArrayList<String>
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        listView = findViewById(R.id.list_view)
        searchView = findViewById(R.id.search_view)

        dataList = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList)
        listView.adapter = adapter

        databaseRef = FirebaseDatabase.getInstance().reference.child("dictionary")

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterDictionary(newText)
                return false
            }
        })

        retrieveDictionary()
    }

    private fun retrieveDictionary() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (wordSnapshot in dataSnapshot.children) {
                    val word = wordSnapshot.key.toString()
                    dataList.add(word)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error al recuperar los datos del diccionario
            }
        })
    }

    private fun filterDictionary(query: String) {
        val filteredData = dataList.filter { it.contains(query) }
        adapter.clear()
        adapter.addAll(filteredData)
        adapter.notifyDataSetChanged()
    }
}
