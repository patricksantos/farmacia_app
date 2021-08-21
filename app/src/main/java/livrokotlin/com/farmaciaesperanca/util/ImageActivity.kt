package livrokotlin.com.farmaciaesperanca.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image.*
import livrokotlin.com.farmaciaesperanca.R

class ImageActivity : AppCompatActivity() {

    companion object {
        lateinit var url: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
    }

    override fun onResume() {
        super.onResume()
        Picasso.get().load(url).into(imageActivity)
    }

}
