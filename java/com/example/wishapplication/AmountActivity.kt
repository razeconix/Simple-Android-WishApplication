package com.example.wishapplication


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_amount.*
import java.io.File


class AmountActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var wish: Wish

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount)

        realm = Realm.getDefaultInstance()
        val id = intent.getStringExtra(EXTRA_WISH_ID)
        wish = realm.where<Wish>().equalTo("id",
            id).findFirst() ?: return
        idEditText.setText(wish.id)
        nameEditText.setText(wish.name)
        typeEditText.setText(wish.type)
        priceEditText.setText(Integer.toString(wish.price))
        amountEditText.setText(Integer.toString(wish.amount))
        addbalanceEditText.setText("0")

            saveButton.setOnClickListener {

                realm.executeTransaction {

                    wish.amount += addbalanceEditText.text.toString().toInt()
                    }

                finish()
            }


        editButton.setOnClickListener{

            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra(EditActivity.EXTRA_WISH_ID, wish.id)
            this.startActivity(intent)
        }
        cancelButton.setOnClickListener {
            finish()
        }
        if (wish.imagePath != null) {
            val file = File(wish.imagePath)
            val uri = Uri.fromFile(file)
            imageView.setImageURI(uri)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    companion object {
        const val EXTRA_WISH_ID = "EXTRA_WISH_ID"
    }

    private fun currentAmount(currentAmount:Int): String {
        var currentAmount = 0
        if (wish.amount > 0) {
            currentAmount += wish.amount
        } else {
            currentAmount = 0
        }
        return currentAmount.toString()
    }

}