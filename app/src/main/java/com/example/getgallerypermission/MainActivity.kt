package com.example.getgallerypermission

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.getgallerypermission.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /****** old method *****/
        binding.btnOld.setOnClickListener {
            startActivityForResult(
                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                },
                1
            )
        }


        /***** new method  *****/
        binding.btnNew.setOnClickListener {
            getImageContent().launch("image/*")
        }
        /***** clear  *****/
        binding.btnClear.setOnClickListener {
            val externalStorage = filesDir
            if (externalStorage.isDirectory){
                val listFiles = externalStorage.listFiles()
                if (listFiles.isEmpty()){
                    Toast.makeText(this, "file is empty", Toast.LENGTH_SHORT).show()
                }else{
                    listFiles.forEach {

                        it.delete()
                    }
                }
            }
        }

    }

    private fun getImageContent()  =
        registerForActivityResult(ActivityResultContracts.GetContent()){
            it ?: return@registerForActivityResult
            binding.ivGallery.setImageURI(it)
            val inputStream = contentResolver?.openInputStream(it)
            val file = File(filesDir,"cody_new_method.img")
            val fileInputStream = FileInputStream(file)
            /** val readBytes  = fileInputStream.readBytes() **/
          /** open class here like: val imageModels = ImageModels(absolutePath, readBytes) **/
          /** use dbHelper.insertImage(imageModels) **/
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            Toast.makeText(this, file.absolutePath, Toast.LENGTH_SHORT).show()

        }


/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1&&resultCode == Activity.RESULT_OK){
            val uri = data?.data ?: return
            binding.ivGallery.setImageURI(uri)
            val inputStream = contentResolver?.openInputStream(uri)
            val file = File(filesDir,"cody.img")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            Toast.makeText(this, "${file.absolutePath}", Toast.LENGTH_SHORT).show()
        }
    }
*/
}