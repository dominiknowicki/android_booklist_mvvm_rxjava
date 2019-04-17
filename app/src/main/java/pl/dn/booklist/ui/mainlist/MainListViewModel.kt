package pl.dn.booklist.ui.mainlist

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pl.dn.booklist.data.DataModel
import pl.dn.booklist.data.models.Book

class MainListViewModel() : ViewModel() {

    private lateinit var mDataModel: DataModel
    private var mBookListSubject = PublishSubject.create<ArrayList<Book>>()
    var mBookListObservable: Observable<ArrayList<Book>> = mBookListSubject

    constructor(dataModel: DataModel) : this() {
        mDataModel = dataModel
    }

    fun refreshObservableBookList() {
        mDataModel.getBookList(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    mBookListSubject.onNext(it)
                })
    }

    fun filterObservableBookList(query: String) {
        mDataModel.getBookList(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val filteredList = ArrayList(it.filter { book: Book -> book.title.contains(query, true) })
                    mBookListSubject.onNext(filteredList)
                })
    }
}
