package pl.dn.booklist.util

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadWithGlide(url: String?, placeholderResId: Int) {
    Glide.with(this)
        .applyDefaultRequestOptions(RequestOptions().placeholder(placeholderResId).error(placeholderResId))
        .load(url)
        .into(this)
}

fun RecyclerView.setHorizontalDivider(drawable: Int) {
    val verticalDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
    ContextCompat.getDrawable(this.context, drawable)?.let {
        verticalDecoration.setDrawable(it)
        this.addItemDecoration(verticalDecoration)
    }
}

fun AppCompatActivity.hideKeyboard() {
    try {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    } catch (ignored: Throwable) {
    }
}

fun View.hideKeyboard() {
    try {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
        clearFocus()
    } catch (ignored: Throwable) {
    }
}

fun TextView.setTextWithSpan(text: String, spanText: String, style: StyleSpan) {
    val sb = SpannableStringBuilder(text)
    val start = text.indexOf(spanText)
    val end = start + spanText.length
    sb.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    this.text = sb
}
