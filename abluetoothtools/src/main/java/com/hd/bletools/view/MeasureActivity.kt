package com.hd.bletools.view

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.Menu
import android.view.MenuItem
import com.hd.bletools.R
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.utils.BL
import kotlinx.android.synthetic.main.activity_measure.*


/**
 * Created by hd on 2017/12/12 .
 * measure
 */
class MeasureActivity : BaseActivity() {
    private var measureFragment: MeasureFragment? = null

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure)
        val bundle = intent.getBundleExtra("bundle") as Bundle
        val version = bundle.getSerializable("version") as DeviceVersion
        val device = bundle.getParcelable<BluetoothDevice>("device")
        BL.d("$version+${device!!.name}")
        val fragmentTransaction = fragmentManager.beginTransaction()
        measureFragment = if (version == DeviceVersion.BLUETOOTH_2) {
            Measure2Fragment()
        } else {
            Measure4Fragment()
        }
        measureFragment!!.arguments = bundle
        fragmentTransaction.replace(R.id.container, measureFragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_measure, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.start_measure -> measureFragment?.startMeasure()
            R.id.stop_measure -> measureFragment?.stopMeasure()
            R.id.send_data -> showInputSheet()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showInputSheet() {
        val behavior = BottomSheetBehavior.from(bottomSheet)
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }

}

