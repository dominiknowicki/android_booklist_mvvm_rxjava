package pl.dn.booklist.ui.mainlist.recyclerview

import pl.dn.booklist.data.models.Book

interface BookListRecyclerCallback {

    fun onClick(book: Book)
}