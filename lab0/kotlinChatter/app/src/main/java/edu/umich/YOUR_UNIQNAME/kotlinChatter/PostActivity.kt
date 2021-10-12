package edu.umich.YOUR_UNIQNAME.kotlinChatter

import android.os.Bundle
import android.view.Menu
import android.view.Menu.FIRST
import android.view.Menu.NONE
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.umich.YOUR_UNIQNAME.kotlinChatter.ChattStore.postChatt
import edu.umich.YOUR_UNIQNAME.kotlinChatter.databinding.ActivityPostBinding

class PostActivity: AppCompatActivity() {
    private lateinit var view: ActivityPostBinding
    private var enableSend = true
    //private lateinit var submitMenuItem: MenuItem  // cannot be saveInstance

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ActivityPostBinding.inflate(layoutInflater)
        setContentView(view.root)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.apply {
            //add(Menu.NONE, Menu.NONE, 2, getString(R.string.post))
            add(NONE, FIRST, NONE, getString(R.string.send))
            getItem(0).setIcon(android.R.drawable.ic_menu_send).setEnabled(enableSend)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            //submitMenuItem = getItem(0)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == FIRST) {
            enableSend = false
            //submitMenuItem.setEnabled(false)
            invalidateOptionsMenu()
            submitChatt()
        }
        return super.onOptionsItemSelected(item)
    }

    fun submitChatt() {
        val chatt = Chatt(username = view.usernameTextView.text.toString(),
            message = view.messageTextView.text.toString())

        postChatt(applicationContext, chatt)
        finish()
    }
}