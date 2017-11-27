package com.hd.bluetoothutil.driver

import java.util.*

/**
 * Created by hd on 2017/6/12 0008.
 * Bluetooth服务发现协议(SDP)规格
 * 参考：https://www.bluetooth.com/zh-cn/specifications/assigned-numbers/service-discovery
 */
object GattAttributeResolver {
//    val SPP_UUID="00001101-0000-1000-8000-00805F9B34FB"
    val BASE_GUID = "00000000-0000-1000-8000-00805f9b34fb"
    val SERVICE_DISCOVERY_PROTOCOL_SDP = "00000001-0000-1000-8000-00805f9b34fb"
    val USER_DATAGRAM_PROTOCOL_UDP = "00000002-0000-1000-8000-00805f9b34fb"
    val RADIO_FREQUENCY_COMMUNICATION_PROTOCOL_RFCOMM = "00000003-0000-1000-8000-00805f9b34fb"
    val TCP = "00000004-0000-1000-8000-00805f9b34fb"
    val TCSBIN = "00000005-0000-1000-8000-00805f9b34fb"
    val TCSAT = "00000006-0000-1000-8000-00805f9b34fb"
    val OBJECT_EXCHANGE_PROTOCOL_OBEX = "00000008-0000-1000-8000-00805f9b34fb"
    val IP = "00000009-0000-1000-8000-00805f9b34fb"
    val FTP = "0000000a-0000-1000-8000-00805f9b34fb"
    val HTTP = "0000000c-0000-1000-8000-00805f9b34fb"
    val WSP = "0000000e-0000-1000-8000-00805f9b34fb"
    val BNEP_SVC = "0000000f-0000-1000-8000-00805f9b34fb"
    val UPNP_PROTOCOL = "00000010-0000-1000-8000-00805f9b34fb"
    val HIDP = "00000011-0000-1000-8000-00805f9b34fb"
    val HARDCOPY_CONTROL_CHANNEL_PROTOCOL = "00000012-0000-1000-8000-00805f9b34fb"
    val HARDCOPY_DATA_CHANNEL_PROTOCOL = "00000014-0000-1000-8000-00805f9b34fb"
    val HARDCOPY_NOTIFICATION_PROTOCOL = "00000016-0000-1000-8000-00805f9b34fb"
    val VCTP_PROTOCOL = "00000017-0000-1000-8000-00805f9b34fb"
    val VDTP_PROTOCOL = "00000019-0000-1000-8000-00805f9b34fb"
    val CMPT_PROTOCOL = "0000001b-0000-1000-8000-00805f9b34fb"
    val UDI_C_PLANE_PROTOCOL = "0000001d-0000-1000-8000-00805f9b34fb"
    val MCAP_CONTROL_CHANNEL = "0000001e-0000-1000-8000-00805f9b34fb"
    val MCAP_DATA_CHANNEL = "0000001f-0000-1000-8000-00805f9b34fb"
    val L2CAP = "00000100-0000-1000-8000-00805f9b34fb"
    val SERVICE_DISCOVERY_SERVER = "00001000-0000-1000-8000-00805f9b34fb"
    val BROWSE_GROUP_DESCRIPTOR = "00001001-0000-1000-8000-00805f9b34fb"
    val PUBLIC_BROWSE_GROUP = "00001002-0000-1000-8000-00805f9b34fb"
    val SPP = "00001101-0000-1000-8000-00805f9b34fb"
    val LAN_ACCESS_USING_PPP = "00001102-0000-1000-8000-00805f9b34fb"
    val DUN_GW = "00001103-0000-1000-8000-00805f9b34fb"
    val OBEX_SYNC = "00001104-0000-1000-8000-00805f9b34fb"
    val OBEX_OBJECT_PUSH = "00001105-0000-1000-8000-00805f9b34fb"
    val OBEX_FILE_TRANSFER = "00001106-0000-1000-8000-00805f9b34fb"
    val IRMC_SYNC_COMMAND = "00001107-0000-1000-8000-00805f9b34fb"
    val HSP_HS = "00001108-0000-1000-8000-00805f9b34fb"
    val CORDLESS_TELEPHONY = "00001109-0000-1000-8000-00805f9b34fb"
    val AUDIO_SOURCE = "0000110a-0000-1000-8000-00805f9b34fb"
    val AUDIO_SINK = "0000110b-0000-1000-8000-00805f9b34fb"
    val AV_REMOTE_CONTROL_TARGET = "0000110c-0000-1000-8000-00805f9b34fb"
    val ADVANCED_AUDIO = "0000110d-0000-1000-8000-00805f9b34fb"
    val AVRCP_REMOTE = "0000110e-0000-1000-8000-00805f9b34fb"
    val VIDEO_CONFERENCING = "0000110f-0000-1000-8000-00805f9b34fb"
    val INTERCOM = "00001110-0000-1000-8000-00805f9b34fb"
    val FAX = "00001111-0000-1000-8000-00805f9b34fb"
    val HEADSET_PROFILE_HSP_AUDIO_GATEWAY = "00001112-0000-1000-8000-00805f9b34fb"
    val WAP = "00001113-0000-1000-8000-00805f9b34fb"
    val WAP_CLIENT = "00001114-0000-1000-8000-00805f9b34fb"
    val PANU = "00001115-0000-1000-8000-00805f9b34fb"
    val NAP = "00001116-0000-1000-8000-00805f9b34fb"
    val GN = "00001117-0000-1000-8000-00805f9b34fb"
    val DIRECT_PRINTING = "00001118-0000-1000-8000-00805f9b34fb"
    val REFERENCE_PRINTING = "00001119-0000-1000-8000-00805f9b34fb"
    val IMAGING = "0000111a-0000-1000-8000-00805f9b34fb"
    val IMAGING_RESPONDER = "0000111b-0000-1000-8000-00805f9b34fb"
    val IMAGING_AUTOMATIC_ARCHIVE = "0000111c-0000-1000-8000-00805f9b34fb"
    val IMAGING_REFERENCE_OBJECTS = "0000111d-0000-1000-8000-00805f9b34fb"
    val HANDS_FREE_PROFILE_HFP = "0000111e-0000-1000-8000-00805f9b34fb"
    val HANDS_FREE_PROFILE_HFP_AUDIO_GATEWAY = "0000111f-0000-1000-8000-00805f9b34fb"
    val DIRECT_PRINTING_REFERENCE_OBJECTS = "00001120-0000-1000-8000-00805f9b34fb"
    val REFLECTED_UI = "00001121-0000-1000-8000-00805f9b34fb"
    val BASIC_PRINTING = "00001122-0000-1000-8000-00805f9b34fb"
    val PRINTING_STATUS = "00001123-0000-1000-8000-00805f9b34fb"
    val HID = "00001124-0000-1000-8000-00805f9b34fb"
    val HARDCOPY_CABLE_REPLACEMENT = "00001125-0000-1000-8000-00805f9b34fb"
    val HCR_PRINT = "00001126-0000-1000-8000-00805f9b34fb"
    val HCR_SCAN = "00001127-0000-1000-8000-00805f9b34fb"
    val COMMON_ISDN_ACCESS = "00001128-0000-1000-8000-00805f9b34fb"
    val VIDEO_CONFERENCING_GATEWAY = "00001129-0000-1000-8000-00805f9b34fb"
    val UDIMT = "0000112a-0000-1000-8000-00805f9b34fb"
    val UDITA = "0000112b-0000-1000-8000-00805f9b34fb"
    val AUDIO_VIDEO = "0000112c-0000-1000-8000-00805f9b34fb"
    val SIM_ACCESS = "0000112d-0000-1000-8000-00805f9b34fb"
    val OBEX_PCE = "0000112e-0000-1000-8000-00805f9b34fb"
    val OBEX_PSE = "0000112f-0000-1000-8000-00805f9b34fb"
    val OBEX_PBAP = "00001130-0000-1000-8000-00805f9b34fb"
    val OBEX_MAS = "00001132-0000-1000-8000-00805f9b34fb"
    val OBEX_MNS = "00001133-0000-1000-8000-00805f9b34fb"
    val OBEX_MAP = "00001134-0000-1000-8000-00805f9b34fb"
    val PNP = "00001200-0000-1000-8000-00805f9b34fb"
    val GENERIC_NETWORKING = "00001201-0000-1000-8000-00805f9b34fb"
    val GENERIC_FILE_TRANSFER = "00001202-0000-1000-8000-00805f9b34fb"
    val GENERIC_AUDIO = "00001203-0000-1000-8000-00805f9b34fb"
    val GENERIC_TELEPHONY = "00001204-0000-1000-8000-00805f9b34fb"
    val UPNP = "00001205-0000-1000-8000-00805f9b34fb"
    val UPNP_IP = "00001206-0000-1000-8000-00805f9b34fb"
    val ESDP_UPNP_IP_PAN = "00001300-0000-1000-8000-00805f9b34fb"
    val ESDP_UPNP_IP_LAP = "00001301-0000-1000-8000-00805f9b34fb"
    val ESDP_UPNP_L2CAP = "00001302-0000-1000-8000-00805f9b34fb"
    val VIDEO_DISTRIBUTION_PROFILE_VDP_SOURCE = "00001303-0000-1000-8000-00805f9b34fb"
    val VIDEO_DISTRIBUTION_PROFILE_VDP_SINK = "00001304-0000-1000-8000-00805f9b34fb"
    val VIDEO_DISTRIBUTION_PROFILE_VDP = "00001305-0000-1000-8000-00805f9b34fb"
    val HEALTH_DEVICE_PROFILE_HDP = "00001400-0000-1000-8000-00805f9b34fb"
    val HEALTH_DEVICE_PROFILE_HDP_SOURCE = "00001401-0000-1000-8000-00805f9b34fb"
    val HEALTH_DEVICE_PROFILE_HDP_SINK = "00001402-0000-1000-8000-00805f9b34fb"
    val GAP = "00001800-0000-1000-8000-00805f9b34fb"
    val GATT = "00001801-0000-1000-8000-00805f9b34fb"
    val IMMEDIATE_ALERT = "00001802-0000-1000-8000-00805f9b34fb"
    val LINK_LOSS = "00001803-0000-1000-8000-00805f9b34fb"
    val TX_POWER = "00001804-0000-1000-8000-00805f9b34fb"
    val HEALTH_THERMOMETER = "00001809-0000-1000-8000-00805f9b34fb"
    val DEVICE_INFORMATION = "0000180a-0000-1000-8000-00805f9b34fb"
    val HEART_RATE = "0000180d-0000-1000-8000-00805f9b34fb"
    val CYCLING_SC = "00001816-0000-1000-8000-00805f9b34fb"
    val CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb"
    val DEVICE_NAME = "00002a00-0000-1000-8000-00805f9b34fb"
    val APPEARANCE = "00002a01-0000-1000-8000-00805f9b34fb"
    val PERIPHERAL_PRIVACY_FLAG = "00002a02-0000-1000-8000-00805f9b34fb"
    val RECONNECTION_ADDRESS = "00002a03-0000-1000-8000-00805f9b34fb"
    val PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "00002a04-0000-1000-8000-00805f9b34fb"
    val SERVICE_CHANGED = "00002a05-0000-1000-8000-00805f9b34fb"
    val ALERT_LEVEL = "00002a06-0000-1000-8000-00805f9b34fb"
    val TX_POWER_LEVEL = "00002a07-0000-1000-8000-00805f9b34fb"
    val DATE_TIME = "00002a08-0000-1000-8000-00805f9b34fb"
    val DAY_OF_WEEK = "00002a09-0000-1000-8000-00805f9b34fb"
    val DAY_DATE_TIME = "00002a0a-0000-1000-8000-00805f9b34fb"
    val EXACT_TIME_256 = "00002a0c-0000-1000-8000-00805f9b34fb"
    val DST_OFFSET = "00002a0d-0000-1000-8000-00805f9b34fb"
    val TIME_ZONE = "00002a0e-0000-1000-8000-00805f9b34fb"
    val LOCAL_TIME_INFORMATION = "00002a0f-0000-1000-8000-00805f9b34fb"
    val TIME_WITH_DST = "00002a11-0000-1000-8000-00805f9b34fb"
    val TIME_ACCURACY = "00002a12-0000-1000-8000-00805f9b34fb"
    val TIME_SOURCE = "00002a13-0000-1000-8000-00805f9b34fb"
    val REFERENCE_TIME_INFORMATION = "00002a14-0000-1000-8000-00805f9b34fb"
    val TIME_UPDATE_CONTROL_POINT = "00002a16-0000-1000-8000-00805f9b34fb"
    val TIME_UPDATE_STATE = "00002a17-0000-1000-8000-00805f9b34fb"
    val TEMPERATURE_MEASUREMENT = "00002a1c-0000-1000-8000-00805f9b34fb"
    val TEMPERATURE_TYPE = "00002a1d-0000-1000-8000-00805f9b34fb"
    val INTERMEDIATE_TEMPERATURE = "00002a1e-0000-1000-8000-00805f9b34fb"
    val MEASUREMENT_INTERVAL = "00002a21-0000-1000-8000-00805f9b34fb"
    val SYSTEM_ID = "00002a23-0000-1000-8000-00805f9b34fb"
    val MODEL_NUMBER_STRING = "00002a24-0000-1000-8000-00805f9b34fb"
    val SERIAL_NUMBER_STRING = "00002a25-0000-1000-8000-00805f9b34fb"
    val FIRMWARE_REVISION_STRING = "00002a26-0000-1000-8000-00805f9b34fb"
    val HARDWARE_REVISION_STRING = "00002a27-0000-1000-8000-00805f9b34fb"
    val SOFTWARE_REVISION_STRING = "00002a28-0000-1000-8000-00805f9b34fb"
    val MANUFACTURER_NAME_STRING = "00002a29-0000-1000-8000-00805f9b34fb"
    val IEEE_1107320601_REGULATORY = "00002a2a-0000-1000-8000-00805f9b34fb"
    val CURRENT_TIME = "00002a2b-0000-1000-8000-00805f9b34fb"
    val BLOOD_PRESSURE_MEASUREMENT = "00002a35-0000-1000-8000-00805f9b34fb"
    val INTERMEDIATE_CUFF_PRESSURE = "00002a36-0000-1000-8000-00805f9b34fb"
    val HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb"
    val BODY_SENSOR_LOCATION = "00002a38-0000-1000-8000-00805f9b34fb"
    val HEART_RATE_CONTROL_POINT = "00002a39-0000-1000-8000-00805f9b34fb"
    val ALERT_STATUS = "00002a3f-0000-1000-8000-00805f9b34fb"
    val RINGER_CONTROL_POINT = "00002a40-0000-1000-8000-00805f9b34fb"
    val RINGER_SETTING = "00002a41-0000-1000-8000-00805f9b34fb"
    val ALERT_CATEGORY_ID_BIT_MASK = "00002a42-0000-1000-8000-00805f9b34fb"
    val ALERT_CATEGORY_ID = "00002a43-0000-1000-8000-00805f9b34fb"
    val ALERT_NOTIFICATION_CONTROL_POINT = "00002a44-0000-1000-8000-00805f9b34fb"
    val UNREAD_ALERT_STATUS = "00002a45-0000-1000-8000-00805f9b34fb"
    val NEW_ALERT = "00002a46-0000-1000-8000-00805f9b34fb"
    val SUPPORTED_NEW_ALERT_CATEGORY = "00002a47-0000-1000-8000-00805f9b34fb"
    val SUPPORTED_UNREAD_ALERT_CATEGORY = "00002a48-0000-1000-8000-00805f9b34fb"
    val BLOOD_PRESSURE_FEATURE = "00002a49-0000-1000-8000-00805f9b34fb"
    val PNPID = "00002a50-0000-1000-8000-00805f9b34fb"
    val SC_CONTROL_POINT = "00002a55-0000-1000-8000-00805f9b34fb"
    val CSC_MEASUREMENT = "00002a5b-0000-1000-8000-00805f9b34fb"
    val CSC_FEATURE = "00002a5c-0000-1000-8000-00805f9b34fb"
    val SENSOR_LOCATION = "00002a5d-0000-1000-8000-00805f9b34fb"
    val ACTIVESYNC = "831c4071-7bc8-4a9c-a01c-15df25a4adbc"
    val ESTIMOTE_SERVICE = "b9403000-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_UUID = "b9403003-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_MAJOR = "b9403001-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_MINOR = "b9403002-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_BATTERY = "b9403041-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_TEMPERATURE = "b9403021-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_POWER = "b9403011-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_ADVERTISING_INTERVAL = "b9403012-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_VERSION_SERVICE = "b9404000-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_SOFTWARE_VERSION = "b9404001-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_HARDWARE_VERSION = "b9404002-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_AUTHENTICATION_SERVICE = "b9402000-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_ADVERTISING_SEED = "b9402001-f5f8-466e-aff9-25556b57fe6d"
    val ESTIMOTE_ADVERTISING_VECTOR = "b9402002-f5f8-466e-aff9-25556b57fe6d"

    private val sGattAttributesMap = populateGattAttributesMap()

    fun getAttributeName(uuid: String, fallback: String): String {
        val name = sGattAttributesMap[uuid.toLowerCase(Locale.US)]
        return name ?: fallback
    }

    private fun populateGattAttributesMap(): Map<String, String> {
        val map = HashMap<String, String>()

        map.put(BASE_GUID, "Base GUID")
        map.put(SERVICE_DISCOVERY_PROTOCOL_SDP, "Service Discovery Protocol (SDP)")
        map.put(USER_DATAGRAM_PROTOCOL_UDP, "User Datagram Protocol (UDP)")
        map.put(RADIO_FREQUENCY_COMMUNICATION_PROTOCOL_RFCOMM, "Radio Frequency Communication Protocol (RFCOMM)")
        map.put(TCP, "TCP")
        map.put(TCSBIN, "TCSBIN")
        map.put(TCSAT, "TCSAT")
        map.put(OBJECT_EXCHANGE_PROTOCOL_OBEX, "Object Exchange Protocol (OBEX)")
        map.put(IP, "IP")
        map.put(FTP, "FTP")
        map.put(HTTP, "HTTP")
        map.put(WSP, "WSP")
        map.put(BNEP_SVC, "BNEP_SVC")
        map.put(UPNP_PROTOCOL, "UPNP Protocol")
        map.put(HIDP, "HIDP")
        map.put(HARDCOPY_CONTROL_CHANNEL_PROTOCOL, "Hardcopy Control Channel Protocol")
        map.put(HARDCOPY_DATA_CHANNEL_PROTOCOL, "Hardcopy Data Channel Protocol")
        map.put(HARDCOPY_NOTIFICATION_PROTOCOL, "Hardcopy Notification Protocol")
        map.put(VCTP_PROTOCOL, "VCTP Protocol")
        map.put(VDTP_PROTOCOL, "VDTP Protocol")
        map.put(CMPT_PROTOCOL, "CMPT Protocol")
        map.put(UDI_C_PLANE_PROTOCOL, "UDI C Plane Protocol")
        map.put(MCAP_CONTROL_CHANNEL, "MCAP Control Channel")
        map.put(MCAP_DATA_CHANNEL, "MCAP Data Channel")
        map.put(L2CAP, "L2CAP")
        map.put(SERVICE_DISCOVERY_SERVER, "Service Discovery Server")
        map.put(BROWSE_GROUP_DESCRIPTOR, "Browse Group Descriptor")
        map.put(PUBLIC_BROWSE_GROUP, "Public Browse Group")
        map.put(SPP, "SPP")
        map.put(LAN_ACCESS_USING_PPP, "LAN Access Using PPP")
        map.put(DUN_GW, "DUN_GW")
        map.put(OBEX_SYNC, "OBEX_SYNC")
        map.put(OBEX_OBJECT_PUSH, "OBEX Object Push")
        map.put(OBEX_FILE_TRANSFER, "OBEX File Transfer")
        map.put(IRMC_SYNC_COMMAND, "IrMC Sync Command")
        map.put(HSP_HS, "HSP_HS")
        map.put(CORDLESS_TELEPHONY, "Cordless Telephony")
        map.put(AUDIO_SOURCE, "Audio Source")
        map.put(AUDIO_SINK, "Audio Sink")
        map.put(AV_REMOTE_CONTROL_TARGET, "AV Remote Control Target")
        map.put(ADVANCED_AUDIO, "ADVANCED_AUDIO")
        map.put(AVRCP_REMOTE, "AVRCP_REMOTE")
        map.put(VIDEO_CONFERENCING, "Video Conferencing")
        map.put(INTERCOM, "Intercom")
        map.put(FAX, "FAX")
        map.put(HEADSET_PROFILE_HSP_AUDIO_GATEWAY, "Headset Profile (HSP) - Audio Gateway")
        map.put(WAP, "WAP")
        map.put(WAP_CLIENT, "WAP Client")
        map.put(PANU, "PANU")
        map.put(NAP, "NAP")
        map.put(GN, "GN")
        map.put(DIRECT_PRINTING, "Direct Printing")
        map.put(REFERENCE_PRINTING, "Reference Printing")
        map.put(IMAGING, "Imaging")
        map.put(IMAGING_RESPONDER, "Imaging Responder")
        map.put(IMAGING_AUTOMATIC_ARCHIVE, "Imaging Automatic Archive")
        map.put(IMAGING_REFERENCE_OBJECTS, "Imaging Reference Objects")
        map.put(HANDS_FREE_PROFILE_HFP, "Hands Free Profile (HFP)")
        map.put(HANDS_FREE_PROFILE_HFP_AUDIO_GATEWAY, "Hands Free Profile (HFP) – Audio Gateway")
        map.put(DIRECT_PRINTING_REFERENCE_OBJECTS, "Direct Printing Reference Objects")
        map.put(REFLECTED_UI, "Reflected UI")
        map.put(BASIC_PRINTING, "Basic Printing")
        map.put(PRINTING_STATUS, "Printing Status")
        map.put(HID, "HID")
        map.put(HARDCOPY_CABLE_REPLACEMENT, "Hardcopy Cable Replacement")
        map.put(HCR_PRINT, "HCR Print")
        map.put(HCR_SCAN, "HCR Scan")
        map.put(COMMON_ISDN_ACCESS, "Common ISDN Access")
        map.put(VIDEO_CONFERENCING_GATEWAY, "Video Conferencing Gateway")
        map.put(UDIMT, "UDIMT")
        map.put(UDITA, "UDITA")
        map.put(AUDIO_VIDEO, "Audio Video")
        map.put(SIM_ACCESS, "SIM Access")
        map.put(OBEX_PCE, "OBEX PCE")
        map.put(OBEX_PSE, "OBEX PSE")
        map.put(OBEX_PBAP, "OBEX PBAP")
        map.put(OBEX_MAS, "OBEX MAS")
        map.put(OBEX_MNS, "OBEX MNS")
        map.put(OBEX_MAP, "OBEX MAP")
        map.put(PNP, "PNP")
        map.put(GENERIC_NETWORKING, "Generic Networking")
        map.put(GENERIC_FILE_TRANSFER, "Generic File Transfer")
        map.put(GENERIC_AUDIO, "Generic Audio")
        map.put(GENERIC_TELEPHONY, "Generic Telephony")
        map.put(UPNP, "UPNP")
        map.put(UPNP_IP, "UPNP IP")
        map.put(ESDP_UPNP_IP_PAN, "ESDP UPnP IP PAN")
        map.put(ESDP_UPNP_IP_LAP, "ESDP UPnP IP LAP")
        map.put(ESDP_UPNP_L2CAP, "ESDP Upnp L2CAP")
        map.put(VIDEO_DISTRIBUTION_PROFILE_VDP_SOURCE, "Video Distribution Profile (VDP) - Source")
        map.put(VIDEO_DISTRIBUTION_PROFILE_VDP_SINK, "Video Distribution Profile (VDP) - Sink")
        map.put(VIDEO_DISTRIBUTION_PROFILE_VDP, "Video Distribution Profile (VDP)")
        map.put(HEALTH_DEVICE_PROFILE_HDP, "Health Device Profile (HDP)")
        map.put(HEALTH_DEVICE_PROFILE_HDP_SOURCE, "Health Device Profile (HDP) - Source")
        map.put(HEALTH_DEVICE_PROFILE_HDP_SINK, "Health Device Profile (HDP) - Sink")
        map.put(GAP, "GAP")
        map.put(GATT, "GATT")
        map.put(IMMEDIATE_ALERT, "IMMEDIATE_ALERT")
        map.put(LINK_LOSS, "LINK_LOSS")
        map.put(TX_POWER, "TX_POWER")
        map.put(HEALTH_THERMOMETER, "Health Thermometer")
        map.put(DEVICE_INFORMATION, "Device Information")
        map.put(HEART_RATE, "HEART_RATE")
        map.put(CYCLING_SC, "CYCLING_SC")
        map.put(CLIENT_CHARACTERISTIC_CONFIG, "CLIENT_CHARACTERISTIC_CONFIG")
        map.put(DEVICE_NAME, "Device Name")
        map.put(APPEARANCE, "Appearance")
        map.put(PERIPHERAL_PRIVACY_FLAG, "Peripheral Privacy Flag")
        map.put(RECONNECTION_ADDRESS, "Reconnection Address")
        map.put(PERIPHERAL_PREFERRED_CONNECTION_PARAMETERS, "Peripheral Preferred Connection Parameters")
        map.put(SERVICE_CHANGED, "Service Changed")
        map.put(ALERT_LEVEL, "Alert Level")
        map.put(TX_POWER_LEVEL, "Tx Power Level")
        map.put(DATE_TIME, "Date Time")
        map.put(DAY_OF_WEEK, "Day of Week")
        map.put(DAY_DATE_TIME, "Day Date Time")
        map.put(EXACT_TIME_256, "Exact Time 256")
        map.put(DST_OFFSET, "DST Offset")
        map.put(TIME_ZONE, "Time Zone")
        map.put(LOCAL_TIME_INFORMATION, "Local Time Information")
        map.put(TIME_WITH_DST, "Time with DST")
        map.put(TIME_ACCURACY, "Time Accuracy")
        map.put(TIME_SOURCE, "Time Source")
        map.put(REFERENCE_TIME_INFORMATION, "Reference Time Information")
        map.put(TIME_UPDATE_CONTROL_POINT, "Time Update Control Point")
        map.put(TIME_UPDATE_STATE, "Time Update State")
        map.put(TEMPERATURE_MEASUREMENT, "Temperature Measurement")
        map.put(TEMPERATURE_TYPE, "Temperature Type")
        map.put(INTERMEDIATE_TEMPERATURE, "Intermediate Temperature")
        map.put(MEASUREMENT_INTERVAL, "Measurement Interval")
        map.put(SYSTEM_ID, "System ID")
        map.put(MODEL_NUMBER_STRING, "Model Number String")
        map.put(SERIAL_NUMBER_STRING, "Serial Number String")
        map.put(FIRMWARE_REVISION_STRING, "Firmware Revision String")
        map.put(HARDWARE_REVISION_STRING, "Hardware Revision String")
        map.put(SOFTWARE_REVISION_STRING, "Software Revision String")
        map.put(MANUFACTURER_NAME_STRING, "Manufacturer Name String")
        map.put(IEEE_1107320601_REGULATORY, "IEEE 11073-20601 Regulatory")
        map.put(CURRENT_TIME, "Current Time")
        map.put(BLOOD_PRESSURE_MEASUREMENT, "Blood Pressure Measurement")
        map.put(INTERMEDIATE_CUFF_PRESSURE, "Intermediate Cuff Pressure")
        map.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement")
        map.put(BODY_SENSOR_LOCATION, "Body Sensor Location")
        map.put(HEART_RATE_CONTROL_POINT, "Heart Rate Control Point")
        map.put(ALERT_STATUS, "Alert Status")
        map.put(RINGER_CONTROL_POINT, "Ringer Control Point")
        map.put(RINGER_SETTING, "Ringer Setting")
        map.put(ALERT_CATEGORY_ID_BIT_MASK, "Alert Category ID Bit Mask")
        map.put(ALERT_CATEGORY_ID, "Alert Category ID")
        map.put(ALERT_NOTIFICATION_CONTROL_POINT, "Alert Notification Control Point")
        map.put(UNREAD_ALERT_STATUS, "Unread Alert Status")
        map.put(NEW_ALERT, "New Alert")
        map.put(SUPPORTED_NEW_ALERT_CATEGORY, "Supported New Alert Category")
        map.put(SUPPORTED_UNREAD_ALERT_CATEGORY, "Supported Unread Alert Category")
        map.put(BLOOD_PRESSURE_FEATURE, "Blood Pressure Feature")
        map.put(PNPID, "PNPID")
        map.put(SC_CONTROL_POINT, "SC_CONTROL_POINT")
        map.put(CSC_MEASUREMENT, "CSC_MEASUREMENT")
        map.put(CSC_FEATURE, "CSC_FEATURE")
        map.put(SENSOR_LOCATION, "SENSOR_LOCATION")
        map.put(ACTIVESYNC, "ActiveSync")
        map.put(ESTIMOTE_SERVICE, "Estimote Service")
        map.put(ESTIMOTE_UUID, "Estimote UUID")
        map.put(ESTIMOTE_MAJOR, "Estimote Major")
        map.put(ESTIMOTE_MINOR, "Estimote Minor")
        map.put(ESTIMOTE_BATTERY, "Estimote Battery")
        map.put(ESTIMOTE_TEMPERATURE, "Estimote Temperature")
        map.put(ESTIMOTE_POWER, "Estimote Power")
        map.put(ESTIMOTE_ADVERTISING_INTERVAL, "Estimote Advertising Interval")
        map.put(ESTIMOTE_VERSION_SERVICE, "Estimote Version Service")
        map.put(ESTIMOTE_SOFTWARE_VERSION, "Estimote Software Version")
        map.put(ESTIMOTE_HARDWARE_VERSION, "Estimote Hardware Version")
        map.put(ESTIMOTE_AUTHENTICATION_SERVICE, "Estimote Authentication Service")
        map.put(ESTIMOTE_ADVERTISING_SEED, "Estimote Advertising Seed")
        map.put(ESTIMOTE_ADVERTISING_VECTOR, "Estimote Advertising Vector")
        return map
    }
}
