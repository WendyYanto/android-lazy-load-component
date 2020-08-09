package dev.wendyyanto.library

import android.content.Context
import android.util.AttributeSet
import android.view.ViewStub
import android.widget.LinearLayout
import java.lang.IllegalArgumentException

class LazyLoadComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    companion object {
        private const val LAYOUT_RESOURCE_IS_INVALID = "LAYOUT_RESOURCE_IS_INVALID"
    }

    private val viewStub by lazy {
        ViewStub(context, attrs)
    }

    init {
        orientation = VERTICAL
        val typeArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LazyLoadComponent, 0, 0
        )
        try {
            val layoutResId = typeArray.getResourceId(R.styleable.LazyLoadComponent_layout_id, 0)
            if (layoutResId == 0) {
                throw IllegalArgumentException(LAYOUT_RESOURCE_IS_INVALID)
            }
            with (viewStub) {
                layoutResource = layoutResId
                addView(this)
            }
        } finally {
            typeArray.recycle()
        }
    }

}