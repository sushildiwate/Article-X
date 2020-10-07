package com.sushil.articlex

import android.app.AlertDialog
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sushil.articlex.utils.ConnectivityReceiver
import com.sushil.articlex.utils.isInternetAvailable
import kotlinx.android.synthetic.main.activity_main.*


open class ApplicationActivity : AppCompatActivity(),
    ConnectivityReceiver.ConnectivityReceiverListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun showMessage(isConnected: Boolean) {
        Log.e("ïsConnection", isConnected.toString())
        if (!isConnected) {
            include_layout_snackbar.visibility = View.VISIBLE
            window.statusBarColor = ContextCompat.getColor(this, R.color.monza);

            include_layout_snackbar.setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Article-X")
                    .setMessage("No Internet Connection but you can still view the downloaded articles")
                    .setNegativeButton(
                        "Ok"
                    ) { _, _ ->

                    }.show()
            }
        } else {
            include_layout_snackbar.visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        }


    }

    override fun onResume() {
        super.onResume()
        try {
            if (!isInternetAvailable(this)) {
                showMessage(false)
            }
            this.registerReceiver(
                ConnectivityReceiver(),
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        } catch (e: Exception) {
        }

        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onPause() {
        try {
            this.unregisterReceiver(ConnectivityReceiver())
        } catch (e: Exception) {
        }
        super.onPause()
    }

    override fun onDestroy() {
        try {
            this.unregisterReceiver(ConnectivityReceiver())
        } catch (e: Exception) {
        }
        super.onDestroy()
    }

    override fun onStop() {

        try {
            this.unregisterReceiver(ConnectivityReceiver())
        } catch (e: Exception) {
        }
        super.onStop()
    }

    /**
     * Callback will be called when there is change
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }
}
