package com.mai.keyboard

import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View
import android.widget.EditText

class HphmKeyboard(context: Context, private val mKeyboardView: CustomKeyboardView, private val mEditText: EditText) :
    CustomKeyboardView.CallBack {

    /**
     * 省份简称键盘
     */
    private val provinceKeyboard: Keyboard = Keyboard(context, R.xml.hphm_province)
    /**
     * 数字与大写字母键盘
     */
    private val numberKeyboard: Keyboard = Keyboard(context, R.xml.hphm_number)

    /**
     * 软键盘展示状态
     */
    val isShowing = mKeyboardView.visibility == View.VISIBLE

    init {
        mKeyboardView.setCallBack(this)
        mKeyboardView.keyboard = provinceKeyboard
        mKeyboardView.isEnabled = true
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
                val start = mEditText.selectionStart
                //判定是否是中文的正则表达式 [\\u4e00-\\u9fa5]判断一个中文 [\\u4e00-\\u9fa5]+多个中文
                val reg = "[\\u4e00-\\u9fa5]"
                when (primaryCode) {
                    -9 -> hideKeyboard()
                    Keyboard.KEYCODE_DELETE -> {
                        //没有输入内容时软键盘重置为省份简称软键盘
                        if (editable.length == 1) {
                            changeKeyboard(false)
                        }
                        if (start > 0) {
                            editable.delete(start - 1, start)
                        }
                    }
                    else -> {
                        editable.insert(start, Character.toString(primaryCode.toChar()))
                        // 判断第一个字符是否是中文,是，则自动切换到数字软键盘
                        if (mEditText.text.toString().matches(reg.toRegex())) {
                            changeKeyboard(true)
                        }
                    }
                }
            }
        })
    }

    /**
     * 指定切换软键盘 isNumber false表示要切换为省份简称软键盘 true表示要切换为数字软键盘
     */
    private fun changeKeyboard(isNumber: Boolean) {
        mKeyboardView.keyboard = if (isNumber) numberKeyboard else provinceKeyboard
    }

    /**
     * 软键盘展示
     */
    fun showKeyboard() {
        mKeyboardView.visibility = View.VISIBLE
    }

    /**
     * 软键盘隐藏
     */
    fun hideKeyboard() {
        mKeyboardView.visibility = View.INVISIBLE
    }

    /**
     * 长按删除清空内容
     */
    override fun onLongPress(popupKey: Keyboard.Key) {
        val codes = popupKey.codes ?: return
        if (codes[0] == Keyboard.KEYCODE_DELETE) {
            mEditText.setText("")
            changeKeyboard(false)
        }
    }
}