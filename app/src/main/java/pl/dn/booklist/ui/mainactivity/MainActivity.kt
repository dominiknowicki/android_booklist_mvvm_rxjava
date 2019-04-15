package pl.dn.booklist.ui.mainactivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import pl.dn.booklist.R
import pl.dn.booklist.ui.mainlist.MainListFragment

class MainActivity : AppCompatActivity() {

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