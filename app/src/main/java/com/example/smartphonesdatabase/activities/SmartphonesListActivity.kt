package com.example.smartphonesdatabase.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

class SmartphonesListActivity : AppCompatActivity() {

    companion object {
        val SMARTPHONE_KEY = "smartphoneKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smartphones_list)

        /*setupData()
        setupData()
        setupData()*/

        showData()
    }

    private fun setupData() {
        val id = UUID.randomUUID().toString()
        println(id)
        val brand = "Huawei"
        val model = "P9"
        val systemVersion = "Android 7.0"
        val website = "https://www.telepolis.pl/telefony/huawei/p9"

        val ref = FirebaseDatabase.getInstance().getReference("/smartphones/$id")
        val smartphone = Smartphone(id, brand, model, systemVersion, website)

        ref.setValue(smartphone)
            .addOnSuccessListener {
                println("git")
            }
            .addOnFailureListener {
                println("nie git")
            }
    }

    private fun showData() {
        val ref = FirebaseDatabase.getInstance().getReference("/smartphones")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    val smartphone = it.getValue(Smartphone::class.java)
                    if (smartphone != null) {
                        adapter.add(SmartphoneItem(smartphone))
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val smartphoneItem = item as SmartphoneItem

                    val intent = Intent(view.context, EditDataActivity::class.java)
                    intent.putExtra(SMARTPHONE_KEY, smartphoneItem.smartphone)
                    startActivity(intent)
                }

                recyclerView_SmartphonesList.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) { }
        })
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
