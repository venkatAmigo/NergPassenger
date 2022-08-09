package com.example.nergpassenger

import android.app.AlertDialog
import android.content.Context

class AlertHelper {
    companion object {
        fun showAlert(
            context: Context, title: String? = null, message: String? = null, positivefunc: (
                () -> Unit)? = null, negativeFunc: (() -> Unit)? = null
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("Ok") { dialog, value ->
                        positivefunc?.invoke()
            }
            builder.setNegativeButton("Cancel"){_,_ ->
                negativeFunc?.invoke()
            }
            builder.show()
        }
    }
}