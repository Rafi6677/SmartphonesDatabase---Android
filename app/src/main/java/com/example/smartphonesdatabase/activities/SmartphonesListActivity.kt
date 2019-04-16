package com.example.smartphonesdatabase.activities

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.SparseIntArray
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.smartphonesdatabase.R
import com.example.smartphonesdatabase.models.Smartphone
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_smartphones_list.*
import kotlinx.android.synthetic.main.smartphones_list_row.view.*
import java.util.*
import kotlin.collections.HashMap

class SmartphonesListActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    var smartphonesToDelete = HashMap<String, Boolean>()

    companion object {
        val SMARTPHONE_KEY = "smartphoneKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smartphones_list)

        showData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var isReadyToDelete = false

        when (item?.itemId) {

            R.id.addSmartphone -> {
                val intent = Intent(this, AddDataActivity::class.java)
                startActivity(intent)
            }
            R.id.deleteSmartphone -> {
                smartphonesToDelete.values.forEach {
                    if(it)
                        isReadyToDelete = true
                }

                if(isReadyToDelete) {
                    AlertDialog.Builder(this)
                        .setTitle("UWAGA!")
                        .setMessage("Zamierzasz usunąć rekordy z bazy danych. Kontynuować?")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                            for((key, value) in smartphonesToDelete) {
                                if(value) {
                                    val ref = FirebaseDatabase.getInstance().getReference("smartphones/$key")
                                    ref.removeValue()
                                        .addOnSuccessListener {
                                            println("usunieto")
                                        }
                                        .addOnFailureListener {
                                            println("nie usunieto")
                                        }
                                }
                            }
                            adapter.clear()
                            showData()
                        })
                        .setNegativeButton("Anuluj", DialogInterface.OnClickListener { dialog, which ->
                            adapter.clear()
                            showData()
                        })
                        .show()
                }
                else {
                    Toast.makeText(this, "Brak zaznaczonych elementów.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showData() {
        val ref = FirebaseDatabase.getInstance().getReference("/smartphones")
        smartphonesToDelete.clear()

        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val smartphone = it.getValue(Smartphone::class.java)
                    if (smartphone != null) {
                        adapter.add(SmartphoneItem(smartphone))
                        smartphonesToDelete[smartphone.id] = false
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) { }
        })

        adapter.setOnItemClickListener { item, view ->
            val smartphoneItem = item as SmartphoneItem

            val intent = Intent(view.context, EditDataActivity::class.java)
            intent.putExtra(SMARTPHONE_KEY, smartphoneItem.smartphone)
            startActivity(intent)
        }

        adapter.setOnItemLongClickListener { item, view ->
            val smartphoneItem = item as SmartphoneItem
            val id = smartphoneItem.smartphone.id

            var color = 0
            val currentBackgroundCollor = view.background
            if(currentBackgroundCollor is ColorDrawable) {
                color = currentBackgroundCollor.color
            }

            if(color == resources.getColor(R.color.notSelected)) {
                view.setBackgroundColor(resources.getColor(R.color.selected))

                smartphonesToDelete[id] = true
            }
            else {
                view.setBackgroundColor(resources.getColor(R.color.notSelected))

                smartphonesToDelete[id] = false
            }

            println(smartphonesToDelete)
            item.isLongClickable
        }

        recyclerView_SmartphonesList.adapter = adapter
    }
}

class SmartphoneItem(val smartphone: Smartphone): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.brand_textview.text = smartphone.brand
        viewHolder.itemView.model_textview.text = smartphone.model
    }

    override fun getLayout(): Int {
        return R.layout.smartphones_list_row
    }
}
