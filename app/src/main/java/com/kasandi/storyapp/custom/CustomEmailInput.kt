package com.kasandi.storyapp.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.kasandi.storyapp.R

class CustomEmailInput : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttributeSet: Int) : super(
        context,
        attrs,
        defStyleAttributeSet
    ) {
        init()
    }

    private fun init() {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (text != null && !isEmailValid(text!!.toString())) {
            setError(context.getString(R.string.error_invalid_email), null)
        } else {
            error = null
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern =
            Regex("[a-z0-9#$%&]" + "[a-z0-9#$%&'\\.\\-_]*" + "@[^\\.\\s@]" + "[a-z0-9_\\.\\-_]*" + "\\.[a-z]{2,4}")
        return email.matches(emailPattern)
    }
}