package com.dea.myappstories.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dea.myappstories.R

class EmailEditText: AppCompatEditText {
    private val errorMessage: String by lazy { context.getString(R.string.invalidEmail) }
    private val emailPattern: Regex by lazy { "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex() }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && !isEmailValid(s.toString())) {
                    error = errorMessage
                } else {
                    error = null
                }
            }
        })
    }

    private fun isEmailValid(email: String): Boolean {
        return emailPattern.matches(email)
    }
}