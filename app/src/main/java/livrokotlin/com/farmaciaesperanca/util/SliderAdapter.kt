package livrokotlin.com.farmaciaesperanca.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import livrokotlin.com.farmaciaesperanca.R
import livrokotlin.com.farmaciaesperanca.view.categorias.CategoriasActivity
import org.jetbrains.anko.toast

class SliderAdapter(context: Context): SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {

    val mContext = context

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        val inflate = LayoutInflater.from(parent!!.context).inflate(livrokotlin.com.farmaciaesperanca.R.layout.image_slider_layout_item, null)
        return SliderAdapterVH(inflate)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {

        //viewHolder!!.textViewDescription.text = "This is slider item $position"
        //.load("https://scontent.fssa2-1.fna.fbcdn.net/v/t1.0-9/55559481_829987130670437_5546069815963156480_n.jpg?_nc_cat=110&_nc_oc=AQmMjB8O9Zl4Q2oeg_bbcvJBqaRsCfAmL8Ys-v9FsnPdUQwKoiVK_GAFdT-lAQ21Sfg&_nc_ht=scontent.fssa2-1.fna&oh=d7469b2097a8dd56c923db41625b87f4&oe=5DA7E88E")
        //.load("https://scontent.fssa2-1.fna.fbcdn.net/v/t1.0-9/61689079_871558316513318_2710877507766190080_n.jpg?_nc_cat=102&_nc_oc=AQmTMJdPYFE1viFz_X4oTS36jQ5FyE1-rBlsHnagj2xE8UaFG2eTrISIlWAZUGdo-Yk&_nc_ht=scontent.fssa2-1.fna&oh=4f731c46f8eb7422dcd857e07594ebbf&oe=5DB43457")
        //.load("https://scontent.fssa2-1.fna.fbcdn.net/v/t1.0-9/53472718_816700615332422_4220564349170745344_n.jpg?_nc_cat=108&_nc_oc=AQkBd5N4r3rQ9hTkKr91DelIl_aoenIezg5C3nBhKjIgk1KUA8svNmJ9uJXgYAAYXAg&_nc_ht=scontent.fssa2-1.fna&oh=804f092964b56b8f1c4a2ec79d20d991&oe=5DBC40D4")
        //.load("https://scontent.fssa2-2.fna.fbcdn.net/v/t1.0-9/59919925_858652304470586_581703550162173952_n.jpg?_nc_cat=107&_nc_oc=AQms-g6rygghtCkedwksy-nPhqBD5QzVWlLAmxWGA7-_zLROZxWVCkromATO8fVNQRw&_nc_ht=scontent.fssa2-2.fna&oh=63192a1002294c3d7bdbf0baf57a5796&oe=5DC109AD")

        when (position) {
            0 -> Glide.with(mContext)
                .load(imageSliderList[0])
                .into(viewHolder!!.imageViewBackground)

            1 -> Glide.with(mContext)
                .load(imageSliderList[1])
                .into(viewHolder!!.imageViewBackground)

            2 -> Glide.with(mContext)
                .load(imageSliderList[2])
                .into(viewHolder!!.imageViewBackground)

            3 -> Glide.with(mContext)
                .load(imageSliderList[3])
                .into(viewHolder!!.imageViewBackground)
        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return 4
    }

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {
        var imageViewBackground: ImageView
        var textViewDescription: TextView

        init {
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider)
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider)

            itemView.setOnClickListener {
                val intent = Intent(mContext, CategoriasActivity::class.java)
                startActivity(mContext, intent, null)
            }
        }
    }

}