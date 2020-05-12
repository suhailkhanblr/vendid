package com.bylancer.classified.bylancerclassified.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.utils.LanguagePack
import com.bylancer.classified.bylancerclassified.utils.Utility
import com.himangi.imagepreview.ImagePreviewActivity
import com.himangi.imagepreview.PreviewFile


class ProductDetailViewPagerAdapter(private val mContext: Context, private val images: List<String>, private val imagePath:String, private val adView: String?) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.product_detail_view_pager_adapter, collection, false) as ViewGroup
        val productImage = layout.findViewById(R.id.product_image_view_pager) as ImageView
        val productImageCounter = layout.findViewById(R.id.product_image_counter_text_view) as AppCompatTextView
        val productViewCounter = layout.findViewById(R.id.product_view_counter_text_view) as AppCompatTextView
        productImageCounter.text = ((position + 1).toString() + "/" + (images.size).toString())
        productViewCounter.text = LanguagePack.getString("Ad Views") + ": " + adView ?: ""
        if (images[position].isNullOrEmpty()) {
            Glide.with(mContext).load(imagePath + images[position]).apply(RequestOptions().placeholder(R.drawable.image_not_available)).into(productImage)
        } else {
            Glide.with(mContext).load(imagePath + images[position]).apply(RequestOptions().placeholder(Utility.getCircularProgressDrawable(mContext))).into(productImage)
        }

        val fullPathImageList = arrayListOf<PreviewFile>()
        for (image in images) {
            fullPathImageList.add(PreviewFile(String.format("%s%s", imagePath, image), ""))
        }

        val parentLayout = layout.findViewById(R.id.product_detail_image_parent_layout) as FrameLayout
        parentLayout?.context?.let {
            parentLayout?.setOnClickListener { _ ->
                val fullImageIntent = Intent(it, ImagePreviewActivity::class.java)
                fullImageIntent.putExtra(ImagePreviewActivity.IMAGE_LIST, fullPathImageList)
                fullImageIntent.putExtra(ImagePreviewActivity.CURRENT_ITEM, position)
                it.startActivity(fullImageIntent)
            }
        }

        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    override fun isViewFromObject(view: View, p1: Any): Boolean {
        return view === p1
    }

}