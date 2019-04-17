package pl.dn.booklist.ui.mainlist

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_list.*
import pl.dn.booklist.APP_TAG
import pl.dn.booklist.AppImpl
import pl.dn.booklist.R
import pl.dn.booklist.ui.details.BookDetailsActivity
import pl.dn.booklist.util.hideKeyboard
import pl.dn.booklist.util.setHorizontalDivider
import java.util.concurrent.TimeUnit

class MainListActivity : AppCompatActivity() {

    private lateinit var viewModel: MainListViewModel
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var bookListAdapter: BookListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_main_list)
        listSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimary)
        val dataModel = (application as AppImpl).dataModel
        viewModel = ViewModelProviders.of(this@MainListActivity, MainListViewModelFactory(dataModel))
            .get(MainListViewModel::class.java)
        setRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        bookListAdapter.setItemViewClickObservable()
    }

    override fun onResume() {
        super.onResume()
        setViewModelReactiveObserver()
        setUiReactiveObservers()
        viewModel.filterObservableBookList(filterEditText.text.toString())
    }

    override fun onPause() {
        compositeDisposable.clear()
        super.onPause()
    }

    private fun setRecyclerView() {
        bookListAdapter = BookListAdapter()
        listRecyclerView.adapter = bookListAdapter
        val layoutManager = LinearLayoutManager(this@MainListActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        listRecyclerView.layoutManager = layoutManager
        listRecyclerView.setHorizontalDivider(R.drawable.divider)
    }

    private fun setViewModelReactiveObserver() {
        compositeDisposable.add(
            viewModel.bookListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { listSwipeRefreshLayout.isRefreshing = false }
                .subscribeBy(
                    onNext = {
                        bookListAdapter.setList(it)
                    },
                    onError = {
                        Log.e(APP_TAG, "MainListActivity.setViewModelReactiveObserver: " + it.localizedMessage)
                    }
                )
        )
    }

    @SuppressLint("CheckResult")
    private fun setUiReactiveObservers() {
        RxTextView.textChanges(filterEditText)
            .subscribe { charSequence -> viewModel.filterObservableBookList(charSequence.toString()) }

        RxSwipeRefreshLayout.refreshes(listSwipeRefreshLayout)
            .subscribe {
                listRecyclerView.requestFocus()
                hideKeyboard()
                viewModel.refreshObservableBookList()
                filterEditText.text.clear()
                listSwipeRefreshLayout.isRefreshing = false
            }

        compositeDisposable.add(bookListAdapter.itemViewClickObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                listRecyclerView.requestFocus()
                hideKeyboard()
                BookDetailsActivity.startActivity(this@MainListActivity, it)
            })
    }
}