package com.mai.keyboard

import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet


class CustomKeyboardView(context: Context, attrs: AttributeSet) : KeyboardView(context, attrs) {

    private var callBack: CallBack? = null

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    override fun onLongPress(popupKey: Keyboard.Key): Boolean {
        callBack?.onLongPress(popupKey)
        return super.onLongPress(popupKey)
    }

    interface CallBack {
        fun onLongPress(popupKey: Keyboard.Key)
    }
}