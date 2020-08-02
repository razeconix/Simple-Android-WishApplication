package com.example.wishapplication


import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*

import kotlinx.android.synthetic.main.item_wish.view.*
import java.io.File


class WishViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val idTextView = itemView.idTextView
    private val nameTextView = itemView.nameTextView
    private val typeTextView = itemView.typeTextView
    private val priceTextView = itemView.priceTextView
    private val amountTextView = itemView.amountTextView
    private val imageView = itemView.imageView
    private val textView = itemView.textView
    private val textView2= itemView.textView2
    private val textView3= itemView.textView3
    private val footer1 = itemView.footer1




    fun bind(wish: Wish) {
        idTextView.text = wish.id
        nameTextView.text = wish.name
        typeTextView.text = wish.type
        priceTextView.text = wish.price.toString()
        amountTextView.text = wish.amount.toString()
        textView.text
        textView2.text
        textView3.text
        footer1




        if (wish.imagePath != null) {
            val file = File(wish.imagePath)
            val uri = Uri.fromFile(file)
            imageView.setImageURI(uri)
        } else {
            imageView.setImageResource(R.drawable.ic_profile)
        }

        itemView.setOnClickListener {
            val intent = Intent(itemView.context,
                AmountActivity::class.java)
            intent.putExtra(AmountActivity.EXTRA_WISH_ID, wish.id)
            itemView.context.startActivity(intent)
        }


    }
}