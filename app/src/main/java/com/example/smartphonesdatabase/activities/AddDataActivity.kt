package com.example.smartphonesdatabase.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smartphonesdatabase.R
import com.example.smartphonesdatabase.models.Smartphone
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_data.*
import java.util.*

class AddDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        supportActionBar?.title = "Dodaj smartfon:"

        performOnLeaveListener()

        addSmartphone_button.setOnClickListener {
            addSmartphoneToDb()
        }
    }

    private fun performOnLeaveListener() {
        brandInput.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                if(brandInput.text.isEmpty()) {
                    Toast.makeText(this, "Pole 'Producent' nie może być puste.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        modelInput.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                if(modelInput.text.isEmpty()) {
                    Toast.makeText(this, "Pole 'Model' nie może być puste.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        systemVersionInput.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                if(systemVersionInput.text.isEmpty()) {
                    Toast.makeText(this, "Pole 'Wersja systemu' nie może być puste.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        websiteInput.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                if(websiteInput.text.isEmpty()) {
                    Toast.makeText(this, "Pole 'WWW' nie może być puste.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addSmartphoneToDb() {
        if(brandInput.text.isEmpty() ||
            modelInput.text.isEmpty() ||
            systemVersionInput.text.isEmpty() ||
            websiteInput.text.isEmpty()) {
            Toast.makeText(this, "Wszystkie pola muszą być wypełnione", Toast.LENGTH_SHORT).show()
        }
        else {
            val id = UUID.randomUUID().toString()
            val brand = brandInput.text.toString()
            val model = modelInput.text.toString()
            val systemVersion = systemVersionInput.text.toString()
            val website = websiteInput.text.toString()

            val ref = FirebaseDatabase.getInstance().getReference("/smartphones/$id")
            val smartphone = Smartphone(id, brand, model, systemVersion, website)

            ref.setValue(smartphone)
                .addOnSuccessListener {
                    println("git")
                }
                .addOnFailureListener {
                    println("nie git")
                }

            finish()
            val intent = Intent(this, SmartphonesListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
