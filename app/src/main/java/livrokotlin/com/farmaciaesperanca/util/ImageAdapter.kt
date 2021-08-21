package livrokotlin.com.farmaciaesperanca.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import livrokotlin.com.farmaciaesperanca.R

class ImageAdapter(context: Context) : PagerAdapter(){

    var mContext: Context = context
    var mImagesId = arrayListOf(R.drawable.image_slide1, R.drawable.image_slide2, R.drawable.image_slide3, R.drawable.image_slide4)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(mContext)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(mImagesId[position])
        container.addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

    override fun getCount(): Int {
        return mImagesId.size
    }


}