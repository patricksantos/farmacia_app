package livrokotlin.com.farmaciaesperanca.view.savvyGerenciador

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_slider_edit.*
import livrokotlin.com.farmaciaesperanca.R
import java.io.IOException

class SliderEditActivity : AppCompatActivity() {

    lateinit var mSelectedUri: Uri
    var bitmap: Bitmap? = null
    val imageSliderList = mutableListOf<String>()
    var nomeSlider = ""

    override fun onResume() {
        super.onResume()
        imageSliders()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider_edit)

        btnVisibility()
        botoes()

    }

    private fun btnVisibility() {
        btn_slider1.setOnClickListener {
            if( slider1_visibility.visibility == View.GONE )
                slider1_visibility.visibility = View.VISIBLE
            else
                slider1_visibility.visibility = View.GONE

            Glide.with(this)
                .load(imageSliderList[0])
                .into(slider1)

        }

        btn_slider2.setOnClickListener {
            if( slider2_visibility.visibility == View.GONE )
                slider2_visibility.visibility = View.VISIBLE
            else
                slider2_visibility.visibility = View.GONE

            Glide.with(this)
                .load(imageSliderList[1])
                .into(slider2)

        }

        btn_slider3.setOnClickListener {
            if( slider3_visibility.visibility == View.GONE )
                slider3_visibility.visibility = View.VISIBLE
            else
                slider3_visibility.visibility = View.GONE

            Glide.with(this)
                .load(imageSliderList[2])
                .into(slider3)

        }

        btn_slider4.setOnClickListener {
            if( slider4_visibility.visibility == View.GONE )
                slider4_visibility.visibility = View.VISIBLE
            else
                slider4_visibility.visibility = View.GONE

            Glide.with(this)
                .load(imageSliderList[3])
                .into(slider4)

        }
    }

    fun botoes()
    {
        btn_add_slider1.setOnClickListener {
            nomeSlider = "slider1"
            if( !checkPermissionFromDevice() ){
                requestPermission()
            }else{
                sendPhoto()
            }
            imageSliders()
        }

        btn_add_slider2.setOnClickListener {
            nomeSlider = "slider2"
            if( !checkPermissionFromDevice() ){
                requestPermission()
            }else{
                sendPhoto()
            }
            imageSliders()
        }

        btn_add_slider3.setOnClickListener {
            nomeSlider = "slider3"
            if( !checkPermissionFromDevice() ){
                requestPermission()
            }else{
                sendPhoto()
            }
            imageSliders()
        }

        btn_add_slider4.setOnClickListener {
            nomeSlider = "slider4"
            if( !checkPermissionFromDevice() ){
                requestPermission()
            }else{
                sendPhoto()
            }
            imageSliders()
        }

    }

    fun sendPhoto()// transforma a foto em um bitmap
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun checkPermissionFromDevice(): Boolean
    {
        val writeExternalStore = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return writeExternalStore == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {// transforma a foto em um bitmap
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == 0 && data != null ){

            mSelectedUri = data.data

            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, mSelectedUri)
                sendPhotoDatabase(nomeSlider)
            }catch (e: IOException){
            }
        }

    }

    private fun sendPhotoDatabase( nome: String )
    {
        val ref = FirebaseStorage.getInstance().getReference("/slider/" + nome )

        ref.putFile(mSelectedUri).addOnSuccessListener{

            ref.downloadUrl.addOnSuccessListener {

                FirebaseFirestore.getInstance()
                    .collection("Configuracoes")
                    .document("Slider")
                    .update(nome, it.toString())

            }
        }
    }

    fun imageSliders()
    {
        FirebaseFirestore.getInstance()
            .collection("Configuracoes")
            .document("Slider")
            .get().addOnSuccessListener {

                val image1 = it.data!!["slider1"] as String
                val image2 = it.data!!["slider2"] as String
                val image3 = it.data!!["slider3"] as String
                val image4 = it.data!!["slider4"] as String

                imageSliderList.add(0, image1)
                imageSliderList.add(1, image2)
                imageSliderList.add(2, image3)
                imageSliderList.add(3, image4)
            }
    }

}
