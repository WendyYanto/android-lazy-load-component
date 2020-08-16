package dev.wendyyanto.lazy_load_components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import dev.wendyyanto.library.LazyLoadComponent

class MainActivity : AppCompatActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var lazyComponent: LazyLoadComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scrollView = findViewById(R.id.cl_parent)
        lazyComponent = findViewById(R.id.llc)
        lazyComponent.setParentView(scrollView)
    }
}