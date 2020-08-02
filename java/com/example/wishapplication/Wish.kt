package com.example.wishapplication


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Wish() : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var type: String = ""
    var price: Int = 0
    var amount: Int = 0
    var total: Int = 0
    var imagePath: String? = null

    constructor(id: String, name: String, type: String, price: Int,amount: Int,total: Int, imagePath : String?= null) : this() {
        this.id = id
        this.name = name
        this.type = type
        this.price = price
        this.amount= amount
        this.total=total
        this.imagePath = imagePath


    }
}