package com.makegoodstuff.association.root.loggedin

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class LoggedInView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), LoggedInInteractor.LoggedInPresenter
