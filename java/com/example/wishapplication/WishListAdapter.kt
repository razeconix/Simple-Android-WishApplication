package com.example.wishapplication


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class WishListAdapter(private val wish: List<Wish>) :
    RecyclerView.Adapter<WishViewHolder>() {
    override fun onCreateViewHolder(group: ViewGroup, viewType: Int): WishViewHolder {
        val view = LayoutInflater.from(group.context).inflate(R.layout.item_wish, group, false)
        return WishViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: WishViewHolder, position: Int) {
        val wish = wish[position]
        if (wish != null) {
            viewHolder.bind(wish)
        }


    }
    override fun getItemCount(): Int = wish.size
}