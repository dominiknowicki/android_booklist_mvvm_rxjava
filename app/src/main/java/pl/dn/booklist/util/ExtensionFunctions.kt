package pl.dn.booklist.util

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide

internal fun getDpAsInt(context: Context, dpValue: Int): Int {
    val d = context.resources.displayMetrics.density
    return (dpValue * d).toInt()
}

fun ImageView.loadWithGlide(url: String?) {
    Glide.with(this)
//            .applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
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