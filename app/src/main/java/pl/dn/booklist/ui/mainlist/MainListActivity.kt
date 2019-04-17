package pl.dn.booklist.ui.mainlist

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_list.*
import pl.dn.booklist.AppImpl
import pl.dn.booklist.R
import pl.dn.booklist.util.hideKeyboard
import pl.dn.booklist.util.setHorizontalDivider
import java.util.concurrent.TimeUnit


class MainListActivity : AppCompatActivity() {

    private lateinit var mViewModel: MainListViewModel
    private lateinit var mCompositeDisposable: CompositeDisposable
    private lateinit var mBookListAdapter: BookListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_list)
        listSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimary)
        val dataModel = (application as AppImpl).dataModel
        mViewModel = ViewModelProviders.of(this@MainListActivity, MainListViewModelFactory(dataModel))
            .get(MainListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        mBookListAdapter = BookListAdapter()
        mCompositeDisposable = CompositeDisposable()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        initViewModelReactiveObserver()
        initUIReactiveObservers()
        mViewModel.filterObservableBookList(filterEditText.text.toString())
    }

    override fun onPause() {
        mCompositeDisposable.clear()
        super.onPause()
    }

    private fun setupRecyclerView() {
        listRecyclerView.adapter = mBookListAdapter
        val layoutManager = LinearLayoutManager(this@MainListActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        listRecyclerView.layoutManager = layoutManager
        listRecyclerView.setHorizontalDivider(R.drawable.divider)
    }

    private fun initViewModelReactiveObserver() {
        mCompositeDisposable.add(
            mViewModel.mBookListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { listSwipeRefreshLayout.isRefreshing = false}
                .subscribeBy(
                    onNext = {
                        mBookListAdapter.setList(it)
                    })
        )
    }

    @SuppressLint("CheckResult")
    private fun initUIReactiveObservers() {
        RxTextView.textChanges(filterEditText)
            .subscribe { charSequence -> mViewModel.filterObservableBookList(charSequence.toString()) }

        RxSwipeRefreshLayout.refreshes(listSwipeRefreshLayout)
            .subscribe {
                hideKeyboard()
                mViewModel.refreshObservableBookList()
                filterEditText.text.clear()
                listSwipeRefreshLayout.isRefreshing = false
            }

        RxView.touches(listRecyclerView) {false}
            .subscribe {
                hideKeyboard()
            }

        mCompositeDisposable.add(mBookListAdapter.mItemViewClickObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                hideKeyboard()
                Toast.makeText(this@MainListActivity, it.title, Toast.LENGTH_SHORT).show()
            })
    }
}