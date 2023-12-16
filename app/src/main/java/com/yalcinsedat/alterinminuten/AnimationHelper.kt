package com.yalcinsedat.alterinminuten

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast

class AnimationHelper(private val context: Context) {

    @SuppressLint("MissingInflatedId")
    fun showAnimatedToast(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        val view = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout, null)

        // Toast'ın görünümünü özelleştirme
        view.setBackgroundResource(R.drawable.toast_background) // İstediğiniz arka planı ayarlayabilirsiniz
        val text = view.findViewById<TextView>(R.id.toast_text)
        text.text = message
        text.setTextColor(Color.WHITE) // Yazı rengini ayarlayabilirsiniz

        toast.view = view

        // Animasyonlar
        val animationIn = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, -1f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        animationIn.duration = 1000 // Animasyon süresini belirleyebilirsiniz

        val animationOut = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, -1f
        )
        animationOut.duration = 500 // Animasyon süresini belirleyebilirsiniz

        // Animasyonları Toast'a ayarlama
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(animationIn)
        animationSet.addAnimation(animationOut)
        view.startAnimation(animationSet)

        toast.show()
    }
}
