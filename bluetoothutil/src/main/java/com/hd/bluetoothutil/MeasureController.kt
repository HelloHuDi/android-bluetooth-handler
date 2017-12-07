package com.hd.bluetoothutil

import android.os.Handler
import android.support.annotation.UiThread
import com.hd.bluetoothutil.utils.BL


/**
 * Created by hd on 2017/11/29 .
 * control switcher bluetooth measure thread
 */
object MeasureController {

    private var uiHandler: Handler

    init {
        @UiThread
        uiHandler = Handler()
        BL.d("init uiHandler")
    }

    fun newChain(openLog: Boolean = false) = Chain { BL.allowLog = openLog }

    class Chain<out Param>(private val priorTask: () -> Param) {

        /** Adds a task that will be executed on a Worker thread. */

        fun <Return> onWorker(task: (Param) -> Return): Chain<Return> = Chain { task(priorTask()) }

        /** Adds a task that will be executed on the UI thread. */

        fun <Return> onUI(task: (Param) -> Return): Chain<Return> = Chain {
            var tempResult: Return? = null
            val param = priorTask()
            var exception: Throwable? = null
            uiHandler.post {
                synchronized(this, {
                    try {
                        tempResult = task(param)
                    } catch (e: Throwable) {
                        exception = e
                    }
                    (this as java.lang.Object).notify()
                })
            }
            synchronized(this, {
                if (tempResult == null && exception == null)
                    (this as java.lang.Object).wait()
            })
            if (exception != null) throw exception!!
            tempResult!!
        }
    }
}