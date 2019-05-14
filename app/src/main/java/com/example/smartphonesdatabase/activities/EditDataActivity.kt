package com.example.smartphonesdatabase.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smartphonesdatabase.R
import com.example.smartphonesdatabase.models.Smartphone
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_data.brandInput
import kotlinx.android.synthetic.main.activity_add_data.modelInput
import kotlinx.android.synthetic.main.activity_add_data.systemVersionInput
import kotlinx.android.synthetic.main.activity_add_data.websiteInput
import kotlinx.android.synthetic.main.activity_edit_data.*
import android.net.Uri


class EditDataActivity : AppCompatActivity() {

    var smartphone: Smartphone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_data)

        supportActionBar?.title = "Edycja danych:"

        smartphone = intent.getParcelableExtra<Smartphone>(SmartphonesListActivity.SMARTPHONE_KEY)

        setupData()
        performOnLeaveListener()
        prepareButtonListeners()
    }

    private fun setupData() {
        brandInput.setText(smartphone!!.brand)
        modelInput.setText(smartphone!!.model)
        systemVersionInput.setText(smartphone!!.systemVersion)
        websiteInput.setText(smartphone!!.website)
    }

    private fun performOnLeaveListener() {
        brandInput.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                if(brandInput.text.isEmpty()) {
                    Toast.makeText(this, "Pole 'Producent' nie może być puste.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        modelInput.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                if(modelInput.text.isEmpty()) {
                    Toast.makeText(this, "Pole 'Model' nie może być puste.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        systemVersionInput.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                if(systemVersionInput.text.isEmpty()) {
                    Toast.makeText(this, "Pole 'Wersja systemu' nie może być puste.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        websiteInput.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                val url = websiteInput.text.toString()

                if(!url.startsWith("http://") && !url.startsWith("https://")) {
                    websiteInput.setText("http://$url")
                }
            }
        }
    }

    private fun prepareButtonListeners() {
        website_button.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteInput.text.toString()))
            startActivity(browserIntent)
        }

        cancel_button.setOnClickListener {
            finish()
        }

        saveChanges_button.setOnClickListener {
            saveChanges()
        }
    }

    private fun saveChanges() {
        if(brandInput.text.isEmpty() || modelInput.text.isEmpty() ||
            systemVersionInput.text.isEmpty() || websiteInput.text.isEmpty()) {
            Toast.makeText(this, "Wszystkie pola muszą być wypełnione", Toast.LENGTH_SHORT).show()
        }
        else {
            val id = smartphone!!.id
            val brand = brandInput.text.toString()
            val model = modelInput.text.toString()
            val systemVersion = systemVersionInput.text.toString()
            val website = websiteInput.text.toString()

            val ref = FirebaseDatabase.getInstance().getReference("/smartphones/$id")
            val smartphone = Smartphone(id, brand, model, systemVersion, website)

            ref.setValue(smartphone)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }

            finish()
            val intent = Intent(this, SmartphonesListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("brand", brandInput.text.toString())
        outState?.putString("model", modelInput.text.toString())
        outState?.putString("systemVersion", systemVersionInput.text.toString())
        outState?.putString("website", websiteInput.text.toString())

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        brandInput.setText(savedInstanceState?.getString("brand"))
        modelInput.setText(savedInstanceState?.getString("model"))
        systemVersionInput.setText(savedInstanceState?.getString("systemVersion"))
        websiteInput.setText(savedInstanceState?.getString("website"))
    }
}
