package com.bylancer.classified.bylancerclassified.utils

class LanguagePack private constructor(){

    companion object {
        var languagePack:LanguagePack? = null

        val instance: LanguagePack
        get() {
            if(languagePack == null) {
                languagePack = LanguagePack()
            }
            return languagePack!!
        }

        fun getString(key: String): String {
            return key
        }
    }
}