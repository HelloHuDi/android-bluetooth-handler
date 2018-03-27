package com.hd.bluetoothutil.driver

import android.bluetooth.BluetoothClass

/**
 * Created by hd on 2017/6/12 0008.
 * Bluetooth device category
 */
object BluetoothClassResolver {

    fun resolveDeviceClass(btClass: Int): String {
        return when (btClass) {
            BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER -> "A/V, Camcorder"
            BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO -> "A/V, Car Audio"
            BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE -> "A/V, Handsfree"
            BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES -> "A/V, Headphones"
            BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO -> "A/V, HiFi Audio"
            BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER -> "A/V, Loudspeaker"
            BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE -> "A/V, Microphone"
            BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO -> "A/V, Portable Audio"
            BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX -> "A/V, Set Top Box"
            BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED -> "A/V, Uncategorized"
            BluetoothClass.Device.AUDIO_VIDEO_VCR -> "A/V, VCR"
            BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA -> "A/V, Video Camera"
            BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING -> "A/V, Video Conferencing"
            BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER -> "A/V, Video Display and Loudspeaker"
            BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY -> "A/V, Video Gaming Toy"
            BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR -> "A/V, Video Monitor"
            BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET -> "A/V, Video Wearable Headset"
            BluetoothClass.Device.COMPUTER_DESKTOP -> "Computer, Desktop"
            BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA -> "Computer, Handheld PC/PDA"
            BluetoothClass.Device.COMPUTER_LAPTOP -> "Computer, Laptop"
            BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA -> "Computer, Palm Size PC/PDA"
            BluetoothClass.Device.COMPUTER_SERVER -> "Computer, Server"
            BluetoothClass.Device.COMPUTER_UNCATEGORIZED -> "Computer, Uncategorized"
            BluetoothClass.Device.COMPUTER_WEARABLE -> "Computer, Wearable"
            BluetoothClass.Device.HEALTH_BLOOD_PRESSURE -> "Health, Blood Pressure"
            BluetoothClass.Device.HEALTH_DATA_DISPLAY -> "Health, Data Display"
            BluetoothClass.Device.HEALTH_GLUCOSE -> "Health, Glucose"
            BluetoothClass.Device.HEALTH_PULSE_OXIMETER -> "Health, Pulse Oximeter"
            BluetoothClass.Device.HEALTH_PULSE_RATE -> "Health, Pulse Rate"
            BluetoothClass.Device.HEALTH_THERMOMETER -> "Health, Thermometer"
            BluetoothClass.Device.HEALTH_UNCATEGORIZED -> "Health, Uncategorized"
            BluetoothClass.Device.HEALTH_WEIGHING -> "Health, Weighting"
            BluetoothClass.Device.PHONE_CELLULAR -> "Phone, Cellular"
            BluetoothClass.Device.PHONE_CORDLESS -> "Phone, Cordless"
            BluetoothClass.Device.PHONE_ISDN -> "Phone, ISDN"
            BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY -> "Phone, Modem or Gateway"
            BluetoothClass.Device.PHONE_SMART -> "Phone, Smart"
            BluetoothClass.Device.PHONE_UNCATEGORIZED -> "Phone, Uncategorized"
            BluetoothClass.Device.TOY_CONTROLLER -> "Toy, Controller"
            BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE -> "Toy, Doll/Action Figure"
            BluetoothClass.Device.TOY_GAME -> "Toy, Game"
            BluetoothClass.Device.TOY_ROBOT -> "Toy, Robot"
            BluetoothClass.Device.TOY_UNCATEGORIZED -> "Toy, Uncategorized"
            BluetoothClass.Device.TOY_VEHICLE -> "Toy, Vehicle"
            BluetoothClass.Device.WEARABLE_GLASSES -> "Wearable, Glasses"
            BluetoothClass.Device.WEARABLE_HELMET -> "Wearable, Helmet"
            BluetoothClass.Device.WEARABLE_JACKET -> "Wearable, Jacket"
            BluetoothClass.Device.WEARABLE_PAGER -> "Wearable, Pager"
            BluetoothClass.Device.WEARABLE_UNCATEGORIZED -> "Wearable, Uncategorized"
            BluetoothClass.Device.WEARABLE_WRIST_WATCH -> "Wearable, Wrist Watch"
            else -> "Unknown, Unknown (class=$btClass)"
        }
    }

    fun resolveMajorDeviceClass(majorBtClass: Int): String {
        return when (majorBtClass) {
            BluetoothClass.Device.Major.AUDIO_VIDEO -> "Audio/ Video"
            BluetoothClass.Device.Major.COMPUTER -> "Computer"
            BluetoothClass.Device.Major.HEALTH -> "Health"
            BluetoothClass.Device.Major.IMAGING -> "Imaging"
            BluetoothClass.Device.Major.MISC -> "Misc"
            BluetoothClass.Device.Major.NETWORKING -> "Networking"
            BluetoothClass.Device.Major.PERIPHERAL -> "Peripheral"
            BluetoothClass.Device.Major.PHONE -> "Phone"
            BluetoothClass.Device.Major.TOY -> "Toy"
            BluetoothClass.Device.Major.UNCATEGORIZED -> "Uncategorized"
            BluetoothClass.Device.Major.WEARABLE -> "Wearable"
            else -> "Unknown ($majorBtClass)"
        }
    }
}
