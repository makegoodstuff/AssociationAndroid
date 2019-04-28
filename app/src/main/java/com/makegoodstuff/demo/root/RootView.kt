package com.makegoodstuff.demoapp.root

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.makegoodstuff.util.isVisible
import io.reactivex.Observable
import kotlinx.android.synthetic.main.root_view.view.root_view_content

class RootView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0) : FrameLayout(context, attrs, defStyle), RootInteractor.RootPresenter {

    val content: ViewGroup get() = root_view_content
}
