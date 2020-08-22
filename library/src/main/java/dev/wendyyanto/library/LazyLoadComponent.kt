package dev.wendyyanto.library

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

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
    private var resetPaddingFlag = false
    private lateinit var parentView: View
    private var imageView: ImageView? = null
    private var textView: TextView? = null

    init {
        orientation = VERTICAL
        val typeArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LazyLoadComponent, 0, 0
        )
        try {
            parentResId =
                typeArray.getResourceId(R.styleable.LazyLoadComponent_parent_layout_id, -1)
            resetPaddingFlag =
                typeArray.getBoolean(R.styleable.LazyLoadComponent_reset_padding_on_load, false)
            val layoutRes = typeArray.getResourceId(R.styleable.LazyLoadComponent_layout_res, -1)
            val imagePlaceholderRes =
                typeArray.getResourceId(R.styleable.LazyLoadComponent_image_placeholder, -1)
            val textPlaceHolder =
                typeArray.getString(R.styleable.LazyLoadComponent_text_placeholder).orEmpty()
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
            if (imagePlaceholderRes != -1) {
                initPlaceHolderImage(imagePlaceholderRes)
            } else if (textPlaceHolder.isNotBlank()) {
                initPlaceholderText(textPlaceHolder)
            }
        } finally {
            typeArray.recycle()
        }
        setupLayoutListener()
        setupScrollListener()
    }

    private fun initPlaceholderText(value: String) {
        textView = TextView(context)
        textView?.apply {
            text = value
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            addView(this)
        }
    }

    private fun initPlaceHolderImage(resource: Int) {
        imageView = ImageView(context)
        imageView?.apply {
            setImageResource(resource)
            addView(this)
        }
    }

    private fun setupLayoutListener() {
        this.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                setupPlaceholderLayout()
                getGlobalVisibleRect(componentRect)
                parentView = rootView.findViewById(parentResId)
                screenHeight = parentView.measuredHeight
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun setupPlaceholderLayout() {
        imageView?.layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        imageView?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        textView?.layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        textView?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
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
        resetUI()
        viewStub.inflate()
        isInflated = true
    }

    private fun resetUI() {
        layoutParams.height = LayoutParams.WRAP_CONTENT
        removeView(imageView)
        removeView(textView)
        if (!resetPaddingFlag) return
        setPadding(0, 0, 0, 0)
    }

}