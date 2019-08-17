package com.bylancer.classified.bylancerclassified.splash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bylancer.classified.bylancerclassified.R
import com.bylancer.classified.bylancerclassified.appconfig.Language
import com.bylancer.classified.bylancerclassified.utils.AppConstants

class LanguageSelectionAdapter(private val languagePackData: List<Language>, private val onLanguageSelected: LanguageSelection) : RecyclerView.Adapter<LanguageSelectionAdapter.LanguageItemLayoutItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageItemLayoutItem {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.language_selection_adapter, parent, false)
        var holder = LanguageItemLayoutItem(itemView)
        return holder!!
    }

    override fun getItemCount() = languagePackData.size

    override fun onBindViewHolder(holder: LanguageItemLayoutItem, position: Int) {
        val languagePackData = languagePackData[position]
        holder.languageName?.text =  languagePackData.name
        holder.languageCountryFlag?.let {
            Glide.with(it.context).load(String.format(AppConstants.FLAG_IMAGE_URL, languagePackData.code)).into(it)
        }

        holder.itemView.setOnClickListener {
            onLanguageSelected?.onLanguageSelected(languagePackData.name, languagePackData.code)
        }
    }

    inner class LanguageItemLayoutItem(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var languageCountryFlag : ImageView? = null
        var languageName : AppCompatTextView? = null
        init {
            languageCountryFlag = itemView.findViewById(R.id.language_country_flag_image_View)
            languageName = itemView.findViewById(R.id.language_name_text_view)
        }
    }
}

