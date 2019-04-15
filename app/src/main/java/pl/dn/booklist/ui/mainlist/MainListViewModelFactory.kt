package pl.dn.booklist.ui.mainlist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pl.dn.booklist.data.DataModel

@Suppress("UNCHECKED_CAST")
class MainListViewModelFactory(private var dataModel: DataModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainListViewModel(dataModel) as T
    }
}