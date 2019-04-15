package pl.dn.booklist.ui.mainlist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_list_fragment.*
import pl.dn.booklist.AppImpl
import pl.dn.booklist.R
import pl.dn.booklist.data.models.Book
import pl.dn.booklist.ui.mainlist.recyclerview.BookListAdapter
import pl.dn.booklist.ui.mainlist.recyclerview.BookListRecyclerCallback
import pl.dn.booklist.util.setHorizontalDivider


class MainListFragment : Fragment(), BookListRecyclerCallback {

    companion object {
        fun newInstance() = MainListFragment()
    }

    private lateinit var mViewModel: MainListViewModel
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val bookListAdapter by lazy {
        BookListAdapter(ArrayList(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dataModel = (activity?.application as AppImpl).dataModel
        mViewModel = ViewModelProviders.of(this, MainListViewModelFactory(dataModel)).get(MainListViewModel::class.java)
        initRecycler()
        initSwipeToRefresh()
    }

    override fun onResume() {
        super.onResume()
        refresh(false)
    }

    override fun onPause() {
        mCompositeDisposable.dispose()
        super.onPause()
    }

    override fun onClick(book: Book) {
        Toast.makeText(activity, book.title, Toast.LENGTH_SHORT).show()
    }

    private fun initRecycler() {
        listRecyclerView.adapter = bookListAdapter
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        listRecyclerView.layoutManager = layoutManager
        listRecyclerView.setHorizontalDivider(R.drawable.divider)
    }

    private fun initSwipeToRefresh() {
        listSwipeRefreshLayout.setOnRefreshListener {
            refresh(true)
        }
    }

    private fun refresh(forceRefresh: Boolean) {
        mCompositeDisposable
            .add(
                mViewModel.refresh(forceRefresh)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onSuccess = {
                            bookListAdapter.setList(it)
                            listSwipeRefreshLayout.isRefreshing = false
                        },
                        onError = {
                            listSwipeRefreshLayout.isRefreshing = false
                            Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_SHORT).show()
                        })
            )
    }
}