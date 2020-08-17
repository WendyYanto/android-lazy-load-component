package dev.wendyyanto.library

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewStub
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout

class LazyLoadComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    companion object {
        private const val LAYOUT_RESOURCE_IS_INVALID = "LAYOUT_RESOURCE_IS_INVALID"
        private const val PARENT_RESOURCE_ID_IS_INVALID = "PARENT_RESOURCE_ID_IS_INVALID"
    }

    private val viewStub by lazy {
        ViewStub(context, attrs)
    }

    private val componentRect by lazy {
        Rect()
    }

    private var screenHeight = -1
    private var parentResId = -1
    private var isInflated = false
    private lateinit var parentView: View

    init {
        orientation = VERTICAL
        val typeArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LazyLoadComponent, 0, 0
        )
        try {
            parentResId =
                typeArray.getResourceId(R.styleable.LazyLoadComponent_parent_layout_id, -1)
            val layoutRes = typeArray.getResourceId(R.styleable.LazyLoadComponent_layout_res, -1)
            if (layoutRes == -1) {
                throw IllegalArgumentException(LAYOUT_RESOURCE_IS_INVALID)
            }
            if (parentResId == -1) {
                throw IllegalArgumentException(PARENT_RESOURCE_ID_IS_INVALID)
            }
            with(viewStub) {
                layoutResource = layoutRes
                addView(this)
            }
        } finally {
            typeArray.recycle()
        }
        setupLayoutListener()
        setupScrollListener()
    }

    private fun setupLayoutListener() {
        this.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                getGlobalVisibleRect(componentRect)
                parentView = rootView.findViewById(parentResId)
                screenHeight = parentView.measuredHeight
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun setupScrollListener() {
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

}