package pl.dn.booklist.ui.mainlist

import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import pl.dn.booklist.data.DataModel
import pl.dn.booklist.data.models.Book

class MainListViewModel() : ViewModel() {

    private lateinit var mDataModel: DataModel

    constructor(dataModel: DataModel) : this() {
        mDataModel = dataModel
    }

    fun refresh(forceFetch: Boolean): Single<ArrayList<Book>> {
        return mDataModel.fetchBookList(forceFetch)
    }
}
