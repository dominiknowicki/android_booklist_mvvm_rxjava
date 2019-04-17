package pl.dn.booklist.ui.details

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.style.StyleSpan
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_book_details.*
import kotlinx.android.synthetic.main.custom_layout_loading.*
import pl.dn.booklist.AppImpl
import pl.dn.booklist.R
import pl.dn.booklist.data.models.Book
import pl.dn.booklist.util.loadWithGlide
import pl.dn.booklist.util.setTextWithSpan

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var mViewModel: BookDetailsViewModel
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var mBook: Book

    companion object {
        private const val BOOK_MODEL = "BOOK_MODEL"
        fun startActivity(activity: Activity, club: Book) {
            val intent = Intent(activity, BookDetailsActivity::class.java)
            intent.putExtra(BOOK_MODEL, club)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)
        val dataModel = (application as AppImpl).dataModel
        mViewModel = ViewModelProviders.of(this@BookDetailsActivity, BookDetailsViewModelFactory(dataModel))
            .get(BookDetailsViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        mBook = intent.getParcelableExtra(BOOK_MODEL)
        setUiReactiveObservers()
        setView()
        getImageUrl()
    }

    override fun onPause() {
        mCompositeDisposable.clear()
        super.onPause()
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(R.anim.stay, R.anim.slide_out_right)
    }

    private fun getImageUrl() {
        mCompositeDisposable.add(
            mViewModel.getImageUrl(mBook.title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingLayout.show() }
                .doFinally { loadingLayout.hide() }
                .subscribeBy(
                    onSuccess = {
                        imageIV.loadWithGlide(it, R.drawable.ic_book_outline)
                    })
        )
    }

    @SuppressLint("CheckResult")
    private fun setUiReactiveObservers() {
        RxView.clicks(closeView)
            .subscribe { finish() }
    }

    private fun setView() {
        titleTV.text = mBook.title
        mBook.author?.let {
            val detailText = getString(R.string.written_by_from_in, it, mBook.year)
            detailsTV.setTextWithSpan(detailText, it, StyleSpan(Typeface.BOLD))
        } ?:run {
            detailsTV.text = getString(R.string.written_in, mBook.year)
        }
    }
}