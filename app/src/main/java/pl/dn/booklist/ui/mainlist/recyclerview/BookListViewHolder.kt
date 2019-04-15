package pl.dn.booklist.ui.mainlist.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.partial_list_item.view.*
import pl.dn.booklist.data.models.Book

class BookListViewHolder(itemView: View, var callback: BookListRecyclerCallback) : RecyclerView.ViewHolder(itemView) {

    private lateinit var mBook: Book

    fun bind(book: Book) {
        this.mBook = book
        itemView.setOnClickListener { callback.onClick(mBook) }
        setupLayout()
    }

    private fun setupLayout() {
        itemView.titleTV.text = mBook.title
    }
}