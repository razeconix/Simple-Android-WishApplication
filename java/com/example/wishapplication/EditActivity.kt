package com.example.wishapplication

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.realm.Realm

import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.amountEditText
import kotlinx.android.synthetic.main.activity_edit.idEditText
import kotlinx.android.synthetic.main.activity_edit.imageView
import kotlinx.android.synthetic.main.activity_edit.nameEditText
import kotlinx.android.synthetic.main.activity_edit.priceEditText
import kotlinx.android.synthetic.main.activity_edit.saveButton
import kotlinx.android.synthetic.main.activity_edit.typeEditText

import java.io.File


class EditActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var wish: Wish
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
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
        val id = intent.getStringExtra(EXTRA_WISH_ID)
        wish = realm.where<Wish>().equalTo("id",
            id).findFirst() ?: return
        idEditText.setText(wish.id)
        nameEditText.setText(wish.name)
        typeEditText.setText(wish.type)
        priceEditText.setText(Integer.toString(wish.price))
        amountEditText.setText(Integer.toString(wish.amount))

        saveButton.setOnClickListener {
            realm.executeTransaction {
                wish.name = nameEditText.text.toString()
                wish.type = typeEditText.text.toString()
                wish.price = Integer.parseInt(priceEditText.text.toString())
                wish.amount= Integer.parseInt(amountEditText.text.toString())

            }
            finish()
        }
        deleteButton.setOnClickListener {
            realm.executeTransaction {
                wish.deleteFromRealm()
            }
            finish()
        }
        chooseImageButton.setOnClickListener {
            validatePermissions()
        }
        if (wish.imagePath != null) {
            val file = File(wish.imagePath)
            val uri = Uri.fromFile(file)
            imageView.setImageURI(uri)
        }
    }
    private val TAKE_PHOTO_REQUEST = 101
    private var currentPhotoPath: String = ""
    private fun validatePermissions() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(response:
                                                 PermissionGrantedResponse?) {
                    launchCamera()
                }
                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?) {
                    AlertDialog.Builder(this@EditActivity)
                        .setTitle(R.string.storage_permission_rationale_title)
                        .setMessage(R.string.storage_permission_rationale_message)
                        .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                            dialog.dismiss()
                            token?.cancelPermissionRequest()
                        }
                        .setPositiveButton(android.R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                            token?.continuePermissionRequest()
                        }
                        .setOnDismissListener { token?.cancelPermissionRequest() }
                        .show()
                }
                override fun onPermissionDenied(response:
                                                PermissionDeniedResponse?) {
                    Toast.makeText(this@EditActivity,
                        R.string.storage_permission_denied_message,
                        Toast.LENGTH_LONG).show()
                }
            })
            .check()
    }
    private fun launchCamera() {
        val values = ContentValues(1)
        54

        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = contentResolver
            .insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            currentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
            && requestCode == TAKE_PHOTO_REQUEST) { processCapturedPhoto()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun processCapturedPhoto() {
        val cursor = contentResolver.query(
            Uri.parse(currentPhotoPath),
            Array(1)
            { android.provider.MediaStore.Images.ImageColumns.DATA },
            null, null, null)
        cursor!!.moveToFirst()
        val photoPath = cursor!!.getString(0)
        cursor!!.close()
        realm.executeTransaction {
            wish.imagePath = photoPath
        }
        val file = File(photoPath)
        val uri = Uri.fromFile(file)
        imageView.setImageURI(uri)
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    companion object {
        const val EXTRA_WISH_ID = "EXTRA_WISH_ID"
    }
}