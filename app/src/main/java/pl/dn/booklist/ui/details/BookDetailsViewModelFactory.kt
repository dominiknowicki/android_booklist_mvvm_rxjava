package pl.dn.booklist.ui.details

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import pl.dn.booklist.data.DataModel

@Suppress("UNCHECKED_CAST")
class BookDetailsViewModelFactory(private var dataModel: DataModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookDetailsViewModel(dataModel) as T
    }
}