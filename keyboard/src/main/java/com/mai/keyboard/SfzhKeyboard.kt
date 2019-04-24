package com.mai.keyboard

import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View
import android.widget.EditText
import com.mai.keyboard.CustomKeyboardView

class SfzhKeyboard(context: Context, private val mKeyboardView: CustomKeyboardView, private val mEditText: EditText) :
    CustomKeyboardView.CallBack {

    val isShowKeyboard = mKeyboardView.visibility == View.VISIBLE

    init {
        mKeyboardView.setCallBack(this)
        mKeyboardView.keyboard = Keyboard(context, R.xml.sfzh_keyboard)
        mKeyboardView.isPreviewEnabled = false
        mKeyboardView.setOnKeyboardActionListener(object : KeyboardView.OnKeyboardActionListener {
            override fun swipeUp() {}

            override fun swipeRight() {}

            override fun swipeLeft() {}

            override fun swipeDown() {}

            override fun onText(text: CharSequence) {}

            override fun onRelease(primaryCode: Int) {}

            override fun onPress(primaryCode: Int) {}

            override fun onKey(primaryCode: Int, keyCodes: IntArray) {
                val editable = mEditText.text ?: return
                val index = mEditText.selectionStart
                when (primaryCode) {
                    Keyboard.KEYCODE_DELETE -> if (index > 0) {
                        editable.delete(index - 1, index)
                    }
                    //33
                    -999 -> editable.insert(index, "33")
                    //隐藏
                    -9 -> hideKeyboard()
                    else -> editable.insert(index, Character.toString(primaryCode.toChar()))
                }
            }
        })
    }

    fun showKeyboard() {
        mKeyboardView.visibility = View.VISIBLE
    }

    fun hideKeyboard() {
        mKeyboardView.visibility = View.GONE
    }

    override fun onLongPress(popupKey: Keyboard.Key) {
        val codes = popupKey.codes ?: return
        if (codes[0] == Keyboard.KEYCODE_DELETE) {
            mEditText.setText("")
        }
    }
}