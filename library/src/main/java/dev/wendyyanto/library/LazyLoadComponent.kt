package dev.wendyyanto.library

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewStub
import android.view.ViewTreeObserver
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

    private val componentRect by lazy {
        Rect()
    }

    private lateinit var screenRect: Rect

    private var isScrollListenerExist = false

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
            with(viewStub) {
                layoutResource = layoutResId
                addView(this)
            }
        } finally {
            typeArray.recycle()
        }
        setupListener()
    }

    private fun setupListener() {
        this.getGlobalVisibleRect(this.componentRect)
        this.screenRect = Rect(0, 0, rootView.measuredWidth, rootView.height)
        this.viewTreeObserver.addOnScrollChangedListener(object :
            ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                if (isScrollListenerExist) return
                isScrollListenerExist = true
                if (!isViewShown()) return
                viewStub.inflate()
                viewTreeObserver.removeOnScrollChangedListener(this)
            }
        })
    }

    private fun isViewShown(): Boolean {
        // ToDo - screenRect doesn't cover until the bottom of layout
        return this.componentRect.intersect(this.screenRect)
    }
}