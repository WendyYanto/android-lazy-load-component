package dev.wendyyanto.library

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewStub
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
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

    private lateinit var parentView: View
    private var screenHeight: Int = -1
    private var isInflated = false

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
        setupMeasuredHeight()
        setupListener()
    }

    private fun setupListener() {
        this.viewTreeObserver.addOnScrollChangedListener(object :
            ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                if (!isViewShown()) return
                loadComponent()
                viewTreeObserver.removeOnScrollChangedListener(this)
            }
        })
    }

    private fun isViewShown(): Boolean {
        return (parentView.scrollY + screenHeight) >= componentRect.top
    }

    private fun loadComponent() {
        if (isInflated) return
        layoutParams.height = LayoutParams.WRAP_CONTENT
        viewStub.inflate()
        isInflated = true
    }

    fun setParentView(view: View) {
        parentView = view
    }

    private fun setupMeasuredHeight() {
        this.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                getGlobalVisibleRect(componentRect)
                screenHeight = parentView.measuredHeight
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

}