package com.dea.myappstories.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dea.myappstories.R

class PasswordEditText: AppCompatEditText {
    private var errorMessage: String

    constructor(context: Context) : super(context) {
        errorMessage = context.getString(R.string.error_password)
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        errorMessage = context.getString(R.string.error_password)
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        errorMessage = context.getString(R.string.error_password)
        init()
    }

    private fun init(){
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length < 8) {
                    error = errorMessage
                } else {
                    error = null
                }
            }
        })
    }
}