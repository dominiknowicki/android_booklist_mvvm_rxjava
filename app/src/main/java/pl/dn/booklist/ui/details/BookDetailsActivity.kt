package pl.dn.booklist.ui.details

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.style.StyleSpan
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_book_details.*
import pl.dn.booklist.R
import pl.dn.booklist.data.models.Book
import pl.dn.booklist.util.loadWithGlide
import pl.dn.booklist.util.setTextWithSpan

class BookDetailsActivity : AppCompatActivity() {

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
    }

    override fun onResume() {
        super.onResume()
        mBook = intent.getParcelableExtra(BOOK_MODEL)
        setUpOnClicks()
        fillWithData()
    }

    override fun finish() {
        super.finish()
        this.overridePendingTransition(R.anim.stay, R.anim.slide_out_right)
    }

    @SuppressLint("CheckResult")
    private fun setUpOnClicks() {
        RxView.clicks(closeView)
            .subscribe { finish() }
    }

    private fun fillWithData() {
        imageIV.loadWithGlide(mBook.imageLink, R.drawable.ic_book_outline)
        titleTV.text = mBook.title
        mBook.author?.let {
            val detailText = getString(R.string.written_by_from_in, it, mBook.year)
            detailsTV.setTextWithSpan(detailText, it, StyleSpan(Typeface.BOLD))
        } ?:run {
            detailsTV.text = getString(R.string.written_in, mBook.year)
        }
    }
}