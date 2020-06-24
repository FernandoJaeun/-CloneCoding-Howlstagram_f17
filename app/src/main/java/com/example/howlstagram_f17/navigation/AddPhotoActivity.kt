package com.example.howlstagram_f17.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.howlstagram_f17.R
import com.example.howlstagram_f17.navigation.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {

    var PICK_IMAGE_FROM_ALBUM = 0
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null
    var auth: FirebaseAuth? = null
    var firestore: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)
        //storage initiate
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseStorage.getInstance()
        //open the Album, photo select
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        //add image upload event
        addphoto_btn_upload.setOnClickListener { contentUpload() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            //this is path  to the selected image
            if (requestCode == Activity.RESULT_OK + 1) {
                photoUri = data?.data
                addphoto_image.setImageURI(photoUri)
            } else {
                //Exit the addPhotoActivity if you leave the album without selecting it
                finish()
            }
        }
    }

    fun contentUpload() {
        // make filename
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //file Upload
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->  }
            var contentDTO = ContentDTO()

            //이미지 다운로드 URL 삽입
            contentDTO.imageUrl = uri.toString()

            contentDTO.uid = auth?.currentUser?.uid
            contentDTO.userId = auth?.currentUser?.email
            contentDTO.explain = addphoto_edit_explain.text.toString()
            contentDTO.timestamp = System.currentTimeMillis()
            firestore?.collection("images").document().set(contentDTO)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}