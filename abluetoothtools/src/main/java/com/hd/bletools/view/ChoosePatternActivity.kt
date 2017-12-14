package com.hd.bletools.view

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.hd.bletools.R
import com.hd.bluetoothutil.callback.ScannerCallback
import com.hd.bluetoothutil.config.BluetoothDeviceEntity
import com.hd.bluetoothutil.config.DeviceVersion
import com.hd.bluetoothutil.scan.Scanner
import com.hd.bluetoothutil.utils.BL
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.activity_choose_pattern.*

class ChoosePatternActivity : BaseActivity() {

    private val REQUEST_CODE = 10086

    private var permission_granted = false

    private var scan_version = DeviceVersion.BLUETOOTH_4

    private val devicesList = mutableListOf<BluetoothDevice>()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission_granted = true
                scan()
            } else {
                permission_granted = false
                snack(R.string.permission_failure)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_pattern)
        checkPermissionGranted()
        addRefresh()
        addAdapter()
    }

    override fun onPause() {
        super.onPause()
        Scanner.stopScan()
    }

    private fun addAdapter() {
        rv_devices.layoutManager = LinearLayoutManager(this)
        rv_devices.itemAnimator = DefaultItemAnimator()
        rv_devices.adapter = commonAdapter()
    }

    @SuppressLint("ResourceAsColor")
    private fun addRefresh() {
        sw_refresh.setColorSchemeResources(R.color.colorAccent)
        sw_refresh.setOnRefreshListener({
            scan()
            sw_refresh.isRefreshing = false
        })
    }

    private fun checkPermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.LOCATION)) {
                permission_granted = false
                snack(R.string.permission_failure)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE)
            }
        } else {
            permission_granted = true
        }
    }

    private fun commonAdapter(): CommonAdapter<BluetoothDevice> {
        val commonAdapter = object : CommonAdapter<BluetoothDevice>(this, R.layout.item_device, devicesList) {
            @SuppressLint("SetTextI18n")
            override fun convert(holder: ViewHolder?, t: BluetoothDevice?, position: Int) {
                if (holder != null && t != null) {
                    holder.getView<TextView>(R.id.tv_details).text = "Name: " + t.name + "\n" +//
                            "Address: " + t.address + "\n" + "BondState: " + (t.bondState == BluetoothDevice.BOND_BONDED)
                }
            }
        }
        commonAdapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
                BL.d("commonAdapter onItemClick")
                Scanner.stopScan()
                val intent = Intent(this@ChoosePatternActivity, MeasureActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("device", devicesList[position])
                bundle.putSerializable("version", scan_version)
                intent.putExtra("bundle", bundle)
                startActivity(intent)
            }
        })
        return commonAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scan_type, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scan2 -> {
                scan_version = DeviceVersion.BLUETOOTH_2
                scan()
            }
            R.id.scan4 -> {
                scan_version = DeviceVersion.BLUETOOTH_4
                scan()
            }
            R.id.stopScan -> Scanner.stopScan()
        }
        return super.onOptionsItemSelected(item)
    }

    /** eliminating duplication equipment*/
    private val devicesSet = mutableSetOf<BluetoothDevice>()

    /** start scan device */
    private fun scan() {
        if (permission_granted) {
            devicesList.clear()
            devicesSet.clear()
            rv_devices.adapter.notifyDataSetChanged()
            snack(if (scan_version == DeviceVersion.BLUETOOTH_2) R.string.scan_bluetooth_2 else R.string.scan_bluetooth_4)
            Scanner.scan(this, entity = BluetoothDeviceEntity(version = scan_version),
                    callback = object : ScannerCallback {
                        override fun scan(scanComplete: Boolean, device: BluetoothDevice?) {
                            BL.d("scan : $scanComplete = ${device?.name}")
                            if (!scanComplete && device != null) {
                                devicesList.clear()
                                devicesSet.add(device)
                                devicesList.addAll(devicesSet)
                                rv_devices.adapter.notifyDataSetChanged()
                            } else {
                                snack(R.string.scan_complete)
                            }
                        }
                    })
        } else {
            checkPermissionGranted()
        }
    }

    private fun snack(@StringRes strId: Int) {
        Snackbar.make(rv_devices, resources.getString(strId), Snackbar.LENGTH_LONG).show()
    }
}

