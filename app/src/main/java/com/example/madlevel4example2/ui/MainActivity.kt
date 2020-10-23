package com.example.madlevel4example2.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.madlevel4example2.R
import com.example.madlevel4example2.repository.GameRepository
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var historyButton: MenuItem
    private lateinit var deleteButton: MenuItem
    private lateinit var gameRepository: GameRepository
    private lateinit var gameListAdapter: GameListAdapter
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbarHistory))
        gameRepository = GameRepository(baseContext)

        navController = findNavController(R.id.nav_host_fragment)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            historyButton = menu.findItem(R.id.btn_nav_icon_history)
            deleteButton = menu.findItem(R.id.btn_nav_icon_delete)
            deleteButton.isVisible = false
        }
        toggleNavIcon()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.btn_nav_icon_delete -> {
                deleteAllGames()
                true
            }
            R.id.btn_nav_icon_history -> {
                navController.navigate(R.id.action_HomeFragment_to_historyFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportActionBar?.setHomeButtonEnabled(false)
            supportFragmentManager.popBackStack()
        }
        return true
    }

    private fun deleteAllGames() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteAllGames()
            }
            gameListAdapter = GameListAdapter(arrayListOf())
            rvGames.adapter = gameListAdapter
            gameListAdapter.notifyDataSetChanged()

            Toast.makeText(
                baseContext,
                "Successful deleted all games",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * This method listens to the destination change and should switch the available
     * icons shown in the toolbar and change the title
     */
    private fun toggleNavIcon() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(R.id.historyFragment)) { // history fragment
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setTitle(R.string.title_fragment_history)
                historyButton.isVisible = false
                deleteButton.isVisible = true
            } else { // home fragment
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.setTitle(R.string.app_name)
                historyButton.isVisible = true
                deleteButton.isVisible = false
            }
        }
    }

}