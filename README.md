<p align="center">
	<img width="72" height="72" src="art/icon.png"/>
</p>
<h3 align="center">android bluetooth handler </h3>
<p align="center">
<a href="https://raw.githubusercontent.com/HelloHuDi/android-bluetooth-handler/master/abluetoothtools/release/abluetoothtools-release.apk" target="_blank"><img src="https://img.shields.io/badge/release-v1.5.0-blue.svg"></img></a>
<a href="https://raw.githubusercontent.com/HelloHuDi/android-bluetooth-handler/master/abluetoothtools/release/abluetoothtools-release.apk" target="_blank"><img src="https://img.shields.io/badge/demo-v1.5.0-blue.svg"></img></a>
</p>

## **provided Bluetooth 2 and 4 working handler of under Android , and Bluetooth 2 achieves automatic binding**

### **support android sdk version 18+**

## [**abluetoothtools**][1] : bluetooth debug tool project,[download][2] the bluetooth debug tool apk

## [**practice**][3] : practice device project

## [google 4.0 demo project][4]

### screenshot

<img src="art/bluetooth4.png" width="300px" height="500px"/> <img src="art/bluetooth2.png" width="300px" height="500px"/>

### **The following is the kotlin usage**

### **usage**：

```
dependencies {
    //...
    implementation 'com.hd:bluetoothutil:1.5.0'
}
```

### **start scan device**

```
Scanner.scan(Context, BluetoothAdapter?, BluetoothDeviceEntity, ScannerCallback)
```

### **stop scan device**

```
Scanner.stopScan()
```

### **query binding state(4.0 default return to true)**

```
BoundBluetoothDevice.newInstance(context).queryBoundStatus(BluetoothDeviceEntity)
```

### **binding device**

```
BoundBluetoothDevice.newInstance(context).boundDevice(BluetoothDeviceEntity, BleBoundStatusCallback, ScannerCallback?)
```

### **start measure**

```
BluetoothController.init(Context,BluetoothDeviceEntity,BluetoothDevice?,MeasureProgressCallback).startMeasure()
```

### **stop measure**

```
BluetoothController.stopMeasure()
```

### **open the print function,default not print**

```
BL.allowLog=BuildConfig.DEBUG
```

### **BluetoothDeviceEntity：**

name                      | attribute   | function
-------------------------|-------|----
deviceName               | String  | The name of the device (if the parameter is empty in the scanning work, all devices are scanned by default)
macAddress               | String   | Device MAC address
pin                      | String   | Paired pin code (for bluetooth 2.0 device), default 1234
targetCharacteristicUuid | UUID   | Focus on reading and writing special effects for BluetoothGattCharacteristic
reconnected              | Boolean   | If there is a failure in the equipment work, if you need to reconnect, the default false
version                  | DeviceVersion   |Target Bluetooth device version (2.0 and 4.0) 


License
=======

    Copyright 2017 HelloHuDi, Inc.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    

[1]: https://github.com/HelloHuDi/android-bluetooth-handler/tree/master/abluetoothtools
[2]: https://raw.githubusercontent.com/HelloHuDi/android-bluetooth-handler/master/abluetoothtools/release/abluetoothtools-release.apk
[3]: https://github.com/HelloHuDi/android-bluetooth-handler/tree/master/practice
[4]: https://github.com/googlesamples/android-BluetoothLeGatt