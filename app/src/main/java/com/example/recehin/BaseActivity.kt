package com.example.recehin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var rootView: View

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Now rootView must be initialized by the subclass
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Add the system bars insets to the existing padding of rootView
            view.updatePadding(
                top = systemBars.top,
                bottom = if (shouldApplyBottomInset()) systemBars.bottom else 0
            )
            insets
        }
    }

    open fun shouldApplyBottomInset(): Boolean = true
}