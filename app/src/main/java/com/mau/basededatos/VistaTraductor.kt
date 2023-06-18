package com.mau.basededatos

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display.Mode
import android.view.Menu
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.mau.basededatos.databinding.ActivityLoginBinding
import com.mau.basededatos.databinding.ActivityVistaTraductorBinding
import java.util.*
import kotlin.collections.ArrayList

class VistaTraductor : AppCompatActivity() {

    private lateinit var idiomaBase: EditText
    private lateinit var idiomaTraducido: TextView
    private lateinit var idiomaBaseSeleccionBtn: MaterialButton
    private lateinit var idiomaTraducidoSeleccionBtn: MaterialButton
    private lateinit var traducirBtn: MaterialButton
    private lateinit var binding: ActivityVistaTraductorBinding
    private lateinit var db: FirebaseFirestore


    companion object{
        //para imprimir logs
        private const val TAG = "MAIN_TAG"
    }

    //
    private var languageArrayList: ArrayList<ModelarIdioma>? = null


    //Seleccionamos los idiomas código y tpitulo
    private var sourceLanguageCode = "en"
    private var sourceLanguageTitle = "English"
    private var targetLanguageCode = "ur"
    private var targetLanguageTitle = "Urdu"

    private lateinit var  translatorOptions: TranslatorOptions
    private lateinit var translator: Translator

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_traductor)

        idiomaBase = findViewById(R.id.lenguajeFuente)
        idiomaTraducido = findViewById(R.id.targetLanguageTv)
        idiomaBaseSeleccionBtn = findViewById(R.id.lenguajeFuenteSelectorBtn)
        idiomaTraducidoSeleccionBtn = findViewById(R.id.targetLanguageChooseBtn)
        traducirBtn = findViewById(R.id.traductorBtn)

        //init setup profress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espera")
        progressDialog.setCanceledOnTouchOutside(false)

        cargarIdiomaDisponible()


        idiomaBaseSeleccionBtn.setOnClickListener {
            sourceLanguageChoose()
        }
        idiomaTraducidoSeleccionBtn.setOnClickListener {
            targetLanguageChoose()
        }
        traducirBtn.setOnClickListener {
            validateData()
        }
    }

    private var sourceLanguageText = ""
    private fun validateData() {

        sourceLanguageText = idiomaBase.text.toString().trim()

        Log.d(TAG, "ValidateData: SourceLanguageText: $sourceLanguageText")

        if(sourceLanguageText.isEmpty()){
            showToast("Introduce un texto para traducir")
        }
        else{
            iniciarTraduccion()
        }
    }

    private fun iniciarTraduccion(){
        progressDialog.setMessage("Procesando el modelo de lenguaje")
        progressDialog.show()

        binding = ActivityVistaTraductorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()


        translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguageCode)
            .setTargetLanguage(targetLanguageCode)
            .build()
        translator = Translation.getClient(translatorOptions)

        val downloadConditions = DownloadConditions.Builder()
            .requireWifi()
            .build()


        translator.downloadModelIfNeeded(downloadConditions)
            .addOnSuccessListener {
                Log.d(TAG, "iniciarTraduccion: modelo listo, iniciar traduccion")

                progressDialog.setMessage("Traduciendo")

                translator.translate(sourceLanguageText)
                    .addOnSuccessListener { translatedText ->

                        Log.d(TAG, "iniciarTraduccion: translatedText: $translatedText")

                        progressDialog.dismiss()

                        idiomaTraducido.text = translatedText

                        val bundle=intent.extras
                        val user= bundle?.getString("Usuario")

                        val dato = hashMapOf(
                            "Traducir" to binding.lenguajeFuente.text.toString(),
                            "Traducido" to translatedText
                        )

                        if (user != null) {
                            db.collection(user)
                                .document()
                                .set(dato)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Añadido al historial", Toast.LENGTH_LONG).show()
                                }
                        }
                    }
                    .addOnFailureListener{ e->
                        progressDialog.dismiss()
                        Log.e(TAG, "iniciarTraduccion",e)

                        showToast("Fallo al truducir debido a ${e.message}")
                    }
            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()
                Log.e(TAG, "iniciarTraduccion",e)
                showToast("Fallo al truducir debido a ${e.message}")
            }
    }

    private fun cargarIdiomaDisponible(){
        languageArrayList = ArrayList()

        val languageCodeList = TranslateLanguage.getAllLanguages()

        for(languageCode in languageCodeList){

            val languageTitle = Locale(languageCode).displayLanguage

            Log.d(TAG, "cargarIdiomaDisponible: languageCode: $languageCode")
            Log.d(TAG, "cargarIdiomaDisponible: languageCode: $languageTitle")

            val modelarIdioma = ModelarIdioma(languageCode,languageTitle)

            languageArrayList!!.add(modelarIdioma)
        }
    }
    private fun sourceLanguageChoose(){

        val popupMenu = PopupMenu(this, idiomaBaseSeleccionBtn)

        for(i in languageArrayList!!.indices){


            popupMenu.menu.add(Menu.NONE, i, i, languageArrayList!![i].languageTitle)
        }

        popupMenu.show()


        popupMenu.setOnMenuItemClickListener { menuItem ->

            val position = menuItem.itemId

            sourceLanguageCode = languageArrayList!![position].languageCode
            sourceLanguageTitle = languageArrayList!![position].languageTitle

            // Coloca el idioma seleccionado a idiomaBaseSeleccionBtn como texto y idiomaBase como pista

            idiomaBaseSeleccionBtn.text = sourceLanguageTitle
            idiomaBase.hint = "Enter $sourceLanguageTitle"

            Log.d(TAG, "idioma base elegido: languageCode: $sourceLanguageCode")
            Log.d(TAG, "idioma base elegido: languageCode: $sourceLanguageTitle")

            false
        }
    }

    private fun targetLanguageChoose(){

        val popupMenu = PopupMenu(this, idiomaTraducidoSeleccionBtn)

        for (i in languageArrayList!!.indices){

            popupMenu.menu.add(Menu.NONE, i, i, languageArrayList!![i].languageTitle)
        }

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { menuItem->

            val position = menuItem.itemId


            targetLanguageCode = languageArrayList!![position].languageCode
            targetLanguageTitle = languageArrayList!![position].languageTitle

            idiomaTraducidoSeleccionBtn.text = targetLanguageTitle

            Log.d(TAG, "idiomaTraducidoSeleccion: targetLanguageCode: $targetLanguageCode")
            Log.d(TAG, "idiomaTraducidoSeleccion: targetLanguageTitle: $targetLanguageTitle")
            false
        }

    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}