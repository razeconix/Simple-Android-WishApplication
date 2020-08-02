package com.example.wishapplication


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_wish.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var adapter: WishListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()
        val wish = realm.where<Wish>().sort("id").findAll()
        adapter = WishListAdapter(wish)


        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(this)
        total.text = realm.where<Wish>().sort("price").findAll().sum("price").toString()

        addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            this.startActivity(intent)
        }
        wish.addChangeListener(changeListener)
    }
    private val changeListener =
        OrderedRealmCollectionChangeListener<RealmResults<Wish>> { _,
                                                                   changeSet ->
            adapter.notifyDataSetChanged()
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

