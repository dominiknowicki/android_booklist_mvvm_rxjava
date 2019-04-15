package pl.dn.booklist.ui.mainlist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import pl.dn.booklist.AppImpl
import pl.dn.booklist.R


class MainListFragment : Fragment() {

    companion object {
        fun newInstance() = MainListFragment()
    }

    private lateinit var mViewModel: MainListViewModel
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

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
    }

    override fun onResume() {
        super.onResume()
        mCompositeDisposable
            .add(
                mViewModel.refresh(false)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onSuccess = {
                            Log.d("BOOKLIST", "SUCCESS")
                        },
                        onError = {
                            Log.d("BOOKLIST", "FAIL")
                        })
            )
    }

    override fun onPause() {
        mCompositeDisposable.dispose()
        super.onPause()
    }
}