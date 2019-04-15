package pl.dn.booklist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pl.dn.booklist.ui.mainlist.MainListFragment

class MainListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_list_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainListFragment.newInstance())
                .commitNow()
        }
    }

}
