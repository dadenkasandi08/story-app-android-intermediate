package com.kasandi.storyapp.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.kasandi.storyapp.R

class CustomPasswordInputText : AppCompatEditText {
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
        if (s.toString().length < 8) {
            setError(context.getString(R.string.error_password), null)
        } else {
            error = null
        }
    }
}