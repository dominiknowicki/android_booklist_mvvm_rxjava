package pl.dn.booklist.ui.mainlist

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.viewholder_booklist_item.view.*
import pl.dn.booklist.data.models.Book

class BookListAdapter : RecyclerView.Adapter<BookListAdapter.BookListViewHolder>() {

    private var mBookList = ArrayList<Book>()
    private val mItemViewClickSubject = PublishSubject.create<Book>()
    var mItemViewClickObservable: Observable<Book> = mItemViewClickSubject

    init {
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListViewHolder {
        return BookListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(pl.dn.booklist.R.layout.viewholder_booklist_item, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(viewHolder: BookListViewHolder, position: Int) {
        viewHolder.bind(mBookList[position])
        RxView.clicks(viewHolder.itemView)
            .takeUntil(RxView.detaches(viewHolder.itemView))
            .subscribe { mItemViewClickSubject.onNext(mBookList[position]) }
    }

    override fun getItemCount(): Int {
        return mBookList.size
    }

    fun setList(bookList: ArrayList<Book>) {
        mBookList = bookList
        notifyDataSetChanged()
    }

    inner class BookListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(book: Book) {
            itemView.titleTV.text = book.title
        }
    }
}