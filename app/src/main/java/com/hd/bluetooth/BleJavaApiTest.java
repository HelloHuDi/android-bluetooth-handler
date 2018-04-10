package com.hd.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hd.bluetoothutil.BluetoothController;
import com.hd.bluetoothutil.callback.MeasureBle2ProgressCallback;
import com.hd.bluetoothutil.callback.MeasureBle4ProgressCallback;
import com.hd.bluetoothutil.callback.MeasureProgressCallback;
import com.hd.bluetoothutil.config.BluetoothDeviceEntity;
import com.hd.bluetoothutil.config.DeviceVersion;
import com.hd.bluetoothutil.driver.BluetoothLeService;
import com.hd.bluetoothutil.utils.BL;

import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by hd on 2018/4/10 .
 * java test
 */
public class BleJavaApiTest extends AppCompatActivity {

    private int version = 4;

    private int REQUEST_CODE = 10086;

    private TextView result;

    private ScrollView sv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result=findViewById(R.id.result);
        sv=findViewById(R.id.sv);
        //set allow log
        BL.INSTANCE.setAllowLog(BuildConfig.DEBUG);
        if (version == 4 && ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.LOCATION)) {
                Toast.makeText(this, "注意权限问题", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,//
                                                                     Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            }
        } else {
            measure();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BluetoothController.INSTANCE.stopMeasure();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                measure();
            } else {
                Toast.makeText(this, "权限请求失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void measure() {
        BluetoothDeviceEntity entity = addEntity();
        showResult("==>current device entity : " + entity.toString() + "\n");
        BluetoothController.INSTANCE.init(this, entity,null, addCallback()).startMeasure();
    }

    private MeasureProgressCallback addCallback(){
         if (version == 2) {
             return addProgress2Callback();
        } else {
             return addProgress4Callback();
        }
    }

    /** test */
    private BluetoothDeviceEntity addEntity() {
        if (version == 2) {//bluetooth 2.0 device
            return new BluetoothDeviceEntity("BF4030", null, "", null, //
                                             false, DeviceVersion.BLUETOOTH_2);
        } else {//bluetooth 4.0 device
            return new BluetoothDeviceEntity("BerryMed", null, "",//
                                             UUID.fromString("49535343-1e4d-4bd9-ba61-23c647249616"), //
                                             false, DeviceVersion.BLUETOOTH_4);
        }
    }

    private MeasureBle4ProgressCallback addProgress4Callback() {
        return new MeasureBle4ProgressCallback() {

            public void startSearch() {
                BL.INSTANCE.d(" startSearch current thread:" + Thread.currentThread());
                showResult("==>start search \n");
            }



            public void searchStatus(boolean success) {
                showResult("==>search $success\n");
            }



            public void startBinding() {
                showResult("==>start binding device \n");
            }



            public void boundStatus(boolean success) {
                showResult("==>binding device status :$success \n");
            }



            public void startConnect() {
                showResult("==>start connect \n");
            }



            public void connectStatus(boolean success) {
                showResult("==>connect $success \n");
            }



            public void startRead() {
                showResult("==>start read \n");
            }



            public void write(BluetoothGattCharacteristic bluetoothGattCharacteristic, @NonNull BluetoothLeService bluetoothLeService) {
                BL.INSTANCE.d("it's now allowed to write");
            }


            public void reading(@NonNull byte[] ByteArray) {
                showResult("==>receive data :${Arrays.toString(data)} \n");
            }

            public void disconnect() {
                BL.INSTANCE.d("disconnect current thread:" + Thread.currentThread());
                showResult("==>device disconnect \n");
            }
        };
    }

    private MeasureBle2ProgressCallback addProgress2Callback() {
        return new MeasureBle2ProgressCallback() {


            public void startSearch() {
                BL.INSTANCE.d(" startSearch current thread:" + Thread.currentThread());
                showResult("==>start search \n");
            }

            public void searchStatus(boolean success) {
                showResult("==>search $success\n");
            }

            public void startBinding() {
                showResult("==>start binding device \n");
            }

            public void boundStatus(boolean success) {
                showResult("==>binding device status :$success \n");
            }

            public void startConnect() {
                showResult("==>start connect \n");
            }

            public void connectStatus(boolean success) {
                showResult("==>connect $success \n");
            }

            public void startRead() {
                showResult("==>start read \n");
            }

            public void write(@NonNull OutputStream outputStream) {
                BL.INSTANCE.d("it's now allowed to write");
            }

            public void reading(@NonNull byte[] data) {
                showResult("==>receive data :${Arrays.toString(data)} \n");
            }

            public void disconnect() {
                BL.INSTANCE.d(" disconnect current thread:" + Thread.currentThread());
                showResult("==>device disconnect \n");
            }
        };
    }

    private Handler handler = new Handler();

    private void showResult(final String msg) {
        runOnUiThread(() -> {
            result.append(msg);
            handler.post(()->sv.fullScroll(ScrollView.FOCUS_DOWN));
        });
    }
}
