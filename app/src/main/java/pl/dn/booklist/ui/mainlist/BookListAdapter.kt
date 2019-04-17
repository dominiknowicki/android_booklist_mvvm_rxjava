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

    private var bookList = listOf<Book>()
    private var itemViewClickSubject = PublishSubject.create<Book>()
    var itemViewClickObservable: Observable<Book> = itemViewClickSubject

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
        viewHolder.bind(bookList[position])
        RxView.clicks(viewHolder.itemView)
            .takeUntil(RxView.detaches(viewHolder.itemView))
            .subscribe { itemViewClickSubject.onNext(bookList[position]) }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    fun setItemViewClickObservable() {
        itemViewClickSubject = PublishSubject.create<Book>()
        itemViewClickObservable = itemViewClickSubject
    }

    fun setList(bookList: List<Book>) {
        this.bookList = bookList
        notifyDataSetChanged()
    }

    inner class BookListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(book: Book) {
            itemView.titleTextView.text = book.title
        }
    }
}