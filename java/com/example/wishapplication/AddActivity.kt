package com.example.wishapplication


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_add.cancelButton
import kotlinx.android.synthetic.main.activity_add.nameEditText
import kotlinx.android.synthetic.main.activity_add.priceEditText
import kotlinx.android.synthetic.main.activity_add.saveButton
import kotlinx.android.synthetic.main.activity_add.typeEditText
import kotlinx.android.synthetic.main.activity_amount.*

import java.util.*


class AddActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val typeNames = arrayOf("เครื่องแต่งกาย","เครื่องประดับ","อุปกรณ์อิเล็กทรอนิกส์","เครื่องใช้ภายในบ้าน")
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,typeNames)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                typeEditText.text = typeNames[position]//To change body of created functions use File | Settings | File Templates.
            }


        }

        realm = Realm.getDefaultInstance()


        saveButton.setOnClickListener {
            val wish = Wish()
            wish.id = randomNumericString(7)
            wish.name = nameEditText.text.toString()
            wish.type = typeEditText.text.toString()
            wish.price = Integer.parseInt(priceEditText.text.toString())
            //wish.amount = Integer.parseInt(amountEditText.text.toString())

            realm.executeTransaction {
                realm.insertOrUpdate(wish)
            }
            finish()
        }
        cancelButton.setOnClickListener {
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    fun randomNumericString(length: Int): String {
        val max = Math.pow(10.0, length.toDouble()).toInt() - 1
        return Random().nextInt(max).toString().padStart(length, '0')
    }

}