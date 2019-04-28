package com.makegoodstuff.association.root.loggedin.home

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.Observable
import kotlinx.android.synthetic.main.home.view.change_password_button

class HomeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), HomeInteractor.HomePresenter {

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override val changePasswordTapped: Observable<Unit> get() = change_password_button.click
}.HomePresenter