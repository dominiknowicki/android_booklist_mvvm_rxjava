package pl.dn.booklist.ui.mainlist

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pl.dn.booklist.APP_TAG
import pl.dn.booklist.data.DataModel
import pl.dn.booklist.data.models.Book

class MainListViewModel() : ViewModel() {

    private lateinit var dataModel: DataModel
    private var bookListSubject = PublishSubject.create<List<Book>>()
    var bookListObservable: Observable<List<Book>> = bookListSubject

    constructor(dataModel: DataModel) : this() {
        this.dataModel = dataModel
    }

    fun refreshObservableBookList() {
        dataModel.getBookList(true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    bookListSubject.onNext(it)
                },
                onError = {
                    Log.e(APP_TAG, "MainListViewModel.refreshObservableBookList: " + it.localizedMessage)
                }
            )
    }

    fun filterObservableBookList(query: String) {
        dataModel.getBookList(false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    val filteredList = it.filter { book: Book -> book.title.contains(query, true) }
                    bookListSubject.onNext(filteredList)
                },
                onError = {
                    Log.e(APP_TAG, "MainListViewModel.filterObservableBookList: " + it.localizedMessage)
                }
            )
    }
}
