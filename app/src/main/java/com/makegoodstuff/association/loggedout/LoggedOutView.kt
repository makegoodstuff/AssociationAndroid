package com.makegoodstuff.association.root.loggedout

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class LoggedOutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), LoggedOutInteractor.LoggedOutPresenter
