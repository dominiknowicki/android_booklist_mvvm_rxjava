package pl.dn.booklist.util

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class CustomRecyclerView : RecyclerView {

    private var customOnScrollListener: OnScrollListener = object : RecyclerView.OnScrollListener() {
        var keyboardDismissed = false

        override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
            when (state) {
                SCROLL_STATE_DRAGGING -> if (!keyboardDismissed) {
                    hideKeyboard()
                    this@CustomRecyclerView.requestFocus()
                    keyboardDismissed = !keyboardDismissed
                }
                SCROLL_STATE_IDLE -> keyboardDismissed = false
            }
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addOnScrollListener(customOnScrollListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeOnScrollListener(customOnScrollListener)
    }
}