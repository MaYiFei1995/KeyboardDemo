package com.mai.keyboarddemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.mai.keyboard.HphmKeyboard
import com.mai.keyboard.SfzhKeyboard

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var sfzhKeyboard: SfzhKeyboard
    lateinit var hphmKeyboard: HphmKeyboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //1 屏蔽掉系统默认输入法
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        try {
            val cls = EditText::class.java
            val setShowSoftInputOnFocus =
                cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType!!)
            setShowSoftInputOnFocus.isAccessible = true
            setShowSoftInputOnFocus.invoke(sfzhEditText, false)
            setShowSoftInputOnFocus.invoke(hphmEditText, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //2 初试化身份证号键盘
        sfzhKeyboard =
            SfzhKeyboard(this@MainActivity, customKeyboardSfzh, sfzhEditText)

        sfzhKeyboard.showKeyboard()

        sfzhEditText.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                hphmKeyboard.hideKeyboard()
                sfzhKeyboard.showKeyboard()
                return false
            }
        })

        //3 初始化号牌号码键盘
        hphmKeyboard =
            HphmKeyboard(this@MainActivity, customKeyboardHphm, hphmEditText)

        hphmEditText.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                sfzhKeyboard.hideKeyboard()
                hphmKeyboard.showKeyboard()
                return false
            }
        })

    }

    //物理返回键
    override fun onBackPressed() {
        when {
            sfzhKeyboard.isShowKeyboard -> sfzhKeyboard.hideKeyboard()
            hphmKeyboard.isShowing -> hphmKeyboard.hideKeyboard()
            else -> finish()
        }
    }

}
