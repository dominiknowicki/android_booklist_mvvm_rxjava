package pl.dn.booklist.ui.mainlist.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.dn.booklist.R
import pl.dn.booklist.data.models.Book

class BookListAdapter(private var mBookList: ArrayList<Book>, private var callback: BookListRecyclerCallback) : RecyclerView.Adapter<BookListViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        return BookListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.partial_list_item, parent, false), callback)
    }

    override fun onBindViewHolder(holder: BookListViewHolder, position: Int) {
        holder.bind(mBookList[position])
    }

    fun setList(bookList: ArrayList<Book>) {
        mBookList = bookList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mBookList.size
    }
}