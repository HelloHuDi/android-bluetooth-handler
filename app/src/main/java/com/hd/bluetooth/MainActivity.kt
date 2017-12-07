package com.hd.bluetooth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hd.bluetoothutil.BluetoothController
import com.hd.bluetoothutil.MeasureController
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.utils.BL
import kotlinx.android.synthetic.main.activity_main.*
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        var callback: MeasureBle2ProgressCallback? = null
        MeasureController.newChain()
                .onWorker {
                    BluetoothController.init(this, BluetoothDeviceEntity(), callback!!).startMeasure()
                }
                .onUI {
                    callback = object : MeasureBle2ProgressCallback {
                        override fun write(outputStream: OutputStream) {
                            BL.d("it's now allowed to write")
                        }

                        override fun startSearch() {
                            result.append("start search \n")
                        }

                        override fun searchStatus(success: Boolean) {
                            result.append("search $success\n")
                        }

                        override fun startConnect() {
                            result.append("start connect \n")
                        }

                        override fun connectStatus(success: Boolean) {
                            result.append("connect status $success \n")
                        }

                        override fun startRead() {
                            result.append("start read \n")
                        }

                        override fun reading(data: ByteArray) {
                            result.append("receive data :${Arrays.toString(data)} \n")
                        }

                        override fun disconnect() {
                            result.append("disconnect \n")
                        }

                        override fun error(string: String) {
                            result.append("measure error : $string \n")
                        }
                    }
                }
    }

    override fun onPause() {
        super.onPause()
        BluetoothController.stopMeasure()
    }
}
